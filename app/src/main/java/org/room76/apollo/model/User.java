package org.room76.apollo.model;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.internal.zzdwf;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexey on 2/3/18.
 */

public final class User {

    private String mName;
    private Uri mPhotoUrl;
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
            this.mPhotoUrl = user.getPhotoUrl();
            mFirebaseUserId = user.getUid();
        }
    }

    public User(String name, String photoUrl) {
        this.mName = name;
        this.mPhotoUrl = Uri.parse(photoUrl);
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public Uri getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(Uri mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }

    public String getFirebaseUserId() {
        return mFirebaseUserId;
    }

    public void setFirebaseUserId(String mFirebaseUserId) {
        this.mFirebaseUserId = mFirebaseUserId;
    }
}
