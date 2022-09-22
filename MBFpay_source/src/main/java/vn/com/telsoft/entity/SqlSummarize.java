package vn.com.telsoft.entity;


import java.io.Serializable;
import java.util.Date;

public class SqlSummarize implements Serializable {

    private String status;
    private Date runDate;
    private String mismatchedQuery;
    private String matchedQuery;
    private String checkQuery;
    private int profileID;
    private int sqlID;
    private int summarizeCycle;
    private String partnerCode;
    private String description;

    public SqlSummarize() {
    }

    public SqlSummarize(String status, Date runDate, String mismatchedQuery,
                        String matchedQuery, String checkQuery, int profileID,
                        int sqlID, int summarizeCycle, String partnerCode,
                        String description) {

        this.status = status;
        this.runDate = runDate;
        this.mismatchedQuery = mismatchedQuery;
        this.matchedQuery = matchedQuery;
        this.checkQuery = checkQuery;
        this.profileID = profileID;
        this.sqlID = sqlID;
        this.summarizeCycle = summarizeCycle;
        this.partnerCode = partnerCode;
        this.description = description;
    }

    public SqlSummarize(SqlSummarize ett) {

        this.status = ett.status;
        this.runDate = ett.runDate;
        this.mismatchedQuery = ett.mismatchedQuery;
        this.matchedQuery = ett.matchedQuery;
        this.checkQuery = ett.checkQuery;
        this.profileID = ett.profileID;
        this.sqlID = ett.sqlID;
        this.summarizeCycle = ett.summarizeCycle;
        this.partnerCode = ett.partnerCode;
        this.description = ett.description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRunDate() {
        return runDate;
    }

    public void setRunDate(Date runDate) {
        this.runDate = runDate;
    }

    public String getMismatchedQuery() {
        return mismatchedQuery;
    }

    public void setMismatchedQuery(String mismatchedQuery) {
        this.mismatchedQuery = mismatchedQuery;
    }

    public String getMatchedQuery() {
        return matchedQuery;
    }

    public void setMatchedQuery(String matchedQuery) {
        this.matchedQuery = matchedQuery;
    }

    public String getCheckQuery() {
        return checkQuery;
    }

    public void setCheckQuery(String checkQuery) {
        this.checkQuery = checkQuery;
    }

    public int getProfileID() {
        return profileID;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public int getSqlID() {
        return sqlID;
    }

    public void setSqlID(int sqlID) {
        this.sqlID = sqlID;
    }

    public int getSummarizeCycle() {
        return summarizeCycle;
    }

    public void setSummarizeCycle(int summarizeCycle) {
        this.summarizeCycle = summarizeCycle;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
