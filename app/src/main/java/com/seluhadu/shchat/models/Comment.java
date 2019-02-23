package com.seluhadu.shchat.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {
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

    protected Comment(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        userDisplayName = in.readString();
        userProfile = in.readString();
        DateTimeCommented = in.readString();
        comment = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(userDisplayName);
        dest.writeString(userProfile);
        dest.writeString(DateTimeCommented);
        dest.writeString(comment);
    }
}
