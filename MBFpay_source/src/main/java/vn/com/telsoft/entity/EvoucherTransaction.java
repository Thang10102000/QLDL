package vn.com.telsoft.entity;

import java.io.Serializable;
import java.util.Date;

public class EvoucherTransaction implements Serializable {
    private int id;
    private String transID;
    private Date transDateTime;
    private String type;
    private String processStatus;
    private String daily;
    private String targetISDN;
    private int amount;
    private String reference2;
    private String fileName;
    private Date logDateTime;
    private Date importDateTime;

    public EvoucherTransaction() {
    }

    public EvoucherTransaction(int id, String transID, Date transDateTime, String type, String processStatus,
                               String daily, String targetISDN, int amount, String reference2, String fileName,
                               Date logDateTime, Date importDateTime) {
        this.id = id;
        this.transID = transID;
        this.transDateTime = transDateTime;
        this.type = type;
        this.processStatus = processStatus;
        this.daily = daily;
        this.targetISDN = targetISDN;
        this.amount = amount;
        this.reference2 = reference2;
        this.fileName = fileName;
        this.logDateTime = logDateTime;
        this.importDateTime = importDateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransID() {
        return transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

    public Date getTransDateTime() {
        return transDateTime;
    }

    public void setTransDateTime(Date transDateTime) {
        this.transDateTime = transDateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getDaily() {
        return daily;
    }

    public void setDaily(String daily) {
        this.daily = daily;
    }

    public String getTargetISDN() {
        return targetISDN;
    }

    public void setTargetISDN(String targetISDN) {
        this.targetISDN = targetISDN;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getReference2() {
        return reference2;
    }

    public void setReference2(String reference2) {
        this.reference2 = reference2;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getLogDateTime() {
        return logDateTime;
    }

    public void setLogDateTime(Date logDateTime) {
        this.logDateTime = logDateTime;
    }

    public Date getImportDateTime() {
        return importDateTime;
    }

    public void setImportDateTime(Date importDateTime) {
        this.importDateTime = importDateTime;
    }
}
