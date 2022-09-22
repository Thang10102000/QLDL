package vn.com.telsoft.entity;

import java.io.Serializable;
import java.util.Date;

public class FileUpload implements Serializable {

    private Date insert_datetime;
    private String data_dir;
    private String file_name;
    private String user_name;
    private String rowid;
    private String partnerCode;

    public FileUpload() {
    }

    public FileUpload(Date insert_datetime, String data_dir, String file_name, String user_name,
                      String rowid, String partnerCode) {

        this.insert_datetime = insert_datetime;
        this.data_dir = data_dir;
        this.file_name = file_name;
        this.rowid = rowid;
        this.user_name = user_name;
        this.partnerCode = partnerCode;
    }

    public FileUpload(FileUpload ett) {

        this.insert_datetime = ett.insert_datetime;
        this.data_dir = ett.data_dir;
        this.file_name = ett.file_name;
        this.rowid = ett.rowid;
        this.partnerCode = ett.partnerCode;
    }

    public Date getInsert_datetime() {
        return this.insert_datetime;
    }

    public void setInsert_datetime(Date insert_datetime) {
        this.insert_datetime = insert_datetime;
    }

    public String getData_dir() {
        return this.data_dir;
    }

    public void setData_dir(String data_dir) {
        this.data_dir = data_dir;
    }

    public String getFile_name() {
        return this.file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getRowid() {
        return this.rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }
}
