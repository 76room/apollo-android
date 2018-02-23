package org.room76.apollo.addroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.room76.apollo.R;
import org.room76.apollo.util.EspressoIdlingResource;
import org.room76.apollo.util.Injection;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;


/**
 * Main UI for the add room screen. Users can enter a room title and description. Images can be
 * added to rooms by clicking on the options menu.
 */
public class AddRoomFragment extends Fragment implements AddRoomContract.View, View.OnClickListener {

    public static final int REQUEST_CODE_IMAGE_CAPTURE = 0x1001;
    public static final int REQUEST_CODE_IMAGE_SELECT = 0x1002;

    private AddRoomContract.UserActionsListener mActionListener;

    private EditText mTitle, mDescription;

    private ImageView mImageThumbnail, mOpenCamera;

    private Uri mImageUri;

    private AlertDialog mSelector;

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
                if (mImageUri != null) {
                    StorageReference ref = FirebaseStorage.getInstance().getReference().child("room-images").child(mImageUri.getLastPathSegment());
                    ref.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            mActionListener.saveRoom(mTitle.getText().toString(),
                                    mDescription.getText().toString(), downloadUrl.toString());
                        }
                    });
                } else {
                    mActionListener.saveRoom(mTitle.getText().toString(),
                            mDescription.getText().toString(), null);
                }
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
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void openCamera() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_select_image_input, null);
        dialogBuilder.setView(dialogView);

        TextView camera = dialogView.findViewById(R.id.take_photo);
        TextView gallery = dialogView.findViewById(R.id.from_gallery);

        camera.setOnClickListener(this);
        gallery.setOnClickListener(this);

        mSelector = dialogBuilder.create();
        mSelector.show();
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
        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            showImagePreview(mImageUri.toString());
        } else if (requestCode == REQUEST_CODE_IMAGE_SELECT && resultCode == RESULT_OK && null != data) {
            mImageUri = data.getData();
            showImagePreview(mImageUri.toString());
        } else {
            mActionListener.imageCaptureFailed();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.from_gallery) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), REQUEST_CODE_IMAGE_SELECT);
        } else if (view.getId() == R.id.take_photo) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_CODE_IMAGE_CAPTURE);
        }
        mSelector.dismiss();
    }


    public void onBackPressed(DialogInterface.OnClickListener listener) {
        if (mTitle.getText().length() > 0 || mDescription.getText().length() > 0 || mImageThumbnail.getDrawable() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialog));
            builder.setTitle("Changes");
            builder.setMessage("You have unsaved changes");
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Dismiss", listener);
            builder.setCancelable(true);
            builder.create().show();
        } else {
            listener.onClick(null, 0);
        }
    }

}
