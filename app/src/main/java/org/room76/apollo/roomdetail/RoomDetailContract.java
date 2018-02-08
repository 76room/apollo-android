package org.room76.apollo.roomdetail;

import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;

import org.room76.apollo.model.Track;
import org.room76.apollo.model.User;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface RoomDetailContract {

    // TODO ask why it needs to be so comlex; it is better just to leave showRoomInfo() function
    interface View {

        void setProgressIndicator(boolean active);

        void showMissingRoom();

        void hideTitle();

        void showTitle(String title);

        void showImage(String imageUrl);

        void hideImage();

        void hideDescription();

        void showDescription(String description);

        void showAuthor(User author);

        void hideAuthor();

        void showIsOpen(boolean isOpen);

        void hideIsOpen();

        void populateUserView(List<User> users);

        void populateTrackList(List<Track> tracks);
    }

    interface UserActionsListener {

        void openRoom(@Nullable String roomId);
    }
}
