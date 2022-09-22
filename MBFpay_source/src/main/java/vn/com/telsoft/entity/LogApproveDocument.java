package vn.com.telsoft.entity;

import com.faplib.applet.util.StringUtil;
import vn.com.telsoft.util.JsfConstant;

import java.io.Serializable;
import java.util.Date;

public class LogApproveDocument implements Serializable {

    private Date insert_datetime;
    private String status;
    private String user_id;
    private String approve_id;
    private String log_id;
    private String name;
    private String approve;
    private String tooltip;

    public LogApproveDocument() {
    }

    public LogApproveDocument(Date insert_datetime, String status, String user_id, String approve_id, String log_id) {

        this.insert_datetime = insert_datetime;
        this.status = status;
        this.user_id = user_id;
        this.approve_id = approve_id;
        this.log_id = log_id;
    }

    public LogApproveDocument(LogApproveDocument ett) {

        this.insert_datetime = ett.insert_datetime;
        this.status = ett.status;
        this.user_id = ett.user_id;
        this.approve_id = ett.approve_id;
        this.log_id = ett.log_id;
    }

    public Date getInsert_datetime() {
        return this.insert_datetime;
    }

    public void setInsert_datetime(Date insert_datetime) {
        this.insert_datetime = insert_datetime;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getApprove_id() {
        return this.approve_id;
    }

    public void setApprove_id(String approve_id) {
        this.approve_id = approve_id;
    }

    public String getLog_id() {
        return this.log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getApprove() {
        if(this.name != null && !this.name.isEmpty()) {
            return this.name + " đã phê duyệt vào lúc " + StringUtil.format(this.insert_datetime, "dd/MM/yyyy HH:mm:ss");
        }
        return "";
    }

    public String getTooltip() {
        return tooltip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
