package vn.com.telsoft.model;

import com.faplib.admin.security.AdminUser;
import com.faplib.lib.ClientMessage;
import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.util.DateUtil;
import vn.com.telsoft.entity.IncorrectFeeTransaction;
import vn.com.telsoft.entity.MismatchedTransaction;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class IncorrectFeeTransactionModel extends AMDataPreprocessor implements Serializable {
    public List<IncorrectFeeTransaction> getIncorrectFeeTransactions(List<String> selectedBankCodes,
                                                                     Date fromDate,
                                                                     Date toDate, String invoiceOrderID,
                                                                     List<String> selectedProcessStatus,
                                                                     List<String> selectedConfirmStatus,
                                                                     List<String> selectedTicketUpdateStatus,
                                                                     List<String> selectedOrderStatusCodes,
                                                                     Date fromAdjustDate,
                                                                     Date toAdjustDate)
            throws Exception {

        List<IncorrectFeeTransaction> incorrectFeeTransactionList = new ArrayList<>();
        String sqlString = "";
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();
        try {
            // Find by ID
            open();
            if (!invoiceOrderID.isEmpty()) {
                sqlString = "SELECT\n" +
                        "    *\n" +
                        "FROM\n" +
                        "    incorrect_fee_transaction\n" +
                        "WHERE\n" +
                        "    invoice_order_id = ? ";
                mStmt = mConnection.prepareStatement(sqlString);
                mStmt.setString(1, invoiceOrderID);
            } else {
                sqlString = "SELECT \n" +
                        "    * \n" +
                        "FROM \n" +
                        "    incorrect_fee_transaction \n" +
                        "WHERE \n" +
                        "    1 = 1 \n" +
                        "    $CREATED_TIME_FROM$\n" +
                        "    $CREATED_TIME_TO$\n" +
                        "    $BANK_CODE$ \n" +
                        "    $REFUND_STATUS$ \n" +
                        "    $CONFIRM_STATUS$ \n" +
                        "    $ORDER_STATUS_CODE$ \n" +
                        "    $TICKET_STATUS$ \n" +
                        "    $ADJUSTED_DATE_FROM$ \n" +
                        "    $ADJUSTED_DATE_TO$ \n" +
                        "ORDER BY \n" +
                        "    created_time";

                //                Filter by Created Time
                if (fromDate != null) {
                    sqlString = sqlString.replace("$CREATED_TIME_FROM$",
                            "AND trunc(created_time) >= ?");
                    vtRow = new Vector();
                    vtRow.addElement("Timestamp");
                    vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(fromDate));
                    vtParams.add(vtRow);
                } else {
                    sqlString = sqlString.replace("$CREATED_TIME_FROM$",
                            "");
                }

                if (toDate != null) {
                    sqlString = sqlString.replace("$CREATED_TIME_TO$",
                            "AND trunc(created_time) <= ?");
                    vtRow = new Vector();
                    vtRow.addElement("Timestamp");
                    vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(toDate));
                    vtParams.add(vtRow);
                }
                sqlString = sqlString.replace("$CREATED_TIME_TO$",
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
                        if (element.equals("Blank")) {
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

                //            Filter by process_status
                if (selectedProcessStatus.size() == 0) {
                    sqlString = sqlString.replace("$REFUND_STATUS$", "");
                } else {
                    List<String> cloneList = new ArrayList<>(selectedProcessStatus);
                    for (final ListIterator<String> items = cloneList.listIterator(); items.hasNext(); ) {
                        final String element = items.next();
                        items.set("'" + element + "'");
                    }
                    String condition = "AND refund_status IN " +
                            cloneList.toString().replace("[", "(").replace("]", ")");
                    sqlString = sqlString.replace("$REFUND_STATUS$", condition);
                }

                //            Filter by confirm_status
                if (selectedConfirmStatus.size() == 0) {
                    sqlString = sqlString.replace("$CONFIRM_STATUS$", "");
                } else {
                    List<String> cloneList = new ArrayList<>(selectedConfirmStatus);
                    for (final ListIterator<String> items = cloneList.listIterator(); items.hasNext(); ) {
                        final String element = items.next();
                        items.set("'" + element + "'");
                    }
                    String condition = "AND confirm_status IN " +
                            cloneList.toString().replace("[", "(").replace("]", ")");
                    sqlString = sqlString.replace("$CONFIRM_STATUS$", condition);
                }

                //            Filter by Ticket Update Status
                if (selectedTicketUpdateStatus.size() == 0 || selectedTicketUpdateStatus.size() == 2) {
                    sqlString = sqlString.replace("$TICKET_STATUS$", "");
                } else {
                    String updateStatus = selectedTicketUpdateStatus.get(0);
                    if (updateStatus.equals("0")) {
                        sqlString = sqlString.replace("$TICKET_STATUS$", "AND ticket_name IS NULL ");
                    } else if (updateStatus.equals("1")) {
                        sqlString = sqlString.replace("$TICKET_STATUS$", "AND ticket_name IS NOT NULL ");
                    }
                }

//                Filter by Order Status Code
                if (selectedOrderStatusCodes.size() == 0) {
                    sqlString = sqlString.replace("$ORDER_STATUS_CODE$", "");
                } else {
                    List<String> cloneList = new ArrayList<>(selectedOrderStatusCodes);
                    for (final ListIterator<String> items = cloneList.listIterator(); items.hasNext(); ) {
                        final String element = items.next();
                        items.set("'" + element + "'");
                    }
                    String condition = "AND order_status_code IN " +
                            cloneList.toString().replace("[", "(").replace("]", ")");
                    sqlString = sqlString.replace("$ORDER_STATUS_CODE$", condition);
                }

//                Filter by Adjusted Date
                if (fromAdjustDate != null) {
                    sqlString = sqlString.replace("$ADJUSTED_DATE_FROM$",
                            "AND trunc(adjust_date) >= ?");
                    vtRow = new Vector();
                    vtRow.addElement("Timestamp");
                    vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(fromAdjustDate));
                    vtParams.add(vtRow);
                } else {
                    sqlString = sqlString.replace("$ADJUSTED_DATE_FROM$",
                            "");
                }

                if (toAdjustDate != null) {
                    sqlString = sqlString.replace("$ADJUSTED_DATE_TO$",
                            "AND trunc(adjust_date) <= ?");
                    vtRow = new Vector();
                    vtRow.addElement("Timestamp");
                    vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(toAdjustDate));
                    vtParams.add(vtRow);
                } else {
                    sqlString = sqlString.replace("$ADJUSTED_DATE_TO$",
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
            }

            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                IncorrectFeeTransaction incorrectFeeTransaction = new IncorrectFeeTransaction();
                incorrectFeeTransaction.setInvoiceOrderID(mRs.getString(1));
                incorrectFeeTransaction.setTransactionID(mRs.getString(2));
                incorrectFeeTransaction.setSourceReceiptID(mRs.getString(3));
                incorrectFeeTransaction.setSaleOrderID(mRs.getString(4));
                incorrectFeeTransaction.setPayType(mRs.getString(5));
                incorrectFeeTransaction.setPayTypeName(mRs.getString(6));
                incorrectFeeTransaction.setPartnerCode(mRs.getString(7));
                incorrectFeeTransaction.setPartnerReferenceID(mRs.getString(8));
                incorrectFeeTransaction.setPartnerOrderID(mRs.getString(9));
                incorrectFeeTransaction.setPartnerInvoiceID(mRs.getString(10));
                incorrectFeeTransaction.setBankCode(mRs.getString(11));
                incorrectFeeTransaction.setBankReferenceID(mRs.getString(12));
                incorrectFeeTransaction.setProductServiceID(mRs.getString(13));
                incorrectFeeTransaction.setProductServiceCode(mRs.getString(14));
                incorrectFeeTransaction.setProductID(mRs.getString(15));
                incorrectFeeTransaction.setProductCode(mRs.getString(16));
                incorrectFeeTransaction.setProductName(mRs.getString(17));
                incorrectFeeTransaction.setProductDetail(mRs.getString(18));
                incorrectFeeTransaction.setDescription(mRs.getString(19));
                incorrectFeeTransaction.setProductAccount(mRs.getString(20));
                incorrectFeeTransaction.setAmount(mRs.getInt(21));
                incorrectFeeTransaction.setQuantity(mRs.getInt(22));
                incorrectFeeTransaction.setSaleDiscount(mRs.getString(23));
                incorrectFeeTransaction.setSaleFee(mRs.getString(24));
                incorrectFeeTransaction.setGrandAmount(mRs.getInt(25));
                incorrectFeeTransaction.setFee(mRs.getInt(26));
                incorrectFeeTransaction.setRelatedFee(mRs.getInt(27));
                incorrectFeeTransaction.setPaymentAmount(mRs.getInt(28));
                incorrectFeeTransaction.setAccountName(mRs.getString(29));
                incorrectFeeTransaction.setRelatedAccount(mRs.getString(30));
                incorrectFeeTransaction.setOrderStatus(mRs.getString(31));
                incorrectFeeTransaction.setOrderStatusCode(mRs.getString(32));
                incorrectFeeTransaction.setCreatedUser(mRs.getString(33));
                incorrectFeeTransaction.setConfirmUser(mRs.getString(34));
                incorrectFeeTransaction.setInitTime(mRs.getTimestamp(35));
                incorrectFeeTransaction.setCreatedTime(mRs.getTimestamp(36));
                incorrectFeeTransaction.setFileName(mRs.getString(37));
                incorrectFeeTransaction.setLogDateTime(mRs.getTimestamp(38));
                incorrectFeeTransaction.setImportDateTime(mRs.getTimestamp(39));
                incorrectFeeTransaction.setBankReferenceData(mRs.getString(40));
                incorrectFeeTransaction.setAdjustDate(mRs.getTimestamp(42));
                incorrectFeeTransaction.setRefundStatus(mRs.getString(43));
                incorrectFeeTransaction.setUserProcess(mRs.getString(44));
                incorrectFeeTransaction.setConfirmStatus(mRs.getString(45));
                incorrectFeeTransaction.setUserConfirm(mRs.getString(46));
                incorrectFeeTransaction.setTicketName(mRs.getString(51));
                incorrectFeeTransaction.setIssuerBankCode(mRs.getString(52));
                incorrectFeeTransaction.setRefundAmount(mRs.getInt(53));

                incorrectFeeTransactionList.add(incorrectFeeTransaction);
            }
        } catch (Exception ex) {
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.ERR, "Lỗi xảy ra trong quá trình tìm kiếm!");
            throw ex;
        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return incorrectFeeTransactionList;
    }

    public void updateRefundStatus(IncorrectFeeTransaction incorrectFeeTransaction) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + mismatchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE incorrect_fee_transaction SET refund_status = ?, " +
                    "user_process = ?, confirm_status = ? " +
                    "WHERE  invoice_order_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, incorrectFeeTransaction.getRefundStatus());
            mStmt.setString(2, incorrectFeeTransaction.getUserProcess());
            mStmt.setString(3, incorrectFeeTransaction.getConfirmStatus());
            mStmt.setString(4, incorrectFeeTransaction.getInvoiceOrderID());
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

    public void confirmProcess(IncorrectFeeTransaction incorrectFeeTransaction) throws Exception {
        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + mismatchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE incorrect_fee_transaction SET confirm_status = ?, " +
                    "user_confirm = ?" +
                    "WHERE  invoice_order_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, incorrectFeeTransaction.getConfirmStatus());
            mStmt.setString(2, AdminUser.getUserLogged().getUserName());
            mStmt.setString(3, incorrectFeeTransaction.getInvoiceOrderID());
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

    public void updateTicketName(String invoiceOrderID, String ticketName) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + mismatchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE incorrect_fee_transaction SET ticket_name = ?" +
                    "WHERE  invoice_order_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, ticketName);
            mStmt.setString(2, invoiceOrderID);
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

    public void updateAdjustDate(String invoiceOrderID, Date adjustDate) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + mismatchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE incorrect_fee_transaction SET adjust_date = ?" +
                    "WHERE  invoice_order_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setTimestamp(1, DateUtil.getSqlTimestamp(adjustDate));
            mStmt.setString(2, invoiceOrderID);
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

    public void updateRefundAmount(String invoiceOrderID, String refundAmount) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + mismatchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE incorrect_fee_transaction SET refund_amount = ?" +
                    "WHERE  invoice_order_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setInt(1, Integer.parseInt(refundAmount));
            mStmt.setString(2, invoiceOrderID);
            mStmt.execute();
//            logAfterUpdate(listChange);
            close(mStmt);

            //Commit
            mConnection.commit();
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.UPDATE, "Cập nhật hoàn tiền/ truy thu thành công!");
        } catch (Exception ex) {
            mConnection.rollback();
            throw ex;
        } finally {
            close(mStmt);
            close();
        }
    }
}
