/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.lib.util.DataUtil;
import com.faplib.lib.util.SQLUtil;
import com.faplib.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import vn.com.telsoft.entity.ApproveDocument;
import vn.com.telsoft.entity.LogApproveDocument;
import vn.com.telsoft.util.DateUtil;
import vn.com.telsoft.util.JsfConstant;
import vn.com.telsoft.utils.VNCharacterUtils;

import java.io.*;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * @author NOINV
 */
public class ApproveDocumentModel extends AMDataPreprocessor implements Serializable {
    public void delete(List<ApproveDocument> listApp) throws Exception {

        try {
            open();

            String strSQL = "DELETE FROM approve_document WHERE approve_id=?";
            mConnection.setAutoCommit(false);

            //Delete app
            for (ApproveDocument approveDocument : listApp) {
                logBeforeDelete("approve_document", "approve_id=" + approveDocument.getApprove_id());
                mStmt = mConnection.prepareStatement(strSQL);
                mStmt.setString(1, approveDocument.getApprove_id());
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

    public String add(ApproveDocument app) throws Exception {
        String strId = "";
        try {
            open();
            mConnection.setAutoCommit(false);
            strId = String.valueOf(SQLUtil.getSequenceValue(mConnection, "approve_document_seq"));
            app.setApprove_id(strId);

            //Update app
            String strSQL = "INSERT INTO approve_document(status,create_datetime,excel_hash,pdf_hash," +
                    "excel_document,pdf_document,profile_id,approve_id, name) VALUES (?,?,?,?,?,?,?,?,?)";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getStatus());
            mStmt.setTimestamp(2, DateUtil.getSqlTimestamp(app.getCreate_datetime()));
            File f = new File(app.getExcel_path());
            mStmt.setString(3, FileUtil.hashSHA1(f));
            File f1 = new File(app.getPdf_path());
            mStmt.setString(4, FileUtil.hashSHA1(f1));
            FileReader fr = new FileReader(f);
            mStmt.setClob(5, fr, (int) f.length());
            FileReader fr1 = new FileReader(f1);
            mStmt.setClob(6, fr1, (int) f1.length());
            mStmt.setString(7, app.getProfile_id());
            mStmt.setString(8, app.getApprove_id());
            mStmt.setString(9, app.getName());
            mStmt.execute();
            logAfterInsert("approve_document", "approve_id=" + strId);

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

    public void edit(ApproveDocument app) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
            List listChange = logBeforeUpdate("approve_document", "approve_id=" + app.getApprove_id());
            String strSQL = "UPDATE approve_document SET  status = ? WHERE approve_id=?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getStatus());
            mStmt.setString(2, app.getApprove_id());
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

    public void edit2(ApproveDocument app) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
            List listChange = logBeforeUpdate("approve_document", "approve_id=" + app.getApprove_id());
            String strSQL = "UPDATE approve_document SET  status = ?, create_datetime = ?, excel_hash = ?, pdf_hash = ?, excel_document = ?, pdf_document = ?, profile_id = ?,name = ? WHERE approve_id=?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getStatus());
            mStmt.setTimestamp(2, DateUtil.getSqlTimestamp(app.getCreate_datetime()));
            File f = new File(app.getExcel_path());
            mStmt.setString(3, FileUtil.hashSHA1(f));
            File f1 = new File(app.getPdf_path());
            mStmt.setString(4, FileUtil.hashSHA1(f1));
            FileInputStream in = new FileInputStream(f);
            mStmt.setBinaryStream(5, in, (int) f.length());
            FileInputStream in1 = new FileInputStream(f1);
            mStmt.setBinaryStream(6, in1, (int) f1.length());
            mStmt.setString(7, app.getProfile_id());
            mStmt.setString(8, app.getApprove_id());
            mStmt.setString(9, app.getName());
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

    public List<ApproveDocument> getListApp() throws Exception {
        List<ApproveDocument> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT status,create_datetime,excel_hash,pdf_hash,excel_document," +
                    "pdf_document,approve_id,profile_id,from_date,to_date,name FROM approve_document ORDER BY create_datetime DESC, approve_id DESC";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            String tempFolder = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_BB);

            while (mRs.next()) {
                ApproveDocument tmpApproveDocument = new ApproveDocument();
                tmpApproveDocument.setStatus(mRs.getString(1));
                tmpApproveDocument.setCreate_datetime(mRs.getTimestamp(2));
                tmpApproveDocument.setExcel_hash(mRs.getString(3));
                tmpApproveDocument.setPdf_hash(mRs.getString(4));
                tmpApproveDocument.setApprove_id(mRs.getString(7));
                tmpApproveDocument.setProfile_id(mRs.getString(8));
                tmpApproveDocument.setFrom_date(mRs.getTimestamp(9));
                tmpApproveDocument.setTo_date(mRs.getTimestamp(10));
                tmpApproveDocument.setName(mRs.getString(11));

                File file = new File(tempFolder + File.separator + tmpApproveDocument.getApprove_id() + ".xlsx");
                if (!file.exists() || !FileUtil.hashSHA1(file).equals(tmpApproveDocument.getExcel_hash())) {
                    FileUtil.forceFolderExist(tempFolder);

                    Blob blob = mRs.getBlob(5);
                    byte[] array = blob.getBytes(1, (int) blob.length());
                    file.createNewFile();
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(array);
                    out.close();
                }
                tmpApproveDocument.setExcel_path(JsfConstant.TEMP_FOLDER_BB + File.separator + tmpApproveDocument.getApprove_id() + ".xlsx");

                file = new File(tempFolder + File.separator + tmpApproveDocument.getApprove_id() + ".pdf");
                if (!file.exists() || !FileUtil.hashSHA1(file).equals(tmpApproveDocument.getPdf_hash())) {
                    FileUtil.forceFolderExist(tempFolder);

                    Blob blob = mRs.getBlob(6);
                    byte[] array = blob.getBytes(1, (int) blob.length());
                    file.createNewFile();
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(array);
                    out.close();
                }
                tmpApproveDocument.setPdf_path(JsfConstant.TEMP_FOLDER_BB + File.separator + tmpApproveDocument.getApprove_id() + ".pdf");
                tmpApproveDocument.setListLogApproveDocument((List<LogApproveDocument>)
                        DataUtil.performAction(LogApproveDocumentModel.class, "getListApp", tmpApproveDocument.getApprove_id()));
                listReturn.add(tmpApproveDocument);
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

    public List<ApproveDocument> getListApp2(String name, String profileID, Date dtStartDate, Date dtEndDate, Integer cycleDate) throws Exception {
        List<ApproveDocument> listReturn = new ArrayList<>();
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();
        try {
            open();
            String strSQL = "SELECT status, \n" +
                    "       create_datetime, \n" +
                    "       excel_hash, \n" +
                    "       pdf_hash, \n" +
                    "       excel_document, \n" +
                    "       pdf_document, \n" +
                    "       approve_id, \n" +
                    "       profile_id, \n" +
                    "       from_date, \n" +
                    "       TO_DATE, \n" +
                    "       name \n" +
                    "	FROM approve_document \n" +
                    "	WHERE     1 = 1 \n" +
                    "      $NAME$ \n" +
                    "      $PROFILE_ID$ \n" +
                    "      AND from_date >= TRUNC (?) \n" +
                    "      AND to_date < TRUNC (?) +1 \n" +
                    "      $CYCLE_DATE$ \n" +
                    "ORDER BY create_datetime DESC, approve_id DESC";
            
            if(cycleDate != null && cycleDate == 0) {
            	String conditionCycle = "AND trunc(from_date) = trunc(to_date) ";
            	strSQL = strSQL.replace("$CYCLE_DATE$", conditionCycle);
            }
            else if(cycleDate != null && cycleDate == 1) {
            	String conditionCycle = "AND (trunc(to_date) - trunc(from_Date) >= 27 AND trunc(to_date) - trunc(from_Date) <= 30)";
            	strSQL = strSQL.replace("$CYCLE_DATE$", conditionCycle);
            }
            else if(cycleDate != null && cycleDate == 2) {
            	String conditionCycle = "AND trunc(to_date) - trunc(from_Date) = 7 ";
            	strSQL = strSQL.replace("$CYCLE_DATE$", conditionCycle);
            }
        	else
        		strSQL = strSQL.replace("$CYCLE_DATE$", "");

            if (profileID != null && !profileID.isEmpty()) {
                strSQL = strSQL.replace("$PROFILE_ID$", " AND profile_id = ? ");
                // Profile ID
                vtRow = new Vector();
                vtRow.addElement("String");
                vtRow.addElement(profileID);
                vtParams.add(vtRow);
            } else {
                strSQL = strSQL.replace("$PROFILE_ID$", "");
            }

            if (name != null && !name.isEmpty()) {
                strSQL = strSQL.replace("$NAME$", " AND FN_CONVERT_TO_VN(UPPER(name))  LIKE ? ");
                // Name
                vtRow = new Vector();
                vtRow.addElement("String");

                vtRow.addElement('%'+ VNCharacterUtils.removeAccent(name.toUpperCase())+ '%');

                vtParams.add(vtRow);
            } else {
                strSQL = strSQL.replace("$NAME$", "");
            }

            // dtStartDate
            vtRow = new Vector();
            vtRow.addElement("Timestamp");
            vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(dtStartDate));
            vtParams.add(vtRow);

            // dtEndDate
            vtRow = new Vector();
            vtRow.addElement("Timestamp");
            vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(dtEndDate));
            vtParams.add(vtRow);

            mStmt = mConnection.prepareStatement(strSQL);
            for (int i = 0; i < vtParams.size(); i++) {
                Vector vtTemp = (Vector) vtParams.get(i);
                String strType = vtTemp.elementAt(0).toString();
                switch (strType) {
                    case "String":
                        mStmt.setString(i + 1, vtTemp.elementAt(1).toString());
                        break;
                    case "Blob":
                        mStmt.setBlob(i + 1, (InputStream) vtTemp.elementAt(1));
                        break;
                    case "Timestamp":
                        mStmt.setTimestamp(i + 1, (Timestamp) vtTemp.elementAt(1));
                        break;
                    default:
                        mStmt.setString(i + 1, vtTemp.elementAt(1).toString());
                        break;
                }
            }
            mRs = mStmt.executeQuery();

            String tempFolder = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_BB);

            while (mRs.next()) {
                ApproveDocument tmpApproveDocument = new ApproveDocument();
                tmpApproveDocument.setStatus(mRs.getString(1));
                tmpApproveDocument.setCreate_datetime(mRs.getTimestamp(2));
                tmpApproveDocument.setExcel_hash(mRs.getString(3));
                tmpApproveDocument.setPdf_hash(mRs.getString(4));
                tmpApproveDocument.setApprove_id(mRs.getString(7));
                tmpApproveDocument.setProfile_id(mRs.getString(8));
                tmpApproveDocument.setFrom_date(mRs.getTimestamp(9));
                tmpApproveDocument.setTo_date(mRs.getTimestamp(10));
                tmpApproveDocument.setName(mRs.getString(11));

                File file = new File(tempFolder + File.separator + tmpApproveDocument.getApprove_id() + ".xlsx");
                if (!file.exists() || !FileUtil.hashSHA1(file).equals(tmpApproveDocument.getExcel_hash())) {
                    FileUtil.forceFolderExist(tempFolder);

                    Blob blob = mRs.getBlob(5);
                    byte[] array = blob.getBytes(1, (int) blob.length());
                    file.createNewFile();
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(array);
                    out.close();
                }
                tmpApproveDocument.setExcel_path(JsfConstant.TEMP_FOLDER_BB + File.separator + tmpApproveDocument.getApprove_id() + ".xlsx");

                file = new File(tempFolder + File.separator + tmpApproveDocument.getApprove_id() + ".pdf");
                if (!file.exists() || !FileUtil.hashSHA1(file).equals(tmpApproveDocument.getPdf_hash())) {
                    FileUtil.forceFolderExist(tempFolder);

                    Blob blob = mRs.getBlob(6);
                    byte[] array = blob.getBytes(1, (int) blob.length());
                    file.createNewFile();
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(array);
                    out.close();
                }
                tmpApproveDocument.setPdf_path(JsfConstant.TEMP_FOLDER_BB + File.separator + tmpApproveDocument.getApprove_id() + ".pdf");
                tmpApproveDocument.setListLogApproveDocument((List<LogApproveDocument>) DataUtil.performAction(LogApproveDocumentModel.class, "getListApp", tmpApproveDocument.getApprove_id()));
                listReturn.add(tmpApproveDocument);
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

    public List<String> getEmailByTitle(String title) throws Exception {
        List<String> emailList = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT\n" +
                    "    email\n" +
                    "FROM\n" +
                    "    am_user\n" +
                    "WHERE\n" +
                    "        title = ?\n" +
                    "    AND email IS NOT NULL";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, title);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                String email = mRs.getString(1);

                emailList.add(email);
            }
        } catch (Exception exception) {
            throw exception;
        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return emailList;
    }

    public List<String> getEmailByProfileID(String profileID) throws Exception {
        List<String> emailList = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT\n" +
                    "    email\n" +
                    "FROM\n" +
                    "    profile_partner\n" +
                    "WHERE\n" +
                    "    profile_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, profileID);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                String email = mRs.getString(1);

                emailList.add(email);
            }
        } catch (Exception exception) {
            throw exception;
        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return emailList;
    }
}
