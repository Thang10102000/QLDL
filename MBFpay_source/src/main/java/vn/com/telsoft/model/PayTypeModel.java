/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.lib.util.SQLUtil;
import vn.com.telsoft.entity.PayType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author NOINV
 */
public class PayTypeModel extends AMDataPreprocessor implements Serializable {
    public List<PayType> getListApp() throws Exception {
        List<PayType> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT pay_type,pay_type_name FROM pay_type ORDER BY pay_type";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                PayType tmpPayType = new PayType();
                tmpPayType.setPay_type(mRs.getInt(1));
                tmpPayType.setPay_type_name(mRs.getString(2));
                listReturn.add(tmpPayType);
            }
        } catch (Exception ex) {
            throw ex;

        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return listReturn;
    }
}
