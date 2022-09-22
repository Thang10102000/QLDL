/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.admin.data.AMDataPreprocessor;
import vn.com.telsoft.entity.FileLoader;

import java.io.Serializable;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author NOINV
 */
public class FileLoaderModel extends AMDataPreprocessor implements Serializable {

    public void delete(List<FileLoader> listApp) throws Exception {

        try {
            open();

            String strSQL = "DELETE FROM file_loader WHERE rowid=?";
            mConnection.setAutoCommit(false);

            //Delete app
            for (FileLoader fileLoader : listApp) {
                logBeforeDelete("file_loader", "rowid='" + fileLoader.getRowid() + "'");
                mStmt = mConnection.prepareStatement(strSQL);
                mStmt.setString(1, fileLoader.getRowid());
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

    public String add(FileLoader app) throws Exception {
        String strId = "";

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
            String strSQL = "INSERT INTO file_loader(group_code, tnsname, dbusername, dbpassword,\n" +
                    "       inputfilepath, tempfilepath, backupfilepath, logpath,\n" +
                    "       wildcard, uncompress, startposition, endposition,\n" +
                    "       dateformat, truncatenextday, typeofday, truncatecurrdate,\n" +
                    "       partitionformat, partitiondateformat, tableinsert,\n" +
                    "       insertfield, delimited, intervalcommit, readbuffers,\n" +
                    "       bindsize, errorallowance, directpath, parrallel,\n" +
                    "       endcloseby, skipline, characterset, logmessagefields,\n" +
                    "       checkmaxcurrloader, logfiletype, status, whencolumn) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            mStmt = mConnection.prepareStatement(strSQL, Statement.RETURN_GENERATED_KEYS);
            mStmt.setString(1, app.getGroup_code());
            mStmt.setString(2, app.getTnsname());
            mStmt.setString(3, app.getDbusername());
            mStmt.setString(4, app.getDbpassword());
            mStmt.setString(5, app.getInputfilepath());
            mStmt.setString(6, app.getTempfilepath());
            mStmt.setString(7, app.getBackupfilepath());
            mStmt.setString(8, app.getLogpath());
            mStmt.setString(9, app.getWildcard());
            mStmt.setString(10, app.getUncompress());
            mStmt.setString(11, app.getStartposition());
            mStmt.setString(12, app.getEndposition());
            mStmt.setString(13, app.getDateformat());
            mStmt.setString(14, app.getTruncatenextday());
            mStmt.setString(15, app.getTypeofday());
            mStmt.setString(16, app.getTruncatecurrdate());
            mStmt.setString(17, app.getPartitionformat());
            mStmt.setString(18, app.getPartitiondateformat());
            mStmt.setString(19, app.getTableinsert());
            mStmt.setString(20, app.getInsertfield());
            mStmt.setString(21, app.getDelimited());
            mStmt.setString(22, app.getIntervalcommit());
            mStmt.setString(23, app.getReadbuffers());
            mStmt.setString(24, app.getBindsize());
            mStmt.setString(25, app.getErrorallowance());
            mStmt.setString(26, app.getDirectpath());
            mStmt.setString(27, app.getParrallel());
            mStmt.setString(28, app.getEndcloseby());
            mStmt.setString(29, app.getSkipline());
            mStmt.setString(30, app.getCharacterset());
            mStmt.setString(31, app.getLogmessagefields());
            mStmt.setString(32, app.getCheckmaxcurrloader());
            mStmt.setString(33, app.getLogfiletype());
            mStmt.setString(34, app.getStatus());
            mStmt.setString(35, app.getWhencolumn());
            mRs = mStmt.executeQuery();
            if (mRs != null && mRs.next()) {
                strId = mRs.getString(1);
                app.setRowid(strId);
            }
            logAfterInsert("file_loader", "rowid='" + strId +"'");

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

    public void edit(FileLoader app) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
            List listChange = logBeforeUpdate("file_loader", "rowid='" + app.getRowid() + "'");
            String strSQL = "UPDATE file_loader SET group_code = ?, tnsname = ?, dbusername = ?, dbpassword = ?, \n" +
                    "inputfilepath = ?, tempfilepath = ?, backupfilepath = ?, logpath = ?, \n" +
                    "wildcard = ?, uncompress = ?, startposition = ?, endposition = ?, \n" +
                    "dateformat = ?, truncatenextday = ?, typeofday = ?, truncatecurrdate = ?, \n" +
                    "partitionformat = ?, partitiondateformat = ?, tableinsert = ?, \n" +
                    "insertfield = ?, delimited = ?, intervalcommit = ?, readbuffers = ?, \n" +
                    "bindsize = ?, errorallowance = ?, directpath = ?, parrallel = ?, \n" +
                    "endcloseby = ?, skipline = ?, characterset = ?, logmessagefields = ?, \n" +
                    "checkmaxcurrloader = ?, logfiletype = ?, status = ?, whencolumn = ? WHERE rowid=?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getGroup_code());
            mStmt.setString(2, app.getTnsname());
            mStmt.setString(3, app.getDbusername());
            mStmt.setString(4, app.getDbpassword());
            mStmt.setString(5, app.getInputfilepath());
            mStmt.setString(6, app.getTempfilepath());
            mStmt.setString(7, app.getBackupfilepath());
            mStmt.setString(8, app.getLogpath());
            mStmt.setString(9, app.getWildcard());
            mStmt.setString(10, app.getUncompress());
            mStmt.setString(11, app.getStartposition());
            mStmt.setString(12, app.getEndposition());
            mStmt.setString(13, app.getDateformat());
            mStmt.setString(14, app.getTruncatenextday());
            mStmt.setString(15, app.getTypeofday());
            mStmt.setString(16, app.getTruncatecurrdate());
            mStmt.setString(17, app.getPartitionformat());
            mStmt.setString(18, app.getPartitiondateformat());
            mStmt.setString(19, app.getTableinsert());
            mStmt.setString(20, app.getInsertfield());
            mStmt.setString(21, app.getDelimited());
            mStmt.setString(22, app.getIntervalcommit());
            mStmt.setString(23, app.getReadbuffers());
            mStmt.setString(24, app.getBindsize());
            mStmt.setString(25, app.getErrorallowance());
            mStmt.setString(26, app.getDirectpath());
            mStmt.setString(27, app.getParrallel());
            mStmt.setString(28, app.getEndcloseby());
            mStmt.setString(29, app.getSkipline());
            mStmt.setString(30, app.getCharacterset());
            mStmt.setString(31, app.getLogmessagefields());
            mStmt.setString(32, app.getCheckmaxcurrloader());
            mStmt.setString(33, app.getLogfiletype());
            mStmt.setString(34, app.getStatus());
            mStmt.setString(35, app.getWhencolumn());
            mStmt.setString(36, app.getRowid());
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

    public List<FileLoader> getListApp() throws Exception {
        List<FileLoader> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT a.rowid, a.group_code, a.tnsname, a.dbusername, a.dbpassword,\n" +
                    "       a.inputfilepath, a.tempfilepath, a.backupfilepath, a.logpath,\n" +
                    "       a.wildcard, a.uncompress, a.startposition, a.endposition,\n" +
                    "       a.dateformat, a.truncatenextday, a.typeofday, a.truncatecurrdate,\n" +
                    "       a.partitionformat, a.partitiondateformat, a.tableinsert,\n" +
                    "       a.insertfield, a.delimited, a.intervalcommit, a.readbuffers,\n" +
                    "       a.bindsize, a.errorallowance, a.directpath, a.parrallel,\n" +
                    "       a.endcloseby, a.skipline, a.characterset, a.logmessagefields,\n" +
                    "       a.checkmaxcurrloader, a.logfiletype, a.status, a.whencolumn\n" +
                    "  FROM file_loader a";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                FileLoader tmpFileLoader = new FileLoader();
                tmpFileLoader.setRowid(mRs.getString(1));
                tmpFileLoader.setGroup_code(mRs.getString(2));
                tmpFileLoader.setTnsname(mRs.getString(3));
                tmpFileLoader.setDbusername(mRs.getString(4));
                tmpFileLoader.setDbpassword(mRs.getString(5));
                tmpFileLoader.setInputfilepath(mRs.getString(6));
                tmpFileLoader.setTempfilepath(mRs.getString(7));
                tmpFileLoader.setBackupfilepath(mRs.getString(8));
                tmpFileLoader.setLogpath(mRs.getString(9));
                tmpFileLoader.setWildcard(mRs.getString(10));
                tmpFileLoader.setUncompress(mRs.getString(11));
                tmpFileLoader.setStartposition(mRs.getString(12));
                tmpFileLoader.setEndposition(mRs.getString(13));
                tmpFileLoader.setDateformat(mRs.getString(14));
                tmpFileLoader.setTruncatenextday(mRs.getString(15));
                tmpFileLoader.setTypeofday(mRs.getString(16));
                tmpFileLoader.setTruncatecurrdate(mRs.getString(17));
                tmpFileLoader.setPartitionformat(mRs.getString(18));
                tmpFileLoader.setPartitiondateformat(mRs.getString(19));
                tmpFileLoader.setTableinsert(mRs.getString(20));
                tmpFileLoader.setInsertfield(mRs.getString(21));
                tmpFileLoader.setDelimited(mRs.getString(22));
                tmpFileLoader.setIntervalcommit(mRs.getString(23));
                tmpFileLoader.setReadbuffers(mRs.getString(24));
                tmpFileLoader.setBindsize(mRs.getString(25));
                tmpFileLoader.setErrorallowance(mRs.getString(26));
                tmpFileLoader.setDirectpath(mRs.getString(27));
                tmpFileLoader.setParrallel(mRs.getString(28));
                tmpFileLoader.setEndcloseby(mRs.getString(29));
                tmpFileLoader.setSkipline(mRs.getString(30));
                tmpFileLoader.setCharacterset(mRs.getString(31));
                tmpFileLoader.setLogmessagefields(mRs.getString(32));
                tmpFileLoader.setCheckmaxcurrloader(mRs.getString(33));
                tmpFileLoader.setLogfiletype(mRs.getString(34));
                tmpFileLoader.setStatus(mRs.getString(35));
                tmpFileLoader.setWhencolumn(mRs.getString(36));
                listReturn.add(tmpFileLoader);
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
