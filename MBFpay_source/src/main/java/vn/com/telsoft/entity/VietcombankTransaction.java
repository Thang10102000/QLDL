package vn.com.telsoft.entity;

import java.io.Serializable;
import java.util.Date;

public class VietcombankTransaction implements Serializable {
    private int id;
    private Date transactionDate;
    private String transactionNumber;
    private int debit;
    private int credit;
    private String transactionDescription;
    private String bankReferenceID;
    private Date importDateTime;
    private String fileName;
    private String transCode;
    private Date valueDate;
    private String fileType;

    public VietcombankTransaction() {
    }

    public VietcombankTransaction(int id, Date transactionDate, String transactionNumber, int debit, int credit,
                                  String transactionDescription, String bankReferenceID, Date importDateTime, String fileName, String transCode, Date valueDate, String fileType) {
        this.id = id;
        this.transactionDate = transactionDate;
        this.transactionNumber = transactionNumber;
        this.debit = debit;
        this.credit = credit;
        this.transactionDescription = transactionDescription;
        this.bankReferenceID = bankReferenceID;
        this.importDateTime = importDateTime;
        this.fileName = fileName;
        this.transCode = transCode;
        this.valueDate = valueDate;
        this.fileType = fileType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public int getDebit() {
        return debit;
    }

    public void setDebit(int debit) {
        this.debit = debit;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public String getBankReferenceID() {
        return bankReferenceID;
    }

    public void setBankReferenceID(String bankReferenceID) {
        this.bankReferenceID = bankReferenceID;
    }

    public Date getImportDateTime() {
        return importDateTime;
    }

    public void setImportDateTime(Date importDateTime) {
        this.importDateTime = importDateTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
