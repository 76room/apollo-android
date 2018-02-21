package org.room76.apollo.addroom;

import android.support.annotation.NonNull;

import org.room76.apollo.model.Repository;
import org.room76.apollo.model.Room;
import org.room76.apollo.model.User;
import org.room76.apollo.signin.SignInState;

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
    private final Repository<Room> mRoomsRepository;
    @NonNull
    private final AddRoomContract.View mAddRoomView;

    private File mImageFile;

    public AddRoomPresenter(@NonNull Repository<Room> roomsRepository,
                            @NonNull AddRoomContract.View addRoomView) {
        mRoomsRepository = roomsRepository;
        mAddRoomView = addRoomView;
    }

    @Override
    public void saveRoom(String title, String description, String imageUrl) {
        User author = new User(SignInState.getInstance().getUser());
        boolean isOpen = true;

        Room newRoom = new Room(author, title, description, isOpen, imageUrl);
        if (newRoom.isEmpty()) {
            mAddRoomView.showEmptyRoomError();
        } else {
            mRoomsRepository.saveItem(newRoom);
            mAddRoomView.showRoomsList();
        }
    }

    @Override
    public void takePicture() throws IOException {
        mAddRoomView.openCamera();
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
