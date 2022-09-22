package vn.com.telsoft.entity;

import java.io.Serializable;
import java.util.Date;

public class SuspiciousTransaction implements Serializable {
    private int id;
    private String bankCode;
    private String mappingCode;
    private String transactionID;
    private Date transactionDate;
    private String description;
    private int creditAmount;
    private int debitAmount;
    private Date adjustDate;
    private String ticketName;
    private String processStatus;
    private String userProcess;
    private String confirmStatus;
    private String userConfirm;

    public SuspiciousTransaction() {
    }

    public SuspiciousTransaction(int id, String bankCode, String mappingCode, String transactionID,
                                 Date transactionDate, String description, int creditAmount, int debitAmount,
                                 Date adjustDate, String ticketName, String processStatus, String userProcess,
                                 String confirmStatus, String userConfirm) {
        this.id = id;
        this.bankCode = bankCode;
        this.mappingCode = mappingCode;
        this.transactionID = transactionID;
        this.transactionDate = transactionDate;
        this.description = description;
        this.creditAmount = creditAmount;
        this.debitAmount = debitAmount;
        this.adjustDate = adjustDate;
        this.ticketName = ticketName;
        this.processStatus = processStatus;
        this.userProcess = userProcess;
        this.confirmStatus = confirmStatus;
        this.userConfirm = userConfirm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getMappingCode() {
        return mappingCode;
    }

    public void setMappingCode(String mappingCode) {
        this.mappingCode = mappingCode;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(int creditAmount) {
        this.creditAmount = creditAmount;
    }

    public int getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(int debitAmount) {
        this.debitAmount = debitAmount;
    }

    public Date getAdjustDate() {
        return adjustDate;
    }

    public void setAdjustDate(Date adjustDate) {
        this.adjustDate = adjustDate;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getUserProcess() {
        return userProcess;
    }

    public void setUserProcess(String userProcess) {
        this.userProcess = userProcess;
    }

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public String getUserConfirm() {
        return userConfirm;
    }

    public void setUserConfirm(String userConfirm) {
        this.userConfirm = userConfirm;
    }
}
