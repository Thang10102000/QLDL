package vn.com.telsoft.entity;


import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.util.*;

public class ApproveDocument implements Serializable {

    private String status;
    private Date create_datetime;
    private String excel_hash;
    private String pdf_hash;
    private Blob excel_document;
    private Blob pdf_document;
    private String approve_id;
    private List<LogApproveDocument> listLogApproveDocument;
    private String excel_path;
    private String pdf_path;
    private String profile_id;
    private Date from_date;
    private Date to_date;
    private String name;

    public ApproveDocument() {
        listLogApproveDocument = new ArrayList<>();
    }

    public ApproveDocument(String status, Date create_datetime, String excel_hash, String pdf_hash, Blob excel_document, Blob pdf_document, String approve_id,
                           List<LogApproveDocument> listLogApproveDocument, String profile_id, Date from_date, Date to_date, String name) {

        this.status = status;
        this.create_datetime = create_datetime;
        this.excel_hash = excel_hash;
        this.pdf_hash = pdf_hash;
        this.excel_document = excel_document;
        this.pdf_document = pdf_document;
        this.approve_id = approve_id;
        this.listLogApproveDocument = listLogApproveDocument;
        this.profile_id = profile_id;
        this.from_date = from_date;
        this.to_date = to_date;
        this.name = name;
    }

    public ApproveDocument(ApproveDocument ett) {

        this.status = ett.status;
        this.create_datetime = ett.create_datetime;
        this.excel_hash = ett.excel_hash;
        this.pdf_hash = ett.pdf_hash;
        this.excel_document = ett.excel_document;
        this.pdf_document = ett.pdf_document;
        this.approve_id = ett.approve_id;
        this.listLogApproveDocument = ett.listLogApproveDocument;
        this.profile_id = ett.profile_id;
        this.from_date = ett.from_date;
        this.to_date = ett.to_date;
        this.name = ett.name;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }

    public String getExcel_hash() {
        return this.excel_hash;
    }

    public void setExcel_hash(String excel_hash) {
        this.excel_hash = excel_hash;
    }

    public String getPdf_hash() {
        return this.pdf_hash;
    }

    public void setPdf_hash(String pdf_hash) {
        this.pdf_hash = pdf_hash;
    }

    public Blob getExcel_document() {
        return this.excel_document;
    }
    public void setExcel_document(Blob excel_document) {
        this.excel_document = excel_document;
    }

    public Blob getPdf_document() {
        return this.pdf_document;
    }

    public void setPdf_document(Blob pdf_document) {
        this.pdf_document = pdf_document;
    }

    public String getApprove_id() {
        return this.approve_id;
    }

    public void setApprove_id(String approve_id) {
        this.approve_id = approve_id;
    }

    public List<LogApproveDocument> getListLogApproveDocument() {
        Collections.sort(listLogApproveDocument, new Comparator<LogApproveDocument>() {
            public int compare(LogApproveDocument o1, LogApproveDocument o2) {
                return o2.getStatus().compareTo(o1.getStatus());
            }
        });
        return listLogApproveDocument;
    }

    public void setListLogApproveDocument(List<LogApproveDocument> listLogApproveDocument) {
        this.listLogApproveDocument = listLogApproveDocument;
    }

    public String getExcel_path() {
        return excel_path;
    }

    public void setExcel_path(String excel_path) {
        this.excel_path = excel_path;
    }

    public String getPdf_path() {
        return pdf_path;
    }

    public void setPdf_path(String pdf_path) {
        this.pdf_path = pdf_path;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public Date getFrom_date() {
        return from_date;
    }

    public void setFrom_date(Date from_date) {
        this.from_date = from_date;
    }

    public Date getTo_date() {
        return to_date;
    }

    public void setTo_date(Date to_date) {
        this.to_date = to_date;
    }
}
