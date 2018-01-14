package org.room76.apollo.model;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Main entry point for accessing rooms data.
 */
public interface RoomsRepository {

    interface LoadRoomsCallback {

        void onRoomsLoaded(List<Room> rooms);
    }

    interface GetRoomCallback {

        void onRoomLoaded(Room room);
    }

    void getRooms(@NonNull LoadRoomsCallback callback);

    void getRoom(@NonNull String roomId, @NonNull GetRoomCallback callback);

    void saveRoom(@NonNull Room room);

    void refreshData();

}
