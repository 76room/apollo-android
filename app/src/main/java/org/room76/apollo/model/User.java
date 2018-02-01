package org.room76.apollo.model;

import org.jetbrains.annotations.Nullable;

/**
 * Basic Room user class
 */
public class User {
    private String mName;

    @Nullable
    private String mProfilePhotoUrl;

    public User(String name) {
        this(name, null);
    }

    public User(String name, @Nullable String profilePhotoUrl) {
        mName = name;
        mProfilePhotoUrl = profilePhotoUrl;
    }

    public String getName() {
        return mName;
    }

    @Nullable
    public String getProfilePhotoUrl() {
        return mProfilePhotoUrl;
    }
}
