package vn.com.telsoft.entity;

import java.io.Serializable;
import java.util.Date;

public class BidvTransaction implements Serializable {
    private int id;
    private Date transactionDate;
    private int debit;
    private int credit;
    private long balance;
    private String transactionDescription;
    private String bankReferenceID;
    private String transactionNumber;
    private Date importDateTime;
    private String fileName;
    private Date valueDate;

    public BidvTransaction() {
    }

    public BidvTransaction(int id, Date transactionDate, int debit, int credit, long balance, String transactionDescription, String bankReferenceID, String transactionNumber, Date importDateTime, String fileName, Date valueDate) {
        this.id = id;
        this.transactionDate = transactionDate;
        this.debit = debit;
        this.credit = credit;
        this.balance = balance;
        this.transactionDescription = transactionDescription;
        this.bankReferenceID = bankReferenceID;
        this.transactionNumber = transactionNumber;
        this.importDateTime = importDateTime;
        this.fileName = fileName;
        this.valueDate = valueDate;
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

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
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

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
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

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }
}
