/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.admin.data.AMDataPreprocessor;
import vn.com.telsoft.entity.CenterCodeBundleTransaction;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author ThongNM
 */
public class CenterCodeBundleTransactionModel extends AMDataPreprocessor implements Serializable {

    public List<CenterCodeBundleTransaction> getCenterCodeBundleTransactions(Date transactionDateFrom,
                                                                             Date transactionDateTo,
                                                                             List<String> selectedProductNames,
                                                                             String searchInput)
            throws Exception {

        List<CenterCodeBundleTransaction> centerCodeBundleTransactionList = new ArrayList<>();
        String sqlString = "";
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();
        try {
            // Find by ID
            open();

            sqlString = "SELECT \n" +
                    "    * \n" +
                    "FROM \n" +
                    "    center_code_bundle \n" +
                    "WHERE \n" +
                    "    1 = 1 \n" +
                    "    $SEARCH_INPUT$\n" +
                    "    $PRODUCT_NAME$\n" +
                    "    $REQUEST_TIME_FROM$\n" +
                    "    $REQUEST_TIME_TO$\n"
            ;

//                Filter by Search Input
            if (!searchInput.isEmpty()) {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "AND ? IN (trans_id, user_name, mobile, status, product_name," +
                                "to_char(product_price), to_char(quantity), from_serial, to_serial, file_name) ").replace(
                        "?", "'" + searchInput + "'");
            } else {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "");
            }

            //                Filter by Created Time
            if (transactionDateFrom != null) {
                sqlString = sqlString.replace("$REQUEST_TIME_FROM$",
                        "AND trunc(request_time) >= ?");
                vtRow = new Vector();
                vtRow.addElement("Timestamp");
                vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(transactionDateFrom));
                vtParams.add(vtRow);
            } else {
                sqlString = sqlString.replace("$REQUEST_TIME_FROM$",
                        "");
            }

            if (transactionDateTo != null) {
                sqlString = sqlString.replace("$REQUEST_TIME_TO$",
                        "AND trunc(request_time) <= ?");
                vtRow = new Vector();
                vtRow.addElement("Timestamp");
                vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(transactionDateTo));
                vtParams.add(vtRow);
            } else {
                sqlString = sqlString.replace("$REQUEST_TIME_TO$",
                        "");
            }

            //              Filter by file_type
            if (selectedProductNames.size() == 0) {
                sqlString = sqlString.replace("$PRODUCT_NAME$", "");
            } else {
                Boolean containNull = false;
                String condition = "";

                List<String> cloneList = new ArrayList<>(selectedProductNames);
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
                    condition = "AND (product_name IN " +
                            cloneList.toString().replace("[", "(").replace("]", ") OR product_name IS NULL)");
                } else {
                    condition = "AND product_name IN " +
                            cloneList.toString().replace("[", "(").replace("]", ")");
                }
                sqlString = sqlString.replace("$PRODUCT_NAME$", condition);
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
                CenterCodeBundleTransaction centerCodeBundleTransaction = new CenterCodeBundleTransaction();
                centerCodeBundleTransaction.setTransID(mRs.getString(1));
                centerCodeBundleTransaction.setRequestTime(mRs.getTimestamp(2));
                centerCodeBundleTransaction.setUserName(mRs.getString(3));
                centerCodeBundleTransaction.setMobile(mRs.getString(4));
                centerCodeBundleTransaction.setStatus(mRs.getString(5));
                centerCodeBundleTransaction.setProductName(mRs.getString(6));
                centerCodeBundleTransaction.setProductPrice(mRs.getInt(7));
                centerCodeBundleTransaction.setQuantity(mRs.getInt(8));
                centerCodeBundleTransaction.setFromSerial(mRs.getString(9));
                centerCodeBundleTransaction.setToSerial(mRs.getString(10));
                centerCodeBundleTransaction.setFileName(mRs.getString(11));
                centerCodeBundleTransaction.setLogDateTime(mRs.getTimestamp(12));
                centerCodeBundleTransaction.setImportDateTime(mRs.getTimestamp(13));
                centerCodeBundleTransaction.setId(mRs.getInt(14));

                centerCodeBundleTransactionList.add(centerCodeBundleTransaction);
            }


        } catch (Exception ex) {
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.ERR, "Lỗi xảy ra trong quá trình tìm kiếm!");
            throw ex;
        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return centerCodeBundleTransactionList;
    }
}
