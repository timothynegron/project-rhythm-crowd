package com.example.rhythmcrowd.Model;

import java.util.Date;

public class User {
    private String username;
    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private String userId;

    private String bio;
    private int numFollowers;
    private int numFollowing;

    private Date dateStartedConnection;

    // Connection
    public User(String username, String userId, Date dateStartedConnection){
        this.username = username;
        this.userId = userId;
        this.dateStartedConnection = dateStartedConnection;
    }

    public User(String username, String userId, String bio, String city, String state, int numFollowers, int numFollowing){
        this.username = username;
        this.userId = userId;
        this.bio = bio;
        this.city = city;
        this.state = state;
        this.numFollowers = numFollowers;
        this.numFollowing = numFollowing;
    }

    public User(String username, String firstName, String lastName, String city, String state,
                String userId) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.state = state;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public int getNumFollowers() { return numFollowers; }

    public void setNumFollowers(int numFollowers) { this.numFollowers = numFollowers; }

    public int getNumFollowing() { return numFollowing; }

    public void setNumFollowing(int numFollowing) { this.numFollowing = numFollowing; }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getDateStartedConnection(){
        return dateStartedConnection;
    }

    public void setDateStartedFollowing(Date dateStartedConnection){
        this.dateStartedConnection = dateStartedConnection;
    }
}
