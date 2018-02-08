package org.room76.apollo.mymusic;

import android.support.annotation.NonNull;

import org.room76.apollo.model.LocalTrackRepository;
import org.room76.apollo.model.Repository;
import org.room76.apollo.model.Track;
import org.room76.apollo.util.Injection;

import java.util.List;


/**
 * Listens to user actions from the UI ({@link MyMusicFragment}), retrieves the data and updates the
 * UI as required.
 */
public class MyMusicPresenter implements MyMusicContract.UserActionsListener {

    private final MyMusicContract.View mMyMusicView;
    private final LocalTrackRepository mRepository;

    public MyMusicPresenter(@NonNull MyMusicContract.View myMusicViewView) {
        mMyMusicView = myMusicViewView;
        mRepository = (LocalTrackRepository) Injection.provideTrackRepository();
    }

    @Override
    public void loadMusic() {
        mMyMusicView.showProgressIndicator(true);
        mRepository.getItems(new Repository.LoadCallback<Track>() {
            @Override
            public void onLoaded(List<Track> items) {
                mMyMusicView.showMusic(items);
                mMyMusicView.showProgressIndicator(false);
            }
        });
    }
}
