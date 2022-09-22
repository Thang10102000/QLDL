package vn.com.telsoft.entity;


import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.util.*;

public class PayType implements Serializable {

    private int pay_type;
    private String pay_type_name;

    public PayType() {
    }

    public PayType(int pay_type, String pay_type_name) {

        this.pay_type = pay_type;
        this.pay_type_name = pay_type_name;
    }

    public PayType(PayType ett) {

        this.pay_type = ett.pay_type;
        this.pay_type_name = ett.pay_type_name;
    }

    public int getPay_type() {
        return this.pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_type_name() {
        return this.pay_type_name;
    }

    public void setPay_type_name(String pay_type_name) {
        this.pay_type_name = pay_type_name;
    }
}
