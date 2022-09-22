package vn.com.telsoft.entity;

import java.io.Serializable;
import java.util.Date;

public class IrisTransaction implements Serializable {
    private int id;
    private String transactionID;
    private String type;
    private String orderNumber;
    private String product;
    private int amount;
    private int quantity;
    private int grandAmount;
    private Date transactionDateTime;
    private String status;
    private Date exportDateTime;
    private String fileName;
    private Date importDateTime;

    public IrisTransaction() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
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

    public int getGrandAmount() {
        return grandAmount;
    }

    public void setGrandAmount(int grandAmount) {
        this.grandAmount = grandAmount;
    }

    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getExportDateTime() {
        return exportDateTime;
    }

    public void setExportDateTime(Date exportDateTime) {
        this.exportDateTime = exportDateTime;
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
}
