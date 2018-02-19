package org.room76.apollo.model;

import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// TODO check if it needs to be immutable

/**
 * Immutable model class for a Room.
 */
public final class Room {

    private String mId;

    @Nullable
    private User mAuthor;
    @Nullable
    private String mTitle;
    @Nullable
    private String mDescription;
    @Nullable
    private String mImageUrl;

    private boolean mIsOpen;

    private List<User> mUsers = new ArrayList<>();

    private List<Track> mTracks = new ArrayList<>();

    public Room(){}

    public Room(@Nullable User author, @Nullable String title, @Nullable String description, boolean isOpen) {
        this(author, title, description, isOpen, null);
    }

    public Room(@Nullable User author, @Nullable String title, @Nullable String description, boolean isOpen, @Nullable String imageUrl) {
        mId = UUID.randomUUID().toString();
        mAuthor = author;
        mTitle = title;
        mDescription = description;
        mImageUrl = imageUrl;
        mIsOpen = isOpen;
    }

    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    @Nullable
    public String getImageUrl() {
        return mImageUrl;
    }

    @Nullable
    public User getAuthor() {
        return mAuthor;
    }

    public boolean isOpen() {
        return mIsOpen;
    }

    public boolean isEmpty() {
        return (mTitle == null || "".equals(mTitle)) &&
                (mDescription == null || "".equals(mDescription));
    }

    public List<User> getUsers() {
        return mUsers;
    }

    public void setUsers(List<User> mUsers) {
        this.mUsers = mUsers;
    }

    public List<Track> getTracks() {
        return mTracks;
    }

    public void setTracks(List<Track> mTracks) {
        this.mTracks = mTracks;
    }
}
