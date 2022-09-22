package vn.com.telsoft.entity;

import java.io.Serializable;
import java.util.Date;

public class NapasTransaction implements Serializable {
    private String transactionID;
    private String merchantCode;
    private String acqBankCode;
    private String issuerBankCode;
    private Date transactionDate;
    private String transactionInfo;
    private int amount;
    private String responseCode;
    private String transactionStatus;
    private String status;
    private String transactionRef;
    private String cardNumber;
    private String cardHolderName;
    private String transactionType;
    private String bankID;
    private String ipAddress;
    private String serviceType;
    private String napasOrderID;
    private String submerchantCode;
    private String fileName;
    private Date importDateTime;
    private String fileType;

    public NapasTransaction() {

    }

    public NapasTransaction(String transactionID, String merchantCode, String acqBankCode, String issuerBankCode,
                            Date transactionDate, String transactionInfo, int amount, String responseCode,
                            String transactionStatus, String status, String transactionRef, String cardNumber,
                            String cardHolderName, String transactionType, String bankID, String ipAddress,
                            String serviceType, String napasOrderID, String submerchantCode, String fileName, Date importDateTime, String fileType) {
        this.transactionID = transactionID;
        this.merchantCode = merchantCode;
        this.acqBankCode = acqBankCode;
        this.issuerBankCode = issuerBankCode;
        this.transactionDate = transactionDate;
        this.transactionInfo = transactionInfo;
        this.amount = amount;
        this.responseCode = responseCode;
        this.transactionStatus = transactionStatus;
        this.status = status;
        this.transactionRef = transactionRef;
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.transactionType = transactionType;
        this.bankID = bankID;
        this.ipAddress = ipAddress;
        this.serviceType = serviceType;
        this.napasOrderID = napasOrderID;
        this.submerchantCode = submerchantCode;
        this.fileName = fileName;
        this.importDateTime = importDateTime;
        this.fileType = fileType;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getAcqBankCode() {
        return acqBankCode;
    }

    public void setAcqBankCode(String acqBankCode) {
        this.acqBankCode = acqBankCode;
    }

    public String getIssuerBankCode() {
        return issuerBankCode;
    }

    public void setIssuerBankCode(String issuerBankCode) {
        this.issuerBankCode = issuerBankCode;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionInfo() {
        return transactionInfo;
    }

    public void setTransactionInfo(String transactionInfo) {
        this.transactionInfo = transactionInfo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getBankID() {
        return bankID;
    }

    public void setBankID(String bankID) {
        this.bankID = bankID;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getNapasOrderID() {
        return napasOrderID;
    }

    public void setNapasOrderID(String napasOrderID) {
        this.napasOrderID = napasOrderID;
    }

    public String getSubmerchantCode() {
        return submerchantCode;
    }

    public void setSubmerchantCode(String submerchantCode) {
        this.submerchantCode = submerchantCode;
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

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
