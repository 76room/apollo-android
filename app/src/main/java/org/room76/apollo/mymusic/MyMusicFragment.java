package org.room76.apollo.mymusic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.room76.apollo.R;
import org.room76.apollo.model.Track;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.room76.apollo.util.Utils.convertTime;


public class MyMusicFragment extends Fragment implements MyMusicContract.View {

    private MyMusicContract.UserActionsListener mActionsListener;

    private TracksAdapter mListAdapter;
    private SeekBar mTimeLineSeekBar;

    private Handler mTimeLineUpdateHandler;
    private Runnable mUpdateTimeLine;

    private MediaPlayer mMediaPlayer;
    private int mCurrentTrackIndex = -1;

    private TextView mTrackDuration;
    private TextView mTrackCurrentPosition;

    private Button mPlayPauseButton;

    public MyMusicFragment() {
        // Requires empty public constructor
    }

    public static MyMusicFragment newInstance() {
        return new MyMusicFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new TracksAdapter(new ArrayList<Track>(0), mItemListener);
        mActionsListener = new MyMusicPresenter(this);
        createMediaPlayer();
        mTimeLineUpdateHandler = new Handler();
        mUpdateTimeLine = new Runnable() {
            @Override
            public void run() {
                updateTimeLine();
            }
        };
    }

    private void updateTimeLine() {
        int currentPosition = mMediaPlayer.getCurrentPosition();
        mTimeLineSeekBar.setProgress(currentPosition);
        mTrackCurrentPosition.setText(convertTime(currentPosition));
        mTimeLineUpdateHandler.postDelayed(mUpdateTimeLine, 50);
    }

    private void createMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.loadMusic();
        mMediaPlayer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMediaPlayer.pause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mymusic, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.my_music_list);
        recyclerView.setAdapter(mListAdapter);

//        int numColumns = getContext().getResources().getInteger(R.integer.num_rooms_columns);

        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mTrackDuration = root.findViewById(R.id.my_music_play_duration);
        mTrackCurrentPosition = root.findViewById(R.id.my_music_play_current);

        mPlayPauseButton = root.findViewById(R.id.my_music_play_btn);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentTrackIndex == -1) {
                    mCurrentTrackIndex = 0;
                    playTrack();
                } else {
                    switch (view.getId()) {
                        case R.id.my_music_play_btn:
                            play();
                            break;
                        case R.id.my_music_previous_btn:
                            previous();
                            break;
                        case R.id.my_music_next_btn:
                            next();
                            break;
                    }
                }
            }
        };

        Button playBtn = root.findViewById(R.id.my_music_play_btn);
        Button previousBtn = root.findViewById(R.id.my_music_previous_btn);
        Button nextBtn = root.findViewById(R.id.my_music_next_btn);

        playBtn.setOnClickListener(onClickListener);
        previousBtn.setOnClickListener(onClickListener);
        nextBtn.setOnClickListener(onClickListener);


        mTimeLineSeekBar = root.findViewById(R.id.my_music_timeline);
        mTimeLineSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playFrom(seekBar.getProgress());
            }
        });

        return root;
    }

    /**
     * Listener for clicks on rooms in the RecyclerView.
     */
    TrackItemListener mItemListener = new TrackItemListener() {
        @Override
        public void onTrackClick(int position) {
            mCurrentTrackIndex = position;
            playTrack();
        }
    };

    @Override
    public void showMusic(List<Track> tracks) {
        mListAdapter.replaceData(tracks);
    }

    @Override
    public void playTrack() {
        createMediaPlayer();

        Track track = mListAdapter.getItem(mCurrentTrackIndex);

        Uri myUri = Uri.fromFile(new File(track.getPath()));
        try {
            mMediaPlayer.setDataSource(this.getContext(), myUri);
            mMediaPlayer.prepare();
        } catch (Exception e) {
        }
        mMediaPlayer.start();
        int duration = track.getDuration();
        mTimeLineSeekBar.setMax(duration);
        mTimeLineUpdateHandler.post(mUpdateTimeLine);
        mTrackDuration.setText(convertTime(duration));
        mPlayPauseButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_my_music_pause), null, null);
    }

    @Override
    public void playFrom(int time) {
        mMediaPlayer.seekTo(time);
    }

    @Override
    public void play() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPlayPauseButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_music_play), null, null);
        } else {
            mPlayPauseButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_my_music_pause), null, null);
            mMediaPlayer.start();
        }
    }

    @Override
    public void previous() {
        if (mCurrentTrackIndex > 0) {
            --mCurrentTrackIndex;
        }
        playTrack();
    }

    @Override
    public void next() {
        if (mCurrentTrackIndex < mListAdapter.getItemCount() - 1) {
            ++mCurrentTrackIndex;
        }
        playTrack();
    }

    private static class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.ViewHolder> {

        private List<Track> mTracks;
        private TrackItemListener mItemListener;

        public TracksAdapter(List<Track> tracks, TrackItemListener itemListener) {
            setList(tracks);
            mItemListener = itemListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View roomView = inflater.inflate(R.layout.item_track, parent, false);

            return new ViewHolder(roomView, mItemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            Track track = mTracks.get(position);

            viewHolder.title.setText(track.getTitle());
            viewHolder.artist.setText(track.getArtist());
            int duration = track.getDuration();
            viewHolder.duration.setText(convertTime(duration));
        }

        public void replaceData(List<Track> tracks) {
            setList(tracks);
            notifyDataSetChanged();
        }

        private void setList(List<Track> tracks) {
            mTracks = tracks;
        }

        @Override
        public int getItemCount() {
            return mTracks.size();
        }

        public Track getItem(int position) {
            return mTracks.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView title;
            public TextView artist;
            public TextView duration;

            private TrackItemListener mItemListener;

            public ViewHolder(View itemView, TrackItemListener listener) {
                super(itemView);
                mItemListener = listener;
                title = itemView.findViewById(R.id.track_detail_title);
                artist = itemView.findViewById(R.id.track_detail_artist);
                duration = itemView.findViewById(R.id.track_detail_duration);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                mItemListener.onTrackClick(position);
            }
        }
    }

    public interface TrackItemListener {

        void onTrackClick(int position);
    }
}
