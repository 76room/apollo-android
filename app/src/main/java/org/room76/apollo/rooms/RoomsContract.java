package org.room76.apollo.rooms;

import android.support.annotation.NonNull;

import org.room76.apollo.model.Room;

import java.util.List;


/**
 * This specifies the contract between the view and the presenter.
 */
public interface RoomsContract {

    interface View {

        void setProgressIndicator(boolean active);

        void showRooms(List<Room> rooms);

        void showAddRoom();

        void showRoomDetailUi(String roomId);
    }

    interface UserActionsListener {

        void loadRooms(boolean forceUpdate);

        void addNewRoom();

        void openRoomDetails(@NonNull Room requestedRoom);
    }
}
