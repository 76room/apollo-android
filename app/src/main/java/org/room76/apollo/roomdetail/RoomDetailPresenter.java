package org.room76.apollo.roomdetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.room76.apollo.model.Room;
import org.room76.apollo.model.RoomsRepository;
import org.room76.apollo.model.User;

/**
 * Listens to user actions from the UI ({@link RoomDetailFragment}), retrieves the data and updates
 * the UI as required.
 */
public class RoomDetailPresenter implements RoomDetailContract.UserActionsListener {

    private final RoomsRepository mRoomsRepository;

    private final RoomDetailContract.View mRoomsDetailView;

    public RoomDetailPresenter(@NonNull RoomsRepository roomsRepository,
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
        mRoomsRepository.getRoom(roomId, new RoomsRepository.GetRoomCallback() {
            @Override
            public void onRoomLoaded(Room room) {
                mRoomsDetailView.setProgressIndicator(false);
                if (null == room) {
                    mRoomsDetailView.showMissingRoom();
                } else {
                    showRoom(room);
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

    }
}
