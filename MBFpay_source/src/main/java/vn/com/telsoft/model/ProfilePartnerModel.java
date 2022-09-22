/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.lib.util.SQLUtil;
import com.faplib.util.DateUtil;
import vn.com.telsoft.entity.ProfilePartner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author NOINV
 */
public class ProfilePartnerModel extends AMDataPreprocessor implements Serializable {

    public void delete(List<ProfilePartner> listApp) throws Exception {

        try {
            open();

            String strSQL = "DELETE FROM profile_partner WHERE profile_id=?";
            mConnection.setAutoCommit(false);

            //Delete app
            for (ProfilePartner profilePartner : listApp) {
                logBeforeDelete("profile_partner", "profile_id=" + profilePartner.getProfileID());
                mStmt = mConnection.prepareStatement(strSQL);
                mStmt.setString(1, profilePartner.getProfileID());
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

    public String add(ProfilePartner app) throws Exception {
        String strId = "";
        try {
            open();
            mConnection.setAutoCommit(false);
            strId = String.valueOf(SQLUtil.getSequenceValue(mConnection, "profile_partner_seq"));
            app.setProfileID(strId);

            //Insert Partner
            String strSQL = "INSERT INTO profile_partner(status,info,email,charge_fee,bank_name,account_number,account_name," +
                    "contract_type,expire_datetime,sign_datetime,contract_number,type,unit,name,code," +
                    "profile_id, subcode, summarize_cycle) " +
                    "VALUES (?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getStatus());
            mStmt.setString(2, app.getInfo());
            mStmt.setString(3, app.getEmail());
            mStmt.setString(4, app.getChargeFee());
            mStmt.setString(5, app.getBankName());
            mStmt.setString(6, app.getAccountNumber());
            mStmt.setString(7, app.getAccountName());
            mStmt.setString(8, app.getContractType());
            mStmt.setTimestamp(9, DateUtil.getSqlTimestamp(app.getExpireDate()));
            mStmt.setTimestamp(10, DateUtil.getSqlTimestamp(app.getSignDate()));
            mStmt.setString(11, app.getContractNumber());
            mStmt.setString(12, app.getType());
            mStmt.setString(13, app.getUnit());
            mStmt.setString(14, app.getName());
            mStmt.setString(15, app.getCode());
            mStmt.setString(16, app.getProfileID());
            mStmt.setString(17, app.getSubCode());
            mStmt.setInt(18, app.getSummarizeCycle());
            mStmt.execute();
            logAfterInsert("profile_partner", "profile_id=" + strId);

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

        return strId;
    }

    public void edit(ProfilePartner app) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
            List listChange = logBeforeUpdate("profile_partner", "profile_id=" + app.getProfileID());
            String strSQL = "UPDATE profile_partner SET  status = ?, info = ?, email = ?, charge_fee = ?, bank_name = ?, " +
                    "account_number = ?, account_name = ?, contract_type = ?, expire_datetime = ?, sign_datetime = ?, " +
                    "contract_number = ?, type = ?, unit = ?, name = ?, code = ?, subcode = ?, " +
                    "summarize_cycle = ?" +
                    "WHERE  profile_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getStatus());
            mStmt.setString(2, app.getInfo());
            mStmt.setString(3, app.getEmail());
            mStmt.setString(4, app.getChargeFee());
            mStmt.setString(5, app.getBankName());
            mStmt.setString(6, app.getAccountNumber());
            mStmt.setString(7, app.getAccountName());
            mStmt.setString(8, app.getContractType());
            mStmt.setTimestamp(9, DateUtil.getSqlTimestamp(app.getExpireDate()));
            mStmt.setTimestamp(10, DateUtil.getSqlTimestamp(app.getSignDate()));
            mStmt.setString(11, app.getContractNumber());
            mStmt.setString(12, app.getType());
            mStmt.setString(13, app.getUnit());
            mStmt.setString(14, app.getName());
            mStmt.setString(15, app.getCode());
            mStmt.setString(16, app.getSubCode());
            mStmt.setInt(17, app.getSummarizeCycle());
            mStmt.setString(18, app.getProfileID());
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

    public List<ProfilePartner> getListApp() throws Exception {
        List<ProfilePartner> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT\n" +
                    "    status,\n" +
                    "    info,\n" +
                    "    email,\n" +
                    "    charge_fee,\n" +
                    "    bank_name,\n" +
                    "    account_number,\n" +
                    "    account_name,\n" +
                    "    contract_type,\n" +
                    "    expire_datetime,\n" +
                    "    sign_datetime,\n" +
                    "    contract_number,\n" +
                    "    type,\n" +
                    "    unit,\n" +
                    "    name,\n" +
                    "    code,\n" +
                    "    profile_id,\n" +
                    "    subcode,\n" +
                    "    summarize_cycle\n" +
                    "FROM\n" +
                    "    profile_partner\n" +
                    "ORDER BY\n" +
                    "    profile_id";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                ProfilePartner tmpProfilePartner = new ProfilePartner();
                tmpProfilePartner.setStatus(mRs.getString(1));
                tmpProfilePartner.setInfo(mRs.getString(2));
                tmpProfilePartner.setEmail(mRs.getString(3));
                tmpProfilePartner.setChargeFee(mRs.getString(4));
                tmpProfilePartner.setBankName(mRs.getString(5));
                tmpProfilePartner.setAccountNumber(mRs.getString(6));
                tmpProfilePartner.setAccountName(mRs.getString(7));
                tmpProfilePartner.setContractType(mRs.getString(8));
                tmpProfilePartner.setExpireDate(mRs.getTimestamp(9));
                tmpProfilePartner.setSignDate(mRs.getTimestamp(10));
                tmpProfilePartner.setContractNumber(mRs.getString(11));
                tmpProfilePartner.setType(mRs.getString(12));
                tmpProfilePartner.setUnit(mRs.getString(13));
                tmpProfilePartner.setName(mRs.getString(14));
                tmpProfilePartner.setCode(mRs.getString(15));
                tmpProfilePartner.setProfileID(mRs.getString(16));
                tmpProfilePartner.setSubCode(mRs.getString(17));
                tmpProfilePartner.setSummarizeCycle(mRs.getInt(18));
                listReturn.add(tmpProfilePartner);
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

    public List<ProfilePartner> getListPartnerCode() throws Exception {
        List<ProfilePartner> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT status,info,email,charge_fee,bank_name,account_number," +
                    "account_name,contract_type,expire_datetime,sign_datetime,contract_number" +
                    ",type,unit,name,code,profile_id,subcode,summarize_cycle FROM " +
                    "profile_partner WHERE type = 0";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                ProfilePartner tmpProfilePartner = new ProfilePartner();
                tmpProfilePartner.setStatus(mRs.getString(1));
                tmpProfilePartner.setInfo(mRs.getString(2));
                tmpProfilePartner.setEmail(mRs.getString(3));
                tmpProfilePartner.setChargeFee(mRs.getString(4));
                tmpProfilePartner.setBankName(mRs.getString(5));
                tmpProfilePartner.setAccountNumber(mRs.getString(6));
                tmpProfilePartner.setAccountName(mRs.getString(7));
                tmpProfilePartner.setContractType(mRs.getString(8));
                tmpProfilePartner.setExpireDate(mRs.getTimestamp(9));
                tmpProfilePartner.setSignDate(mRs.getTimestamp(10));
                tmpProfilePartner.setContractNumber(mRs.getString(11));
                tmpProfilePartner.setType(mRs.getString(12));
                tmpProfilePartner.setUnit(mRs.getString(13));
                tmpProfilePartner.setName(mRs.getString(14));
                tmpProfilePartner.setCode(mRs.getString(15));
                tmpProfilePartner.setProfileID(mRs.getString(16));
                tmpProfilePartner.setSubCode(mRs.getString(17));
                tmpProfilePartner.setSummarizeCycle(mRs.getInt(18));
                listReturn.add(tmpProfilePartner);
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

    public List<ProfilePartner> getListBank() throws Exception {
        List<ProfilePartner> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT status,info,email,charge_fee,bank_name,account_number," +
                    "account_name,contract_type,expire_datetime,sign_datetime,contract_number" +
                    ",type,unit,name,code,profile_id,subcode,summarize_cycle FROM " +
                    "profile_partner WHERE type in (1,2)";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                ProfilePartner tmpProfilePartner = new ProfilePartner();
                tmpProfilePartner.setStatus(mRs.getString(1));
                tmpProfilePartner.setInfo(mRs.getString(2));
                tmpProfilePartner.setEmail(mRs.getString(3));
                tmpProfilePartner.setChargeFee(mRs.getString(4));
                tmpProfilePartner.setBankName(mRs.getString(5));
                tmpProfilePartner.setAccountNumber(mRs.getString(6));
                tmpProfilePartner.setAccountName(mRs.getString(7));
                tmpProfilePartner.setContractType(mRs.getString(8));
                tmpProfilePartner.setExpireDate(mRs.getTimestamp(9));
                tmpProfilePartner.setSignDate(mRs.getTimestamp(10));
                tmpProfilePartner.setContractNumber(mRs.getString(11));
                tmpProfilePartner.setType(mRs.getString(12));
                tmpProfilePartner.setUnit(mRs.getString(13));
                tmpProfilePartner.setName(mRs.getString(14));
                tmpProfilePartner.setCode(mRs.getString(15));
                tmpProfilePartner.setProfileID(mRs.getString(16));
                tmpProfilePartner.setSubCode(mRs.getString(17));
                tmpProfilePartner.setSummarizeCycle(mRs.getInt(18));
                listReturn.add(tmpProfilePartner);
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

    public List<String> getListOrderStatusCode(Date fromDate, Date toDate) throws Exception {
        List<String> orderStatusCodeList = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT DISTINCT\n" +
                    "    order_status_code\n" +
                    "FROM\n" +
                    "    mobishop\n" +
                    "WHERE\n" +
                    "        trunc(created_time) >= ?\n" +
                    "    AND trunc(created_time) <= ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setTimestamp(1, DateUtil.getSqlTimestamp(fromDate));
            mStmt.setTimestamp(2, DateUtil.getSqlTimestamp(toDate));

            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                orderStatusCodeList.add(mRs.getString(1));
            }

        } catch (Exception ex) {
            throw ex;

        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return orderStatusCodeList;
    }

    public ProfilePartner getListAppById(String profileId) throws Exception {
        ProfilePartner tmpProfilePartner = null;

        try {
            open();
            String strSQL = "SELECT status,info,email,charge_fee,bank_name,account_number,account_name,contract_type," +
                    "expire_datetime,sign_datetime,contract_number,type,unit,name,code,profile_id,subcode, " +
                    "summarize_cycle" +
                    " FROM profile_partner " +
                    "WHERE profile_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, profileId);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                tmpProfilePartner = new ProfilePartner();
                tmpProfilePartner.setStatus(mRs.getString(1));
                tmpProfilePartner.setInfo(mRs.getString(2));
                tmpProfilePartner.setEmail(mRs.getString(3));
                tmpProfilePartner.setChargeFee(mRs.getString(4));
                tmpProfilePartner.setBankName(mRs.getString(5));
                tmpProfilePartner.setAccountNumber(mRs.getString(6));
                tmpProfilePartner.setAccountName(mRs.getString(7));
                tmpProfilePartner.setContractType(mRs.getString(8));
                tmpProfilePartner.setExpireDate(mRs.getTimestamp(9));
                tmpProfilePartner.setSignDate(mRs.getTimestamp(10));
                tmpProfilePartner.setContractNumber(mRs.getString(11));
                tmpProfilePartner.setType(mRs.getString(12));
                tmpProfilePartner.setUnit(mRs.getString(13));
                tmpProfilePartner.setName(mRs.getString(14));
                tmpProfilePartner.setCode(mRs.getString(15));
                tmpProfilePartner.setProfileID(mRs.getString(16));
                tmpProfilePartner.setSubCode(mRs.getString(17));
                tmpProfilePartner.setSummarizeCycle(mRs.getInt(18));
            }
        } catch (Exception ex) {
            throw ex;

        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return tmpProfilePartner;
    }

    public String getTypeByProfileID(String profileID) throws Exception {
        String type = "";

        try {
            open();
            String strSQL = "SELECT\n" +
                    "    type\n" +
                    "FROM\n" +
                    "    profile_partner\n" +
                    "WHERE\n" +
                    "    profile_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, profileID);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                type = mRs.getString(1);
            }
        } catch (Exception exception) {
            throw exception;
        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return type;
    }

    public String getCodeByProfileID(String profileID) throws Exception {
        String code = "";

        try {
            open();
            String strSQL = "SELECT\n" +
                    "    code\n" +
                    "FROM\n" +
                    "    profile_partner\n" +
                    "WHERE\n" +
                    "    profile_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, profileID);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                code = mRs.getString(1);
            }
        } catch (Exception exception) {
            throw exception;
        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return code;
    }
}
