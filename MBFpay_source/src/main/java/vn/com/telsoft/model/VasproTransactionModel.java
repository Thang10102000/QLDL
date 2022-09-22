/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.admin.data.AMDataPreprocessor;
import vn.com.telsoft.entity.VasproTransaction;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author ThongNM
 */
public class VasproTransactionModel extends AMDataPreprocessor implements Serializable {

    public List<VasproTransaction> getVasproTransactions(Date transactionDateFrom,
                                                     Date transactionDateTo,
                                                     String searchInput)
            throws Exception {

        List<VasproTransaction> vasproTransactionList = new ArrayList<>();
        String sqlString = "";
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();
        try {
            // Find by ID
            open();

            sqlString = "SELECT \n" +
                    "    * \n" +
                    "FROM \n" +
                    "    Vaspro \n" +
                    "WHERE \n" +
                    "    1 = 1 \n" +
                    "    $SEARCH_INPUT$\n" +
                    "    $REG_DATETIME_FROM$\n" +
                    "    $REG_DATETIME_TO$\n"
            ;

//                Filter by Search Input
            if (!searchInput.isEmpty()) {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "AND ? IN ( isdn, mob_type, charge_code, group_code, to_char(charge_price),\n" +
                                " source_code, reg_id, file_name )").replace(
                        "?", "'" + searchInput + "'");
            } else {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "");
            }

            //                Filter by Created Time
            if (transactionDateFrom != null) {
                sqlString = sqlString.replace("$REG_DATETIME_FROM$",
                        "AND trunc(reg_datetime) >= ?");
                vtRow = new Vector();
                vtRow.addElement("Timestamp");
                vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(transactionDateFrom));
                vtParams.add(vtRow);
            } else {
                sqlString = sqlString.replace("$REG_DATETIME_FROM$",
                        "");
            }

            if (transactionDateTo != null) {
                sqlString = sqlString.replace("$REG_DATETIME_TO$",
                        "AND trunc(reg_datetime) <= ?");
                vtRow = new Vector();
                vtRow.addElement("Timestamp");
                vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(transactionDateTo));
                vtParams.add(vtRow);
            }else {
                sqlString = sqlString.replace("$REG_DATETIME_TO$",
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
                VasproTransaction vasproTransaction = new VasproTransaction();
                vasproTransaction.setIsdn(mRs.getString(1));
                vasproTransaction.setRegDateTime(mRs.getTimestamp(2));
                vasproTransaction.setEndDateTime(mRs.getTimestamp(3));
                vasproTransaction.setExpdateTime(mRs.getTimestamp(4));
                vasproTransaction.setMobType(mRs.getString(5));
                vasproTransaction.setChargeCode(mRs.getString(6));
                vasproTransaction.setGroupCode(mRs.getString(7));
                vasproTransaction.setChargePrice(mRs.getInt(8));
                vasproTransaction.setSourceCode(mRs.getString(9));
                vasproTransaction.setRegID(mRs.getString(10));
                vasproTransaction.setFileName(mRs.getString(11));
                vasproTransaction.setLogDateTime(mRs.getTimestamp(12));
                vasproTransaction.setImportDateTime(mRs.getTimestamp(13));
                vasproTransaction.setId(mRs.getInt(14));

                vasproTransactionList.add(vasproTransaction);
            }


        } catch (Exception ex) {
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.ERR, "Lỗi xảy ra trong quá trình tìm kiếm!");
            throw ex;
        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return vasproTransactionList;
    }
}
