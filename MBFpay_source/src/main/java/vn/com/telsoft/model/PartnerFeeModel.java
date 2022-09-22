/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.lib.util.SQLUtil;
import com.faplib.util.DateUtil;
import vn.com.telsoft.entity.PartnerFee;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ThongNM
 */
public class PartnerFeeModel extends AMDataPreprocessor implements Serializable {

    public void delete(List<PartnerFee> listApp) throws Exception {

        try {
            open();

            String strSQL = "DELETE FROM partner_fee WHERE fee_id=?";
            mConnection.setAutoCommit(false);

            //Delete app
            for (PartnerFee profileFee : listApp) {
                logBeforeDelete("partner_fee", "fee_id=" + profileFee.getFeeID());
                mStmt = mConnection.prepareStatement(strSQL);
                mStmt.setInt(1, profileFee.getFeeID());
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

    public int add(PartnerFee app) throws Exception {
        int idFromSequence;
        try {
            open();
            mConnection.setAutoCommit(false);
            idFromSequence = (int) SQLUtil.getSequenceValue(mConnection, "partner_fee_seq");
            app.setFeeID(idFromSequence);

            //Update app

            String strSQL = "    INSERT INTO partner_fee (\n" +
                    "        profile_id,\n" +
                    "        product_code,\n" +
                    "        mbfp_rate,\n" +
                    "        status,\n" +
                    "        fee_id,\n" +
                    "        partner_type,\n" +
                    "        provider_rate,\n" +
                    "        provider_rate_type,\n" +
                    "        discount_rate,\n" +
                    "        special_discount,\n" +
                    "        product_name,\n" +
                    "        process_fee,\n" +
                    "        payment_fee,\n" +
                    "        special_date,\n" +
                    "        min_fee,\n" +
                    "        max_fee,\n" +
                    "        pay_type,\n" +
                    "        pay_type_name\n" +
                    "    ) VALUES (\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?,\n" +
                    "        ?\n" +
                    "    )";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setInt(1, app.getProfileID());
            mStmt.setString(2, app.getProductCode());
            mStmt.setDouble(3, app.getMbfpRate());
            mStmt.setString(4, app.getStatus());
            mStmt.setInt(5, app.getFeeID());
            mStmt.setString(6, app.getPartnerType());
            mStmt.setDouble(7, app.getProviderRate());
            mStmt.setInt(8, app.getProviderRateType());
            mStmt.setDouble(9, app.getDiscountRate());
            mStmt.setDouble(10, app.getSpecialDiscount());
            mStmt.setString(11, app.getProductName());
            mStmt.setInt(12, app.getProcessFee());
            mStmt.setDouble(13, app.getPaymentFee());
            mStmt.setTimestamp(14, DateUtil.getSqlTimestamp(app.getSpecialDate()));
            mStmt.setDouble(15, app.getMinFee());
            mStmt.setDouble(16, app.getMaxFee());
            mStmt.setString(17, app.getPayType());
            mStmt.setString(18, app.getPayTypeName());
            mStmt.execute();
            logAfterInsert("partner_fee", "fee_id=" + idFromSequence);

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

    public void edit(PartnerFee app) throws Exception {
        String payTypeName = "";

        try {
            open();
            mConnection.setAutoCommit(false);

            String getNameQuery = "SELECT\n" +
                    "    pay_type_name\n" +
                    "FROM\n" +
                    "    mobishop\n" +
                    "WHERE\n" +
                    "        pay_type = ?\n" +
                    "    AND ROWNUM = 1";
            mStmt = mConnection.prepareStatement(getNameQuery);
            mStmt.setString(1, app.getPayType());
            mRs = mStmt.executeQuery();
            while(mRs.next()){
                payTypeName = mRs.getString(1);
            }


            //Update app
            List listChange = logBeforeUpdate("partner_fee", "fee_id="
                    + app.getFeeID());

            String strSQL = "UPDATE partner_fee\n" +
                    "SET\n" +
                    "    profile_id = ?,\n" +
                    "    product_code = ?,\n" +
                    "    mbfp_rate = ?,\n" +
                    "    status = ?,\n" +
                    "    partner_type = ?,\n" +
                    "    provider_rate = ?,\n" +
                    "    provider_rate_type = ?,\n" +
                    "    discount_rate = ?,\n" +
                    "    special_discount = ?,\n" +
                    "    product_name = ?,\n" +
                    "    process_fee = ?,\n" +
                    "    payment_fee = ?,\n" +
                    "    special_date = ?,\n" +
                    "    min_fee = ?,\n" +
                    "    max_fee = ?,\n" +
                    "    pay_type = ?,\n" +
                    "    pay_type_name = ?\n" +
                    "WHERE\n" +
                    "    fee_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setInt(1, app.getProfileID());
            mStmt.setString(2, app.getProductCode());
            mStmt.setDouble(3, app.getMbfpRate());
            mStmt.setString(4, app.getStatus());
            mStmt.setString(5, app.getPartnerType());
            mStmt.setDouble(6, app.getProviderRate());
            mStmt.setInt(7, app.getProviderRateType());
            mStmt.setDouble(8, app.getDiscountRate());
            mStmt.setDouble(9, app.getSpecialDiscount());
            mStmt.setString(10, app.getProductName());
            mStmt.setDouble(11, app.getProcessFee());
            mStmt.setDouble(12, app.getPaymentFee());
            mStmt.setTimestamp(13, DateUtil.getSqlTimestamp(app.getSpecialDate()));
            mStmt.setLong(14, app.getMinFee());
            mStmt.setLong(15, app.getMaxFee());
            mStmt.setString(16, app.getPayType());
            mStmt.setString(17, payTypeName);
            mStmt.setInt(18, app.getFeeID());
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

    public List<PartnerFee> getListApp() throws Exception {
        List<PartnerFee> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT\n" +
                    "    fee_id,\n" +
                    "    profile_id,\n" +
                    "    product_code,\n" +
                    "    mbfp_rate,\n" +
                    "    status,\n" +
                    "    partner_type,\n" +
                    "    provider_rate,\n" +
                    "    provider_rate_type,\n" +
                    "    discount_rate,\n" +
                    "    special_discount,\n" +
                    "    product_name,\n" +
                    "    process_fee,\n" +
                    "    payment_fee,\n" +
                    "    special_date,\n" +
                    "    min_fee,\n" +
                    "    max_fee,\n" +
                    "    pay_type,\n" +
                    "    pay_type_name\n" +
                    "FROM\n" +
                    "    partner_fee";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                PartnerFee partnerFee = new PartnerFee();
                partnerFee.setFeeID(mRs.getInt(1));
                partnerFee.setProfileID(mRs.getInt(2));
                partnerFee.setProductCode(mRs.getString(3));
                partnerFee.setMbfpRate(mRs.getDouble(4));
                partnerFee.setStatus(mRs.getString(5));
                partnerFee.setPartnerType(mRs.getString(6));
                partnerFee.setProviderRate(mRs.getDouble(7));
                partnerFee.setProviderRateType(mRs.getInt(8));
                partnerFee.setDiscountRate(mRs.getDouble(9));
                partnerFee.setSpecialDiscount(mRs.getDouble(10));
                partnerFee.setProductName(mRs.getString(11));
                partnerFee.setProcessFee(mRs.getInt(12));
                partnerFee.setPaymentFee(mRs.getDouble(13));
                partnerFee.setSpecialDate(mRs.getDate(14));
                partnerFee.setMinFee(mRs.getInt(15));
                partnerFee.setMaxFee(mRs.getInt(16));
                partnerFee.setPayType(mRs.getString(17));
                partnerFee.setPayTypeName(mRs.getString(18));
                listReturn.add(partnerFee);
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

    public List<PartnerFee> getListAppByProfileID(String strProfileID) throws Exception {
        List<PartnerFee> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT\n" +
                    "    fee_id,\n" +
                    "    profile_id,\n" +
                    "    product_code,\n" +
                    "    mbfp_rate,\n" +
                    "    status,\n" +
                    "    partner_type,\n" +
                    "    provider_rate,\n" +
                    "    provider_rate_type,\n" +
                    "    discount_rate,\n" +
                    "    special_discount,\n" +
                    "    product_name,\n" +
                    "    process_fee,\n" +
                    "    payment_fee,\n" +
                    "    special_date,\n" +
                    "    min_fee,\n" +
                    "    max_fee,\n" +
                    "    pay_type,\n" +
                    "    pay_type_name\n" +
                    "FROM\n" +
                    "    partner_fee\n" +
                    "WHERE\n" +
                    "    profile_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, strProfileID);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                PartnerFee partnerFee = new PartnerFee();
                partnerFee.setFeeID(mRs.getInt(1));
                partnerFee.setProfileID(mRs.getInt(2));
                partnerFee.setProductCode(mRs.getString(3));
                partnerFee.setMbfpRate(mRs.getDouble(4));
                partnerFee.setStatus(mRs.getString(5));
                partnerFee.setPartnerType(mRs.getString(6));
                partnerFee.setProviderRate(mRs.getDouble(7));
                partnerFee.setProviderRateType(mRs.getInt(8));
                partnerFee.setDiscountRate(mRs.getDouble(9));
                partnerFee.setSpecialDiscount(mRs.getDouble(10));
                partnerFee.setProductName(mRs.getString(11));
                partnerFee.setProcessFee(mRs.getInt(12));
                partnerFee.setPaymentFee(mRs.getDouble(13));
                partnerFee.setSpecialDate(mRs.getDate(14));
                partnerFee.setMinFee(mRs.getLong(15));
                partnerFee.setMaxFee(mRs.getLong(16));
                partnerFee.setPayType(mRs.getString(17));
                partnerFee.setPayTypeName(mRs.getString(18));
                listReturn.add(partnerFee);
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
