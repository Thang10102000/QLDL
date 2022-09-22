/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.admin.data.AMDataPreprocessor;
import vn.com.telsoft.entity.BidvTransaction;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author ThongNM
 */
public class BidvTransactionModel extends AMDataPreprocessor implements Serializable {

    public List<BidvTransaction> getBidvTransactions(Date transactionDateFrom,
                                                               Date transactionDateTo,
                                                               String searchInput)
            throws Exception {

        List<BidvTransaction> bidvTransactionList = new ArrayList<>();
        String sqlString = "";
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();
        try {
            // Find by ID
            open();

            sqlString = "SELECT \n" +
                    "    * \n" +
                    "FROM \n" +
                    "    bidv \n" +
                    "WHERE \n" +
                    "    1 = 1 \n" +
                    "    $SEARCH_INPUT$\n" +
                    "    $TRANSACTION_DATE_FROM$\n" +
                    "    $TRANSACTION_DATE_TO$\n"
            ;

//                Filter by Search Input
            if (!searchInput.isEmpty()) {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "AND ? IN (to_char(debit), to_char(credit), to_char(balance), " +
                                "transaction_description, bank_reference_id, transaction_number, " +
                                "file_name) ").replace(
                        "?", "'" + searchInput + "'");
            } else {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "");
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
                BidvTransaction bidvTransaction = new BidvTransaction();
                bidvTransaction.setTransactionDate(mRs.getTimestamp(1));
                bidvTransaction.setDebit(mRs.getInt(2));
                bidvTransaction.setCredit(mRs.getInt(3));
                bidvTransaction.setBalance(mRs.getLong(4));
                bidvTransaction.setTransactionDescription(mRs.getString(5));
                bidvTransaction.setBankReferenceID(mRs.getString(6));
                bidvTransaction.setTransactionNumber(mRs.getString(7));
                bidvTransaction.setImportDateTime(mRs.getTimestamp(8));
                bidvTransaction.setFileName(mRs.getString(9));
                bidvTransaction.setValueDate(mRs.getTimestamp(10));
                bidvTransaction.setId(mRs.getInt(11));

                bidvTransactionList.add(bidvTransaction);
            }


        } catch (Exception ex) {
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.ERR, "Lỗi xảy ra trong quá trình tìm kiếm!");
            throw ex;
        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return bidvTransactionList;
    }
}
