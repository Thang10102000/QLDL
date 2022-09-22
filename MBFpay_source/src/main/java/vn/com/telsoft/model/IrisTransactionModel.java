/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.admin.data.AMDataPreprocessor;
import vn.com.telsoft.entity.IrisTransaction;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author ThongNM
 */
public class IrisTransactionModel extends AMDataPreprocessor implements Serializable {

    public List<IrisTransaction> getIrisTransactions(Date transactionDateFrom,
                                                             Date transactionDateTo,
                                                     List<String> selectedTypes,
                                                             String searchInput)
            throws Exception {

        List<IrisTransaction> irisTransactionList = new ArrayList<>();
        String sqlString = "";
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();
        try {
            // Find by ID
            open();

            sqlString = "SELECT \n" +
                    "    * \n" +
                    "FROM \n" +
                    "    Iris \n" +
                    "WHERE \n" +
                    "    1 = 1 \n" +
                    "    $SEARCH_INPUT$\n" +
                    "    $TYPE$\n" +
                    "    $TRANSACTION_DATETIME_FROM$\n" +
                    "    $TRANSACTION_DATETIME_TO$\n"
            ;

//                Filter by Search Input
            if (!searchInput.isEmpty()) {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "AND ? IN ( transaction_id, type, order_number, product, to_char(amount),\n" +
                                " to_char(quantity), to_char(grand_amount), status, file_name )").replace(
                        "?", "'" + searchInput + "'");
            } else {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "");
            }

            //                Filter by Created Time
            if (transactionDateFrom != null) {
                sqlString = sqlString.replace("$TRANSACTION_DATETIME_FROM$",
                        "AND trunc(transaction_datetime) >= ?");
                vtRow = new Vector();
                vtRow.addElement("Timestamp");
                vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(transactionDateFrom));
                vtParams.add(vtRow);
            } else {
                sqlString = sqlString.replace("$TRANSACTION_DATETIME_FROM$",
                        "");
            }

            if (transactionDateTo != null) {
                sqlString = sqlString.replace("$TRANSACTION_DATETIME_TO$",
                        "AND trunc(transaction_datetime) <= ?");
                vtRow = new Vector();
                vtRow.addElement("Timestamp");
                vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(transactionDateTo));
                vtParams.add(vtRow);
            }else {
                sqlString = sqlString.replace("$TRANSACTION_DATETIME_TO$",
                        "");
            }

            //              Filter by file_type
            if (selectedTypes.size() == 0) {
                sqlString = sqlString.replace("$TYPE$", "");
            } else {
                Boolean containNull = false;
                String condition = "";

                List<String> cloneList = new ArrayList<>(selectedTypes);
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
                    condition = "AND (lower(type) IN " +
                            cloneList.toString().replace("[", "(").replace("]", ") OR type IS NULL)");
                } else {
                    condition = "AND lower(type) IN " +
                            cloneList.toString().replace("[", "(").replace("]", ")");
                }
                sqlString = sqlString.replace("$TYPE$", condition);
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
                IrisTransaction irisTransaction = new IrisTransaction();
                irisTransaction.setTransactionID(mRs.getString(1));
                irisTransaction.setType(mRs.getString(2));
                irisTransaction.setOrderNumber(mRs.getString(3));
                irisTransaction.setProduct(mRs.getString(4));
                irisTransaction.setAmount(mRs.getInt(5));
                irisTransaction.setQuantity(mRs.getInt(6));
                irisTransaction.setGrandAmount(mRs.getInt(7));
                irisTransaction.setTransactionDateTime(mRs.getTimestamp(8));
                irisTransaction.setStatus(mRs.getString(9));
                irisTransaction.setExportDateTime(mRs.getTimestamp(10));
                irisTransaction.setFileName(mRs.getString(11));
                irisTransaction.setImportDateTime(mRs.getTimestamp(12));
                irisTransaction.setId(mRs.getInt(13));

                irisTransactionList.add(irisTransaction);
            }


        } catch (Exception ex) {
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.ERR, "Lỗi xảy ra trong quá trình tìm kiếm!");
            throw ex;
        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return irisTransactionList;
    }
}
