package vn.com.telsoft.entity;


import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.util.*;

public class PartnerFee implements Serializable {

    private int feeID;
    private int profileID;
    private String productCode;
    private double mbfpRate;
    private String status;
    private String partnerType;
    private double providerRate;
    private int providerRateType;
    private double discountRate;
    private double specialDiscount;
    private String productName;
    private int processFee;
    private double paymentFee;
    private Date specialDate;
    private long minFee;
    private long maxFee;
    private String payType;
    private String payTypeName;

    public PartnerFee() {
    }

    public PartnerFee(int feeID, int profileID, String productCode, double mbfpRate, String status,
                      String partnerType, double providerRate, int providerRateType,
                      double discountRate, double specialDiscount, String productName,
                      int processFee, double paymentFee, Date specialDate, long minFee, long maxFee,
                      String payType, String payTypeName) {
        this.feeID = feeID;
        this.profileID = profileID;
        this.productCode = productCode;
        this.mbfpRate = mbfpRate;
        this.status = status;
        this.partnerType = partnerType;
        this.providerRate = providerRate;
        this.providerRateType = providerRateType;
        this.discountRate = discountRate;
        this.specialDiscount = specialDiscount;
        this.productName = productName;
        this.processFee = processFee;
        this.paymentFee = paymentFee;
        this.specialDate = specialDate;
        this.minFee = minFee;
        this.maxFee = maxFee;
        this.payType = payType;
        this.payTypeName = payTypeName;
    }

    public double getMbfpRate() {
        return mbfpRate;
    }

    public void setMbfpRate(double mbfpRate) {
        this.mbfpRate = mbfpRate;
    }

    public int getFeeID() {
        return feeID;
    }

    public void setFeeID(int feeID) {
        this.feeID = feeID;
    }

    public int getProfileID() {
        return profileID;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(String partnerType) {
        this.partnerType = partnerType;
    }

    public double getProviderRate() {
        return providerRate;
    }

    public void setProviderRate(double providerRate) {
        this.providerRate = providerRate;
    }

    public int getProviderRateType() {
        return providerRateType;
    }

    public void setProviderRateType(int providerRateType) {
        this.providerRateType = providerRateType;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getSpecialDiscount() {
        return specialDiscount;
    }

    public void setSpecialDiscount(double specialDiscount) {
        this.specialDiscount = specialDiscount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProcessFee() {
        return processFee;
    }

    public void setProcessFee(int processFee) {
        this.processFee = processFee;
    }

    public double getPaymentFee() {
        return paymentFee;
    }

    public void setPaymentFee(double paymentFee) {
        this.paymentFee = paymentFee;
    }

    public Date getSpecialDate() {
        return specialDate;
    }

    public void setSpecialDate(Date specialDate) {
        this.specialDate = specialDate;
    }

    public long getMinFee() {
        return minFee;
    }

    public void setMinFee(long minFee) {
        this.minFee = minFee;
    }

    public long getMaxFee() {
        return maxFee;
    }

    public void setMaxFee(long maxFee) {
        this.maxFee = maxFee;
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
}
