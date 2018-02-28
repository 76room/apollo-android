package org.room76.apollo.model;

import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.zatsepin on 08/02/2018.
 */

public final class LocalTrackRepository implements Repository<Track> {

    private List<Track> mTracks = new ArrayList<>();
    private Thread mRefreshThread;

    @Override public void updateItem(Track item) {}

    @Override
    public void getItems(@NonNull LoadCallback<Track> callback) {
        if (mRefreshThread.isAlive()){
            try {
                mRefreshThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        callback.onLoaded(mTracks);
    }

    @Override
    public void getItem(@NonNull String title, @NonNull GetCallback<Track> callback) {
        for (Track item : mTracks) {
            if (item.getTitle().equals(title)) {
                callback.onLoaded(item);
            }
        }
    }

    @Override public void saveItem(@NonNull Track item) {}

    @Override
    public void refreshData() {
        mRefreshThread = new Thread(new Runnable() {
            @Override
            public void run() {
                load();
            }
        });
        mRefreshThread.start();
    }

    @Override
    public boolean contains(Track item) {
        return false;
    }

    private void load(){
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

        }
        mTracks.addAll(tracks);
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
        } catch (Exception e) {
            Log.e("getAudioFiles" ,e.getMessage());
        } finally {
            return fileList;
        }
    }
}
