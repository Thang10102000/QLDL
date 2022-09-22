/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.lib.util.SQLUtil;
import vn.com.telsoft.entity.SourceData;
import vn.com.telsoft.util.DateUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author NOINV
 */
public class SourceDataModel extends AMDataPreprocessor implements Serializable {

    public void delete(List<SourceData> listApp) throws Exception {

        try {
            open();

            String strSQL = "DELETE FROM source_data WHERE source_id=?";
            mConnection.setAutoCommit(false);

            //Delete app
            for (SourceData sourceData : listApp) {
                logBeforeDelete("source_data", "source_id=" + sourceData.getSource_id());
                mStmt = mConnection.prepareStatement(strSQL);
                mStmt.setString(1, sourceData.getSource_id());
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

    public String add(SourceData app) throws Exception {
        String strId = "";
        try {
            open();
            mConnection.setAutoCommit(false);
            strId = String.valueOf(SQLUtil.getSequenceValue(mConnection, "source_data_seq"));
            app.setSource_id(strId);

            //Update app
            String strSQL = "INSERT INTO source_data(status,time,compress_data,data_dir,data_type,source_code,profile_id,source_id) VALUES (?,?,?,?,?,?,?,?)";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getStatus());
            mStmt.setTimestamp(2, DateUtil.getSqlTimestamp(app.getTime()));
            mStmt.setString(3, app.getCompress_data());
            mStmt.setString(4, app.getData_dir());
            mStmt.setString(5, app.getData_type());
            mStmt.setString(6, app.getSource_code());
            mStmt.setString(7, app.getProfile_id());
            mStmt.setString(8, app.getSource_id());
            mStmt.execute();
            logAfterInsert("source_data", "source_id=" + strId);

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

    public void edit(SourceData app) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
            List listChange = logBeforeUpdate("source_data", "source_id=" + app.getSource_id());
            String strSQL = "UPDATE source_data SET  status = ?, time = ?, compress_data = ?, data_dir = ?, data_type = ?, source_code = ?, profile_id = ? WHERE source_id=?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getStatus());
            mStmt.setTimestamp(2, DateUtil.getSqlTimestamp(app.getTime()));
            mStmt.setString(3, app.getCompress_data());
            mStmt.setString(4, app.getData_dir());
            mStmt.setString(5, app.getData_type());
            mStmt.setString(6, app.getSource_code());
            mStmt.setString(7, app.getProfile_id());
            mStmt.setString(8, app.getSource_id());
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

    public List<SourceData> getListApp() throws Exception {
        List<SourceData> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT status,time,compress_data,data_dir,data_type,source_code,profile_id,source_id FROM source_data";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                SourceData tmpSourceData = new SourceData();
                tmpSourceData.setStatus(mRs.getString(1));
                tmpSourceData.setTime(mRs.getTimestamp(2));
                tmpSourceData.setCompress_data(mRs.getString(3));
                tmpSourceData.setData_dir(mRs.getString(4));
                tmpSourceData.setData_type(mRs.getString(5));
                tmpSourceData.setSource_code(mRs.getString(6));
                tmpSourceData.setProfile_id(mRs.getString(7));
                tmpSourceData.setSource_id(mRs.getString(8));
                listReturn.add(tmpSourceData);
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
