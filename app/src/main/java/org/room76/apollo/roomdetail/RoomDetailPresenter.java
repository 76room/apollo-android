package org.room76.apollo.roomdetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;

import org.room76.apollo.model.Repository;
import org.room76.apollo.model.Room;
import org.room76.apollo.model.User;

/**
 * Listens to user actions from the UI ({@link RoomDetailFragment}), retrieves the data and updates
 * the UI as required.
 */
public class RoomDetailPresenter implements RoomDetailContract.UserActionsListener {

    private final Repository<Room> mRoomsRepository;

    private final RoomDetailContract.View mRoomsDetailView;

    public RoomDetailPresenter(@NonNull Repository<Room> roomsRepository,
                               @NonNull RoomDetailContract.View roomDetailView) {
        mRoomsRepository = roomsRepository;
        mRoomsDetailView = roomDetailView;
    }

    @Override
    public void openRoom(@Nullable String roomId) {
        if (null == roomId || roomId.isEmpty()) {
            mRoomsDetailView.showMissingRoom();
            return;
        }

        mRoomsDetailView.setProgressIndicator(true);
        mRoomsRepository.getItem(roomId, new Repository.GetCallback<Room>() {
            @Override
            public void onLoaded(Room item) {
                mRoomsDetailView.setProgressIndicator(false);
                if (null == item) {
                    mRoomsDetailView.showMissingRoom();
                } else {
                    showRoom(item);
                }
            }
        });
    }

    private void showRoom(Room room) {
        String title = room.getTitle();
        String description = room.getDescription();
        String imageUrl = room.getImageUrl();
        User author = room.getAuthor();
        boolean isOpen = room.isOpen();

        if (author != null) {
            mRoomsDetailView.showAuthor(author);
        } else {
            mRoomsDetailView.hideAuthor();
        }

        if (author != null) {
            mRoomsDetailView.showIsOpen(isOpen);
        } else {
            mRoomsDetailView.hideIsOpen();
        }

        if (title != null && title.isEmpty()) {
            mRoomsDetailView.hideTitle();
        } else {
            mRoomsDetailView.showTitle(title);
        }

        if (description != null && description.isEmpty()) {
            mRoomsDetailView.hideDescription();
        } else {
            mRoomsDetailView.showDescription(description);
        }

        if (imageUrl != null) {
            mRoomsDetailView.showImage(imageUrl);
        } else {
            mRoomsDetailView.hideImage();
        }
        mRoomsDetailView.populateTrackList(room.getTracks());
        mRoomsDetailView.populateUserView(room.getUsers());
    }
}
