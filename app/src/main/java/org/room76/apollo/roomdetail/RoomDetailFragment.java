package org.room76.apollo.roomdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.room76.apollo.R;
import org.room76.apollo.util.EspressoIdlingResource;
import org.room76.apollo.util.Injection;

/**
 * Main UI for the room detail screen.
 */
public class RoomDetailFragment extends Fragment implements RoomDetailContract.View {

    public static final String ARGUMENT_ROOM_ID = "ROOM_ID";

    private RoomDetailContract.UserActionsListener mActionsListener;

    private TextView mDetailTitle;

    private TextView mDetailDescription;

    private ImageView mDetailImage;

    public static RoomDetailFragment newInstance(String roomId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_ROOM_ID, roomId);
        RoomDetailFragment fragment = new RoomDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActionsListener = new RoomDetailPresenter(Injection.provideRoomsRepository(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        mDetailTitle = root.findViewById(R.id.room_detail_title);
        mDetailDescription = root.findViewById(R.id.room_detail_description);
        mDetailImage = root.findViewById(R.id.room_detail_image);
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
            mDetailTitle.setText("");
            mDetailDescription.setText(getString(R.string.loading));
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
    public void showTitle(String title) {
        mDetailTitle.setVisibility(View.VISIBLE);
        mDetailTitle.setText(title);
    }

    @Override
    public void showImage(String imageUrl) {
        // The image is loaded in a different thread so in order to UI-test this, an idling resource
        // is used to specify when the app is idle.
        EspressoIdlingResource.increment(); // App is busy until further notice.

        mDetailImage.setVisibility(View.VISIBLE);

        // This app uses Glide for image loading
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(new GlideDrawableImageViewTarget(mDetailImage) {
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
        mDetailImage.setImageDrawable(null);
        mDetailImage.setVisibility(View.GONE);
    }

    @Override
    public void showMissingRoom() {
        mDetailTitle.setText("");
        mDetailDescription.setText(getString(R.string.no_data));
    }
}
