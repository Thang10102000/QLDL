/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.admin.data.AMDataPreprocessor;
import vn.com.telsoft.entity.SacombankTransaction;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author ThongNM
 */
public class SacombankTransactionModel extends AMDataPreprocessor implements Serializable {

    public List<SacombankTransaction> getSacombankTransactions(Date transactionDateFrom,
                                                               Date transactionDateTo,
                                                               List<String> selectedFileTypes,
                                                               String searchInput)
            throws Exception {

        List<SacombankTransaction> sacombankList = new ArrayList<>();
        String sqlString = "";
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();
        try {
            // Find by ID
            open();

            sqlString = "SELECT \n" +
                    "    * \n" +
                    "FROM \n" +
                    "    sacombank \n" +
                    "WHERE \n" +
                    "    1 = 1 \n" +
                    "    $SEARCH_INPUT$\n" +
                    "    $FILE_TYPE$\n" +
                    "    $TRANSACTION_DATE_FROM$\n" +
                    "    $TRANSACTION_DATE_TO$\n"
            ;

//                Filter by Search Input
            if (!searchInput.isEmpty()) {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "AND ? IN (transaction_id, transaction_remarks, bank_reference_id, file_name, file_type," +
                                "to_char(debit_amount), to_char(credit_amount), to_char(running_balance)) ").replace(
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

            //              Filter by file_type
            if (selectedFileTypes.size() == 0) {
                sqlString = sqlString.replace("$FILE_TYPE$", "");
            } else {
                Boolean containNull = false;
                String condition = "";

                List<String> cloneList = new ArrayList<>(selectedFileTypes);
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
                    condition = "AND (file_type IN " +
                            cloneList.toString().replace("[", "(").replace("]", ") OR file_type IS NULL)");
                } else {
                    condition = "AND file_type IN " +
                            cloneList.toString().replace("[", "(").replace("]", ")");
                }
                sqlString = sqlString.replace("$FILE_TYPE$", condition);
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
                SacombankTransaction sacombank = new SacombankTransaction();
                sacombank.setTransactionID(mRs.getString(1));
                sacombank.setTransactionDate(mRs.getTimestamp(2));
                sacombank.setValueDate(mRs.getTimestamp(3));
                sacombank.setTransactionRemarks(mRs.getString(4));
                sacombank.setBankReferenceID(mRs.getString(5));
                sacombank.setDebitAmount(mRs.getInt(6));
                sacombank.setCreditAmount(mRs.getInt(7));
                sacombank.setRunningBalance(mRs.getLong(8));
                sacombank.setFileName(mRs.getString(9));
                sacombank.setLogDateTime(mRs.getTimestamp(10));
                sacombank.setImportDateTime(mRs.getTimestamp(11));
                sacombank.setFileType(mRs.getString(12));
                sacombank.setId(mRs.getInt(13));

                sacombankList.add(sacombank);
            }


        } catch (Exception ex) {
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.ERR, "Lỗi xảy ra trong quá trình tìm kiếm!");
            throw ex;
        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return sacombankList;
    }
}
