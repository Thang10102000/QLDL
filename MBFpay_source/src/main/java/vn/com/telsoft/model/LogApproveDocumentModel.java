/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.lib.util.SQLUtil;
import vn.com.telsoft.entity.LogApproveDocument;
import vn.com.telsoft.util.DateUtil;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author NOINV
 */
public class LogApproveDocumentModel extends AMDataPreprocessor implements Serializable {

    public void delete(List<LogApproveDocument> listApp) throws Exception {

        try {
            open();

            String strSQL = "DELETE FROM log_approve_document WHERE log_id=?";
            mConnection.setAutoCommit(false);

            //Delete app
            for (LogApproveDocument logApproveDocument : listApp) {
                logBeforeDelete("log_approve_document", "log_id=" + logApproveDocument.getLog_id());
                mStmt = mConnection.prepareStatement(strSQL);
                mStmt.setString(1, logApproveDocument.getLog_id());
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

    public String add(LogApproveDocument app) throws Exception {
        String strId = "";
        try {
            open();
            mConnection.setAutoCommit(false);
            strId = String.valueOf(SQLUtil.getSequenceValue(mConnection, "log_approve_document_seq"));
            app.setLog_id(strId);

            //Update app
            String strSQL = "INSERT INTO log_approve_document(insert_datetime,status,user_id,approve_id,log_id) VALUES (?,?,?,?,?)";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setTimestamp(1, DateUtil.getSqlTimestamp(app.getInsert_datetime()));
            mStmt.setString(2, app.getStatus());
            mStmt.setString(3, app.getUser_id());
            mStmt.setString(4, app.getApprove_id());
            mStmt.setString(5, app.getLog_id());
            mStmt.execute();
            logAfterInsert("log_approve_document", "log_id=" + strId);

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

    public void edit(LogApproveDocument app) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
            List listChange = logBeforeUpdate("log_approve_document", "log_id=" + app.getLog_id());
            String strSQL = "UPDATE log_approve_document SET  insert_datetime = ?, status = ?, user_id = ?, approve_id = ? WHERE log_id=?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setTimestamp(1, DateUtil.getSqlTimestamp(app.getInsert_datetime()));
            mStmt.setString(2, app.getStatus());
            mStmt.setString(3, app.getUser_id());
            mStmt.setString(4, app.getApprove_id());
            mStmt.setString(5, app.getLog_id());
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

    public List<LogApproveDocument> getListApp() throws Exception {
        List<LogApproveDocument> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT insert_datetime,status,user_id,approve_id,log_id FROM log_approve_document ORDER BY TO_NUMBER(approve_id), TO_NUMBER(status)";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                LogApproveDocument tmpLogApproveDocument = new LogApproveDocument();
                tmpLogApproveDocument.setInsert_datetime(mRs.getTimestamp(1));
                tmpLogApproveDocument.setStatus(mRs.getString(2));
                tmpLogApproveDocument.setUser_id(mRs.getString(3));
                tmpLogApproveDocument.setApprove_id(mRs.getString(4));
                tmpLogApproveDocument.setLog_id(mRs.getString(5));
                listReturn.add(tmpLogApproveDocument);
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

    public List<LogApproveDocument> getListApp(String approve_id) throws Exception {
        List<LogApproveDocument> listReturn = new ArrayList<>();
        try {
            String strSQL = "SELECT a.insert_datetime,\n" +
                    "       a.status,\n" +
                    "       a.user_id,\n" +
                    "       a.approve_id,\n" +
                    "       a.log_id,\n" +
                    "       b.full_name\n" +
                    "FROM log_approve_document a, am_user b\n" +
                    "WHERE a.approve_id = ? AND a.user_id = b.user_id\n" +
                    "ORDER BY TO_NUMBER (a.status)";
            open();
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, approve_id);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                LogApproveDocument tmpLogApproveDocument = new LogApproveDocument();
                tmpLogApproveDocument.setInsert_datetime(mRs.getTimestamp(1));
                tmpLogApproveDocument.setStatus(mRs.getString(2));
                tmpLogApproveDocument.setUser_id(mRs.getString(3));
                tmpLogApproveDocument.setApprove_id(mRs.getString(4));
                tmpLogApproveDocument.setLog_id(mRs.getString(5));
                tmpLogApproveDocument.setName(mRs.getString(6));
                listReturn.add(tmpLogApproveDocument);
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
