package vn.com.telsoft.entity;

import org.primefaces.model.UploadedFile;

import java.io.Serializable;
import java.util.Date;

public class ReportGenerate implements Serializable {

    private String status;
    private String excelHash;
    private UploadedFile excelDesign;
    private String excelFileName;
    private String pdfHash;
    private UploadedFile pdfDesign;
    private String pdfFileName;
    private Date runDate;
    private String profileID;
    private String reportID;
    private String sqlBefore;
    private String sqlAfter;
    private String excelPath;
    private String pdfPath;
    private String reportCycle;
    private String name;

    public ReportGenerate() {
    }

    public ReportGenerate(String status, String excelHash, UploadedFile excelDesign, String excelFileName,
                          String pdfHash, UploadedFile pdfDesign, String pdfFileName, Date runDate,
                          String profileID, String reportID, String sqlBefore, String sqlAfter,
                          String excelPath, String pdfPath, String reportCycle, String name) {
        this.status = status;
        this.excelHash = excelHash;
        this.excelDesign = excelDesign;
        this.excelFileName = excelFileName;
        this.pdfHash = pdfHash;
        this.pdfDesign = pdfDesign;
        this.pdfFileName = pdfFileName;
        this.runDate = runDate;
        this.profileID = profileID;
        this.reportID = reportID;
        this.sqlBefore = sqlBefore;
        this.sqlAfter = sqlAfter;
        this.excelPath = excelPath;
        this.pdfPath = pdfPath;
        this.reportCycle = reportCycle;
        this.name = name;
    }

    public ReportGenerate(ReportGenerate ett) {

        this.status = ett.status;
        this.excelHash = ett.excelHash;
        this.excelDesign = ett.excelDesign;
        this.pdfHash = ett.pdfHash;
        this.pdfDesign = ett.pdfDesign;
        this.runDate = ett.runDate;
        this.profileID = ett.profileID;
        this.reportID = ett.reportID;
        this.sqlBefore = ett.sqlBefore;
        this.sqlAfter = ett.sqlAfter;
        this.reportCycle = ett.reportCycle;
        this.name = ett.name;
    }

    public String getName() { return name;}

    public void setName(String name) { this.name = name;}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExcelHash() {
        return excelHash;
    }

    public void setExcelHash(String excelHash) {
        this.excelHash = excelHash;
    }

    public UploadedFile getExcelDesign() {
        return excelDesign;
    }

    public void setExcelDesign(UploadedFile excelDesign) {
        this.excelDesign = excelDesign;
    }

    public String getExcelFileName() {
        return excelFileName;
    }

    public void setExcelFileName(String excelFileName) {
        this.excelFileName = excelFileName;
    }

    public String getPdfHash() {
        return pdfHash;
    }

    public void setPdfHash(String pdfHash) {
        this.pdfHash = pdfHash;
    }

    public UploadedFile getPdfDesign() {
        return pdfDesign;
    }

    public void setPdfDesign(UploadedFile pdfDesign) {
        this.pdfDesign = pdfDesign;
    }

    public String getPdfFileName() {
        return pdfFileName;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    public Date getRunDate() {
        return runDate;
    }

    public void setRunDate(Date runDate) {
        this.runDate = runDate;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getSqlBefore() {
        return sqlBefore;
    }

    public void setSqlBefore(String sqlBefore) {
        this.sqlBefore = sqlBefore;
    }

    public String getSqlAfter() {
        return sqlAfter;
    }

    public void setSqlAfter(String sqlAfter) {
        this.sqlAfter = sqlAfter;
    }

    public String getExcelPath() {
        return excelPath;
    }

    public void setExcelPath(String excelPath) {
        this.excelPath = excelPath;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String getReportCycle() {
        return reportCycle;
    }

    public void setReportCycle(String reportCycle) {
        this.reportCycle = reportCycle;
    }
}
