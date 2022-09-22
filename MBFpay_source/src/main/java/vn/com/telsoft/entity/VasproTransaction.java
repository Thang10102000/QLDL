package vn.com.telsoft.entity;

import java.io.Serializable;
import java.util.Date;

public class VasproTransaction implements Serializable {
    private String isdn;;
    private Date regDateTime;
    private Date endDateTime;
    private Date expdateTime;
    private String mobType;
    private String chargeCode;
    private String groupCode;
    private int chargePrice;
    private String sourceCode;
    private String regID;
    private String fileName;
    private Date logDateTime;
    private Date importDateTime;
    private int id;

    public VasproTransaction() {
    }

    public VasproTransaction(String isdn, Date regDateTime, Date endDateTime, Date expdateTime,
                             String mobType, String chargeCode, String groupCode, int chargePrice,
                             String sourceCode, String regID, String fileName, Date logDateTime,
                             Date importDateTime, int id) {
        this.isdn = isdn;
        this.regDateTime = regDateTime;
        this.endDateTime = endDateTime;
        this.expdateTime = expdateTime;
        this.mobType = mobType;
        this.chargeCode = chargeCode;
        this.groupCode = groupCode;
        this.chargePrice = chargePrice;
        this.sourceCode = sourceCode;
        this.regID = regID;
        this.fileName = fileName;
        this.logDateTime = logDateTime;
        this.importDateTime = importDateTime;
        this.id = id;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public Date getRegDateTime() {
        return regDateTime;
    }

    public void setRegDateTime(Date regDateTime) {
        this.regDateTime = regDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Date getExpdateTime() {
        return expdateTime;
    }

    public void setExpdateTime(Date expdateTime) {
        this.expdateTime = expdateTime;
    }

    public String getMobType() {
        return mobType;
    }

    public void setMobType(String mobType) {
        this.mobType = mobType;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public int getChargePrice() {
        return chargePrice;
    }

    public void setChargePrice(int chargePrice) {
        this.chargePrice = chargePrice;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getRegID() {
        return regID;
    }

    public void setRegID(String regID) {
        this.regID = regID;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
