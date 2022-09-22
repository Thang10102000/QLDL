/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.lib.util.SQLUtil;
import vn.com.telsoft.entity.SqlSummarize;
import vn.com.telsoft.util.DateUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author NOINV
 */
public class SqlSummarizeModel extends AMDataPreprocessor implements Serializable {

    public void delete(List<SqlSummarize> listApp) throws Exception {

        try {
            open();

            String strSQL = "DELETE FROM sql_summarize WHERE sql_id=?";
            mConnection.setAutoCommit(false);

            //Delete app
            for (SqlSummarize sourceData : listApp) {
                logBeforeDelete("sql_summarize", "sql_id=" + sourceData.getSqlID());
                mStmt = mConnection.prepareStatement(strSQL);
                mStmt.setInt(1, sourceData.getSqlID());
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

    public int add(SqlSummarize app) throws Exception {
        int idFromSequence;
        try {
            open();
            mConnection.setAutoCommit(false);
            idFromSequence = (int) SQLUtil.getSequenceValue(mConnection, "sql_summarize_seq");
            app.setSqlID(idFromSequence);

            //Update app
            String strSQL = "INSERT INTO sql_summarize(status,run_datetime,sql_not_succ,sql_succ,sql_check," +
                    "profile_id,sql_id, description) VALUES (?,?,?,?,?,?,?,?)";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getStatus());
            mStmt.setTimestamp(2, DateUtil.getSqlTimestamp(app.getRunDate()));
            mStmt.setString(3, app.getMismatchedQuery());
            mStmt.setString(4, app.getMatchedQuery());
            mStmt.setString(5, app.getCheckQuery());
            mStmt.setInt(6, app.getProfileID());
            mStmt.setInt(7, app.getSqlID());
            mStmt.setString(8, app.getDescription());
            mStmt.execute();
            logAfterInsert("sql_summarize", "sql_id=" + idFromSequence);

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

    public void edit(SqlSummarize app) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
            List listChange = logBeforeUpdate("sql_summarize", "sql_id=" + app.getSqlID());
            String strSQL = "UPDATE sql_summarize SET  status = ?, run_datetime = ?, sql_not_succ = ?, " +
                    "sql_succ = ?, sql_check = ?, profile_id = ?, description = ? WHERE  sql_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getStatus());
            mStmt.setTimestamp(2, DateUtil.getSqlTimestamp(app.getRunDate()));
            mStmt.setString(3, app.getMismatchedQuery());
            mStmt.setString(4, app.getMatchedQuery());
            mStmt.setString(5, app.getCheckQuery());
            mStmt.setInt(6, app.getProfileID());
            mStmt.setString(7, app.getDescription());
            mStmt.setInt(8, app.getSqlID());
            mStmt.execute();
//            logAfterUpdate(listChange);
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

    public List<SqlSummarize> getListApp() throws Exception {
        List<SqlSummarize> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT\n" +
                    "    ss.status,\n" +
                    "    run_datetime,\n" +
                    "    sql_not_succ,\n" +
                    "    sql_succ,\n" +
                    "    sql_check,\n" +
                    "    ss.profile_id,\n" +
                    "    sql_id,\n" +
                    "    pp.summarize_cycle,\n" +
                    "    pp.code,\n" +
                    "    ss.description\n" +
                    "FROM\n" +
                    "    sql_summarize ss\n" +
                    "    join profile_partner pp\n" +
                    "    on ss.profile_id = pp.profile_id\n" +
                    "ORDER BY\n" +
                    "    ss.profile_id";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                SqlSummarize tmpSqlSummarize = new SqlSummarize();
                tmpSqlSummarize.setStatus(mRs.getString(1));
                tmpSqlSummarize.setRunDate(mRs.getTimestamp(2));
                tmpSqlSummarize.setMismatchedQuery(mRs.getString(3));
                tmpSqlSummarize.setMatchedQuery(mRs.getString(4));
                tmpSqlSummarize.setCheckQuery(mRs.getString(5));
                tmpSqlSummarize.setProfileID(mRs.getInt(6));
                tmpSqlSummarize.setSqlID(mRs.getInt(7));
                tmpSqlSummarize.setSummarizeCycle(mRs.getInt(8));
                tmpSqlSummarize.setPartnerCode(mRs.getString(9));
                tmpSqlSummarize.setDescription(mRs.getString(10));
                listReturn.add(tmpSqlSummarize);
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
