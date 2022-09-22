package vn.com.telsoft.model;

import com.faplib.admin.security.AdminUser;
import com.faplib.lib.ClientMessage;
import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.util.DateUtil;
import vn.com.telsoft.entity.IncorrectFeeTransaction;
import vn.com.telsoft.entity.MismatchedTransaction;
import vn.com.telsoft.entity.SuspiciousTransaction;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

public class SuspiciousTransactionModel extends AMDataPreprocessor implements Serializable {
    public List<SuspiciousTransaction> getSuspiciousTransactions(List<String> selectedBankCodes,
                                                                 Date fromDate,
                                                                 Date toDate,
                                                                 String searchInput)
            throws Exception {

        List<SuspiciousTransaction> suspiciousTransactionList = new ArrayList<>();
        String sqlString = "";
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();

        try {
            // Find by ID
            open();
            if (!searchInput.isEmpty()) {
                sqlString = "SELECT\n" +
                        "    *\n" +
                        "FROM\n" +
                        "    suspicious_transaction\n" +
                        "WHERE\n" +
                        "    transaction_id = ?\n" +
                        "    OR mapping_code = ?"
                ;
                mStmt = mConnection.prepareStatement(sqlString);
                mStmt.setString(1, searchInput);
                mStmt.setString(2, searchInput);

            } else {
                sqlString = "SELECT \n" +
                        "    * \n" +
                        "FROM \n" +
                        "    suspicious_transaction \n" +
                        "WHERE \n" +
                        "    1 = 1 \n" +
                        "    $TRANSACTION_DATE_FROM$\n" +
                        "    $TRANSACTION_DATE_TO$\n" +
                        "    $BANK_CODE$ \n" +
                        "ORDER BY \n" +
                        "    transaction_date";

                //                Filter by Created Time
                if (fromDate != null) {
                    sqlString = sqlString.replace("$TRANSACTION_DATE_FROM$",
                            "AND trunc(transaction_date) >= ?");
                    vtRow = new Vector();
                    vtRow.addElement("Timestamp");
                    vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(fromDate));
                    vtParams.add(vtRow);
                } else {
                    sqlString = sqlString.replace("$TRANSACTION_DATE_FROM$",
                            "");
                }

                if (toDate != null) {
                    sqlString = sqlString.replace("$TRANSACTION_DATE_TO$",
                            "AND trunc(transaction_date) <= ?");
                    vtRow = new Vector();
                    vtRow.addElement("Timestamp");
                    vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(toDate));
                    vtParams.add(vtRow);
                }
                sqlString = sqlString.replace("$TRANSACTION_DATE_TO$",
                        "");

//              Filter by bank_code
                if (selectedBankCodes.size() == 0) {
                    sqlString = sqlString.replace("$BANK_CODE$", "");
                } else {
                    Boolean containNull = false;
                    String condition = "";

                    List<String> cloneList = new ArrayList<>(selectedBankCodes);
                    for (final ListIterator<String> items = cloneList.listIterator(); items.hasNext(); ) {
                        final String element = items.next();
                        if (element.equals("None")) {
                            containNull = true;
                            items.set("''");
                        } else {
                            items.set("'" + element + "'");
                        }
                    }

                    if (containNull) {
                        condition = "AND (bank_code IN " +
                                cloneList.toString().replace("[", "(").replace("]", ") OR bank_code IS NULL)");
                    } else {
                        condition = "AND bank_code IN " +
                                cloneList.toString().replace("[", "(").replace("]", ")");
                    }
                    sqlString = sqlString.replace("$BANK_CODE$", condition);
                }


                mStmt = mConnection.prepareStatement(sqlString);
                for (int i = 0; i < vtParams.size(); i++) {
                    Vector vtTemp = (Vector) vtParams.get(i);
                    String strType = vtTemp.elementAt(0).toString();
                    switch (strType) {
                        case "String":
                            mStmt.setString(i + 1, vtTemp.elementAt(1).toString());
                            break;
                        case "Blob":
                            mStmt.setBlob(i + 1, (InputStream) vtTemp.elementAt(1));
                            break;
                        case "Timestamp":
                            mStmt.setTimestamp(i + 1, (Timestamp) vtTemp.elementAt(1));
                            break;
                        case "NULL":
                            break;
                        default:
                            mStmt.setString(i + 1, vtTemp.elementAt(1).toString());
                            break;
                    }
                }
            }

            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                SuspiciousTransaction suspiciousTransaction = new SuspiciousTransaction();
                suspiciousTransaction.setId(mRs.getInt(1));
                suspiciousTransaction.setBankCode(mRs.getString(2));
                suspiciousTransaction.setMappingCode(mRs.getString(3));
                suspiciousTransaction.setTransactionID(mRs.getString(4));
                suspiciousTransaction.setTransactionDate(mRs.getTimestamp(5));
                suspiciousTransaction.setDescription(mRs.getString(6));
                suspiciousTransaction.setCreditAmount(mRs.getInt(7));
                suspiciousTransaction.setDebitAmount((int) mRs.getDouble(8));
                suspiciousTransaction.setAdjustDate(mRs.getTimestamp(9) );
                suspiciousTransaction.setTicketName(mRs.getString(10));
                suspiciousTransaction.setProcessStatus(mRs.getString(11));
                suspiciousTransaction.setUserProcess(mRs.getString(12));
                suspiciousTransaction.setConfirmStatus(mRs.getString(13));
                suspiciousTransaction.setUserConfirm(mRs.getString(14));

                suspiciousTransactionList.add(suspiciousTransaction);
            }

        } catch (Exception ex) {
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.ERR, "Lỗi xảy ra trong quá trình tìm kiếm!");
            throw ex;
        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return suspiciousTransactionList;
    }

    public void processTransaction(SuspiciousTransaction suspiciousTransaction) throws Exception {
        try {
            open();
            mConnection.setAutoCommit(false);

            String strSQL = "UPDATE suspicious_transaction SET process_status = ?, " +
                    "user_process = ?, confirm_status = ? " +
                    "WHERE id = ?";

            mStmt = mConnection.prepareStatement(strSQL);

            mStmt.setString(1, suspiciousTransaction.getProcessStatus());
            mStmt.setString(2, suspiciousTransaction.getUserProcess());
            mStmt.setString(3, suspiciousTransaction.getConfirmStatus());
            mStmt.setInt(4, suspiciousTransaction.getId());
            mStmt.execute();
//            logAfterUpdate(listChange);
            close(mStmt);

            //Commit
            mConnection.commit();

        } catch (Exception ex) {
            mConnection.rollback();
            throw ex;
        } finally {
            close(mStmt);
            close();
        }
    }

    public void confirmProcess(SuspiciousTransaction suspiciousTransaction) throws Exception {
        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + mismatchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE suspicious_transaction SET confirm_status = ?, " +
                    "user_confirm = ?" +
                    "WHERE  id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, suspiciousTransaction.getConfirmStatus());
            mStmt.setString(2, AdminUser.getUserLogged().getUserName());
            mStmt.setInt(3, suspiciousTransaction.getId());
            mStmt.execute();
//            logAfterUpdate(listChange);
            close(mStmt);

            //Commit
            mConnection.commit();

        } catch (Exception ex) {
            mConnection.rollback();
            throw ex;

        } finally {
            close(mStmt);
            close();
        }
    }

    public void updateTicketName(String id, String ticketName) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + mismatchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE suspicious_transaction SET ticket_name = ?" +
                    "WHERE  id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, ticketName);
            mStmt.setString(2, id);
            mStmt.execute();
//            logAfterUpdate(listChange);
            close(mStmt);

            //Commit
            mConnection.commit();
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.UPDATE, "Cập nhật tên ticket thành công!");
        } catch (Exception ex) {
            mConnection.rollback();
            throw ex;

        } finally {
            close(mStmt);
            close();
        }
    }

    public void updateAdjustDate(String id, Date adjustDate) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            String strSQL = "UPDATE suspicious_transaction SET adjust_date = ?" +
                    "WHERE id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setTimestamp(1, DateUtil.getSqlTimestamp(adjustDate));
            mStmt.setString(2, id);
            mStmt.execute();
//            logAfterUpdate(listChange);
            close(mStmt);

            //Commit
            mConnection.commit();
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.UPDATE, "Cập nhật ngày điều chỉnh thành công!");
        } catch (Exception ex) {
            mConnection.rollback();
            throw ex;

        } finally {
            close(mStmt);
            close();
        }
    }
}
