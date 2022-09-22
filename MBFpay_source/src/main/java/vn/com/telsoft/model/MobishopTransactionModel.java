/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.admin.data.AMDataPreprocessor;
import vn.com.telsoft.entity.MobishopTransaction;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author ThongNM
 */
public class MobishopTransactionModel extends AMDataPreprocessor implements Serializable {

    public List<MobishopTransaction> getMobishopTransactions(List<String> selectedPartnerCodes,
                                                             List<String> selectedBankCodes,
                                                             Date createdTimeFrom,
                                                             Date createdTimeTo,
                                                             String searchInput,
                                                             List<String> selectedOrderStatusCodes,
                                                             List<String> selectedPayTypeNames,
                                                             List<String> selectedProductServiceCodes,
                                                             List<String> selectedProductCodes,
                                                             Date endTimeFrom,
                                                             Date endTimeTo,
                                                             HashMap<String, String> sumResults)
            throws Exception {
        Double totalAmount = 0.0;
        int totalQuantity = 0;
        Double totalPaymentAmount = 0.0;
        Double totalGrandAmount = 0.0;

        List<MobishopTransaction> mobishopTransactionList = new ArrayList<>();
        String sqlString = "";
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();
        try {
            // Find by ID
            open();

            sqlString = "SELECT \n" +
                    "    * \n" +
                    "FROM \n" +
                    "    mobishop \n" +
                    "WHERE \n" +
                    "    1 = 1 \n" +
                    "    $SEARCH_INPUT$\n" +
                    "    $CREATED_TIME_FROM$\n" +
                    "    $CREATED_TIME_TO$\n" +
                    "    $PARTNER_CODE$ \n" +
                    "    $BANK_CODE$ \n" +
                    "    $ORDER_STATUS_CODE$ \n" +
                    "    $PAY_TYPE_NAME$ \n" +
                    "    $PRODUCT_SERVICE_CODE$ \n" +
                    "    $PRODUCT_CODE$ \n" +
                    "    $END_TIME_FROM$ \n" +
                    "    $END_TIME_TO$ \n" +
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
                                "               bank_reference_data, account_id, user_type, issuer_bank_code )").replace(
                                        "?", "'" + searchInput + "'");
            } else {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "");
            }

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
                    if (element.equals("(null)")) {
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
                    if (element.equals("(null)")) {
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

//            Filter by Pay Type Name
            if (selectedPayTypeNames.size() == 0) {
                sqlString = sqlString.replace("$PAY_TYPE_NAME$", "");
            } else {
                Boolean containNull = false;
                String condition = "";

                List<String> cloneList = new ArrayList<>(selectedPayTypeNames);
                for (final ListIterator<String> items = cloneList.listIterator(); items.hasNext(); ) {
                    final String element = items.next();
                    if (element.equals("")) {
                        containNull = true;
                        items.set("''");
                    } else {
                        items.set("'" + element + "'");
                    }
                }

                if (containNull) {
                    condition = "AND (pay_type_name IN " +
                            cloneList.toString().replace("[", "(").replace("]", ") OR pay_type_name IS NULL)");
                } else {
                    condition = "AND pay_type_name IN " +
                            cloneList.toString().replace("[", "(").replace("]", ")");
                }
                sqlString = sqlString.replace("$PAY_TYPE_NAME$", condition);
            }

//            Filter by Product Service Codes
            if (selectedProductServiceCodes.size() == 0) {
                sqlString = sqlString.replace("$PRODUCT_SERVICE_CODE$", "");
            } else {
                Boolean containNull = false;
                String condition = "";

                List<String> cloneList = new ArrayList<>(selectedProductServiceCodes);
                for (final ListIterator<String> items = cloneList.listIterator(); items.hasNext(); ) {
                    final String element = items.next();
                    if (element.equals("")) {
                        containNull = true;
                        items.set("''");
                    } else {
                        items.set("'" + element + "'");
                    }
                }

                if (containNull) {
                    condition = "AND (product_service_code IN " +
                            cloneList.toString().replace("[", "(").replace("]", ") OR product_service_code IS NULL)");
                } else {
                    condition = "AND product_service_code IN " +
                            cloneList.toString().replace("[", "(").replace("]", ")");
                }
                sqlString = sqlString.replace("$PRODUCT_SERVICE_CODE$", condition);
            }

//            Filter by Product Codes
            if (selectedProductCodes.size() == 0) {
                sqlString = sqlString.replace("$PRODUCT_CODE$", "");
            } else {
                Boolean containNull = false;
                String condition = "";

                List<String> cloneList = new ArrayList<>(selectedProductCodes);
                for (final ListIterator<String> items = cloneList.listIterator(); items.hasNext(); ) {
                    final String element = items.next();
                    if (element.equals("")) {
                        containNull = true;
                        items.set("''");
                    } else {
                        items.set("'" + element + "'");
                    }
                }

                if (containNull) {
                    condition = "AND (product_code IN " +
                            cloneList.toString().replace("[", "(").replace("]", ") OR product_code IS NULL)");
                } else {
                    condition = "AND product_code IN " +
                            cloneList.toString().replace("[", "(").replace("]", ")");
                }
                sqlString = sqlString.replace("$PRODUCT_CODE$", condition);
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


            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                MobishopTransaction mobishopTransaction = new MobishopTransaction();
                mobishopTransaction.setInvoiceOrderID(mRs.getString(1));
                mobishopTransaction.setTransactionID(mRs.getString(2));
                mobishopTransaction.setSourceReceiptID(mRs.getString(3));
                mobishopTransaction.setSaleOrderID(mRs.getString(4));
                mobishopTransaction.setPayType(mRs.getString(5));
                mobishopTransaction.setPayTypeName(mRs.getString(6));
                mobishopTransaction.setPartnerCode(mRs.getString(7));
                mobishopTransaction.setPartnerReferenceID(mRs.getString(8));
                mobishopTransaction.setPartnerOrderID(mRs.getString(9));
                mobishopTransaction.setPartnerInvoiceID(mRs.getString(10));
                mobishopTransaction.setBankCode(mRs.getString(11));
                mobishopTransaction.setBankReferenceID(mRs.getString(12));
                mobishopTransaction.setProductServiceID(mRs.getString(13));
                mobishopTransaction.setProductServiceCode(mRs.getString(14));
                mobishopTransaction.setProductID(mRs.getString(15));
                mobishopTransaction.setProductCode(mRs.getString(16));
                mobishopTransaction.setProductName(mRs.getString(17));
                mobishopTransaction.setProductDetail(mRs.getString(18));
                mobishopTransaction.setDescription(mRs.getString(19));
                mobishopTransaction.setProductAccount(mRs.getString(20));
                mobishopTransaction.setAmount(mRs.getInt(21));
                mobishopTransaction.setQuantity(mRs.getInt(22));
                mobishopTransaction.setSaleDiscount(mRs.getString(23));
                mobishopTransaction.setSaleFee(mRs.getString(24));
                mobishopTransaction.setGrandAmount(mRs.getInt(25));
                mobishopTransaction.setFee(mRs.getInt(26));
                mobishopTransaction.setRelatedFee(mRs.getInt(27));
                mobishopTransaction.setPaymentAmount(mRs.getInt(28));
                mobishopTransaction.setAccountName(mRs.getString(29));
                mobishopTransaction.setRelatedAccount(mRs.getString(30));
                mobishopTransaction.setOrderStatus(mRs.getString(31));
                mobishopTransaction.setOrderStatusCode(mRs.getString(32));
                mobishopTransaction.setCreatedUser(mRs.getString(33));
                mobishopTransaction.setConfirmUser(mRs.getString(34));
                mobishopTransaction.setInitTime(mRs.getTimestamp(35));
                mobishopTransaction.setCreatedTime(mRs.getTimestamp(36));
                mobishopTransaction.setFileName(mRs.getString(37));
                mobishopTransaction.setLogDateTime(mRs.getTimestamp(38));
                mobishopTransaction.setImportDateTime(mRs.getTimestamp(39));
                mobishopTransaction.setBankReferenceData(mRs.getString(40));
                mobishopTransaction.setAccountID(mRs.getString(41));
                mobishopTransaction.setUserType(mRs.getString(42));
                mobishopTransaction.setEndTime(mRs.getTimestamp(43));
                mobishopTransaction.setTransTime(mRs.getTimestamp(44));
                mobishopTransaction.setIssuerBankCode(mRs.getString(45));

                totalAmount += mobishopTransaction.getAmount() * mobishopTransaction.getQuantity();
                totalQuantity += mobishopTransaction.getQuantity();
                totalPaymentAmount += mobishopTransaction.getPaymentAmount();
                totalGrandAmount += mobishopTransaction.getGrandAmount();

                mobishopTransactionList.add(mobishopTransaction);
            }

            sumResults.put("total_amount",
                    new DecimalFormat("###,###.###").format(totalAmount) + "");
            sumResults.put("total_quantity",
                    new DecimalFormat("###,###.###").format(totalQuantity) + "");
            sumResults.put("total_payment_amount",
                    new DecimalFormat("###,###.###").format(totalPaymentAmount) + "");
            sumResults.put("total_grand_amount",
                    new DecimalFormat("###,###.###").format(totalGrandAmount) + "");

        } catch (Exception ex) {
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.ERR, "Lỗi xảy ra trong quá trình tìm kiếm!");
            throw ex;
        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return mobishopTransactionList;
    }

    public List<String> getDistinctProductCode() throws Exception {
        List<String> distinctProductCodeList = new ArrayList<>();
        try{
            open();
            String sqlString = "SELECT DISTINCT\n" +
                    "    product_code\n" +
                    "FROM\n" +
                    "    mobishop\n" +
                    "ORDER BY\n" +
                    "    product_code";

            mStmt = mConnection.prepareStatement(sqlString);
            mRs = mStmt.executeQuery();
            while (mRs.next()){
                if(mRs.getString(1) == null){
                    distinctProductCodeList.add("");
                }else{
                    distinctProductCodeList.add(mRs.getString(1));
                }

            }

        }catch (Exception exception){
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.ERR, "Đã có lỗi xảy ra!");
            throw exception;
        }finally {
            close(mRs);
            close(mStmt);
            close();
        }
        return distinctProductCodeList;
    }

    public List<String> getDistinctPayTypeName() throws Exception {
        List<String> distinctPayTypeNames = new ArrayList<>();
        try{
            open();
            String sqlString = "SELECT DISTINCT\n" +
                    "    pay_type_name\n" +
                    "FROM\n" +
                    "    mobishop\n" +
                    "ORDER BY\n" +
                    "    pay_type_name";

            mStmt = mConnection.prepareStatement(sqlString);
            mRs = mStmt.executeQuery();
            while (mRs.next()){
                if(mRs.getString(1) == null){
                    distinctPayTypeNames.add("");
                }else{
                    distinctPayTypeNames.add(mRs.getString(1));
                }

            }

        }catch (Exception exception){
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.ERR, "Đã có lỗi xảy ra!");
            throw exception;
        }finally {
            close(mRs);
            close(mStmt);
            close();
        }
        return distinctPayTypeNames;
    }

    public List<String> getDistinctProductServiceCode() throws Exception {
        List<String> distinctProductServices = new ArrayList<>();
        try{
            open();
            String sqlString = "SELECT DISTINCT\n" +
                    "    product_service_code\n" +
                    "FROM\n" +
                    "    mobishop\n" +
                    "ORDER BY\n" +
                    "    product_service_code";

            mStmt = mConnection.prepareStatement(sqlString);
            mRs = mStmt.executeQuery();
            while (mRs.next()){
                if(mRs.getString(1) == null){
                    distinctProductServices.add("");
                }else{
                    distinctProductServices.add(mRs.getString(1));
                }

            }

        }catch (Exception exception){
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.ERR, "Đã có lỗi xảy ra!");
            throw exception;
        }finally {
            close(mRs);
            close(mStmt);
            close();
        }
        return distinctProductServices;
    }
}
