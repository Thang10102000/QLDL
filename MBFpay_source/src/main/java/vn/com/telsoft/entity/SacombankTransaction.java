package vn.com.telsoft.entity;

import java.io.Serializable;
import java.util.Date;

public class SacombankTransaction implements Serializable {
    private int id;
    private String transactionID;
    private Date transactionDate;
    private Date valueDate;
    private String transactionRemarks;
    private String bankReferenceID;
    private int debitAmount;
    private int creditAmount;
    private long runningBalance;
    private String fileName;
    private Date logDateTime;
    private Date importDateTime;
    private String fileType;

    public SacombankTransaction() {
    }

    public SacombankTransaction(int id, String transactionID, Date transactionDate, Date valueDate,
                                String transactionRemarks, String bankReferenceID, int debitAmount,
                                int creditAmount, long runningBalance, String fileName, Date logDateTime,
                                Date importDateTime, String fileType) {
        this.id = id;
        this.transactionID = transactionID;
        this.transactionDate = transactionDate;
        this.valueDate = valueDate;
        this.transactionRemarks = transactionRemarks;
        this.bankReferenceID = bankReferenceID;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.runningBalance = runningBalance;
        this.fileName = fileName;
        this.logDateTime = logDateTime;
        this.importDateTime = importDateTime;
        this.fileType = fileType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public String getTransactionRemarks() {
        return transactionRemarks;
    }

    public void setTransactionRemarks(String transactionRemarks) {
        this.transactionRemarks = transactionRemarks;
    }

    public String getBankReferenceID() {
        return bankReferenceID;
    }

    public void setBankReferenceID(String bankReferenceID) {
        this.bankReferenceID = bankReferenceID;
    }

    public int getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(int debitAmount) {
        this.debitAmount = debitAmount;
    }

    public int getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(int creditAmount) {
        this.creditAmount = creditAmount;
    }

    public long getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(long runningBalance) {
        this.runningBalance = runningBalance;
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

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
