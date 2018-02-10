package org.room76.apollo.addroom;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import java.io.IOException;


/**
 * Main UI for the add room screen. Users can enter a room title and description. Images can be
 * added to rooms by clicking on the options menu.
 */
public class AddRoomFragment extends Fragment implements AddRoomContract.View {

    public static final int REQUEST_CODE_IMAGE_CAPTURE = 0x1001;

    private AddRoomContract.UserActionsListener mActionListener;

    private EditText mTitle, mDescription;

    private ImageView mImageThumbnail, mOpenCamera;

    public static AddRoomFragment newInstance() {
        return new AddRoomFragment();
    }

    public AddRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActionListener = new AddRoomPresenter(Injection.provideRoomsRepository(), this);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_rooms);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionListener.saveRoom(mTitle.getText().toString(),
                        mDescription.getText().toString());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addroom, container, false);
        mTitle = root.findViewById(R.id.add_room_title);
        mDescription = root.findViewById(R.id.add_room_description);
        mImageThumbnail = root.findViewById(R.id.add_room_image_thumbnail);
        mOpenCamera = root.findViewById(R.id.open_camera);
        mOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mActionListener.takePicture();
                } catch (IOException e) {
                    showImageError();
                }
            }
        });

        setRetainInstance(true);
//        mActionListener.takePicture();
        return root;
    }

    @Override
    public void showEmptyRoomError() {
        Snackbar.make(mTitle, getString(R.string.empty_room_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showRoomsList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void openCamera(String saveTo) {
        // Open the camera to take a picture.
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Check if there is a camera app installed to handle our Intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(saveTo));
            startActivityForResult(takePictureIntent, REQUEST_CODE_IMAGE_CAPTURE);
        } else {
            Snackbar.make(mTitle, getString(R.string.cannot_connect_to_camera_message),
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showImagePreview(@NonNull String imageUrl) {
        mOpenCamera.setVisibility(View.GONE);

        // The image is loaded in a different thread so in order to UI-test this, an idling resource
        // is used to specify when the app is idle.
        EspressoIdlingResource.increment(); // App is busy until further notice.

        // This app uses Glide for image loading
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(new GlideDrawableImageViewTarget(mImageThumbnail) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        EspressoIdlingResource.decrement(); // Set app as idle.
                    }
                });
    }

    @Override
    public void showImageError() {
        Snackbar.make(mTitle, getString(R.string.cannot_connect_to_camera_message),
                Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If an image is received, display it on the ImageView.
        if (REQUEST_CODE_IMAGE_CAPTURE == requestCode && Activity.RESULT_OK == resultCode) {
            mActionListener.imageAvailable();
        } else {
            mActionListener.imageCaptureFailed();
        }
    }
}
