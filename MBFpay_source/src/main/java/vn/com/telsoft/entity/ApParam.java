package vn.com.telsoft.entity;


import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.util.*;

public class ApParam implements Serializable {

    private String rowid;
    private String par_type;
    private String par_name;
    private String par_value;
    private String description;

    public ApParam() {
    }

    public ApParam(String rowid, String par_type, String par_name, String par_value, String description) {
        this.rowid = rowid;
        this.par_type = par_type;
        this.par_name = par_name;
        this.par_value = par_value;
        this.description = description;
    }

    public ApParam(ApParam ett) {
        this.rowid = ett.rowid;
        this.par_type = ett.par_type;
        this.par_name = ett.par_name;
        this.par_value = ett.par_value;
        this.description = ett.description;
    }

    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }

    public String getPar_type() {
        return this.par_type;
    }

    public void setPar_type(String par_type) {
        this.par_type = par_type;
    }

    public String getPar_name() {
        return this.par_name;
    }

    public void setPar_name(String par_name) {
        this.par_name = par_name;
    }

    public String getPar_value() {
        return this.par_value;
    }

    public void setPar_value(String par_value) {
        this.par_value = par_value;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
