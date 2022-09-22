/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.admin.data.AMDataPreprocessor;
import vn.com.telsoft.entity.NapasTransaction;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author ThongNM
 */
public class NapasTransactionModel extends AMDataPreprocessor implements Serializable {

    public List<NapasTransaction> getNapasTransactions( List<String> selectedMerchantCodes,
                                                        List<String> selectedACQBankCodes,
                                                        List<String> selectedIssuerBankCodes,
                                                        Date transactionDateFrom,
                                                       Date transactionDateTo,
                                                       String searchInput)
            throws Exception {

        List<NapasTransaction> napasTransactionList = new ArrayList<>();
        String sqlString = "";
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();
        try {
            // Find by ID
            open();

            sqlString = "SELECT \n" +
                    "    * \n" +
                    "FROM \n" +
                    "    napas \n" +
                    "WHERE \n" +
                    "    1 = 1 \n" +
                    "    $SEARCH_INPUT$\n" +
                    "    $MERCHANT_CODE$\n" +
                    "    $ACQ_BANK_CODE$\n" +
                    "    $ISSUER_BANK_CODE$\n" +
                    "    $TRANSACTION_DATE_FROM$\n" +
                    "    $TRANSACTION_DATE_TO$\n"
            ;

//                Filter by Search Input
            if (!searchInput.isEmpty()) {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "AND ? IN (merchant_code, acq_bank_code, issuer_bank_code, transaction_info," +
                                "to_char(amount), response_code, transaction_status, status, to_char(transaction_ref)," +
                                "card_number, card_holder_name, transaction_type, bank_id, ip_address, service_type, " +
                                "napas_order_id, submerchant_code, file_name, file_type) ").replace(
                        "?", "'" + searchInput + "'");
            } else {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "");
            }

            //                Filter by Transaction Date
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

            //              Filter by merchant_code
            if (selectedMerchantCodes.size() == 0) {
                sqlString = sqlString.replace("$MERCHANT_CODE$", "");
            } else {
                Boolean containNull = false;
                String condition = "";

                List<String> cloneList = new ArrayList<>(selectedMerchantCodes);
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
                    condition = "AND (merchant_code IN " +
                            cloneList.toString().replace("[", "(").replace("]", ") OR merchant_code IS NULL)");
                } else {
                    condition = "AND merchant_code IN " +
                            cloneList.toString().replace("[", "(").replace("]", ")");
                }
                sqlString = sqlString.replace("$MERCHANT_CODE$", condition);
            }

            //              Filter by ACQ_BANK_CODE
            if (selectedACQBankCodes.size() == 0) {
                sqlString = sqlString.replace("$ACQ_BANK_CODE$", "");
            } else {
                Boolean containNull = false;
                String condition = "";

                List<String> cloneList = new ArrayList<>(selectedACQBankCodes);
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
                    condition = "AND (acq_bank_code IN " +
                            cloneList.toString().replace("[", "(").replace("]", ") OR acq_bank_code IS NULL)");
                } else {
                    condition = "AND acq_bank_code IN " +
                            cloneList.toString().replace("[", "(").replace("]", ")");
                }
                sqlString = sqlString.replace("$ACQ_BANK_CODE$", condition);
            }

            //              Filter by ISSUER_BANK_CODE
            if (selectedIssuerBankCodes.size() == 0) {
                sqlString = sqlString.replace("$ISSUER_BANK_CODE$", "");
            } else {
                Boolean containNull = false;
                String condition = "";

                List<String> cloneList = new ArrayList<>(selectedIssuerBankCodes);
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
                    condition = "AND (issuer_bank_code IN " +
                            cloneList.toString().replace("[", "(").replace("]", ") OR issuer_bank_code IS NULL)");
                } else {
                    condition = "AND issuer_bank_code IN " +
                            cloneList.toString().replace("[", "(").replace("]", ")");
                }
                sqlString = sqlString.replace("$ISSUER_BANK_CODE$", condition);
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
                NapasTransaction napasTransaction = new NapasTransaction();
                napasTransaction.setTransactionID(mRs.getString(1));
                napasTransaction.setMerchantCode(mRs.getString(2));
                napasTransaction.setAcqBankCode(mRs.getString(3));
                napasTransaction.setIssuerBankCode(mRs.getString(4));
                napasTransaction.setTransactionDate(mRs.getTimestamp(5));
                napasTransaction.setTransactionInfo(mRs.getString(6));
                napasTransaction.setAmount(mRs.getInt(7));
                napasTransaction.setResponseCode(mRs.getString(8));
                napasTransaction.setTransactionStatus(mRs.getString(9));
                napasTransaction.setStatus(mRs.getString(10));
                napasTransaction.setTransactionRef(mRs.getString(11));
                napasTransaction.setCardNumber(mRs.getString(12));
                napasTransaction.setCardHolderName(mRs.getString(13));
                napasTransaction.setTransactionType(mRs.getString(14));
                napasTransaction.setBankID(mRs.getString(15));
                napasTransaction.setIpAddress(mRs.getString(16));
                napasTransaction.setServiceType(mRs.getString(17));
                napasTransaction.setNapasOrderID(mRs.getString(18));
                napasTransaction.setSubmerchantCode(mRs.getString(19));
                napasTransaction.setFileName(mRs.getString(20));
                napasTransaction.setImportDateTime( mRs.getTimestamp(21));
                napasTransaction.setFileType(mRs.getString(22));

                napasTransactionList.add(napasTransaction);
            }


        } catch (Exception ex) {
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.ERR, "Lỗi xảy ra trong quá trình tìm kiếm!");
            throw ex;
        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return napasTransactionList;
    }

    public List<String> getDistinctMerchantCode() throws Exception {
        List<String> distinctMerchantCodes = new ArrayList<>();
        try{
            open();
            String sqlString = "SELECT DISTINCT\n" +
                    "    merchant_code \n" +
                    "FROM\n" +
                    "    napas\n" +
                    "ORDER BY\n" +
                    "    merchant_code";

            mStmt = mConnection.prepareStatement(sqlString);
            mRs = mStmt.executeQuery();
            while (mRs.next()){
                if(mRs.getString(1) == null){
                    distinctMerchantCodes.add("");
                }else{
                    distinctMerchantCodes.add(mRs.getString(1));
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
        return distinctMerchantCodes;
    }

    public List<String> getDistinctACQBankCode() throws Exception {
        List<String> distinctACQBankCodes = new ArrayList<>();
        try{
            open();
            String sqlString = "SELECT DISTINCT\n" +
                    "    acq_bank_code \n" +
                    "FROM\n" +
                    "    napas\n" +
                    "ORDER BY\n" +
                    "    acq_bank_code";

            mStmt = mConnection.prepareStatement(sqlString);
            mRs = mStmt.executeQuery();
            while (mRs.next()){
                if(mRs.getString(1) == null){
                    distinctACQBankCodes.add("");
                }else{
                    distinctACQBankCodes.add(mRs.getString(1));
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
        return distinctACQBankCodes;
    }

    public List<String> getDistinctIssuerBankCode() throws Exception {
        List<String> distinctIssuerBankCodes = new ArrayList<>();
        try{
            open();
            String sqlString = "SELECT DISTINCT\n" +
                    "    issuer_bank_code \n" +
                    "FROM\n" +
                    "    napas\n" +
                    "ORDER BY\n" +
                    "    issuer_bank_code";

            mStmt = mConnection.prepareStatement(sqlString);
            mRs = mStmt.executeQuery();
            while (mRs.next()){
                if(mRs.getString(1) == null){
                    distinctIssuerBankCodes.add("");
                }else{
                    distinctIssuerBankCodes.add(mRs.getString(1));
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
        return distinctIssuerBankCodes;
    }
}
