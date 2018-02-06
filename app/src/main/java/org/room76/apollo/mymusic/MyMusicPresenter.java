package org.room76.apollo.mymusic;

import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.room76.apollo.model.Track;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Listens to user actions from the UI ({@link MyMusicFragment}), retrieves the data and updates the
 * UI as required.
 */
public class MyMusicPresenter implements MyMusicContract.UserActionsListener {

    private final MyMusicContract.View mMyMusicView;

    public MyMusicPresenter(@NonNull MyMusicContract.View myMusicViewView) {
        mMyMusicView = myMusicViewView;
    }

    @Override
    public void loadMusic() {
        mMyMusicView.showProgressIndicator(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String myMusicPath = Environment.getExternalStorageDirectory() + "/";
                List<File> audioFiles = getAudioFiles(myMusicPath);

                if (audioFiles.isEmpty()) {
                    return;
                }

                List<Track> tracks = new ArrayList<>();
                for (File file:audioFiles) {
                    MediaMetadataRetriever md = new MediaMetadataRetriever();
                    md.setDataSource(file.getPath());
                    String artist = md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    String title = md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    int duration = Integer.parseInt(md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

                    if (TextUtils.isEmpty(artist)) {
                        artist = "";
                    }
                    if (TextUtils.isEmpty(title)) {
                        title = file.getName();
                    }

                    Track track = new Track(title, artist, duration, file.getPath());
                    tracks.add(track);

                    mMyMusicView.showProgressIndicator(false);
                    mMyMusicView.showMusic(tracks);
                }

            }
        }).start();
    }

    private List<File> getAudioFiles(String rootPath) {
        List<File> fileList = new ArrayList<>();
        try {
            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    List<File> moreFiles = getAudioFiles(file.getAbsolutePath());
                    if (moreFiles != null) {
                        fileList.addAll(moreFiles);
                    } else {
                        break;
                    }
                } else if (file.getName().endsWith(".mp3")) { //TODO add other formats
                    fileList.add(file);
                }
            }
            return fileList;
        } catch (Exception e) {
            return null;
        }
    }
}
