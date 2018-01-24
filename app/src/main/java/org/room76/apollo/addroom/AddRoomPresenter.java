package org.room76.apollo.addroom;

import android.support.annotation.NonNull;

import org.room76.apollo.model.Room;
import org.room76.apollo.model.RoomsRepository;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Listens to user actions from the UI ({@link AddRoomFragment}), retrieves the data and updates
 * the UI as required.
 */
public class AddRoomPresenter implements AddRoomContract.UserActionsListener {

    @NonNull
    private final RoomsRepository mRoomsRepository;
    @NonNull
    private final AddRoomContract.View mAddRoomView;

    private File mImageFile = new File("/") ;

    public AddRoomPresenter(@NonNull RoomsRepository roomsRepository,
                            @NonNull AddRoomContract.View addRoomView) {
        mRoomsRepository = roomsRepository;
        mAddRoomView = addRoomView;
    }

    @Override
    public void saveRoom(String title, String description) {
        String imageUrl = null;
        if (mImageFile.exists()) {
            imageUrl = mImageFile.getPath();
        }
        Room newRoom = new Room(title, description, imageUrl);
        if (newRoom.isEmpty()) {
            mAddRoomView.showEmptyRoomError();
        } else {
            mRoomsRepository.saveRoom(newRoom);
            mAddRoomView.showRoomsList();
        }
    }

    @Override
    public void takePicture() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        mImageFile.create(imageFileName, ".jpg");
        mAddRoomView.openCamera(mImageFile.getPath());
    }

    @Override
    public void imageAvailable() {
        if (mImageFile.exists()) {
            mAddRoomView.showImagePreview(mImageFile.getPath());
        } else {
            imageCaptureFailed();
        }
    }

    @Override
    public void imageCaptureFailed() {
        mAddRoomView.showImageError();
    }
}
