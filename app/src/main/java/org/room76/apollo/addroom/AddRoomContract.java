package org.room76.apollo.addroom;

import android.support.annotation.NonNull;

import java.io.IOException;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface AddRoomContract {

    interface View {

        void showEmptyRoomError();

        void showRoomsList();

        void openCamera();

        void showImagePreview(@NonNull String uri);

        void showImageError();
    }

    interface UserActionsListener {

        void saveRoom(String title, String description, String imageUrl);

        void takePicture() throws IOException;

        void imageAvailable();

        void imageCaptureFailed();
    }
}
