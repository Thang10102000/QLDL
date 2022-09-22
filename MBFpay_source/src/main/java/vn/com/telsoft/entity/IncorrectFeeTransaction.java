package vn.com.telsoft.entity;

import java.io.Serializable;
import java.util.Date;

public class IncorrectFeeTransaction implements Serializable {
    // mbs fields
    private String invoiceOrderID;
    private String transactionID;
    private String sourceReceiptID;
    private String saleOrderID;
    private String payType;
    private String payTypeName;
    private String partnerCode;
    private String partnerReferenceID;
    private String partnerOrderID;
    private String partnerInvoiceID;
    private String bankCode;
    private String bankReferenceID;
    private String productServiceID;
    private String productServiceCode;
    private String productID;
    private String productCode;
    private String productName;
    private String productDetail;
    private String description;
    private String productAccount;
    private int amount;
    private int quantity;
    private String saleDiscount;
    private String saleFee;
    private int grandAmount;
    private int fee;
    private int relatedFee;
    private int paymentAmount;
    private String accountName;
    private String relatedAccount;
    private String orderStatus;
    private String orderStatusCode;
    private String createdUser;
    private String confirmUser;
    private Date initTime;
    private Date createdTime;
    private String fileName;
    private Date logDateTime;
    private Date importDateTime;
    private String bankReferenceData;
    private String accountID;
    private String userType;
    private Date endTime;
    private Date transTime;
    private String issuerBankCode;

    // additional fields
    private Date adjustDate;
    private String refundStatus;
    private String userProcess;
    private String confirmStatus;
    private String userConfirm;
    private String ticketName;
    private int refundAmount;

    public IncorrectFeeTransaction() {
    }

    public IncorrectFeeTransaction(String invoiceOrderID, String transactionID, String sourceReceiptID,
                                   String saleOrderID, String payType, String payTypeName, String partnerCode,
                                   String partnerReferenceID, String partnerOrderID, String partnerInvoiceID,
                                   String bankCode, String bankReferenceID, String productServiceID,
                                   String productServiceCode, String productID, String productCode,
                                   String productName, String productDetail, String description,
                                   String productAccount, int amount, int quantity, String saleDiscount,
                                   String saleFee, int grandAmount, int fee, int relatedFee, int paymentAmount,
                                   String accountName, String relatedAccount, String orderStatus,
                                   String orderStatusCode, String createdUser, String confirmUser,
                                   Date initTime, Date createdTime, String fileName, Date logDateTime,
                                   Date importDateTime, String bankReferenceData, String accountID,
                                   String userType, Date endTime, Date transTime, String issuerBankCode,
                                   Date adjustDate, String userProcess,
                                   String confirmStatus, String userConfirm, String ticketName, int refundAmount,
                                   String refundStatus) {
        this.invoiceOrderID = invoiceOrderID;
        this.transactionID = transactionID;
        this.sourceReceiptID = sourceReceiptID;
        this.saleOrderID = saleOrderID;
        this.payType = payType;
        this.payTypeName = payTypeName;
        this.partnerCode = partnerCode;
        this.partnerReferenceID = partnerReferenceID;
        this.partnerOrderID = partnerOrderID;
        this.partnerInvoiceID = partnerInvoiceID;
        this.bankCode = bankCode;
        this.bankReferenceID = bankReferenceID;
        this.productServiceID = productServiceID;
        this.productServiceCode = productServiceCode;
        this.productID = productID;
        this.productCode = productCode;
        this.productName = productName;
        this.productDetail = productDetail;
        this.description = description;
        this.productAccount = productAccount;
        this.amount = amount;
        this.quantity = quantity;
        this.saleDiscount = saleDiscount;
        this.saleFee = saleFee;
        this.grandAmount = grandAmount;
        this.fee = fee;
        this.relatedFee = relatedFee;
        this.paymentAmount = paymentAmount;
        this.accountName = accountName;
        this.relatedAccount = relatedAccount;
        this.orderStatus = orderStatus;
        this.orderStatusCode = orderStatusCode;
        this.createdUser = createdUser;
        this.confirmUser = confirmUser;
        this.initTime = initTime;
        this.createdTime = createdTime;
        this.fileName = fileName;
        this.logDateTime = logDateTime;
        this.importDateTime = importDateTime;
        this.bankReferenceData = bankReferenceData;
        this.accountID = accountID;
        this.userType = userType;
        this.endTime = endTime;
        this.transTime = transTime;
        this.issuerBankCode = issuerBankCode;
        this.adjustDate = adjustDate;
        this.userProcess = userProcess;
        this.confirmStatus = confirmStatus;
        this.userConfirm = userConfirm;
        this.ticketName = ticketName;
        this.refundAmount = refundAmount;
        this.refundStatus = refundStatus;
    }

    public String getInvoiceOrderID() {
        return invoiceOrderID;
    }

    public void setInvoiceOrderID(String invoiceOrderID) {
        this.invoiceOrderID = invoiceOrderID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getSourceReceiptID() {
        return sourceReceiptID;
    }

    public void setSourceReceiptID(String sourceReceiptID) {
        this.sourceReceiptID = sourceReceiptID;
    }

    public String getSaleOrderID() {
        return saleOrderID;
    }

    public void setSaleOrderID(String saleOrderID) {
        this.saleOrderID = saleOrderID;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getPartnerReferenceID() {
        return partnerReferenceID;
    }

    public void setPartnerReferenceID(String partnerReferenceID) {
        this.partnerReferenceID = partnerReferenceID;
    }

    public String getPartnerOrderID() {
        return partnerOrderID;
    }

    public void setPartnerOrderID(String partnerOrderID) {
        this.partnerOrderID = partnerOrderID;
    }

    public String getPartnerInvoiceID() {
        return partnerInvoiceID;
    }

    public void setPartnerInvoiceID(String partnerInvoiceID) {
        this.partnerInvoiceID = partnerInvoiceID;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankReferenceID() {
        return bankReferenceID;
    }

    public void setBankReferenceID(String bankReferenceID) {
        this.bankReferenceID = bankReferenceID;
    }

    public String getProductServiceID() {
        return productServiceID;
    }

    public void setProductServiceID(String productServiceID) {
        this.productServiceID = productServiceID;
    }

    public String getProductServiceCode() {
        return productServiceCode;
    }

    public void setProductServiceCode(String productServiceCode) {
        this.productServiceCode = productServiceCode;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductAccount() {
        return productAccount;
    }

    public void setProductAccount(String productAccount) {
        this.productAccount = productAccount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSaleDiscount() {
        return saleDiscount;
    }

    public void setSaleDiscount(String saleDiscount) {
        this.saleDiscount = saleDiscount;
    }

    public String getSaleFee() {
        return saleFee;
    }

    public void setSaleFee(String saleFee) {
        this.saleFee = saleFee;
    }

    public int getGrandAmount() {
        return grandAmount;
    }

    public void setGrandAmount(int grandAmount) {
        this.grandAmount = grandAmount;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getRelatedFee() {
        return relatedFee;
    }

    public void setRelatedFee(int relatedFee) {
        this.relatedFee = relatedFee;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getRelatedAccount() {
        return relatedAccount;
    }

    public void setRelatedAccount(String relatedAccount) {
        this.relatedAccount = relatedAccount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusCode() {
        return orderStatusCode;
    }

    public void setOrderStatusCode(String orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getConfirmUser() {
        return confirmUser;
    }

    public void setConfirmUser(String confirmUser) {
        this.confirmUser = confirmUser;
    }

    public Date getInitTime() {
        return initTime;
    }

    public void setInitTime(Date initTime) {
        this.initTime = initTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
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

    public String getBankReferenceData() {
        return bankReferenceData;
    }

    public void setBankReferenceData(String bankReferenceData) {
        this.bankReferenceData = bankReferenceData;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public String getIssuerBankCode() {
        return issuerBankCode;
    }

    public void setIssuerBankCode(String issuerBankCode) {
        this.issuerBankCode = issuerBankCode;
    }

    public Date getAdjustDate() {
        return adjustDate;
    }

    public void setAdjustDate(Date adjustDate) {
        this.adjustDate = adjustDate;
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

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public int getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }
}
