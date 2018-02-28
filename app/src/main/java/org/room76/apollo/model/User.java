package org.room76.apollo.model;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Alexey on 2/3/18.
 */

public final class User {

    private String mName;
    private String mPhotoUrl;
    private String mFirebaseUserId;

    public User(){}

    public User(String name) {
        this.mName = name;
        this.mPhotoUrl = null;
        mFirebaseUserId = null;
    }

    public User(FirebaseUser user) {
        if (user != null) {
            this.mName = user.getDisplayName() == null || user.getDisplayName().isEmpty()
                    ? user.getEmail() : user.getDisplayName();
            if (user.getPhotoUrl()!=null) this.mPhotoUrl = user.getPhotoUrl().toString();
            mFirebaseUserId = user.getUid();
        }
    }

    public User(String name, String photoUrl) {
        this.mName = name;
        this.mPhotoUrl = photoUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }

    public String getFirebaseUserId() {
        return mFirebaseUserId;
    }

    public void setFirebaseUserId(String mFirebaseUserId) {
        this.mFirebaseUserId = mFirebaseUserId;
    }
}
