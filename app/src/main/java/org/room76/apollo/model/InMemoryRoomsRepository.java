package org.room76.apollo.model;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation to load rooms from the data source.
 */
public final class InMemoryRoomsRepository implements Repository<Room> {

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
    public void getItems(@NonNull final LoadCallback callback) {
        // Load from API only if needed.
        if (mCachedRooms == null) {
            mRoomsServiceApi.getAllRooms(new RoomsServiceApi.RoomsServiceCallback<List<Room>>() {
                @Override
                public void onLoaded(List<Room> rooms) {
                    mCachedRooms = new ArrayList<>(rooms);
                    callback.onLoaded(mCachedRooms);
                }
            });
        } else {
            callback.onLoaded(mCachedRooms);
        }
    }

    @Override
    public void updateItem(Room item) {

    }

    @Override
    public void saveItem(@NonNull Room room) {
        mRoomsServiceApi.saveRoom(room);
        refreshData();
    }

    @Override
    public void getItem(@NonNull final String roomId, @NonNull final GetCallback callback) {
        // Load rooms matching the id always directly from the API.
        mRoomsServiceApi.getRoom(roomId, new RoomsServiceApi.RoomsServiceCallback<Room>() {
            @Override
            public void onLoaded(Room room) {
                callback.onLoaded(room);
            }
        });
    }

    @Override
    public void refreshData() {
        mCachedRooms = null;
    }

    @Override
    public boolean contains(Room item) {
        return false;
    }

}
