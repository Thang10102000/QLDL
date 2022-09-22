/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.lib.util.SQLUtil;
import com.faplib.util.FileUtil;
import vn.com.telsoft.entity.ReportGenerate;
import vn.com.telsoft.util.DateUtil;

import java.io.*;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author NOINV
 */
public class ReportGenerateModel extends AMDataPreprocessor implements Serializable {

    public void delete(List<ReportGenerate> listApp) throws Exception {

        try {
            open();

            String strSQL = "DELETE FROM report_generate WHERE report_id=?";
            mConnection.setAutoCommit(false);

            //Delete app
            for (ReportGenerate sourceData : listApp) {
                logBeforeDelete("report_generate", "report_id=" + sourceData.getReportID());
                mStmt = mConnection.prepareStatement(strSQL);
                mStmt.setString(1, sourceData.getReportID());
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

    public String add(ReportGenerate app) throws Exception {
        String strId = "";
        try {
            open();
            mConnection.setAutoCommit(false);
            strId = String.valueOf(SQLUtil.getSequenceValue(mConnection, "report_generate_seq"));
            app.setReportID(strId);

            //Update app
            String strSQL = "INSERT INTO report_generate(status,excel_hash,excel_design,pdf_hash," +
                    "pdf_design, run_datetime,profile_id,report_id,sql_before,sql_after, name, cycle) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getStatus());
            mStmt.setString(2, app.getExcelHash());
            mStmt.setBlob(3, app.getExcelDesign().getInputstream());
            mStmt.setString(4, app.getPdfHash());
            mStmt.setBlob(5, app.getPdfDesign().getInputstream());
            mStmt.setTimestamp(6, DateUtil.getSqlTimestamp(app.getRunDate()));
            mStmt.setString(7, app.getProfileID());
            mStmt.setString(8, app.getReportID());
            mStmt.setString(9, app.getSqlBefore());
            mStmt.setString(10, app.getSqlAfter());
            mStmt.setString(11, app.getName());
            mStmt.setString(12, app.getReportCycle());
            mStmt.execute();
            logAfterInsert("report_generate", "report_id=" + strId);

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

    public String add2(ReportGenerate app) throws Exception {
        String strId = "";
        try {
            open();
            mConnection.setAutoCommit(false);
            strId = String.valueOf(SQLUtil.getSequenceValue(mConnection, "report_generate_seq"));
            app.setReportID(strId);

            //Update app
            String strSQL = "INSERT INTO report_generate(status,excel_hash,excel_design,pdf_hash," +
                    "pdf_design, run_datetime,profile_id,report_id,sql_before,sql_after, name, cycle )  VALUES (?," +
                    "?,?,?,?,?,?,?,?,?,?,?)";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getStatus());
            File f = new File(app.getExcelPath());
            mStmt.setString(2, FileUtil.hashSHA1(f));
            FileInputStream in = new FileInputStream(f);
            mStmt.setBinaryStream(3, in, (int) f.length());
            File f1 = new File(app.getPdfPath());
            mStmt.setString(4, FileUtil.hashSHA1(f1));
            FileInputStream in1 = new FileInputStream(f1);
            mStmt.setBinaryStream(5, in1, (int) f1.length());
            mStmt.setTimestamp(6, DateUtil.getSqlTimestamp(app.getRunDate()));
            mStmt.setString(7, app.getProfileID());
            mStmt.setString(8, app.getReportID());
            mStmt.setString(9, app.getSqlBefore());
            mStmt.setString(10, app.getSqlAfter());
            mStmt.setString(11, app.getName());
            mStmt.setString(12, app.getReportCycle());
            mStmt.execute();
            logAfterInsert("report_generate", "report_id=" + strId);

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

    public void edit(ReportGenerate app) throws Exception {
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();
        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
            List listChange = logBeforeUpdate("report_generate", "report_id=" + app.getReportID());
            String strSQL = "UPDATE report_generate SET  status = ?, $EXCEL$ $PDF$ run_datetime = ?,  " +
                    "profile_id = ?,  sql_before = ?, sql_after = ?, name = ?, cycle = ? WHERE  report_id = ?";
            // Status
            vtRow = new Vector();
            vtRow.addElement("String");
            vtRow.addElement(app.getStatus());
            vtParams.add(vtRow);
            if (app.getExcelDesign() != null) {
                strSQL = strSQL.replace("$EXCEL$", "excel_hash = ?, excel_design = ?, ");
                // Excel Hash
                vtRow = new Vector();
                vtRow.addElement("String");
                vtRow.addElement(app.getExcelHash());
                vtParams.add(vtRow);

                // Excel Design
                vtRow = new Vector();
                vtRow.addElement("Blob");
                vtRow.addElement(app.getExcelDesign().getInputstream());
                vtParams.add(vtRow);

            } else {
                strSQL = strSQL.replace("$EXCEL$", "");
            }

            if (app.getExcelDesign() != null) {
                strSQL = strSQL.replace("$PDF$", "pdf_hash = ?, pdf_design = ?, ");
                // Pdf Hash
                vtRow = new Vector();
                vtRow.addElement("String");
                vtRow.addElement(app.getPdfHash());
                vtParams.add(vtRow);

                // Pdf Design
                vtRow = new Vector();
                vtRow.addElement("Blob");
                vtRow.addElement(app.getPdfDesign().getInputstream());
                vtParams.add(vtRow);
            } else {
                strSQL = strSQL.replace("$PDF$", "");
            }
            // Run_datetime
            vtRow = new Vector();
            vtRow.addElement("Timestamp");
            vtRow.addElement(DateUtil.getSqlTimestamp(app.getRunDate()));
            vtParams.add(vtRow);

            // Profile ID
            vtRow = new Vector();
            vtRow.addElement("String");
            vtRow.addElement(app.getProfileID());
            vtParams.add(vtRow);

            // SQL Before
            vtRow = new Vector();
            vtRow.addElement("String");
            vtRow.addElement(app.getSqlBefore());
            vtParams.add(vtRow);

            // SQL After
            vtRow = new Vector();
            vtRow.addElement("String");
            vtRow.addElement(app.getSqlAfter());
            vtParams.add(vtRow);

            // Name
            vtRow = new Vector();
            vtRow.addElement("String");
            vtRow.addElement(app.getName());
            vtParams.add(vtRow);

            //Cycle
            vtRow = new Vector();
            vtRow.addElement("String");
            vtRow.addElement(app.getReportCycle());
            vtParams.add(vtRow);

            // Report ID
            vtRow = new Vector();
            vtRow.addElement("String");
            vtRow.addElement(app.getReportID());
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

    public List<ReportGenerate> getListApp() throws Exception {
        List<ReportGenerate> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT status,run_datetime,profile_id,report_id,sql_before,sql_after,cycle, name FROM report_generate";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                ReportGenerate tmpReportGenerate = new ReportGenerate();
                tmpReportGenerate.setStatus(mRs.getString(1));
                tmpReportGenerate.setRunDate(mRs.getTimestamp(2));
                tmpReportGenerate.setProfileID(mRs.getString(3));
                tmpReportGenerate.setReportID(mRs.getString(4));
                tmpReportGenerate.setSqlBefore(mRs.getString(5));
                tmpReportGenerate.setSqlAfter(mRs.getString(6));
                tmpReportGenerate.setReportCycle(mRs.getString(7));
                tmpReportGenerate.setName(mRs.getString(8));
                listReturn.add(tmpReportGenerate);
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

    public File downloadPdf(ReportGenerate reportFile, String tplFolderPath) throws Exception {
        File file = null;

        try {
            open();
            String strSQL = "" +
                    "SELECT a.pdf_design,\n" +
                    "       a.pdf_hash\n" +
                    "  FROM report_generate a\n" +
                    " WHERE a.report_id = ?";

            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, reportFile.getReportID());
            mRs = mStmt.executeQuery();

            if (mRs.next()) {
                file = new File(
                        tplFolderPath +
                                File.separator +
                                "Overview_" +
                                reportFile.getReportID() +
                                "_" +
                                reportFile.getProfileID() +
                                ".rptdesign"
                );

                if (!file.exists() || !FileUtil.hashSHA1(file).equals(mRs.getString("pdf_hash"))) {
                    FileUtil.forceFolderExist(tplFolderPath);

                    Blob blob = mRs.getBlob("pdf_design");
                    byte[] array = blob.getBytes(1, (int) blob.length());
                    file.createNewFile();
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(array);
                    out.close();
                }
            }

        } catch (Exception ex) {
            throw ex;

        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return file;
    }

    public File downloadExcel(ReportGenerate reportFile, String tplFolderPath) throws Exception {
        File file = null;

        try {
            open();
            String strSQL = "" +
                    "SELECT a.excel_design,\n" +
                    "       a.excel_hash\n" +
                    "  FROM report_generate a\n" +
                    " WHERE a.report_id = ?";

            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, reportFile.getReportID());
            mRs = mStmt.executeQuery();

            if (mRs.next()) {
                file = new File(
                        tplFolderPath +
                                File.separator +
                                "Detail_" +
                                reportFile.getReportID() +
                                "_" +
                                reportFile.getProfileID() +
                                ".rptdesign"
                );

                if (!file.exists() || !FileUtil.hashSHA1(file).equals(mRs.getString("excel_hash"))) {
                    FileUtil.forceFolderExist(tplFolderPath);

                    Blob blob = mRs.getBlob("excel_design");
                    byte[] array = blob.getBytes(1, (int) blob.length());
                    file.createNewFile();
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(array);
                    out.close();
                }
            }

        } catch (Exception ex) {
            throw ex;

        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return file;
    }
}
