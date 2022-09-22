package vn.com.telsoft.entity;

import java.io.Serializable;
import java.util.Date;

public class ProfilePartner implements Serializable {

    private String status;
    private String info;
    private String email;
    private String chargeFee;
    private String bankName;
    private String accountNumber;
    private String accountName;
    private String contractType;
    private Date expireDate;
    private Date signDate;
    private String contractNumber;
    private String type;
    private String unit;
    private String name;
    private String code;
    private String profileID;
    private String subCode;
    private int summarizeCycle;

    public ProfilePartner() {
    }

    public ProfilePartner(String status, String info, String email,
                          String chargeFee, String bankName,
                          String accountNumber, String accountName,
                          String contractType, Date expireDate,
                          Date signDate, String contractNumber,
                          String type, String unit, String name,
                          String code, String profileID, String subCode, int summarizeCycle) {

        this.status = status;
        this.info = info;
        this.email = email;
        this.chargeFee = chargeFee;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.contractType = contractType;
        this.expireDate = expireDate;
        this.signDate = signDate;
        this.contractNumber = contractNumber;
        this.type = type;
        this.unit = unit;
        this.name = name;
        this.code = code;
        this.profileID = profileID;
        this.subCode = subCode;
        this.summarizeCycle = summarizeCycle;
    }

    public ProfilePartner(ProfilePartner ett) {

        this.status = ett.status;
        this.info = ett.info;
        this.email = ett.email;
        this.chargeFee = ett.chargeFee;
        this.bankName = ett.bankName;
        this.accountNumber = ett.accountNumber;
        this.accountName = ett.accountName;
        this.contractType = ett.contractType;
        this.expireDate = ett.expireDate;
        this.signDate = ett.signDate;
        this.contractNumber = ett.contractNumber;
        this.type = ett.type;
        this.unit = ett.unit;
        this.name = ett.name;
        this.code = ett.code;
        this.profileID = ett.profileID;
        this.subCode = ett.subCode;
        this.summarizeCycle = ett.summarizeCycle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChargeFee() {
        return chargeFee;
    }

    public void setChargeFee(String chargeFee) {
        this.chargeFee = chargeFee;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public int getSummarizeCycle() {
        return summarizeCycle;
    }

    public void setSummarizeCycle(int summarizeCycle) {
        this.summarizeCycle = summarizeCycle;
    }

}
