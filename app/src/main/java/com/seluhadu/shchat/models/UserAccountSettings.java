package com.seluhadu.shchat.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserAccountSettings implements Parcelable{
    private long follower;
    private long following;
    private long posts;
    private String userDisplayName;
    private String  description;
    private String  website;
    private String  userId;
    private String userName;

    public UserAccountSettings() {
    }

    public UserAccountSettings(long follower, long following, long posts, String userDisplayName, String description, String website, String userId, String userName) {
        this.follower = follower;
        this.following = following;
        this.posts = posts;
        this.userDisplayName = userDisplayName;
        this.description = description;
        this.website = website;
        this.userId = userId;
        this.userName = userName;
    }

    protected UserAccountSettings(Parcel in) {
        follower = in.readLong();
        following = in.readLong();
        posts = in.readLong();
        userDisplayName = in.readString();
        description = in.readString();
        website = in.readString();
        userId = in.readString();
        userName = in.readString();
    }

    public static final Creator<UserAccountSettings> CREATOR = new Creator<UserAccountSettings>() {
        @Override
        public UserAccountSettings createFromParcel(Parcel in) {
            return new UserAccountSettings(in);
        }

        @Override
        public UserAccountSettings[] newArray(int size) {
            return new UserAccountSettings[size];
        }
    };

    public long getFollower() {
        return follower;
    }

    public void setFollower(long follower) {
        this.follower = follower;
    }

    public long getFollowing() {
        return following;
    }

    public void setFollowing(long following) {
        this.following = following;
    }

    public long getPosts() {
        return posts;
    }

    public void setPosts(long posts) {
        this.posts = posts;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(follower);
        dest.writeLong(following);
        dest.writeLong(posts);
        dest.writeString(userDisplayName);
        dest.writeString(description);
        dest.writeString(website);
        dest.writeString(userId);
        dest.writeString(userName);
    }

    @Override
    public String toString() {
        return "UserAccountSettings{" +
                "follower=" + follower +
                ", following=" + following +
                ", posts=" + posts +
                ", userDisplayName='" + userDisplayName + '\'' +
                ", description='" + description + '\'' +
                ", website='" + website + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
