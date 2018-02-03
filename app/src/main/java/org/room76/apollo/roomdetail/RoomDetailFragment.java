package org.room76.apollo.roomdetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.firebase.auth.FirebaseUser;

import org.room76.apollo.R;
import org.room76.apollo.util.EspressoIdlingResource;
import org.room76.apollo.util.Injection;

import java.util.List;

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

    private UsersAdapter mUserAdapter;

    private TrackAdapter mTrackAdapter;

    private View mProgressBar;

    private ImageView mIsOpen;

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
        mIsOpen = root.findViewById(R.id.room_is_open);
//        mAuthorImage = root.findViewById(R.id.room_author_image);
        mUserRecyclerView = root.findViewById(R.id.recycler_view_users);
        mTracksRecyclerView = root.findViewById(R.id.recycler_view_music);
        if (getActivity() != null) {
            mHeaderImage = getActivity().findViewById(R.id.header_image);
            mDetailTitle = getActivity().findViewById(R.id.toolbar);
            mProgressBar = getActivity().findViewById(R.id.animation_view);
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
    public void populateTrackList(List<String> tracks) {
        mTrackAdapter = new TrackAdapter(tracks);
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
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
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
        if (position == ItemTouchHelper.LEFT) {

        } else if (position == ItemTouchHelper.RIGHT) {

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
                    .override(36,36)
                    .centerCrop()
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

    public static class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

        private List<String> mTracks;

        public TrackAdapter(List<String> users) {
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
            String user = mTracks.get(position);
            Glide.with(viewHolder.itemView.getContext())
                    .load("https://az616578.vo.msecnd.net/files/2016/07/16/6360427652852023551050101223_friend.jpg")
                    .error(R.drawable.dr_gradient_3_colors)
                    .into(viewHolder.mImageView);
            viewHolder.mDisLikeTextView.setText("21");
            viewHolder.mLikeTextView.setText("101");
            viewHolder.mPrimaryTextView.setText(user);
            viewHolder.mSecondaryTextView.setText("Rape me");
            viewHolder.mTimeTextView.setText("3:33");
        }

        private void setList(List<String> users) {
            mTracks = users;
        }

        @Override
        public int getItemCount() {
            return mTracks.size();
        }

        public class TrackViewHolder extends RecyclerView.ViewHolder {
            ImageView mImageView;
            TextView mTimeTextView, mPrimaryTextView, mSecondaryTextView, mLikeTextView, mDisLikeTextView;
            LinearLayout mBackground;
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
            }

        }
    }
}
