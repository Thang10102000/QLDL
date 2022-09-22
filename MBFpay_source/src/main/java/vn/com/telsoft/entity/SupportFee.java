package vn.com.telsoft.entity;


import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.util.*;

public class SupportFee implements Serializable {

    private int fee_id;
    private int profile_id;
    private String product_code;
    private int fee_amount;
    private int fee_percent;
    private String status;

    public SupportFee() {
    }

    public SupportFee(int fee_id, int profile_id, String product_code, int fee_amount, int fee_percent, String status) {

        this.fee_id = fee_id;
        this.profile_id = profile_id;
        this.product_code = product_code;
        this.fee_amount = fee_amount;
        this.fee_percent = fee_percent;
        this.status = status;
    }

    public SupportFee(SupportFee ett) {

        this.fee_id = ett.fee_id;
        this.profile_id = ett.profile_id;
        this.product_code = ett.product_code;
        this.fee_amount = ett.fee_amount;
        this.fee_percent = ett.fee_percent;
        this.status = ett.status;
    }

    public int getFee_id() {
        return this.fee_id;
    }

    public void setFee_id(int fee_id) {
        this.fee_id = fee_id;
    }

    public int getProfile_id() {
        return this.profile_id;
    }

    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }

    public String getProduct_code() {
        return this.product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public int getFee_amount() {
        return this.fee_amount;
    }

    public void setFee_amount(int fee_amount) {
        this.fee_amount = fee_amount;
    }

    public int getFee_percent() {
        return this.fee_percent;
    }

    public void setFee_percent(int fee_percent) {
        this.fee_percent = fee_percent;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
