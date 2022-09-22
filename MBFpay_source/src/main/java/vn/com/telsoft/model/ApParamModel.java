/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.SystemLogger;
import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.lib.admin.gui.entity.AccessRight;
import com.faplib.lib.admin.gui.entity.ModuleGUI;
import com.faplib.lib.config.Config;
import com.faplib.lib.util.SQLUtil;
import com.faplib.util.StringUtil;
import com.faplib.ws.client.ClientRequestProcessor;
import vn.com.telsoft.entity.ApParam;
import vn.com.telsoft.util.JsfConstant;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author NOINV
 */
public class ApParamModel extends AMDataPreprocessor implements Serializable {

    public String getEmailSubject() throws Exception {
        String strReturn = "";
        try {
            open();
            String strSQL = "SELECT par_value FROM ap_param WHERE par_type = ? AND par_name = ? ";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, JsfConstant.SEND_EMAIL);
            mStmt.setString(2, JsfConstant.EMAIL_SUBJECT);
            mRs = mStmt.executeQuery();
            if (mRs != null & mRs.next()) {
                strReturn = StringUtil.nvl(mRs.getString(1), "");
            }
        } finally {
            close(mConnection, mStmt, mRs);
        }
        return strReturn;
    }

    public String getEmailMessage() throws Exception {
        String strReturn = "";
        try {
            open();
            String strSQL = "SELECT par_value FROM ap_param WHERE par_type = ? AND par_name = ? ";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, JsfConstant.SEND_EMAIL);
            mStmt.setString(2, JsfConstant.EMAIL_MESSAGE);
            mRs = mStmt.executeQuery();
            if (mRs != null & mRs.next()) {
                strReturn = StringUtil.nvl(mRs.getString(1), "");
            }
        } finally {
            close(mConnection, mStmt, mRs);
        }
        return strReturn;
    }

    public String getEmailHost() throws Exception {
        String strReturn = "";
        try {
            open();
            String strSQL = "SELECT par_value FROM ap_param WHERE par_type = ? AND par_name = ? ";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, JsfConstant.SEND_EMAIL);
            mStmt.setString(2, JsfConstant.EMAIL_HOST);
            mRs = mStmt.executeQuery();
            if (mRs != null & mRs.next()) {
                strReturn = StringUtil.nvl(mRs.getString(1), "");
            }
        } finally {
            close(mConnection, mStmt, mRs);
        }
        return strReturn;
    }

    public String getEmailPort() throws Exception {
        String strReturn = "";
        try {
            open();
            String strSQL = "SELECT par_value FROM ap_param WHERE par_type = ? AND par_name = ? ";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, JsfConstant.SEND_EMAIL);
            mStmt.setString(2, JsfConstant.EMAIL_PORT);
            mRs = mStmt.executeQuery();
            if (mRs != null & mRs.next()) {
                strReturn = StringUtil.nvl(mRs.getString(1), "");
            }
        } finally {
            close(mConnection, mStmt, mRs);
        }
        return strReturn;
    }

    public String getEmailEncrypt() throws Exception {
        String strReturn = "";
        try {
            open();
            String strSQL = "SELECT par_value FROM ap_param WHERE par_type = ? AND par_name = ? ";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, JsfConstant.SEND_EMAIL);
            mStmt.setString(2, JsfConstant.EMAIL_ENCRYPT);
            mRs = mStmt.executeQuery();
            if (mRs != null & mRs.next()) {
                strReturn = StringUtil.nvl(mRs.getString(1), "");
            }
        } finally {
            close(mConnection, mStmt, mRs);
        }
        return strReturn;
    }

    public String getEmailUsername() throws Exception {
        String strReturn = "";
        try {
            open();
            String strSQL = "SELECT par_value FROM ap_param WHERE par_type = ? AND par_name = ? ";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, JsfConstant.SEND_EMAIL);
            mStmt.setString(2, JsfConstant.EMAIL_USERNAME);
            mRs = mStmt.executeQuery();
            if (mRs != null & mRs.next()) {
                strReturn = StringUtil.nvl(mRs.getString(1), "");
            }
        } finally {
            close(mConnection, mStmt, mRs);
        }
        return strReturn;
    }

    public String getEmailPassword() throws Exception {
        String strReturn = "";
        try {
            open();
            String strSQL = "SELECT par_value FROM ap_param WHERE par_type = ? AND par_name = ? ";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, JsfConstant.SEND_EMAIL);
            mStmt.setString(2, JsfConstant.EMAIL_PASSWORD);
            mRs = mStmt.executeQuery();
            if (mRs != null & mRs.next()) {
                strReturn = StringUtil.nvl(mRs.getString(1), "");
            }
        } finally {
            close(mConnection, mStmt, mRs);
        }
        return strReturn;
    }


    public void delete(List<ApParam> listApp) throws Exception {

        try {
            open();

            String strSQL = "DELETE FROM ap_param WHERE rowid=?";
            mConnection.setAutoCommit(false);

            //Delete app
            for (ApParam apParam : listApp) {
                logBeforeDelete("ap_param", "rowid='" + apParam.getRowid() + "'");
                mStmt = mConnection.prepareStatement(strSQL);
                mStmt.setString(1, apParam.getRowid());
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

    public String add(ApParam app) throws Exception {
        String strId = "";

        try {
            open();
            mConnection.setAutoCommit(false);

            String strSQL = "INSERT INTO ap_param(par_type,par_name,par_value,description) VALUES (?,?,?,?)";
            mStmt = mConnection.prepareStatement(strSQL, Statement.RETURN_GENERATED_KEYS);
            mStmt.setString(1, app.getPar_type());
            mStmt.setString(2, app.getPar_name());
            mStmt.setString(3, app.getPar_value());
            mStmt.setString(4, app.getDescription());
            mRs = mStmt.executeQuery();
            if (mRs != null && mRs.next()) {
                strId = mRs.getString(1);
                app.setRowid(strId);
            }
            logAfterInsert("ap_param", "rowid='" + strId + "'");

            //Done
            mConnection.commit();

        } catch (Exception ex) {
            mConnection.rollback();
            throw ex;

        } finally {
            close(mStmt);
            close();
        }

        return strId;
    }

    public void edit(ApParam app) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
            List listChange = logBeforeUpdate("ap_param", "rowid='" + app.getRowid() + "'");
            String strSQL = "UPDATE ap_param SET  par_type = ?, par_name = ?, par_value = ?, description = ? WHERE rowid=?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getPar_type());
            mStmt.setString(2, app.getPar_name());
            mStmt.setString(3, app.getPar_value());
            mStmt.setString(4, app.getDescription());
            mStmt.setString(5, app.getRowid());
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

    public List<ApParam> getListApp() throws Exception {
        List<ApParam> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT rowid, par_type,par_name,par_value,description FROM ap_param";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                ApParam tmpApParam = new ApParam();
                tmpApParam.setRowid(mRs.getString(1));
                tmpApParam.setPar_type(mRs.getString(2));
                tmpApParam.setPar_name(mRs.getString(3));
                tmpApParam.setPar_value(mRs.getString(4));
                tmpApParam.setDescription(mRs.getString(5));
                listReturn.add(tmpApParam);
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
