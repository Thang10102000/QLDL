/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.lib.util.SQLUtil;
import vn.com.telsoft.entity.SupportFee;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author NOINV
 */
public class SupportFeeModel extends AMDataPreprocessor implements Serializable {

    public void delete(List<SupportFee> listApp) throws Exception {

        try {
            open();

            String strSQL = "DELETE FROM support_fee WHERE fee_id=?";
            mConnection.setAutoCommit(false);

            //Delete app
            for (SupportFee profileFee : listApp) {
                logBeforeDelete("support_fee", "fee_id=" + profileFee.getFee_id());
                mStmt = mConnection.prepareStatement(strSQL);
                mStmt.setInt(1, profileFee.getFee_id());
                mStmt.execute();
            }

            //Commit
            mConnection.commit();

        } catch (Exception ex) {
            mConnection.rollback();
            throw ex;
        } finally {
            close(mStmt);
            close(mConnection);
        }
    }

    public int add(SupportFee app) throws Exception {
        int idFromSequence;
        try {
            open();
            mConnection.setAutoCommit(false);
            idFromSequence = (int) SQLUtil.getSequenceValue(mConnection, "support_fee_seq");
            app.setFee_id(idFromSequence);

            //Update app
            String strSQL = "INSERT INTO support_fee(fee_id,profile_id,product_code,fee_amount,fee_percent,status) VALUES (?,?,?,?,?,?)";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setInt(1, app.getFee_id());
            mStmt.setInt(2, app.getProfile_id());
            mStmt.setString(3, app.getProduct_code());
            mStmt.setInt(4, app.getFee_amount());
            mStmt.setInt(5, app.getFee_percent());
            mStmt.setString(6, app.getStatus());
            mStmt.execute();
            logAfterInsert("support_fee", "fee_id=" + idFromSequence);

            //Done
            mConnection.commit();

        } catch (Exception ex) {
            mConnection.rollback();
            throw ex;

        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return idFromSequence;
    }

    public void edit(SupportFee app) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
            List listChange = logBeforeUpdate("support_fee", "fee_id=" + app.getFee_id());
            String strSQL = "UPDATE support_fee SET  profile_id = ?, product_code = ?, fee_amount = ?, fee_percent = ?, status = ? WHERE  fee_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setInt(1, app.getProfile_id());
            mStmt.setString(2, app.getProduct_code());
            mStmt.setInt(3, app.getFee_amount());
            mStmt.setInt(4, app.getFee_percent());
            mStmt.setString(5, app.getStatus());
            mStmt.setInt(6, app.getFee_id());
            mStmt.execute();
            logAfterUpdate(listChange);
            close(mStmt);

            //Commit
            mConnection.commit();

        } catch (Exception ex) {
            mConnection.rollback();
            throw ex;

        } finally {
            close(mStmt);
            close();
        }
    }

    public List<SupportFee> getListApp() throws Exception {
        List<SupportFee> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT profile_id,product_code,fee_amount,fee_percent,status,fee_id FROM support_fee";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                SupportFee tmpSupportFee = new SupportFee();
                tmpSupportFee.setProfile_id(mRs.getInt(1));
                tmpSupportFee.setProduct_code(mRs.getString(2));
                tmpSupportFee.setFee_amount(mRs.getInt(3));
                tmpSupportFee.setFee_percent(mRs.getInt(4));
                tmpSupportFee.setStatus(mRs.getString(5));
                tmpSupportFee.setFee_id(mRs.getInt(6));
                listReturn.add(tmpSupportFee);
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

    public List<SupportFee> getListAppByProfileID(String strProfileID) throws Exception {
        List<SupportFee> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT profile_id,product_code,fee_amount,fee_percent,status,fee_id FROM support_fee WHERE profile_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, strProfileID);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                SupportFee tmpSupportFee = new SupportFee();
                tmpSupportFee.setProfile_id(mRs.getInt(1));
                tmpSupportFee.setProduct_code(mRs.getString(2));
                tmpSupportFee.setFee_amount(mRs.getInt(3));
                tmpSupportFee.setFee_percent(mRs.getInt(4));
                tmpSupportFee.setStatus(mRs.getString(5));
                tmpSupportFee.setFee_id(mRs.getInt(6));
                listReturn.add(tmpSupportFee);
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
