package org.room76.apollo.model;

import java.util.List;

/**
 * Defines an interface to the service API that is used by this application. All data request should
 * be piped through this interface.
 */
public interface RoomsServiceApi {

    interface RoomsServiceCallback<T> {
        void onLoaded(T rooms);
    }

    void getAllRooms(RoomsServiceCallback<List<Room>> callback);

    void getRoom(String roomId, RoomsServiceCallback<Room> callback);

    void saveRoom(Room room);
}
