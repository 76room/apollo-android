package org.room76.apollo.model;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.google.firebase.auth.FirebaseUser;

import org.room76.apollo.signin.SignInState;

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

    private ArrayMap<FirebaseUser, Byte> mVoted;

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
}
