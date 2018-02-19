package org.room76.apollo.mymusic;

import android.support.annotation.NonNull;

import org.room76.apollo.model.Room;
import org.room76.apollo.model.Track;

import java.util.List;


/**
 * This specifies the contract between the view and the presenter.
 */
public interface MyMusicContract {

    interface View {

        void showMusic(List<Track> tracks);

        void playTrack();
        void playFrom(int time);

        void play();
        void previous();
        void next();

        void showProgressIndicator(boolean active);
    }

    interface UserActionsListener {

        void loadMusic();
    }
}
