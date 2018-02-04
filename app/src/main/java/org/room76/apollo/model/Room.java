package org.room76.apollo.model;

import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Immutable model class for a Room.
 */
public final class Room {

    private String mId;

    @Nullable
    private FirebaseUser mAuthor;
    @Nullable
    private String mTitle;
    @Nullable
    private String mDescription;
    @Nullable
    private String mImageUrl;

    private boolean mIsOpen;

    private List<FirebaseUser> mUsers = new ArrayList<>();

    private List<Track> mTracks = new ArrayList<>();

    public Room() {
    }



    public Room(@Nullable FirebaseUser author, @Nullable String title, @Nullable String description, boolean isOpen) {
        this(author, title, description, isOpen, null);
    }

    public Room(@Nullable FirebaseUser author, @Nullable String title, @Nullable String description, boolean isOpen, @Nullable String imageUrl) {
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
    public FirebaseUser getAuthor() {
        return mAuthor;
    }

    public boolean isOpen() {
        return mIsOpen;
    }

    public boolean isEmpty() {
        return (mTitle == null || "".equals(mTitle)) &&
                (mDescription == null || "".equals(mDescription));
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public void setAuthor(@Nullable FirebaseUser mAuthor) {
        this.mAuthor = mAuthor;
    }

    public void setTitle(@Nullable String mTitle) {
        this.mTitle = mTitle;
    }

    public void setDescription(@Nullable String mDescription) {
        this.mDescription = mDescription;
    }

    public void setImageUrl(@Nullable String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public void setIsOpen(boolean mIsOpen) {
        this.mIsOpen = mIsOpen;
    }

    public List<FirebaseUser> getUsers() {
        return mUsers;
    }

    public void setUsers(List<FirebaseUser> mUsers) {
        this.mUsers = mUsers;
    }

    public List<Track> getTracks() {
        return mTracks;
    }

    public void setTracks(List<Track> mTracks) {
        this.mTracks = mTracks;
    }

    public static Room roomFromMap(Map<String, Object> roomMap) {
        Map<String, Object> authorMap = (Map<String, Object>) roomMap.get("author");
        FirebaseUserMock author = FirebaseUserMock.userFromMap(authorMap);
        List<FirebaseUser> users = new ArrayList<>();
        List<Track> tracks = new ArrayList<>();
        if (roomMap.containsKey("users")) {
            List<HashMap<String, Object>> usersMap = (ArrayList<HashMap<String, Object>>) roomMap.get("users");
            for (HashMap key : usersMap) {
                users.add(FirebaseUserMock
                        .userFromMap(key));
            }
        }
        if (roomMap.containsKey("tracks")) {
            ArrayList<HashMap<String, Object>> tracksMap = (ArrayList<HashMap<String, Object>>) roomMap.get("tracks");
            for (HashMap key : tracksMap) {
                tracks.add(Track
                        .trackFromMap(key));
            }
        }
        Room r = new Room();
        if (roomMap.containsKey("id")) {
            r.setId((String) roomMap.get("id"));
        }
        r.setAuthor(author);
        if (roomMap.containsKey("title")) {
            r.setTitle((String) roomMap.get("title"));
        }
        if (roomMap.containsKey("description")) {
            r.setDescription((String) roomMap.get("description"));
        }
        if (roomMap.containsKey("imageUrl")) {
            r.setImageUrl((String) roomMap.get("imageUrl"));
        }
        if (roomMap.containsKey("open")) {
            r.setIsOpen((Boolean) roomMap.get("open"));
        }
        r.setUsers(users);
        r.setTracks(tracks);
        return r;
    }
}
