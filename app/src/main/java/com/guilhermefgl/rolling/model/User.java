package com.guilhermefgl.rolling.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;

@SuppressWarnings({"WeakerAccess", "unused"})
public class User implements Parcelable {

    private String userId;
    private String userName;
    private String userEmail;
    private String userAvatarUrl;

    public User() { }

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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    protected User(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        userEmail = in.readString();
        userAvatarUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(userEmail);
        dest.writeString(userAvatarUrl);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public static User createFromFirebaseUser(final FirebaseUser currentUser) {
        if (currentUser != null) {
            return new User() {{
                setUserId(currentUser.getUid());
                setUserName(currentUser.getDisplayName());
                setUserEmail(currentUser.getEmail());
                if (currentUser.getPhotoUrl() != null) {
                    setUserAvatarUrl(currentUser.getPhotoUrl().toString());
                }
            }};
        }
        return null;
    }
}
