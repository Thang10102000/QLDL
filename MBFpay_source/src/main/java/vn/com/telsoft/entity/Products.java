package vn.com.telsoft.entity;


import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.util.*;

public class Products implements Serializable {

    private String payType;
    private String payTypeName;

    public Products() {
    }

    public Products(String payType, String payTypeName) {
        this.payType = payType;
        this.payTypeName = payTypeName;
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
