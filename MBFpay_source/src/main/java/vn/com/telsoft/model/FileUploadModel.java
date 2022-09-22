/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.admin.data.AMDataPreprocessor;
import vn.com.telsoft.entity.FileUpload;
import vn.com.telsoft.util.DateUtil;

import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * @author NOINV
 */
public class FileUploadModel extends AMDataPreprocessor implements Serializable {

    public String add(FileUpload app) throws Exception {
        String strId = "";
        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
            String strSQL = "INSERT INTO file_upload(insert_datetime,data_dir,file_name,user_name," +
                    "partner_code) VALUES (?,?,?,?,?)";
            mStmt = mConnection.prepareStatement(strSQL, Statement.RETURN_GENERATED_KEYS);
            mStmt.setTimestamp(1, DateUtil.getSqlTimestamp(app.getInsert_datetime()));
            mStmt.setString(2, app.getData_dir());
            mStmt.setString(3, app.getFile_name());
            mStmt.setString(4, app.getUser_name());
            mStmt.setString(5, app.getPartnerCode());
            mRs = mStmt.executeQuery();
            if (mRs != null && mRs.next()) {
                strId = mRs.getString(1);
                app.setRowid(strId);
            }
            logAfterInsert("file_upload", "rowid='" + strId + "'");

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

    public List<FileUpload> getListApp() throws Exception {
        List<FileUpload> listReturn = new ArrayList<>();
        try {
            open();
            String strSQL = "SELECT *\n" +
                    "FROM (SELECT insert_datetime,\n" +
                    "             data_dir,\n" +
                    "             file_name,\n" +
                    "             ROWID,\n" +
                    "             user_name,\n" +
                    "             partner_code\n" +
                    "      FROM file_upload\n" +
                    "      ORDER BY insert_datetime DESC)\n" +
                    "WHERE ROWNUM <= 100";



            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                FileUpload tmpFileUpload = new FileUpload();
                tmpFileUpload.setInsert_datetime(mRs.getTimestamp(1));
                tmpFileUpload.setData_dir(mRs.getString(2));
                tmpFileUpload.setFile_name(mRs.getString(3));
                tmpFileUpload.setRowid(mRs.getString(4));
                tmpFileUpload.setUser_name(mRs.getString(5));
                tmpFileUpload.setPartnerCode(mRs.getString(6));
                listReturn.add(tmpFileUpload);
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

    public List<SelectItem> getListSources() throws Exception {
        List<SelectItem> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT b.code, data_dir\n" +
                    "FROM source_data a, profile_partner b\n" +
                    "WHERE a.profile_id = b.profile_id AND a.status = '1' AND b.status = '1'";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                SelectItem tmp = new SelectItem();
                tmp.setLabel(mRs.getString(1));
                tmp.setValue(mRs.getString(2));
                listReturn.add(tmp);
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

    public List<String> getListPartnerCode() throws Exception {
        List<String> partnerCodeList = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT DISTINCT\n" +
                    "    partner_code\n" +
                    "FROM\n" +
                    "    file_upload";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                partnerCodeList.add(mRs.getString(1));
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return partnerCodeList;
    }

    public List<FileUpload> getFileUpload(String partnerCode, Date fromDate, Date toDate, String searchInput)
            throws Exception {
        System.out.println(" 1 : "+partnerCode);
        System.out.println(" 2: " +searchInput);
        System.out.println(" 3: " +fromDate);
        System.out.println(" 4: " +toDate);


        String sqlString = "";
        List<FileUpload> fileUploadList = new ArrayList<>();
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();
        try {
            open();
                      sqlString = "SELECT\n" +
                    "    *\n" +
                    "FROM\n" +
                    "    file_upload\n" +
                    "WHERE\n" +
                    " 1 = 1 " +
                    "$FROM_DATE$ \n" +
                     "$TO_DATE$ \n"+
                    "$PARTNER_CODE$ \n" +
                     "$SEARCH_INPUT$ \n"
                    ;

            // search
            if (!searchInput.isEmpty()) {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "AND ? IN (data_dir,partner_code)").replace(
                        "?", "'" + searchInput + "'");
            } else {
                sqlString = sqlString.replace("$SEARCH_INPUT$",
                        "");
            }

//            Check partner code
            if (!partnerCode.equals("All")) {
                sqlString = sqlString.replace("$PARTNER_CODE$", "AND partner_code = ? ");
            }else{
                sqlString = sqlString.replace("$PARTNER_CODE$", "");
            }


            if (fromDate != null) {
                sqlString = sqlString.replace("$FROM_DATE$",
                        "AND trunc(insert_datetime) >= ?");
                vtRow = new Vector();
                vtRow.addElement("Timestamp");
                vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(fromDate));
                vtParams.add(vtRow);
            } else {
                sqlString = sqlString.replace("$FROM_DATE$",
                        "");
            }

            if (toDate != null) {
                sqlString = sqlString.replace("$TO_DATE$",
                        "AND trunc(insert_datetime) <= ?");
                vtRow = new Vector();
                vtRow.addElement("Timestamp");
                vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(toDate));
                vtParams.add(vtRow);
            } else {
                sqlString = sqlString.replace("$TO_DATE$",
                        "");
            }

            System.out.println("#1:" + sqlString);

            mStmt = mConnection.prepareStatement(sqlString);
            mStmt.setTimestamp(1, DateUtil.getSqlTimestamp(fromDate));
            mStmt.setTimestamp(2, DateUtil.getSqlTimestamp(toDate));
            if(!partnerCode.equals("All")){
                mStmt.setString(3, partnerCode);
            }
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                FileUpload fileUpload = new FileUpload();
                fileUpload.setUser_name(mRs.getString(1));
                fileUpload.setFile_name(mRs.getString(2));
                fileUpload.setData_dir(mRs.getString(3));
                fileUpload.setInsert_datetime(mRs.getTimestamp(4));
                fileUpload.setPartnerCode(mRs.getString(5));
                fileUploadList.add(fileUpload);
            }
        } catch (Exception ex) {
            throw ex;

        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return fileUploadList;
    }
}
