package org.room76.apollo.model;

import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// TODO check if it needs to be immutable

/**
 * Immutable model class for a Room.
 */
public final class Room {
    @PropertyName("id")
    private String mId;
    @Nullable
    @PropertyName("author")
    private User mAuthor;
    @Nullable
    @PropertyName("title")
    private String mTitle;
    @Nullable
    @PropertyName("description")
    private String mDescription;
    @Nullable
    @PropertyName("imageUrl")
    private String mImageUrl;
    @PropertyName("open")
    private boolean mIsOpen;
    @PropertyName("users")
    private List<User> mUsers = new ArrayList<>();
    @PropertyName("tracks")
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
        mUsers.add(author);
        List<Track> tracks = new ArrayList<>();
        tracks.add(new Track("Demons", "Imagine", 200000, "http://test"));
        tracks.add(new Track("Rape me", "Nirvana", 400000, "http://test"));
        tracks.add(new Track("Выхода нет", "Сплин", 50000, "http://test"));
        mTracks = tracks;
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
