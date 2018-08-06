package com.seluhadu.shchat.models;

public class Like {
    private String userId;
    private String userName;
    private String userDisplayName;

    public Like() {
    }

    public Like(String userId, String userName, String userDisplayName) {
        this.userId = userId;
        this.userName = userName;
        this.userDisplayName = userDisplayName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }
}

