package org.room76.apollo.model;

/**
 * Immutable model class for a Track.
 */
public final class Track {

    private String mArtist;
    private String mTitle;
    private String mPath;
    private int mDuration;

    private String mPhotoUri;

    private int mLikes;
    private int mDislikes;

    public Track() {
    }

    public Track(String title, String artist, int duration, String path) {
        mArtist = artist;
        mTitle = title;
        mDuration = duration;
        mPath = path;
        mPhotoUri = "https://az616578.vo.msecnd.net/files/2016/07/16/6360427652852023551050101223_friend.jpg";
        mLikes = 0;
        mDislikes = 0;
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
        mLikes ++;
    }

    public int getDislikes() {
        return mDislikes;
    }

    public void dislike() {
        mDislikes++;
    }

    public String getPhotoUri() {
        return mPhotoUri;
    }

    public void setPhotoUri(String mPhotoUri) {
        this.mPhotoUri = mPhotoUri;
    }
}
