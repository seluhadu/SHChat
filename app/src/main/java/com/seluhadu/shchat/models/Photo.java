package com.seluhadu.shchat.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {
    private String tags;
    private String userId;
    private int likeCount;
    private String caption;
    private String imageId;
    private String imageUrl;
    private String userName;
    private int commentCount;
    private String statusType;
    private String datePosted;
    private String userDisplayName;


    public Photo() {
    }

    public Photo(String caption, String datePosted, String imagePath, String imageId, String userId, String tags, String userDisplayName, String userName, int like, int comments, String statuType) {
        this.caption = caption;
        this.datePosted = datePosted;
        this.imageUrl = imagePath;
        this.imageId = imageId;
        this.userId = userId;
        this.tags = tags;
        this.userDisplayName = userDisplayName;
        this.userName = userName;
        this.likeCount = like;
        this.commentCount = comments;
        this.statusType = statuType;
    }

    protected Photo(Parcel in) {
        tags = in.readString();
        userId = in.readString();
        likeCount = in.readInt();
        caption = in.readString();
        imageId = in.readString();
        imageUrl = in.readString();
        userName = in.readString();
        commentCount = in.readInt();
        datePosted = in.readString();
        statusType = in.readString();
        userDisplayName = in.readString();

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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public static Creator<Photo> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tags);
        dest.writeString(userId);
        dest.writeInt(likeCount);
        dest.writeString(caption);
        dest.writeString(imageId);
        dest.writeString(imageUrl);
        dest.writeString(userName);
        dest.writeInt(commentCount);
        dest.writeString(datePosted);
        dest.writeString(userDisplayName);
        dest.writeString(statusType);
    }
}
