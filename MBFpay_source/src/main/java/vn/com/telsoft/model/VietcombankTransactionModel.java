/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.admin.data.AMDataPreprocessor;
import vn.com.telsoft.entity.VietcombankTransaction;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author ThongNM
 */
public class VietcombankTransactionModel extends AMDataPreprocessor implements Serializable {

    public List<VietcombankTransaction> getVietcombankTransactions(Date transactionDateFrom,
                                                               Date transactionDateTo,
                                                                   List<String> selectedFileTypes,
                                                               String searchInput)
            throws Exception {

        List<VietcombankTransaction> vietcombankTransactionList = new ArrayList<>();
        String sqlString = "";
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();
        try {
            // Find by ID
            open();

            sqlString = "SELECT \n" +
                    "    * \n" +
                    "FROM \n" +
                    "    vietcombank \n" +
                    "WHERE \n" +
                    "    1 = 1 \n" +
                    "    $SEARCH_INPUT$\n" +
                    "    $FILE_TYPE$\n" +
                    "    $TRANSACTION_DATE_FROM$\n" +
                    "    $TRANSACTION_DATE_TO$\n"
            ;

//                Filter by Search Input
            if (!searchInput.isEmpty()) {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "AND ? IN (transaction_number, to_char(debit), to_char(credit), " +
                                "transaction_description, bank_reference_id, file_name, trans_code) ").replace(
                        "?", "'" + searchInput + "'");
            } else {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "");
            }

            //              Filter by file_type
            if (selectedFileTypes.size() == 0) {
                sqlString = sqlString.replace("$FILE_TYPE$", "");
            } else {
                Boolean containNull = false;
                String condition = "";

                List<String> cloneList = new ArrayList<>(selectedFileTypes);
                for (final ListIterator<String> items = cloneList.listIterator(); items.hasNext(); ) {
                    final String element = items.next();
                    if (element.equals("(null)")) {
                        containNull = true;
                        items.set("''");
                    } else {
                        items.set("'" + element + "'");
                    }
                }

                if (containNull) {
                    condition = "AND (file_type IN " +
                            cloneList.toString().replace("[", "(").replace("]", ") OR file_type IS NULL)");
                } else {
                    condition = "AND file_type IN " +
                            cloneList.toString().replace("[", "(").replace("]", ")");
                }
                sqlString = sqlString.replace("$FILE_TYPE$", condition);
            }

            //                Filter by Created Time
            if (transactionDateFrom != null) {
                sqlString = sqlString.replace("$TRANSACTION_DATE_FROM$",
                        "AND trunc(transaction_date) >= ?");
                vtRow = new Vector();
                vtRow.addElement("Timestamp");
                vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(transactionDateFrom));
                vtParams.add(vtRow);
            } else {
                sqlString = sqlString.replace("$TRANSACTION_DATE_FROM$",
                        "");
            }

            if (transactionDateTo != null) {
                sqlString = sqlString.replace("$TRANSACTION_DATE_TO$",
                        "AND trunc(transaction_date) <= ?");
                vtRow = new Vector();
                vtRow.addElement("Timestamp");
                vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(transactionDateTo));
                vtParams.add(vtRow);
            }else {
                sqlString = sqlString.replace("$TRANSACTION_DATE_TO$",
                        "");
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


            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                VietcombankTransaction vietcombankTransaction = new VietcombankTransaction();
                vietcombankTransaction.setTransactionDate(mRs.getTimestamp(1));
                vietcombankTransaction.setTransactionNumber(mRs.getString(2));
                vietcombankTransaction.setDebit(mRs.getInt(3));
                vietcombankTransaction.setCredit(mRs.getInt(4));
                vietcombankTransaction.setTransactionDescription(mRs.getString(5));
                vietcombankTransaction.setBankReferenceID(mRs.getString(6));
                vietcombankTransaction.setImportDateTime(mRs.getTimestamp(7));
                vietcombankTransaction.setFileName(mRs.getString(8));
                vietcombankTransaction.setTransCode(mRs.getString(9));
                vietcombankTransaction.setValueDate(mRs.getTimestamp(10));
                vietcombankTransaction.setFileType(mRs.getString(11));
                vietcombankTransaction.setId(mRs.getInt(12));

                vietcombankTransactionList.add(vietcombankTransaction);
            }


        } catch (Exception ex) {
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.ERR, "Lỗi xảy ra trong quá trình tìm kiếm!");
            throw ex;
        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return vietcombankTransactionList;
    }
}
