package com.seluhadu.shchat.models;

public class Comment {
    private String userId;
    private String userName;
    private String userDisplayName;
    private String userProfile;
    private String DateTimeCommented;
    private String comment;

    public Comment() {
    }

    public Comment(String userId, String userName, String userDisplayName, String userProfile, String dateTimeCommented, String comment) {
        this.userId = userId;
        this.userName = userName;
        this.userDisplayName = userDisplayName;
        this.userProfile = userProfile;
        DateTimeCommented = dateTimeCommented;
        this.comment = comment;
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

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getDateTimeCommented() {
        return DateTimeCommented;
    }

    public void setDateTimeCommented(String dateTimeCommented) {
        DateTimeCommented = dateTimeCommented;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
