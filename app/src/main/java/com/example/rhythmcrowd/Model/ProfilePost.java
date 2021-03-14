package com.example.rhythmcrowd.Model;

import java.util.Date;

public class ProfilePost {
    private String selectedCategory;

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
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

    public String getProfilePostTxt() {
        return profilePostTxt;
    }

    public void setProfilePostTxt(String profilePostTxt) {
        this.profilePostTxt = profilePostTxt;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
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

    public ProfilePost(String selectedCategory, String username, Date timestamp, String profilePostTxt, int numLikes, int numComments, String documentId, String userId) {
        this.selectedCategory = selectedCategory;
        this.username = username;
        this.timestamp = timestamp;
        this.profilePostTxt = profilePostTxt;
        this.numLikes = numLikes;
        this.numComments = numComments;
        this.documentId = documentId;
        this.userId = userId;
    }

    private String username;
    private Date timestamp;
    private String profilePostTxt;
    private int numLikes;
    private int numComments;
    private String documentId;
    private String userId;
}
