/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.admin.security.AdminUser;
import com.faplib.lib.ClientMessage;
import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.util.DateUtil;
import vn.com.telsoft.entity.MismatchedTransaction;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author ThongNM
 */
public class MismatchedTransactionModel extends AMDataPreprocessor implements Serializable {

    public List<MismatchedTransaction> getMismatchedTransactions(List<String> selectedPartnerCodes, List<String> selectedBankCodes,
                                                                 Date dtStartDate,
                                                                 Date dtEndDate, String searchInput,
                                                                 List<String> selectedProcessStatus,
                                                                 List<String> selectedConfirmStatus,
                                                                 List<String> selectedRefundStatus,
                                                                 List<String> selectedTicketUpdateStatus,
                                                                 List<String> selectedOrderStatusCodes,
                                                                 List<String> selectedFinalStatus,
                                                                 Date adjustedFromDate,
                                                                 Date adjustedToDate,
                                                                 HashMap<String, String> sumResults)
            throws Exception {
        Double totalAmount = 0.0;
        int totalQuantity = 0;
        Double totalPaymentAmount = 0.0;
        Double totalGrandAmount = 0.0;

        List<MismatchedTransaction> mismatchedTransactionList = new ArrayList<>();
        String sqlString = "";
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();
        try {
            // Find by ID
            open();

            sqlString = "SELECT \n" +
                    "    * \n" +
                    "FROM \n" +
                    "    mismatched_transaction \n" +
                    "WHERE \n" +
                    "    1 = 1 \n" +
                    "    $SEARCH_INPUT$\n" +
                    "    $CREATED_TIME_FROM$\n" +
                    "    $CREATED_TIME_TO$\n" +
                    "    $PARTNER_CODE$ \n" +
                    "    $BANK_CODE$ \n" +
                    "    $PROCESS_STATUS$ \n" +
                    "    $CONFIRM_STATUS$ \n" +
                    "    $REFUND_STATUS$ \n" +
                    "    $FINAL_STATUS$ \n" +
                    "    $ORDER_STATUS_CODE$ \n" +
                    "    $TICKET_STATUS$ \n" +
                    "    $ADJUSTED_DATE_FROM$ \n" +
                    "    $ADJUSTED_DATE_TO$ \n" +
                    "ORDER BY \n" +
                    "    created_time";

//                Filter by Search Input
            if (!searchInput.isEmpty()) {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "AND ? IN ( to_char(invoice_order_id), transaction_id, source_receipt_id, to_char(sale_order_id), pay_type,\n" +
                                "               pay_type_name, partner_code, partner_reference_id, partner_order_id, partner_invoice_id,\n" +
                                "               bank_code, bank_reference_id, product_service_id, product_service_code, product_id,\n" +
                                "               product_code, product_name, product_detail, description, product_account,\n" +
                                "               to_char(amount), to_char(quantity), sale_discount, sale_fee, to_char(grand_amount),\n" +
                                "               to_char(fee), to_char(related_fee), to_char(payment_amount), account_name, related_account,\n" +
                                "               order_status, order_status_code, created_user, confirm_user, file_name,\n" +
                                "               bank_reference_data, account_id, user_type, issuer_bank_code, ticket_name )").replace(
                        "?", "'" + searchInput + "'");
            } else {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "");
            }

            //                Filter by Created Time
            if (dtStartDate != null) {
                sqlString = sqlString.replace("$CREATED_TIME_FROM$",
                        "AND trunc(created_time) >= ?");
                vtRow = new Vector();
                vtRow.addElement("Timestamp");
                vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(dtStartDate));
                vtParams.add(vtRow);
            } else {
                sqlString = sqlString.replace("$CREATED_TIME_FROM$",
                        "");
            }

            if (dtEndDate != null) {
                sqlString = sqlString.replace("$CREATED_TIME_TO$",
                        "AND trunc(created_time) <= ?");
                vtRow = new Vector();
                vtRow.addElement("Timestamp");
                vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(dtEndDate));
                vtParams.add(vtRow);
            }
            sqlString = sqlString.replace("$CREATED_TIME_TO$",
                    "");

//              Filter by partner_code
            if (selectedPartnerCodes.size() == 0) {
                sqlString = sqlString.replace("$PARTNER_CODE$", "");
            } else {
                Boolean containNull = false;
                String condition = "";

                List<String> cloneList = new ArrayList<>(selectedPartnerCodes);
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
                    condition = "AND (partner_code IN " +
                            cloneList.toString().replace("[", "(").replace("]", ") OR partner_code IS NULL)");
                } else {
                    condition = "AND partner_code IN " +
                            cloneList.toString().replace("[", "(").replace("]", ")");
                }
                sqlString = sqlString.replace("$PARTNER_CODE$", condition);
            }

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
                sqlString = sqlString.replace("$PROCESS_STATUS$", "");
            } else {
                List<String> cloneList = new ArrayList<>(selectedProcessStatus);
                for (final ListIterator<String> items = cloneList.listIterator(); items.hasNext(); ) {
                    final String element = items.next();
                    items.set("'" + element + "'");
                }
                String condition = "AND process_status IN " +
                        cloneList.toString().replace("[", "(").replace("]", ")");
                sqlString = sqlString.replace("$PROCESS_STATUS$", condition);
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

            //            Filter by refund_status
            if (selectedRefundStatus.size() == 0) {
                sqlString = sqlString.replace("$REFUND_STATUS$", "");
            } else {
                List<String> cloneList = new ArrayList<>(selectedRefundStatus);
                for (final ListIterator<String> items = cloneList.listIterator(); items.hasNext(); ) {
                    final String element = items.next();
                    items.set("'" + element + "'");
                }
                String condition = "AND refund_status IN " +
                        cloneList.toString().replace("[", "(").replace("]", ")");
                sqlString = sqlString.replace("$REFUND_STATUS$", condition);
            }


            //            Filter by refund_status
            if (selectedFinalStatus.size() == 0) {
                sqlString = sqlString.replace("$FINAL_STATUS$", "");
            } else {
                List<String> cloneList = new ArrayList<>(selectedFinalStatus);
                for (final ListIterator<String> items = cloneList.listIterator(); items.hasNext(); ) {
                    final String element = items.next();
                    items.set("'" + element + "'");
                }
                String condition = "AND final_status IN " +
                        cloneList.toString().replace("[", "(").replace("]", ")");
                sqlString = sqlString.replace("$FINAL_STATUS$", condition);
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
            if (adjustedFromDate != null) {
                sqlString = sqlString.replace("$ADJUSTED_DATE_FROM$",
                        "AND trunc(adjust_date) >= ?");
                vtRow = new Vector();
                vtRow.addElement("Timestamp");
                vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(adjustedFromDate));
                vtParams.add(vtRow);
            } else {
                sqlString = sqlString.replace("$ADJUSTED_DATE_FROM$",
                        "");
            }

            if (adjustedToDate != null) {
                sqlString = sqlString.replace("$ADJUSTED_DATE_TO$",
                        "AND trunc(adjust_date) <= ?");
                vtRow = new Vector();
                vtRow.addElement("Timestamp");
                vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(adjustedToDate));
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


            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                MismatchedTransaction mismatchedTransaction = new MismatchedTransaction();
                mismatchedTransaction.setInvoiceOrderID(mRs.getString(1));
                mismatchedTransaction.setTransactionID(mRs.getString(2));
                mismatchedTransaction.setSourceReceiptID(mRs.getString(3));
                mismatchedTransaction.setSaleOrderID(mRs.getString(4));
                mismatchedTransaction.setPayType(mRs.getString(5));
                mismatchedTransaction.setPayTypeName(mRs.getString(6));
                mismatchedTransaction.setPartnerCode(mRs.getString(7));
                mismatchedTransaction.setPartnerReferenceID(mRs.getString(8));
                mismatchedTransaction.setPartnerOrderID(mRs.getString(9));
                mismatchedTransaction.setPartnerInvoiceID(mRs.getString(10));
                mismatchedTransaction.setBankCode(mRs.getString(11));
                mismatchedTransaction.setBankReferenceID(mRs.getString(12));
                mismatchedTransaction.setProductServiceID(mRs.getString(13));
                mismatchedTransaction.setProductServiceCode(mRs.getString(14));
                mismatchedTransaction.setProductID(mRs.getString(15));
                mismatchedTransaction.setProductCode(mRs.getString(16));
                mismatchedTransaction.setProductName(mRs.getString(17));
                mismatchedTransaction.setProductDetail(mRs.getString(18));
                mismatchedTransaction.setDescription(mRs.getString(19));
                mismatchedTransaction.setProductAccount(mRs.getString(20));
                mismatchedTransaction.setAmount(mRs.getInt(21));
                mismatchedTransaction.setQuantity(mRs.getInt(22));
                mismatchedTransaction.setSaleDiscount(mRs.getString(23));
                mismatchedTransaction.setSaleFee(mRs.getString(24));
                mismatchedTransaction.setGrandAmount(mRs.getInt(25));
                mismatchedTransaction.setFee(mRs.getInt(26));
                mismatchedTransaction.setRelatedFee(mRs.getInt(27));
                mismatchedTransaction.setPaymentAmount(mRs.getInt(28));
                mismatchedTransaction.setAccountName(mRs.getString(29));
                mismatchedTransaction.setRelatedAccount(mRs.getString(30));
                mismatchedTransaction.setOrderStatus(mRs.getString(31));
                mismatchedTransaction.setOrderStatusCode(mRs.getString(32));
                mismatchedTransaction.setCreatedUser(mRs.getString(33));
                mismatchedTransaction.setConfirmUser(mRs.getString(34));
                mismatchedTransaction.setInitTime(mRs.getTimestamp(35));
                mismatchedTransaction.setCreatedTime(mRs.getTimestamp(36));
                mismatchedTransaction.setFileName(mRs.getString(37));
                mismatchedTransaction.setLogDateTime(mRs.getTimestamp(38));
                mismatchedTransaction.setImportDateTime(mRs.getTimestamp(39));
                mismatchedTransaction.setBankReferenceData(mRs.getString(40));
                mismatchedTransaction.setProfileID(mRs.getString(41));
                mismatchedTransaction.setPartnerTransID(mRs.getString(42));
                mismatchedTransaction.setForcontrolCode(mRs.getString(43));
                mismatchedTransaction.setPartnerStatus(mRs.getString(44));
                mismatchedTransaction.setSumDate(mRs.getTimestamp(45));
                mismatchedTransaction.setAdjustDate(mRs.getTimestamp(46));
                mismatchedTransaction.setProcessStatus(mRs.getString(47));
                mismatchedTransaction.setUserProcess(mRs.getString(48));
                mismatchedTransaction.setConfirmStatus(mRs.getString(49));
                mismatchedTransaction.setUserConfirm(mRs.getString(50));
                mismatchedTransaction.setAccountID(mRs.getString(51));
                mismatchedTransaction.setUserType(mRs.getString(52));
                mismatchedTransaction.setEndTime(mRs.getTimestamp(53));
                mismatchedTransaction.setTransTime(mRs.getTimestamp(54));
                mismatchedTransaction.setRefundStatus(mRs.getString(55));
                mismatchedTransaction.setTicketName(mRs.getString(56));
                mismatchedTransaction.setIssuerBankCode(mRs.getString(57));
                mismatchedTransaction.setFinalStatus(mRs.getString(58));

                totalAmount += mismatchedTransaction.getAmount() * mismatchedTransaction.getQuantity();
                totalQuantity += mismatchedTransaction.getQuantity();
                totalPaymentAmount += mismatchedTransaction.getPaymentAmount();
                totalGrandAmount += mismatchedTransaction.getGrandAmount();

                mismatchedTransactionList.add(mismatchedTransaction);
            }

            sumResults.put("total_amount", new DecimalFormat("###,###.###").format(totalAmount) + "");
            sumResults.put("total_quantity", new DecimalFormat("###,###.###").format(totalQuantity) + "");
            sumResults.put("total_payment_amount", new DecimalFormat("###,###.###").format(totalPaymentAmount) + "");
            sumResults.put("total_grand_amount", new DecimalFormat("###,###.###").format(totalGrandAmount) + "");

        } catch (Exception ex) {
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.ERR, "Lỗi xảy ra trong quá trình tìm kiếm!");
            throw ex;
        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return mismatchedTransactionList;
    }

    public void processTransaction(MismatchedTransaction mismatchedTransaction) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + mismatchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE mismatched_transaction SET process_status = ?, " +
                    "user_process = ?, confirm_status = ? " +
                    "WHERE  invoice_order_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, mismatchedTransaction.getProcessStatus());
            mStmt.setString(2, mismatchedTransaction.getUserProcess());
            mStmt.setString(3, mismatchedTransaction.getConfirmStatus());
            mStmt.setString(4, mismatchedTransaction.getInvoiceOrderID());
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

    public void confirmProcess(MismatchedTransaction mismatchedTransaction) throws Exception {
        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + mismatchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE mismatched_transaction SET confirm_status = ?, " +
                    "user_confirm = ?" +
                    "WHERE  invoice_order_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, mismatchedTransaction.getConfirmStatus());
            mStmt.setString(2, AdminUser.getUserLogged().getUserName());
            mStmt.setString(3, mismatchedTransaction.getInvoiceOrderID());
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

    public void updateRefundStatus(MismatchedTransaction mismatchedTransaction) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + mismatchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE mismatched_transaction SET refund_status = ?" +
                    "WHERE  invoice_order_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, mismatchedTransaction.getRefundStatus());
            mStmt.setString(2, mismatchedTransaction.getInvoiceOrderID());
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

    public void handleAdd(MismatchedTransaction mismatchedTransaction) throws Exception {
//        String strId = "";
        try {
            open();
            mConnection.setAutoCommit(false);
//            strId = String.valueOf(SQLUtil.getSequenceValue(mConnection, "summarize_not_succ_seq"));
//            app.setProfile_id(strId);

            //Update app
            String strSQL = "    INSERT INTO matched_transaction VALUES (\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?\n" +
                    "    )";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, mismatchedTransaction.getInvoiceOrderID());
            mStmt.setString(2, mismatchedTransaction.getTransactionID());
            mStmt.setString(3, mismatchedTransaction.getSourceReceiptID());
            mStmt.setString(4, mismatchedTransaction.getSaleOrderID());
            mStmt.setString(5, mismatchedTransaction.getPayType());
            mStmt.setString(6, mismatchedTransaction.getPayTypeName());
            mStmt.setString(7, mismatchedTransaction.getPartnerCode());
            mStmt.setString(8, mismatchedTransaction.getPartnerReferenceID());
            mStmt.setString(9, mismatchedTransaction.getPartnerOrderID());
            mStmt.setString(10, mismatchedTransaction.getPartnerInvoiceID());
            mStmt.setString(11, mismatchedTransaction.getBankCode());
            mStmt.setString(12, mismatchedTransaction.getBankReferenceID());
            mStmt.setString(13, mismatchedTransaction.getProductServiceID());
            mStmt.setString(14, mismatchedTransaction.getProductServiceCode());
            mStmt.setString(15, mismatchedTransaction.getProductID());
            mStmt.setString(16, mismatchedTransaction.getProductCode());
            mStmt.setString(17, mismatchedTransaction.getProductName());
            mStmt.setString(18, mismatchedTransaction.getProductDetail());
            mStmt.setString(19, mismatchedTransaction.getDescription());
            mStmt.setString(20, mismatchedTransaction.getProductAccount());
            mStmt.setInt(21, mismatchedTransaction.getAmount());
            mStmt.setInt(22, mismatchedTransaction.getQuantity());
            mStmt.setString(23, mismatchedTransaction.getSaleDiscount());
            mStmt.setString(24, mismatchedTransaction.getSaleFee());
            mStmt.setInt(25, mismatchedTransaction.getGrandAmount());
            mStmt.setInt(26, mismatchedTransaction.getFee());
            mStmt.setInt(27, mismatchedTransaction.getRelatedFee());
            mStmt.setInt(28, mismatchedTransaction.getPaymentAmount());
            mStmt.setString(29, mismatchedTransaction.getAccountName());
            mStmt.setString(30, mismatchedTransaction.getRelatedAccount());
            mStmt.setString(31, mismatchedTransaction.getOrderStatus());
            mStmt.setString(32, mismatchedTransaction.getOrderStatusCode());
            mStmt.setString(33, mismatchedTransaction.getCreatedUser());
            mStmt.setString(34, mismatchedTransaction.getConfirmUser());
            mStmt.setTimestamp(35, DateUtil.getSqlTimestamp(mismatchedTransaction.getInitTime()));
            mStmt.setTimestamp(36, DateUtil.getSqlTimestamp(mismatchedTransaction.getCreatedTime()));
            mStmt.setString(37, mismatchedTransaction.getFileName());
            mStmt.setTimestamp(38, DateUtil.getSqlTimestamp(mismatchedTransaction.getLogDateTime()));
            mStmt.setTimestamp(39, DateUtil.getSqlTimestamp(mismatchedTransaction.getImportDateTime()));
            mStmt.setString(40, mismatchedTransaction.getBankReferenceData());
            mStmt.setString(41, mismatchedTransaction.getProfileID());
            mStmt.setString(42, mismatchedTransaction.getPartnerTransID());
            mStmt.setString(43, mismatchedTransaction.getForcontrolCode());
            mStmt.setString(44, mismatchedTransaction.getPartnerStatus());
            mStmt.setTimestamp(45, DateUtil.getSqlTimestamp(mismatchedTransaction.getSumDate()));
            mStmt.setTimestamp(46, DateUtil.getSqlTimestamp(mismatchedTransaction.getAdjustDate()));
            mStmt.setString(47, mismatchedTransaction.getProcessStatus());
            mStmt.setString(48, mismatchedTransaction.getUserProcess());
            mStmt.setString(49, mismatchedTransaction.getConfirmStatus());
            mStmt.setString(50, mismatchedTransaction.getUserConfirm());
            mStmt.setString(51, mismatchedTransaction.getAccountID());
            mStmt.setString(52, mismatchedTransaction.getUserType());
            mStmt.setTimestamp(53, DateUtil.getSqlTimestamp(mismatchedTransaction.getEndTime()));
            mStmt.setTimestamp(54, DateUtil.getSqlTimestamp(mismatchedTransaction.getTransTime()));
            mStmt.setString(55, mismatchedTransaction.getIssuerBankCode());
            mStmt.setString(56, mismatchedTransaction.getFinalStatus());
            mStmt.setString(57, mismatchedTransaction.getRefundStatus());
            mStmt.setString(58, mismatchedTransaction.getTicketName());

            mStmt.executeQuery();
//            logAfterInsert("summarize_not_succ", "invoice_order_id=" + strId);

            //Done
            mConnection.commit();

        } catch (Exception ex) {
            mConnection.rollback();
            throw ex;

        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

    }

    public void updateAdjustDate(MismatchedTransaction mismatchedTransaction) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + mismatchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE mismatched_transaction SET adjust_date = ?" +
                    "WHERE  invoice_order_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setTimestamp(1, DateUtil.getSqlTimestamp(mismatchedTransaction.getAdjustDate()));
            mStmt.setString(2, mismatchedTransaction.getInvoiceOrderID());
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

    public void updateTicketNameCell(String invoiceOrderID, String ticketName) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + mismatchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE mismatched_transaction SET ticket_name = ?" +
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

    public void updateAdjustDateCell(String invoiceOrderID, Date adjustDate) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + mismatchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE mismatched_transaction SET adjust_date = ?" +
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
}
