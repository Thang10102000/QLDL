package vn.com.telsoft.entity;

import java.io.Serializable;
import java.util.Date;

public class CenterCodeBundleTransaction implements Serializable {
    private int id;
    private String transID;
    private Date requestTime;
    private String userName;
    private String mobile;
    private String status;
    private String productName;
    private int productPrice;
    private int quantity;
    private String fromSerial;
    private String toSerial;
    private String fileName;
    private Date logDateTime;
    private Date importDateTime;

    public CenterCodeBundleTransaction() {
    }

    public CenterCodeBundleTransaction(int id, String transID, Date requestTime, String userName, String mobile, String status, String productName, int productPrice, int quantity, String fromSerial, String toSerial, String fileName, Date logDateTime, Date importDateTime) {
        this.id = id;
        this.transID = transID;
        this.requestTime = requestTime;
        this.userName = userName;
        this.mobile = mobile;
        this.status = status;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.fromSerial = fromSerial;
        this.toSerial = toSerial;
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

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFromSerial() {
        return fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    public String getToSerial() {
        return toSerial;
    }

    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
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
