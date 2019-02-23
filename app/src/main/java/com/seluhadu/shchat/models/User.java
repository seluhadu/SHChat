package com.seluhadu.shchat.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

import static android.support.constraint.Constraints.TAG;

public class User implements Parcelable {

    private String userName;
    private String userId;
    private String userProfile;
    private String userEmail;
    private String UserDisplayName;
    private long lastSeenAt;
    private boolean isActive = true;


    public User() {
    }

    public HashMap<String, Object> build(String userName, String userId, String userProfile, String userEmail, String userDisplayName, boolean isActive, boolean isOnline, long lastSeenAt) {
        HashMap<String, Object> obj = new HashMap<>();
        obj.put("userId", userId);
        obj.put("userName", userName);
        obj.put("userProfile", userProfile);
        obj.put("userEmail", userEmail);
        obj.put("userDisplayName", userDisplayName);
        obj.put("isActive", isActive);
        obj.put("isOnline", isOnline);
        obj.put("lastSeenAt", lastSeenAt);
        return obj;
    }

    User(HashMap<String, Object> obj) {
        if (obj.containsKey("userId")) {
            this.userId = obj.get("userId").toString();
        }
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
    public static User getUserFromId(final String userId) {
        final User user = new User();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        final DocumentReference dr = firebaseFirestore.collection("Users").document(userId);
        dr.get().addOnSuccessListener(documentSnapshot -> {
            user.setUserId(documentSnapshot.get("userId").toString());
            Log.d(TAG, "profile: " + documentSnapshot.get("userProfile"));
            user.setUserName(documentSnapshot.get("userName").toString());
            user.setUserProfile(documentSnapshot.get("userProfile").toString());
            user.setUserDisplayName(documentSnapshot.get("userDisplayName").toString());
        });
        return user;
    }
    public static class UserBuilder {
        private String userName;
        private String userId;
        private String userProfile;
        private String userEmail;
        private String UserDisplayName;
        private long lastSeenAt;
        private boolean isActive = true;
    }
}
