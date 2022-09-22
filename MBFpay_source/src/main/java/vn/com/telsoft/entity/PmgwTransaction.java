package vn.com.telsoft.entity;

import java.io.Serializable;
import java.util.Date;

public class PmgwTransaction implements Serializable {
    private int id;
    private int serviceCode;
    private Date settlementDate;
    private String transID;
    private String processCode;
    private Date transDateTime;
    private String custCode;
    private String isdn;
    private String centerCode;
    private String lastDebt;
    private int cycleNumber;
    private int settlementAmount;
    private int debtRemain;
    private long paymentID;
    private Date paymentStartDate;
    private Date paymentEndDate;
    private String fileName;
    private Date logDateTime;
    private Date importDateTime;

    public PmgwTransaction() {
    }

    public PmgwTransaction(int id, int serviceCode, Date settlementDate, String transID,
                           String processCode, Date transDateTime, String custCode,
                           String isdn, String centerCode, String lastDebt, int cycleNumber, int settlementAmount, int debtRemain, long paymentID, Date paymentStartDate, Date paymentEndDate, String fileName, Date logDateTime, Date importDateTime) {
        this.id = id;
        this.serviceCode = serviceCode;
        this.settlementDate = settlementDate;
        this.transID = transID;
        this.processCode = processCode;
        this.transDateTime = transDateTime;
        this.custCode = custCode;
        this.isdn = isdn;
        this.centerCode = centerCode;
        this.lastDebt = lastDebt;
        this.cycleNumber = cycleNumber;
        this.settlementAmount = settlementAmount;
        this.debtRemain = debtRemain;
        this.paymentID = paymentID;
        this.paymentStartDate = paymentStartDate;
        this.paymentEndDate = paymentEndDate;
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

    public int getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(int serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Date getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getTransID() {
        return transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public Date getTransDateTime() {
        return transDateTime;
    }

    public void setTransDateTime(Date transDateTime) {
        this.transDateTime = transDateTime;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }

    public String getLastDebt() {
        return lastDebt;
    }

    public void setLastDebt(String lastDebt) {
        this.lastDebt = lastDebt;
    }

    public int getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(int cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public int getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(int settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public int getDebtRemain() {
        return debtRemain;
    }

    public void setDebtRemain(int debtRemain) {
        this.debtRemain = debtRemain;
    }

    public long getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(long paymentID) {
        this.paymentID = paymentID;
    }

    public Date getPaymentStartDate() {
        return paymentStartDate;
    }

    public void setPaymentStartDate(Date paymentStartDate) {
        this.paymentStartDate = paymentStartDate;
    }

    public Date getPaymentEndDate() {
        return paymentEndDate;
    }

    public void setPaymentEndDate(Date paymentEndDate) {
        this.paymentEndDate = paymentEndDate;
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
