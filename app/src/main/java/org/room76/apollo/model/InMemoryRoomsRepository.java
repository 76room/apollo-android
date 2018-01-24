package org.room76.apollo.model;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation to load rooms from the a data source.
 */
public class InMemoryRoomsRepository implements RoomsRepository {

    private final RoomsServiceApi mRoomsServiceApi;

    /**
     * This method has reduced visibility for testing and is only visible to tests in the same
     * package.
     */
    @VisibleForTesting
    List<Room> mCachedRooms;

    public InMemoryRoomsRepository(@NonNull RoomsServiceApi roomsServiceApi) {
        mRoomsServiceApi = roomsServiceApi;
    }

    @Override
    public void getRooms(@NonNull final LoadRoomsCallback callback) {
        // Load from API only if needed.
        if (mCachedRooms == null) {
            mRoomsServiceApi.getAllRooms(new RoomsServiceApi.RoomsServiceCallback<List<Room>>() {
                @Override
                public void onLoaded(List<Room> rooms) {
                    mCachedRooms = new ArrayList<>(rooms);
                    callback.onRoomsLoaded(mCachedRooms);
                }
            });
        } else {
            callback.onRoomsLoaded(mCachedRooms);
        }
    }

    @Override
    public void saveRoom(@NonNull Room room) {
        mRoomsServiceApi.saveRoom(room);
        refreshData();
    }

    @Override
    public void getRoom(@NonNull final String roomId, @NonNull final GetRoomCallback callback) {
        // Load rooms matching the id always directly from the API.
        mRoomsServiceApi.getRoom(roomId, new RoomsServiceApi.RoomsServiceCallback<Room>() {
            @Override
            public void onLoaded(Room room) {
                callback.onRoomLoaded(room);
            }
        });
    }

    @Override
    public void refreshData() {
        mCachedRooms = null;
    }

}
