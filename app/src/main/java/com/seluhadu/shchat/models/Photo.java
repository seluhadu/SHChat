package com.seluhadu.shchat.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Photo implements Parcelable {
    private String caption;
    private String dateCreated;
    private String imagePath;
    private String imageId;
    private String userId;
    private String tags;
    private String userDisplayName;
    private String userName;
    private List<Like> like;
    private List<Comment> comments;

    public Photo() {
    }

    public Photo(String caption, String dateCreated, String imagePath, String imageId, String userId, String tags, String userDisplayName, String userName, List<Like> like, List<Comment> comments) {
        this.caption = caption;
        this.dateCreated = dateCreated;
        this.imagePath = imagePath;
        this.imageId = imageId;
        this.userId = userId;
        this.tags = tags;
        this.userDisplayName = userDisplayName;
        this.userName = userName;
        this.like = like;
        this.comments = comments;
    }

    private Photo(Parcel in) {
        caption = in.readString();
        dateCreated = in.readString();
        imagePath = in.readString();
        imageId = in.readString();
        userId = in.readString();
        tags = in.readString();
        userDisplayName = in.readString();
        userName = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Like> getLike() {
        return like;
    }

    public void setLike(List<Like> like) {
        this.like = like;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeString(dateCreated);
        dest.writeString(imagePath);
        dest.writeString(imageId);
        dest.writeString(userId);
        dest.writeString(tags);
        dest.writeString(userDisplayName);
        dest.writeString(userName);
    }
}
