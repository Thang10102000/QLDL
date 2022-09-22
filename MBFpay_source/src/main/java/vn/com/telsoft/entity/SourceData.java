package vn.com.telsoft.entity;

import java.io.Serializable;
import java.util.Date;

public class SourceData implements Serializable {

    private String status;
    private Date time;
    private String compress_data;
    private String data_dir;
    private String data_type;
    private String source_code;
    private String profile_id;
    private String source_id;

    public SourceData() {
    }

    public SourceData(String status, Date time, String compress_data, String data_dir, String data_type, String source_code, String profile_id, String source_id) {

        this.status = status;
        this.time = time;
        this.compress_data = compress_data;
        this.data_dir = data_dir;
        this.data_type = data_type;
        this.source_code = source_code;
        this.profile_id = profile_id;
        this.source_id = source_id;
    }

    public SourceData(SourceData ett) {

        this.status = ett.status;
        this.time = ett.time;
        this.compress_data = ett.compress_data;
        this.data_dir = ett.data_dir;
        this.data_type = ett.data_type;
        this.source_code = ett.source_code;
        this.profile_id = ett.profile_id;
        this.source_id = ett.source_id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getCompress_data() {
        return this.compress_data;
    }

    public void setCompress_data(String compress_data) {
        this.compress_data = compress_data;
    }

    public String getData_dir() {
        return this.data_dir;
    }

    public void setData_dir(String data_dir) {
        this.data_dir = data_dir;
    }

    public String getData_type() {
        return this.data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getSource_code() {
        return this.source_code;
    }

    public void setSource_code(String source_code) {
        this.source_code = source_code;
    }

    public String getProfile_id() {
        return this.profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public String getSource_id() {
        return this.source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }
}
