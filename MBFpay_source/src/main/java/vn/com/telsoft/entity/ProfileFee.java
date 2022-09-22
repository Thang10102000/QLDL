package vn.com.telsoft.entity;


import java.io.Serializable;

public class ProfileFee implements Serializable {

    private String status;
    private String fee_amount2;
    private String fee_kind2;
    private String fee_amount1;
    private String fee_kind1;
    private String fee_type;
    private int profile_id;
    private int fee_id;

    public ProfileFee() {
    }

    public ProfileFee(String status, String fee_amount2, String fee_kind2, String fee_amount1, String fee_kind1, String fee_type, int profile_id, int fee_id) {

        this.status = status;
        this.fee_amount2 = fee_amount2;
        this.fee_kind2 = fee_kind2;
        this.fee_amount1 = fee_amount1;
        this.fee_kind1 = fee_kind1;
        this.fee_type = fee_type;
        this.profile_id = profile_id;
        this.fee_id = fee_id;
    }

    public ProfileFee(ProfileFee ett) {

        this.status = ett.status;
        this.fee_amount2 = ett.fee_amount2;
        this.fee_kind2 = ett.fee_kind2;
        this.fee_amount1 = ett.fee_amount1;
        this.fee_kind1 = ett.fee_kind1;
        this.fee_type = ett.fee_type;
        this.profile_id = ett.profile_id;
        this.fee_id = ett.fee_id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFee_amount2() {
        return this.fee_amount2;
    }

    public void setFee_amount2(String fee_amount2) {
        this.fee_amount2 = fee_amount2;
    }

    public String getFee_kind2() {
        return this.fee_kind2;
    }

    public void setFee_kind2(String fee_kind2) {
        this.fee_kind2 = fee_kind2;
    }

    public String getFee_amount1() {
        return this.fee_amount1;
    }

    public void setFee_amount1(String fee_amount1) {
        this.fee_amount1 = fee_amount1;
    }

    public String getFee_kind1() {
        return this.fee_kind1;
    }

    public void setFee_kind1(String fee_kind1) {
        this.fee_kind1 = fee_kind1;
    }

    public String getFee_type() {
        return this.fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public int getProfile_id() {
        return this.profile_id;
    }

    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }

    public int getFee_id() {
        return this.fee_id;
    }

    public void setFee_id(int fee_id) {
        this.fee_id = fee_id;
    }
}
