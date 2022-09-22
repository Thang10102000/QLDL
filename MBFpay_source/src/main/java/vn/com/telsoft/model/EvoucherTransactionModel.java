/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.admin.data.AMDataPreprocessor;
import vn.com.telsoft.entity.EvoucherTransaction;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author ThongNM
 */
public class EvoucherTransactionModel extends AMDataPreprocessor implements Serializable {

    public List<EvoucherTransaction> getEvoucherTransactions(Date transactionDateFrom,
                                                                             Date transactionDateTo,
                                                                             String searchInput)
            throws Exception {

        List<EvoucherTransaction> evoucherTransactionList = new ArrayList<>();
        String sqlString = "";
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();
        try {
            // Find by ID
            open();

            sqlString = "SELECT \n" +
                    "    * \n" +
                    "FROM \n" +
                    "    evoucher \n" +
                    "WHERE \n" +
                    "    1 = 1 \n" +
                    "    $SEARCH_INPUT$\n" +
                    "    $TRANS_DATETIME_FROM$\n" +
                    "    $TRANS_DATETIME_TO$\n"
            ;

//                Filter by Search Input
            if (!searchInput.isEmpty()) {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "AND ? IN (trans_id, type, process_status, daily, target_isdn, " +
                                "to_char(sotien), reference2, file_name) ").replace(
                        "?", "'%" + searchInput + "%'");
            } else {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "");
            }

            //                Filter by Created Time
            if (transactionDateFrom != null) {
                sqlString = sqlString.replace("$TRANS_DATETIME_FROM$",
                        "AND trunc(trans_datetime) >= ?");
                vtRow = new Vector();
                vtRow.addElement("Timestamp");
                vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(transactionDateFrom));
                vtParams.add(vtRow);
            } else {
                sqlString = sqlString.replace("$TRANS_DATETIME_FROM$",
                        "");
            }

            if (transactionDateTo != null) {
                sqlString = sqlString.replace("$TRANS_DATETIME_TO$",
                        "AND trunc(trans_datetime) <= ?");
                vtRow = new Vector();
                vtRow.addElement("Timestamp");
                vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(transactionDateTo));
                vtParams.add(vtRow);
            }else {
                sqlString = sqlString.replace("$TRANS_DATETIME_TO$",
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
                EvoucherTransaction evoucherTransaction = new EvoucherTransaction();
                evoucherTransaction.setTransID(mRs.getString(1));
                evoucherTransaction.setTransDateTime(mRs.getTimestamp(2));
                evoucherTransaction.setType(mRs.getString(3));
                evoucherTransaction.setProcessStatus(mRs.getString(4));
                evoucherTransaction.setDaily(mRs.getString(5));
                evoucherTransaction.setTargetISDN(mRs.getString(6));
                evoucherTransaction.setAmount(mRs.getInt(7));
                evoucherTransaction.setReference2(mRs.getString(8));
                evoucherTransaction.setFileName(mRs.getString(9));
                evoucherTransaction.setLogDateTime(mRs.getTimestamp(10));
                evoucherTransaction.setImportDateTime(mRs.getTimestamp(11));
                evoucherTransaction.setId(mRs.getInt(12));

                evoucherTransactionList.add(evoucherTransaction);
            }


        } catch (Exception ex) {
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.ERR, "Lỗi xảy ra trong quá trình tìm kiếm!");
            throw ex;
        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return evoucherTransactionList;
    }
}
