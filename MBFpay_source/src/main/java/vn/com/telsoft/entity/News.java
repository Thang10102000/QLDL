/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Chien Do Xo
 */
public class News implements Serializable {

    private long newsId;
    private String title;
    private Date createdDate;
    private String status;
    private long createdId;
    private String createdName;
    private long modifiedId;
    private String modifiedName;
    private Date modifiedDate;
    private String content;
    private String color;

    public News() {
    }

    public News(News ett) {
        this.newsId = ett.newsId;
        this.title = ett.title;
        this.createdDate = ett.createdDate;
        this.status = ett.status;
        this.createdId = ett.createdId;
        this.modifiedId = ett.modifiedId;
        this.modifiedDate = ett.modifiedDate;
        this.content = ett.content;
        this.color = ett.color;
        this.createdName = ett.createdName;
        this.modifiedName = ett.modifiedName;
    }

    public long getNewsId() {
        return newsId;
    }

    public void setNewsId(long newsId) {
        this.newsId = newsId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createDate) {
        this.createdDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreatedId() {
        return createdId;
    }

    public void setCreatedId(long createdId) {
        this.createdId = createdId;
    }

    public long getModifiedId() {
        return modifiedId;
    }

    public void setModifiedId(long modifiedId) {
        this.modifiedId = modifiedId;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public String getModifiedName() {
        return modifiedName;
    }

    public void setModifiedName(String modifiedName) {
        this.modifiedName = modifiedName;
    }
        
}
