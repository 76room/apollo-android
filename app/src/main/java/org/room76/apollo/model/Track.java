package org.room76.apollo.model;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.google.firebase.auth.FirebaseUser;

import org.room76.apollo.signin.SignInState;

import java.util.Map;
import java.util.Random;

/**
 * Immutable model class for a Track.
 */
public final class Track implements Comparable<Track>{
    private String mArtist;
    private String mTitle;
    private String mPath;
    private int mDuration;

    private String mPhotoUri;

    private int mLikes;
    private int mDislikes;

    private ArrayMap<FirebaseUser, Byte> mVoted = new ArrayMap<>();

    public Track() {
    }

    public Track(String title, String artist, int duration, String path) {
        mArtist = artist;
        mTitle = title;
        mDuration = duration;
        mPath = path;
        mPhotoUri = "https://az616578.vo.msecnd.net/files/2016/07/16/6360427652852023551050101223_friend.jpg";
        mLikes = Math.abs(new Random().nextInt()%100);
        mDislikes =  Math.abs(new Random().nextInt()%100);
        mVoted = new ArrayMap<>();
    }


    public String getTitle() {
        return mTitle;
    }

    public String getPath() {
        return mPath;
    }

    public String getArtist() {
        return mArtist;
    }

    public int getDuration() {
        return mDuration;
    }

    public int getLikes() {
        return mLikes;
    }

    public void like() {
        if (!isVoted(SignInState.getInstance().getUser())) {
            mLikes++;
            mVoted.put(SignInState.getInstance().getUser(), (byte) 1);
        }
    }

    public int getDislikes() {
        return mDislikes;
    }

    public void dislike() {
        if (!isVoted(SignInState.getInstance().getUser())) {
            mDislikes++;
            mVoted.put(SignInState.getInstance().getUser(), (byte) -1);
        }
    }

    public String getPhotoUri() {
        return mPhotoUri;
    }

    public void setPhotoUri(String mPhotoUri) {
        this.mPhotoUri = mPhotoUri;
    }

    public boolean isVoted(FirebaseUser user) {
        return mVoted.containsKey(user) && (mVoted.get(user) == 1 || mVoted.get(user) == -1);
    }

    public boolean isLiked(FirebaseUser user){
        return mVoted.containsKey(user) && mVoted.get(user) == 1;
    }

    public boolean isDisliked(FirebaseUser user){
        return mVoted.containsKey(user) && mVoted.get(user) == -1;
    }

    @Override
    public int compareTo(@NonNull Track track) {
        if (mLikes-mDislikes < track.mLikes-track.mDislikes) {
            return 1;
        } else {
            return -1;
        }
    }

    public void setArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setPath(String mPath) {
        this.mPath = mPath;
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public void setLikes(int mLikes) {
        this.mLikes = mLikes;
    }

    public void setDislikes(int mDislikes) {
        this.mDislikes = mDislikes;
    }

    public void setVoted(ArrayMap<FirebaseUser, Byte> mVoted) {
        this.mVoted = mVoted;
    }

    public static Track trackFromMap(Map<String, Object> trackMap) {
        Track t = new Track();
        if (trackMap.containsKey("artist")) {
            t.setArtist((String) trackMap.get("artist"));
        }
        if (trackMap.containsKey("title")) {
            t.setTitle((String) trackMap.get("title"));
        }
        if (trackMap.containsKey("path")) {
            t.setPath((String) trackMap.get("path"));
        }
        if (trackMap.containsKey("duration")) {
            Long l = (long) trackMap.get("duration");
            t.setDuration(l.intValue());
        }
        if (trackMap.containsKey("photoUri")) {
            t.setPhotoUri((String) trackMap.get("photoUri"));
        }
        if (trackMap.containsKey("likes")) {
            Long l = (long) trackMap.get("likes");
            t.setLikes(l.intValue());
        }
        if (trackMap.containsKey("photoUri")) {
            Long l = (long) trackMap.get("dislikes");
            t.setDislikes(l.intValue());
        }
        return t;
    }
}
