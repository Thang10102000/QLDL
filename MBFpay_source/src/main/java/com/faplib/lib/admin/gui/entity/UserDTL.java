package com.faplib.lib.admin.gui.entity;

import com.faplib.util.StringUtil;
import com.faplib.ws.thread.EttUserObject;
import java.io.Serializable;
import java.util.Date;

public class UserDTL implements Serializable {
    private static final long serialVersionUID = 1L;

    private long userId;

    private String userName;

    private String password;

    private long expireStatus;

    private String fullName;

    private long status;

    private String phone;

    private String mobile;

    private String fax;

    private String email;

    private String address;

    private Date modifiedPassword;

    private Date lockedDate;

    private long failureCount;

    private long createdId;

    private long deletedId;

    private long modifiedId;

    private String config;

    private String branchCode;

    private String districtCode;

    private long title;

    public UserDTL(UserDTL account) {
        this.userId = account.userId;
        this.userName = account.userName;
        this.password = account.password;
        this.expireStatus = account.expireStatus;
        this.fullName = account.fullName;
        this.status = account.status;
        this.phone = account.phone;
        this.mobile = account.mobile;
        this.fax = account.fax;
        this.email = account.email;
        this.address = account.address;
        this.modifiedPassword = account.modifiedPassword;
        this.lockedDate = account.lockedDate;
        this.failureCount = account.failureCount;
        this.createdId = account.createdId;
        this.deletedId = account.deletedId;
        this.modifiedId = account.modifiedId;
        this.config = account.config;
        this.branchCode = account.branchCode;
        this.districtCode = account.districtCode;
        this.title = account.title;
    }

    public UserDTL(EttUserObject account) {
        this.userId = account.getUserId();
        this.userName = account.getUserName();
        this.password = account.getPassword();
        this.expireStatus = account.getExpireStatus();
        this.fullName = account.getFullName();
        this.status = account.getStatus();
        this.phone = account.getPhone();
        this.mobile = account.getMobile();
        this.fax = account.getFax();
        this.email = account.getEmail();
        this.address = account.getAddress();
        this.modifiedPassword = account.getModifiedPassword();
        this.lockedDate = account.getLockedDate();
        this.failureCount = account.getFailureCount();
        this.createdId = account.getCreatedId();
        this.deletedId = account.getDeletedId();
        this.modifiedId = account.getModifiedId();
        this.config = account.getConfig();
        this.branchCode = account.getBranchCode();
        this.districtCode = account.getDistrictCode();
    }

    public UserDTL() {
        this.expireStatus = 1L;
        this.status = 1L;
        this.title = 0L;
    }

    public static long getSerialVersionUID() {
        return 1L;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = (userName != null) ? userName.toUpperCase() : null;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getExpireStatus() {
        return this.expireStatus;
    }

    public void setExpireStatus(long expireStatus) {
        this.expireStatus = expireStatus;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getStatus() {
        return this.status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getModifiedPassword() {
        return this.modifiedPassword;
    }

    public void setModifiedPassword(Date modifiedPassword) {
        this.modifiedPassword = modifiedPassword;
    }

    public Date getLockedDate() {
        return this.lockedDate;
    }

    public void setLockedDate(Date lockedDate) {
        this.lockedDate = lockedDate;
    }

    public long getFailureCount() {
        return this.failureCount;
    }

    public void setFailureCount(long failureCount) {
        this.failureCount = failureCount;
    }

    public long getCreatedId() {
        return this.createdId;
    }

    public void setCreatedId(long createdId) {
        this.createdId = createdId;
    }

    public long getDeletedId() {
        return this.deletedId;
    }

    public void setDeletedId(long deletedId) {
        this.deletedId = deletedId;
    }

    public long getModifiedId() {
        return this.modifiedId;
    }

    public void setModifiedId(long modifiedId) {
        this.modifiedId = modifiedId;
    }

    public String getConfig() {
        return StringUtil.fix(this.config);
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getDistrictCode() {
        return this.districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public long getTitle() {
        return this.title;
    }

    public void setTitle(long title) {
        this.title = title;
    }
}