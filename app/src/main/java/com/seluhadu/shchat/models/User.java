package com.seluhadu.shchat.models;

public class User {
    private String userName;
    private String userId;
    private String userProfile;
    private String userDesc;
    private String userEmail;

    public User() {
    }

    public User(String userName, String userId, String userProfile, String userDesc, String userEmail) {
        this.userName = userName;
        this.userId = userId;
        this.userProfile = userProfile;
        this.userDesc = userDesc;
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", userProfile='" + userProfile + '\'' +
                ", userDesc='" + userDesc + '\'' +
                '}';
    }
}
