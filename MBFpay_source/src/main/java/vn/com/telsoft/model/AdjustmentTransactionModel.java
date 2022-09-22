/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.admin.security.AdminUser;
import com.faplib.lib.ClientMessage;
import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.util.DateUtil;
import vn.com.telsoft.entity.AdjustmentTransaction;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author ThongNM
 */
public class AdjustmentTransactionModel extends AMDataPreprocessor implements Serializable {

    public List<AdjustmentTransaction> getAdjustmentTransactions(List<String> selectedPartnerCodes, List<String> selectedBankCodes,
                                                                 Date createdTimeFrom,
                                                                 Date createdTimeTo, String invoiceOrderID,
                                                                 List<String> selectedOrderStatusCodes,
                                                                 Date endTimeFrom,
                                                                 Date endTimeTo,
                                                                 HashMap<String, String> sumResults)
            throws Exception {
        Double totalAmount = 0.0;
        int totalQuantity = 0;
        Double totalPaymentAmount = 0.0;
        Double totalGrandAmount = 0.0;

        List<AdjustmentTransaction> adjustmentTransactionList = new ArrayList<>();
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
                        "    mobishop_gddc\n" +
                        "WHERE\n" +
                        "    invoice_order_id = ? ";
                mStmt = mConnection.prepareStatement(sqlString);
                mStmt.setString(1, invoiceOrderID);

            } else {
                sqlString = "SELECT \n" +
                        "    * \n" +
                        "FROM \n" +
                        "    mobishop_gddc \n" +
                        "WHERE \n" +
                        "    1 = 1 \n" +
                        "    $CREATED_TIME_FROM$\n" +
                        "    $CREATED_TIME_TO$\n" +
                        "    $PARTNER_CODE$ \n" +
                        "    $BANK_CODE$ \n" +
                        "    $ORDER_STATUS_CODE$ \n" +
                        "    $END_TIME_FROM$ \n" +
                        "    $END_TIME_TO$ \n" +
                        "ORDER BY \n" +
                        "    created_time";

                //                Filter by Created Time
                if (createdTimeFrom != null) {
                    sqlString = sqlString.replace("$CREATED_TIME_FROM$",
                            "AND trunc(created_time) >= ?");
                    vtRow = new Vector();
                    vtRow.addElement("Timestamp");
                    vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(createdTimeFrom));
                    vtParams.add(vtRow);
                } else {
                    sqlString = sqlString.replace("$CREATED_TIME_FROM$",
                            "");
                }

                if (createdTimeTo != null) {
                    sqlString = sqlString.replace("$CREATED_TIME_TO$",
                            "AND trunc(created_time) <= ?");
                    vtRow = new Vector();
                    vtRow.addElement("Timestamp");
                    vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(createdTimeTo));
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
                if (endTimeFrom != null) {
                    sqlString = sqlString.replace("$END_TIME_FROM$",
                            "AND trunc(adjust_date) >= ?");
                    vtRow = new Vector();
                    vtRow.addElement("Timestamp");
                    vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(endTimeFrom));
                    vtParams.add(vtRow);
                } else {
                    sqlString = sqlString.replace("$END_TIME_FROM$",
                            "");
                }

                if (endTimeTo != null) {
                    sqlString = sqlString.replace("$END_TIME_TO$",
                            "AND trunc(adjust_date) <= ?");
                    vtRow = new Vector();
                    vtRow.addElement("Timestamp");
                    vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(endTimeTo));
                    vtParams.add(vtRow);
                } else {
                    sqlString = sqlString.replace("$END_TIME_TO$",
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
                AdjustmentTransaction adjustmentTransaction = new AdjustmentTransaction();
                adjustmentTransaction.setInvoiceOrderID(mRs.getString(1));
                adjustmentTransaction.setTransactionID(mRs.getString(2));
                adjustmentTransaction.setSourceReceiptID(mRs.getString(3));
                adjustmentTransaction.setSaleOrderID(mRs.getString(4));
                adjustmentTransaction.setPayType(mRs.getString(5));
                adjustmentTransaction.setPayTypeName(mRs.getString(6));
                adjustmentTransaction.setPartnerCode(mRs.getString(7));
                adjustmentTransaction.setPartnerReferenceID(mRs.getString(8));
                adjustmentTransaction.setPartnerOrderID(mRs.getString(9));
                adjustmentTransaction.setPartnerInvoiceID(mRs.getString(10));
                adjustmentTransaction.setBankCode(mRs.getString(11));
                adjustmentTransaction.setBankReferenceID(mRs.getString(12));
                adjustmentTransaction.setProductServiceID(mRs.getString(13));
                adjustmentTransaction.setProductServiceCode(mRs.getString(14));
                adjustmentTransaction.setProductID(mRs.getString(15));
                adjustmentTransaction.setProductCode(mRs.getString(16));
                adjustmentTransaction.setProductName(mRs.getString(17));
                adjustmentTransaction.setProductDetail(mRs.getString(18));
                adjustmentTransaction.setDescription(mRs.getString(19));
                adjustmentTransaction.setProductAccount(mRs.getString(20));
                adjustmentTransaction.setAmount(mRs.getInt(21));
                adjustmentTransaction.setQuantity(mRs.getInt(22));
                adjustmentTransaction.setSaleDiscount(mRs.getString(23));
                adjustmentTransaction.setSaleFee(mRs.getString(24));
                adjustmentTransaction.setGrandAmount(mRs.getInt(25));
                adjustmentTransaction.setFee(mRs.getInt(26));
                adjustmentTransaction.setRelatedFee(mRs.getInt(27));
                adjustmentTransaction.setPaymentAmount(mRs.getInt(28));
                adjustmentTransaction.setAccountName(mRs.getString(29));
                adjustmentTransaction.setRelatedAccount(mRs.getString(30));
                adjustmentTransaction.setOrderStatus(mRs.getString(31));
                adjustmentTransaction.setOrderStatusCode(mRs.getString(32));
                adjustmentTransaction.setCreatedUser(mRs.getString(33));
                adjustmentTransaction.setConfirmUser(mRs.getString(34));
                adjustmentTransaction.setInitTime(mRs.getTimestamp(35));
                adjustmentTransaction.setCreatedTime(mRs.getTimestamp(36));
                adjustmentTransaction.setFileName(mRs.getString(37));
                adjustmentTransaction.setLogDateTime(mRs.getTimestamp(38));
                adjustmentTransaction.setImportDateTime(mRs.getTimestamp(39));
                adjustmentTransaction.setBankReferenceData(mRs.getString(40));
                adjustmentTransaction.setAccountID(mRs.getString(41));
                adjustmentTransaction.setUserType(mRs.getString(42));
                adjustmentTransaction.setEndTime(mRs.getTimestamp(43));
                adjustmentTransaction.setTransTime(mRs.getTimestamp(44));
                adjustmentTransaction.setIssuerBankCode(mRs.getString(45));

                totalAmount += adjustmentTransaction.getAmount() * adjustmentTransaction.getQuantity();
                totalQuantity += adjustmentTransaction.getQuantity();
                totalPaymentAmount += adjustmentTransaction.getPaymentAmount();
                totalGrandAmount += adjustmentTransaction.getGrandAmount();

                adjustmentTransactionList.add(adjustmentTransaction);
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

        return adjustmentTransactionList;
    }
}
