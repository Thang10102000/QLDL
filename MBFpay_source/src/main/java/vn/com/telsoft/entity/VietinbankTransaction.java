package vn.com.telsoft.entity;

import java.io.Serializable;
import java.util.Date;

public class VietinbankTransaction implements Serializable {
    private int id;
    private Date transactionDate;
    private String transactionDescription;
    private int debit;
    private int credit;
    private int accountBalance;
    private String transactionNumber;
    private String mtid_citad;
    private String fileName;
    private Date importDateTime;
    private Date valueDate;
    private String fileType;

    public VietinbankTransaction() {
    }

    public VietinbankTransaction(int id, Date transactionDate, String transactionDescription, int debit, int credit, int accountBalance, String transactionNumber, String mtid_citad, String fileName, Date importDateTime, Date valueDate, String fileType) {
        this.id = id;

        this.transactionDate = transactionDate;
        this.transactionDescription = transactionDescription;
        this.debit = debit;
        this.credit = credit;
        this.accountBalance = accountBalance;
        this.transactionNumber = transactionNumber;
        this.mtid_citad = mtid_citad;
        this.fileName = fileName;
        this.importDateTime = importDateTime;
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

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
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

    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getMtid_citad() {
        return mtid_citad;
    }

    public void setMtid_citad(String mtid_citad) {
        this.mtid_citad = mtid_citad;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getImportDateTime() {
        return importDateTime;
    }

    public void setImportDateTime(Date importDateTime) {
        this.importDateTime = importDateTime;
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
