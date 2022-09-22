/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.admin.data.AMDataPreprocessor;
import vn.com.telsoft.entity.PmgwTransaction;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author ThongNM
 */
public class PmgwTransactionModel extends AMDataPreprocessor implements Serializable {

    public List<PmgwTransaction> getPmgwTransactions(Date transactionDateFrom,
                                                     Date transactionDateTo,
                                                     List<String> selectedCenterCodes,
                                                     String searchInput)
            throws Exception {

        List<PmgwTransaction> pmgwTransactionList = new ArrayList<>();
        String sqlString = "";
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();
        try {
            // Find by ID
            open();

            sqlString = "SELECT \n" +
                    "    * \n" +
                    "FROM \n" +
                    "    payment_gateway \n" +
                    "WHERE \n" +
                    "    1 = 1 \n" +
                    "    $SEARCH_INPUT$\n" +
                    "    $CENTER_CODE$\n" +
                    "    $TRANS_DATETIME_FROM$\n" +
                    "    $TRANS_DATETIME_TO$\n"
            ;

//                Filter by Search Input
            if (!searchInput.isEmpty()) {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "AND ? IN ( service_code, trans_id, process_code, cust_code, isdn, center_code," +
                                "last_debt, to_char(cycle_number), to_char(settlement_amount)\n" +
                                " to_char(debt_remain), to_char(payment_id), file_name )").replace(
                        "?", "'" + searchInput + "'");
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

            //              Filter by file_type
            if (selectedCenterCodes.size() == 0) {
                sqlString = sqlString.replace("$CENTER_CODE$", "");
            } else {
                Boolean containNull = false;
                String condition = "";

                List<String> cloneList = new ArrayList<>(selectedCenterCodes);
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
                    condition = "AND (center_code IN " +
                            cloneList.toString().replace("[", "(").replace("]", ") OR center_code IS NULL)");
                } else {
                    condition = "AND center_code IN " +
                            cloneList.toString().replace("[", "(").replace("]", ")");
                }
                sqlString = sqlString.replace("$CENTER_CODE$", condition);
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
                PmgwTransaction PmgwTransaction = new PmgwTransaction();
                PmgwTransaction.setServiceCode(mRs.getInt(1));
                PmgwTransaction.setSettlementDate(mRs.getTimestamp(2));
                PmgwTransaction.setTransID(mRs.getString(3));
                PmgwTransaction.setProcessCode(mRs.getString(4));
                PmgwTransaction.setTransDateTime(mRs.getTimestamp(5));
                PmgwTransaction.setCustCode(mRs.getString(6));
                PmgwTransaction.setIsdn(mRs.getString(7));
                PmgwTransaction.setCenterCode(mRs.getString(8));
                PmgwTransaction.setLastDebt(mRs.getString(9));
                PmgwTransaction.setCycleNumber(mRs.getInt(10));
                PmgwTransaction.setSettlementAmount(mRs.getInt(11));
                PmgwTransaction.setDebtRemain(mRs.getInt(12));
                PmgwTransaction.setPaymentID(mRs.getLong(13));
                PmgwTransaction.setPaymentStartDate(mRs.getTimestamp(14));
                PmgwTransaction.setPaymentEndDate(mRs.getTimestamp(15));
                PmgwTransaction.setFileName(mRs.getString(16));
                PmgwTransaction.setLogDateTime(mRs.getTimestamp(17));
                PmgwTransaction.setImportDateTime(mRs.getTimestamp(18));
                PmgwTransaction.setId(mRs.getInt(19));

                pmgwTransactionList.add(PmgwTransaction);
            }


        } catch (Exception ex) {
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.ERR, "Lỗi xảy ra trong quá trình tìm kiếm!");
            throw ex;
        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return pmgwTransactionList;
    }
}
