package com.seluhadu.shchat.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String userName;
    private String userId;
    private String userProfile;
    private String userEmail;
    private String UserDisplayName;
    private long mLastSeenAt;
    private boolean mIsActive = true;
    private @UserMode.userMode int currentMode;


    public User() {
    }

    public User(String userName, String userId, String userProfile, String userEmail, String userDisplayName) {
        this.userName = userName;
        this.userId = userId;
        this.userProfile = userProfile;
        this.userEmail = userEmail;
        this.UserDisplayName = userDisplayName;
    }

    public String getUserDisplayName() {
        return UserDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        UserDisplayName = userDisplayName;
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }

    private User(Parcel in) {
        userName = in.readString();
        userId = in.readString();
        userProfile = in.readString();
        userEmail = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public void setCurrentMode(@UserMode.userMode int currentMode) {
        this.currentMode = currentMode;
    }
    @UserMode.userMode
    public int getCurrentMode() {
        return currentMode;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", userProfile='" + userProfile + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", UserDisplayName='" + UserDisplayName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userId);
        dest.writeString(userProfile);
        dest.writeString(userEmail);
    }
}