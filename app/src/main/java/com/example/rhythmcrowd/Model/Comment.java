package com.example.rhythmcrowd.Model;

import java.util.Date;

public class Comment {

    private String username;
    private Date timestamp;
    private String commentTxt;
    private String documentId;
    private String userId;

    public Comment(String username, Date timestamp, String commentTxt, String documentId, String userId) {
        this.username = username;
        this.timestamp = timestamp;
        this.commentTxt = commentTxt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getCommentTxt() {
        return commentTxt;
    }

    public void setCommentTxt(String commentTxt) {
        this.commentTxt = commentTxt;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
