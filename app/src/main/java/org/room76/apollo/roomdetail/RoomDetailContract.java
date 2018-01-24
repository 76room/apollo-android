package org.room76.apollo.roomdetail;

import android.support.annotation.Nullable;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface RoomDetailContract {

    interface View {

        void setProgressIndicator(boolean active);

        void showMissingRoom();

        void hideTitle();

        void showTitle(String title);

        void showImage(String imageUrl);

        void hideImage();

        void hideDescription();

        void showDescription(String description);
    }

    interface UserActionsListener {

        void openRoom(@Nullable String roomId);
    }
}
