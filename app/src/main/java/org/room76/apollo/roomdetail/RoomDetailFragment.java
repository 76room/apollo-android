package org.room76.apollo.roomdetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.SortedList;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.firebase.auth.FirebaseUser;

import org.room76.apollo.R;
import org.room76.apollo.model.Track;
import org.room76.apollo.signin.SignInState;
import org.room76.apollo.util.CircleTransform;
import org.room76.apollo.util.EspressoIdlingResource;
import org.room76.apollo.util.Injection;

import java.util.List;

import static org.room76.apollo.util.Utils.convertTime;

/**
 * Main UI for the room detail screen.
 */
public class RoomDetailFragment extends Fragment implements RoomDetailContract.View, SwipeItemTouchHelper.RecyclerItemTouchHelperListener{

    public static final String ARGUMENT_ROOM_ID = "ROOM_ID";

    private RoomDetailContract.UserActionsListener mActionsListener;

    private Toolbar mDetailTitle;

    private TextView mDetailDescription;

    private ImageView mHeaderImage;

    private RecyclerView mUserRecyclerView;

    private RecyclerView mTracksRecyclerView;

    private RecyclerView mTracksRecomRecyclerView;

    private UsersAdapter mUserAdapter;

    private TrackAdapter mTrackAdapter;

    private View mProgressBar;

    private ImageView mIsOpen;

    private SortedList<Track> mTracks;

//    private ImageButton mAuthorImage;


    public static RoomDetailFragment newInstance(String roomId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_ROOM_ID, roomId);
        RoomDetailFragment fragment = new RoomDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        mDetailDescription = root.findViewById(R.id.room_detail_description);
//        mAuthorImage = root.findViewById(R.id.room_author_image);
        mUserRecyclerView = root.findViewById(R.id.recycler_view_users);
        mTracksRecyclerView = root.findViewById(R.id.recycler_view_music);
        mTracksRecomRecyclerView = root.findViewById(R.id.recycler_view_music_recom);
        if (getActivity() != null) {
            mHeaderImage = getActivity().findViewById(R.id.header_image);
            mDetailTitle = getActivity().findViewById(R.id.toolbar);
            mProgressBar = getActivity().findViewById(R.id.animation_view);
            mIsOpen = getActivity().findViewById(R.id.room_is_open);
        }
        mActionsListener = new RoomDetailPresenter(Injection.provideRoomsRepository(), this);
        DividerItemDecoration decor = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        decor.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dr_divider));
        mTracksRecyclerView.addItemDecoration(decor);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new SwipeItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mTracksRecyclerView);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        String roomId = getArguments().getString(ARGUMENT_ROOM_ID);
        mActionsListener.openRoom(roomId);
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if (active) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideDescription() {
        mDetailDescription.setVisibility(View.GONE);
    }

    @Override
    public void hideTitle() {
        mDetailTitle.setVisibility(View.GONE);
    }

    @Override
    public void showDescription(String description) {
        mDetailDescription.setVisibility(View.VISIBLE);
        mDetailDescription.setText(description);
    }

    @Override
    public void showAuthor(FirebaseUser author) {
//        mAuthorImage.setVisibility(View.VISIBLE);
//
//        if (author.getPhotoUrl() != null) {
//            // This app uses Glide for image loading
//            Glide.with(this)
//                    .load(author.getPhotoUrl())
//                    .centerCrop()
//                    .into(mAuthorImage);
//        }
    }

    @Override
    public void hideAuthor() {
//        mAuthorImage.setVisibility(View.GONE);
    }

    @Override
    public void showIsOpen(boolean isOpen) {
        mIsOpen.setVisibility(View.VISIBLE);
        if (isOpen) {
            mIsOpen.setBackgroundResource(R.drawable.ic_door);
        } else {
            mIsOpen.setBackgroundResource(R.drawable.ic_lock_outline);
        }
    }

    @Override
    public void hideIsOpen() {
        mIsOpen.setVisibility(View.GONE);
    }

    public void populateUserView(List<FirebaseUser> users) {
        mUserAdapter = new UsersAdapter(users);
        mUserRecyclerView.setAdapter(mUserAdapter);
    }

    @Override
    public void populateTrackList(List<Track> tracks) {
        SortedList<Track> mRecomm = new SortedList<>(Track.class, new SortedList.Callback<Track>() {
            @Override
            public int compare(Track o1, Track o2) {
                return 0;
            }

            @Override
            public void onChanged(int position, int count) {

            }

            @Override
            public boolean areContentsTheSame(Track oldItem, Track newItem) {
                return false;
            }

            @Override
            public boolean areItemsTheSame(Track item1, Track item2) {
                return false;
            }

            @Override
            public void onInserted(int position, int count) {

            }

            @Override
            public void onRemoved(int position, int count) {

            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {

            }
        });
        mRecomm.add(new Track("Thunder", "Imagine Dragons", 200000, "http://test"));
        mRecomm.add(new Track("Believer", "Imagine Dragons", 400000, "http://test"));
        mRecomm.add(new Track("Розовое вино", "Eлджей", 50000, "http://test"));
        RecommendationsAdapter adapter = new RecommendationsAdapter(mRecomm);
        mTracksRecomRecyclerView.setAdapter(adapter);

        mTrackAdapter = new TrackAdapter(mTracks);
        mTracks = new SortedList<>(Track.class, new SortedList.Callback<Track>() {
            @Override
            public int compare(Track o1, Track o2) {
                return o1.compareTo(o2);
            }

            @Override
            public void onChanged(int position, int count) {
                mTrackAdapter.notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(Track oldItem, Track newItem) {
                return oldItem.getLikes()-oldItem.getDislikes() == newItem.getLikes()-newItem.getDislikes();
            }

            @Override
            public boolean areItemsTheSame(Track item1, Track item2) {
                return item1.equals(item2);
            }

            @Override
            public void onInserted(int position, int count) {
                mTrackAdapter.notifyItemInserted(position);
            }

            @Override
            public void onRemoved(int position, int count) {
                mTrackAdapter.notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                mTrackAdapter.notifyItemMoved(fromPosition, toPosition);
            }
        });
        mTracks.addAll(tracks);
        mTracksRecyclerView.setAdapter(mTrackAdapter);

    }

    @Override
    public void showTitle(String title) {
        mDetailTitle.setVisibility(View.VISIBLE);
        mDetailTitle.setTitle(title);
    }

    @Override
    public void showImage(String imageUrl) {
        // The image is loaded in a different thread so in order to UI-test this, an idling resource
        // is used to specify when the app is idle.
        EspressoIdlingResource.increment(); // App is busy until further notice.

        mHeaderImage.setVisibility(View.VISIBLE);

        // This app uses Glide for image loading
        int density = (int) getResources().getDisplayMetrics().density;
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(new GlideDrawableImageViewTarget(mHeaderImage) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        EspressoIdlingResource.decrement(); // App is idle.
                    }
                });
    }

    @Override
    public void hideImage() {
        mHeaderImage.setImageDrawable(null);
        mHeaderImage.setVisibility(View.GONE);
    }

    @Override
    public void showMissingRoom() {
        mDetailTitle.setTitle("Empty room");
       // mDetailDescription.setText(getString(R.string.no_data));
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (mTracks.get(position).isVoted(SignInState.getInstance().getUser())) {
            Toast.makeText(getContext(),"You already voted", Toast.LENGTH_SHORT).show();
        }
        if (direction == ItemTouchHelper.LEFT) {
            mTracks.get(position).dislike();
        } else if (direction == ItemTouchHelper.RIGHT) {
            mTracks.get(position).like();
        }
        mTrackAdapter.notifyItemChanged(viewHolder.getAdapterPosition());

    }

    private static class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

        private List<FirebaseUser> mUsers;

        public UsersAdapter(List<FirebaseUser> users) {
            setList(users);
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View roomView = inflater.inflate(R.layout.component_user_item, parent, false);

            return new UserViewHolder(roomView);
        }

        @Override
        public void onBindViewHolder(UserViewHolder viewHolder, int position) {
            FirebaseUser user = mUsers.get(position);
            viewHolder.mTextView.setText(user.getDisplayName() == null || user.getDisplayName().isEmpty()
                    ? user.getEmail() : user.getDisplayName());

            EspressoIdlingResource.increment();

            Glide.with(viewHolder.itemView.getContext())
                    .load(user.getPhotoUrl())
                    .error(R.drawable.ic_default_user_image)
                    .transform(new CircleTransform(viewHolder.itemView.getContext()))
                    .into(new GlideDrawableImageViewTarget(viewHolder.mImageView) {
                        @Override
                        public void onResourceReady(GlideDrawable resource,
                                                    GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            EspressoIdlingResource.decrement(); // App is idle.
                        }
                    });

        }

        private void setList(List<FirebaseUser> users) {
            mUsers = users;
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }

        public class UserViewHolder extends RecyclerView.ViewHolder{

            ImageView mImageView;
            TextView mTextView;

            public UserViewHolder(View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.user_image);
                mTextView = itemView.findViewById(R.id.user_email);
            }
        }
    }

    public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

        public TrackAdapter(SortedList<Track> users) {
            setList(users);
        }

        @Override
        public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View roomView = inflater.inflate(R.layout.component_track_item, parent, false);
            return new TrackViewHolder(roomView);
        }

        @Override
        public void onBindViewHolder(TrackViewHolder viewHolder, int position) {
            Track track = mTracks.get(position);
            Glide.with(viewHolder.itemView.getContext())
                    .load(track.getPhotoUri())
                    .error(R.drawable.dr_gradient_3_colors)
                    .into(viewHolder.mImageView);
            viewHolder.mDisLikeTextView.setText(String.valueOf(track.getDislikes()));
            viewHolder.mLikeTextView.setText(String.valueOf(track.getLikes()));
            viewHolder.mPrimaryTextView.setText(track.getTitle());
            viewHolder.mSecondaryTextView.setText(track.getArtist());
            viewHolder.mTimeTextView.setText(convertTime(track.getDuration()));
        }

        private void setList(SortedList<Track> tracks) {
            mTracks = tracks;
        }

        @Override
        public int getItemCount() {
            return mTracks.size();
        }

        public class TrackViewHolder extends RecyclerView.ViewHolder {
            ImageView mImageView;
            TextView mTimeTextView, mPrimaryTextView, mSecondaryTextView, mLikeTextView, mDisLikeTextView;
            LinearLayout mBackground, mPlusImageView, mLikeContainer;
            ConstraintLayout mForeground;

            public TrackViewHolder(View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.track_image);
                mTimeTextView = itemView.findViewById(R.id.track_timeview);
                mPrimaryTextView = itemView.findViewById(R.id.track_title);
                mSecondaryTextView = itemView.findViewById(R.id.track_description);
                mLikeTextView = itemView.findViewById(R.id.like);
                mDisLikeTextView = itemView.findViewById(R.id.dislike);
                mBackground = itemView.findViewById(R.id.view_background);
                mForeground = itemView.findViewById(R.id.view_foreground);
                mPlusImageView = itemView.findViewById(R.id.recommendatios_container);
                mLikeContainer = itemView.findViewById(R.id.like_container);
            }

        }
    }

    public class RecommendationsAdapter extends TrackAdapter {
        public RecommendationsAdapter(SortedList<Track> tracks) {
            super(tracks);
        }

        @Override
        public void onBindViewHolder(TrackViewHolder viewHolder, int position) {
            super.onBindViewHolder(viewHolder,position);
            viewHolder.mLikeContainer.setVisibility(View.GONE);
            viewHolder.mPlusImageView.setVisibility(View.VISIBLE);
        }
    }
}
