/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.admin.security.AdminUser;
import com.faplib.lib.ClientMessage;
import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.util.DateUtil;
import vn.com.telsoft.entity.MatchedTransaction;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author ThongNM
 */
public class MatchedTransactionModel extends AMDataPreprocessor implements Serializable {

    public List<MatchedTransaction> getMatchedTransactions(List<String> selectedPartnerCodes, List<String> selectedBankCodes,
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

        List<MatchedTransaction> matchedTransactionList = new ArrayList<>();
        String sqlString = "";
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();
        try {
            // Find by ID
            open();

            sqlString = "SELECT \n" +
                    "    * \n" +
                    "FROM \n" +
                    "    matched_transaction \n" +
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
                    "    $ORDER_STATUS_CODE$ \n" +
                    "    $FINAL_STATUS$ \n" +
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
                MatchedTransaction matchedTransaction = new MatchedTransaction();
                matchedTransaction.setInvoiceOrderID(mRs.getString(1));
                matchedTransaction.setTransactionID(mRs.getString(2));
                matchedTransaction.setSourceReceiptID(mRs.getString(3));
                matchedTransaction.setSaleOrderID(mRs.getString(4));
                matchedTransaction.setPayType(mRs.getString(5));
                matchedTransaction.setPayTypeName(mRs.getString(6));
                matchedTransaction.setPartnerCode(mRs.getString(7));
                matchedTransaction.setPartnerReferenceID(mRs.getString(8));
                matchedTransaction.setPartnerOrderID(mRs.getString(9));
                matchedTransaction.setPartnerInvoiceID(mRs.getString(10));
                matchedTransaction.setBankCode(mRs.getString(11));
                matchedTransaction.setBankReferenceID(mRs.getString(12));
                matchedTransaction.setProductServiceID(mRs.getString(13));
                matchedTransaction.setProductServiceCode(mRs.getString(14));
                matchedTransaction.setProductID(mRs.getString(15));
                matchedTransaction.setProductCode(mRs.getString(16));
                matchedTransaction.setProductName(mRs.getString(17));
                matchedTransaction.setProductDetail(mRs.getString(18));
                matchedTransaction.setDescription(mRs.getString(19));
                matchedTransaction.setProductAccount(mRs.getString(20));
                matchedTransaction.setAmount(mRs.getInt(21));
                matchedTransaction.setQuantity(mRs.getInt(22));
                matchedTransaction.setSaleDiscount(mRs.getString(23));
                matchedTransaction.setSaleFee(mRs.getString(24));
                matchedTransaction.setGrandAmount(mRs.getInt(25));
                matchedTransaction.setFee(mRs.getInt(26));
                matchedTransaction.setRelatedFee(mRs.getInt(27));
                matchedTransaction.setPaymentAmount(mRs.getInt(28));
                matchedTransaction.setAccountName(mRs.getString(29));
                matchedTransaction.setRelatedAccount(mRs.getString(30));
                matchedTransaction.setOrderStatus(mRs.getString(31));
                matchedTransaction.setOrderStatusCode(mRs.getString(32));
                matchedTransaction.setCreatedUser(mRs.getString(33));
                matchedTransaction.setConfirmUser(mRs.getString(34));
                matchedTransaction.setInitTime(mRs.getTimestamp(35));
                matchedTransaction.setCreatedTime(mRs.getTimestamp(36));
                matchedTransaction.setFileName(mRs.getString(37));
                matchedTransaction.setLogDateTime(mRs.getTimestamp(38));
                matchedTransaction.setImportDateTime(mRs.getTimestamp(39));
                matchedTransaction.setBankReferenceData(mRs.getString(40));
                matchedTransaction.setProfileID(mRs.getString(41));
                matchedTransaction.setPartnerTransID(mRs.getString(42));
                matchedTransaction.setForcontrolCode(mRs.getString(43));
                matchedTransaction.setPartnerStatus(mRs.getString(44));
                matchedTransaction.setSumDate(mRs.getTimestamp(45));
                matchedTransaction.setAdjustDate(mRs.getTimestamp(46));
                matchedTransaction.setProcessStatus(mRs.getString(47));
                matchedTransaction.setUserProcess(mRs.getString(48));
                matchedTransaction.setConfirmStatus(mRs.getString(49));
                matchedTransaction.setUserConfirm(mRs.getString(50));
                matchedTransaction.setAccountID(mRs.getString(51));
                matchedTransaction.setUserType(mRs.getString(52));
                matchedTransaction.setEndTime(mRs.getTimestamp(53));
                matchedTransaction.setTransTime(mRs.getTimestamp(54));
                matchedTransaction.setIssuerBankCode(mRs.getString(55));
                matchedTransaction.setFinalStatus(mRs.getString(56));
                matchedTransaction.setRefundStatus(mRs.getString(57));
                matchedTransaction.setTicketName(mRs.getString(58));

                totalAmount += matchedTransaction.getAmount() * matchedTransaction.getQuantity();
                totalQuantity += matchedTransaction.getQuantity();
                totalPaymentAmount += matchedTransaction.getPaymentAmount();
                totalGrandAmount += matchedTransaction.getGrandAmount();

                matchedTransactionList.add(matchedTransaction);
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

        return matchedTransactionList;
    }

    public void processTransaction(MatchedTransaction matchedTransaction) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + matchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE matched_transaction SET process_status = ?, " +
                    "user_process = ?, confirm_status = ? " +
                    "WHERE  invoice_order_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, matchedTransaction.getProcessStatus());
            mStmt.setString(2, matchedTransaction.getUserProcess());
            mStmt.setString(3, matchedTransaction.getConfirmStatus());
            mStmt.setString(4, matchedTransaction.getInvoiceOrderID());
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

    public void confirmProcess(MatchedTransaction matchedTransaction) throws Exception {
        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + matchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE matched_transaction SET confirm_status = ?, " +
                    "user_confirm = ?" +
                    "WHERE  invoice_order_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, matchedTransaction.getConfirmStatus());
            mStmt.setString(2, AdminUser.getUserLogged().getUserName());
            mStmt.setString(3, matchedTransaction.getInvoiceOrderID());
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

    public void updateRefundStatus(MatchedTransaction matchedTransaction) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + matchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE matched_transaction SET refund_status = ?" +
                    "WHERE  invoice_order_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, matchedTransaction.getRefundStatus());
            mStmt.setString(2, matchedTransaction.getInvoiceOrderID());
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

    public void handleDeduction(MatchedTransaction matchedTransaction) throws Exception {
//        String strId = "";
        try {
            open();
            mConnection.setAutoCommit(false);
//            strId = String.valueOf(SQLUtil.getSequenceValue(mConnection, "summarize_not_succ_seq"));
//            app.setProfile_id(strId);

            //Update app
            String strSQL = "    INSERT INTO mismatched_transaction VALUES (\n" +
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
            mStmt.setString(1, matchedTransaction.getInvoiceOrderID());
            mStmt.setString(2, matchedTransaction.getTransactionID());
            mStmt.setString(3, matchedTransaction.getSourceReceiptID());
            mStmt.setString(4, matchedTransaction.getSaleOrderID());
            mStmt.setString(5, matchedTransaction.getPayType());
            mStmt.setString(6, matchedTransaction.getPayTypeName());
            mStmt.setString(7, matchedTransaction.getPartnerCode());
            mStmt.setString(8, matchedTransaction.getPartnerReferenceID());
            mStmt.setString(9, matchedTransaction.getPartnerOrderID());
            mStmt.setString(10, matchedTransaction.getPartnerInvoiceID());
            mStmt.setString(11, matchedTransaction.getBankCode());
            mStmt.setString(12, matchedTransaction.getBankReferenceID());
            mStmt.setString(13, matchedTransaction.getProductServiceID());
            mStmt.setString(14, matchedTransaction.getProductServiceCode());
            mStmt.setString(15, matchedTransaction.getProductID());
            mStmt.setString(16, matchedTransaction.getProductCode());
            mStmt.setString(17, matchedTransaction.getProductName());
            mStmt.setString(18, matchedTransaction.getProductDetail());
            mStmt.setString(19, matchedTransaction.getDescription());
            mStmt.setString(20, matchedTransaction.getProductAccount());
            mStmt.setInt(21, matchedTransaction.getAmount());
            mStmt.setInt(22, matchedTransaction.getQuantity());
            mStmt.setString(23, matchedTransaction.getSaleDiscount());
            mStmt.setString(24, matchedTransaction.getSaleFee());
            mStmt.setInt(25, matchedTransaction.getGrandAmount());
            mStmt.setInt(26, matchedTransaction.getFee());
            mStmt.setInt(27, matchedTransaction.getRelatedFee());
            mStmt.setInt(28, matchedTransaction.getPaymentAmount());
            mStmt.setString(29, matchedTransaction.getAccountName());
            mStmt.setString(30, matchedTransaction.getRelatedAccount());
            mStmt.setString(31, matchedTransaction.getOrderStatus());
            mStmt.setString(32, matchedTransaction.getOrderStatusCode());
            mStmt.setString(33, matchedTransaction.getCreatedUser());
            mStmt.setString(34, matchedTransaction.getConfirmUser());
            mStmt.setTimestamp(35, DateUtil.getSqlTimestamp(matchedTransaction.getInitTime()));
            mStmt.setTimestamp(36, DateUtil.getSqlTimestamp(matchedTransaction.getCreatedTime()));
            mStmt.setString(37, matchedTransaction.getFileName());
            mStmt.setTimestamp(38, DateUtil.getSqlTimestamp(matchedTransaction.getLogDateTime()));
            mStmt.setTimestamp(39, DateUtil.getSqlTimestamp(matchedTransaction.getImportDateTime()));
            mStmt.setString(40, matchedTransaction.getBankReferenceData());
            mStmt.setString(41, matchedTransaction.getProfileID());
            mStmt.setString(42, matchedTransaction.getPartnerTransID());
            mStmt.setString(43, matchedTransaction.getForcontrolCode());
            mStmt.setString(44, matchedTransaction.getPartnerStatus());
            mStmt.setTimestamp(45, DateUtil.getSqlTimestamp(matchedTransaction.getSumDate()));
            mStmt.setTimestamp(46, DateUtil.getSqlTimestamp(matchedTransaction.getAdjustDate()));
            mStmt.setString(47, matchedTransaction.getProcessStatus());
            mStmt.setString(48, matchedTransaction.getUserProcess());
            mStmt.setString(49, matchedTransaction.getConfirmStatus());
            mStmt.setString(50, matchedTransaction.getUserConfirm());
            mStmt.setString(51, matchedTransaction.getAccountID());
            mStmt.setString(52, matchedTransaction.getUserType());
            mStmt.setTimestamp(53, DateUtil.getSqlTimestamp(matchedTransaction.getEndTime()));
            mStmt.setTimestamp(54, DateUtil.getSqlTimestamp(matchedTransaction.getTransTime()));
            mStmt.setString(55, matchedTransaction.getRefundStatus());
            mStmt.setString(56, matchedTransaction.getTicketName());
            mStmt.setString(57, matchedTransaction.getIssuerBankCode());
            mStmt.setString(58, matchedTransaction.getFinalStatus());

            mStmt.executeQuery();

            // delete transaction in matched_transaction table after insert into mismatched_transaction
            strSQL = "DELETE FROM matched_transaction\n" +
                    "WHERE\n" +
                    "    invoice_order_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, matchedTransaction.getInvoiceOrderID());

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

    public void updateTicketNameCell(String invoiceOrderID, String ticketName) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + matchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE matched_transaction SET ticket_name = ?" +
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

}
