/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.lib.util.SQLUtil;
import vn.com.telsoft.entity.ProfileFee;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author NOINV
 */
public class ProfileFeeModel extends AMDataPreprocessor implements Serializable {

    public void delete(List<ProfileFee> listApp) throws Exception {

        try {
            open();

            String strSQL = "DELETE FROM profile_fee WHERE fee_id=?";
            mConnection.setAutoCommit(false);

            //Delete app
            for (ProfileFee profileFee : listApp) {
                logBeforeDelete("profile_fee", "fee_id=" + profileFee.getFee_id());
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

    public int add(ProfileFee app) throws Exception {
        int idFromSequence;
        try {
            open();
            mConnection.setAutoCommit(false);
            idFromSequence = (int) SQLUtil.getSequenceValue(mConnection, "profile_fee_seq");
            app.setFee_id(idFromSequence);

            //Update app
            String strSQL = "INSERT INTO profile_fee(status,fee_amount2,fee_kind2,fee_amount1,fee_kind1,fee_type,profile_id,fee_id) VALUES (?,?,?,?,?,?,?,?)";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getStatus());
            mStmt.setString(2, app.getFee_amount2());
            mStmt.setString(3, app.getFee_kind2());
            mStmt.setString(4, app.getFee_amount1());
            mStmt.setString(5, app.getFee_kind1());
            mStmt.setString(6, app.getFee_type());
            mStmt.setInt(7, app.getProfile_id());
            mStmt.setInt(8, app.getFee_id());
            mStmt.execute();
            logAfterInsert("profile_fee", "fee_id=" + idFromSequence);

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

    public void edit(ProfileFee app) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
            List listChange = logBeforeUpdate("profile_fee", "fee_id=" + app.getFee_id());
            String strSQL = "UPDATE profile_fee SET  status = ?, fee_amount2 = ?, fee_kind2 = ?, fee_amount1 = ?, fee_kind1 = ?, fee_type = ?, profile_id = ? WHERE  fee_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getStatus());
            mStmt.setString(2, app.getFee_amount2());
            mStmt.setString(3, app.getFee_kind2());
            mStmt.setString(4, app.getFee_amount1());
            mStmt.setString(5, app.getFee_kind1());
            mStmt.setString(6, app.getFee_type());
            mStmt.setInt(7, app.getProfile_id());
            mStmt.setInt(8, app.getFee_id());
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

    public List<ProfileFee> getListApp() throws Exception {
        List<ProfileFee> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT status,fee_amount2,fee_kind2,fee_amount1,fee_kind1,fee_type,profile_id,fee_id FROM profile_fee";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                ProfileFee tmpProfileFee = new ProfileFee();
                tmpProfileFee.setStatus(mRs.getString(1));
                tmpProfileFee.setFee_amount2(mRs.getString(2));
                tmpProfileFee.setFee_kind2(mRs.getString(3));
                tmpProfileFee.setFee_amount1(mRs.getString(4));
                tmpProfileFee.setFee_kind1(mRs.getString(5));
                tmpProfileFee.setFee_type(mRs.getString(6));
                tmpProfileFee.setProfile_id(mRs.getInt(7));
                tmpProfileFee.setFee_id(mRs.getInt(8));
                listReturn.add(tmpProfileFee);
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

    public List<ProfileFee> getListAppByProfileID(String strProfileID) throws Exception {
        List<ProfileFee> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT status,fee_amount2,fee_kind2,fee_amount1,fee_kind1,fee_type,profile_id,fee_id FROM profile_fee WHERE profile_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, strProfileID);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                ProfileFee tmpProfileFee = new ProfileFee();
                tmpProfileFee.setStatus(mRs.getString(1));
                tmpProfileFee.setFee_amount2(mRs.getString(2));
                tmpProfileFee.setFee_kind2(mRs.getString(3));
                tmpProfileFee.setFee_amount1(mRs.getString(4));
                tmpProfileFee.setFee_kind1(mRs.getString(5));
                tmpProfileFee.setFee_type(mRs.getString(6));
                tmpProfileFee.setProfile_id(mRs.getInt(7));
                tmpProfileFee.setFee_id(mRs.getInt(8));
                listReturn.add(tmpProfileFee);
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
