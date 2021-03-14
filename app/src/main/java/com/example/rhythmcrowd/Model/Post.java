package com.example.rhythmcrowd.Model;

import java.util.Date;

public class Post {
    private String username;
    private Date timestamp;
    private String postTxt;
    private int numLikes;
    private int numComments;
    private String documentId;
    private String userId;

    public Post(String username, Date timestamp, String postTxt, int numLikes, int numComments, String documentId, String userId) {
        this.username = username;
        this.timestamp = timestamp;
        this.postTxt = postTxt;
        this.numLikes = numLikes;
        this.numComments = numComments;
        this.documentId = documentId;
        this.userId = userId;
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

    public String getPostTxt() {
        return postTxt;
    }

    public void setPostTxt(String postTxt) {
        this.postTxt = postTxt;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
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

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }
}
