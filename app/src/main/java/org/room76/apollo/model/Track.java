package org.room76.apollo.model;

import android.support.annotation.Nullable;

import java.util.UUID;

/**
 * Immutable model class for a Track.
 */
public final class Track {

    private final String mArtist;
    private final String mTitle;
    private final String mPath;
    private final int mDuration;


    public Track(String title, String artist, int duration, String path) {
        mArtist = artist;
        mTitle = title;
        mDuration = duration;
        mPath = path;
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
}
