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

public class FirebaseUserMock extends FirebaseUser {

    private String mName;
    private Uri mPhotoUrl;


    public FirebaseUserMock() {
    }

    public FirebaseUserMock(String name) {
        this.mName = name;
        this.mPhotoUrl = null;
    }

    public FirebaseUserMock(String name, String photoUrl) {
        this.mName = name;
        this.mPhotoUrl = Uri.parse(photoUrl);
    }

    @NonNull
    @Override
    public String getUid() {
        return "0";
    }

    @NonNull
    @Override
    public String getProviderId() {
        return "0";
    }

    @Override
    public boolean isAnonymous() {
        return false;
    }

    @Nullable
    @Override
    public List<String> getProviders() {
        return null;
    }

    @NonNull
    @Override
    public List<? extends UserInfo> getProviderData() {
        return new ArrayList<>();
    }

    @NonNull
    @Override
    public FirebaseUser zzap(@NonNull List<? extends UserInfo> list) {
        return this;
    }

    @Override
    public FirebaseUser zzcc(boolean b) {
        return null;
    }

    @NonNull
    @Override
    public FirebaseApp zzbpn() {
        return null;
    }

    @Nullable
    @Override
    public String getDisplayName() {
        return mName;
    }

    @Nullable
    @Override
    public Uri getPhotoUrl() {
        return mPhotoUrl;
    }

    @Nullable
    @Override
    public String getEmail() {
        return null;
    }

    @Nullable
    @Override
    public String getPhoneNumber() {
        return null;
    }

    @Override
    public boolean isEmailVerified() {
        return false;
    }

    @NonNull
    @Override
    public zzdwf zzbpo() {
        return null;
    }

    @Override
    public void zza(@NonNull zzdwf zzdwf) {

    }

    @NonNull
    @Override
    public String zzbpp() {
        return null;
    }

    @NonNull
    @Override
    public String zzbpq() {
        return null;
    }

    @Nullable
    @Override
    public FirebaseUserMetadata getMetadata() {
        return null;
    }
}
