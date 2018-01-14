package org.room76.apollo.model;

import android.os.Handler;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the Rooms Service API that adds a latency simulating network.
 */
public class RoomsServiceApiImpl implements RoomsServiceApi {

    private static final int SERVICE_LATENCY_IN_MILLIS = 2000;
    private static final ArrayMap<String, Room> ROOMS_SERVICE_DATA =
            RoomsServiceApiEndpoint.loadPersistedRooms();

    @Override
    public void getAllRooms(final RoomsServiceCallback callback) {
        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Room> rooms = new ArrayList<>(ROOMS_SERVICE_DATA.values());
                callback.onLoaded(rooms);
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void getRoom(final String roomId, final RoomsServiceCallback callback) {
        //TODO: Add network latency here too.
        Room room = ROOMS_SERVICE_DATA.get(roomId);
        callback.onLoaded(room);
    }

    @Override
    public void saveRoom(Room room) {
        ROOMS_SERVICE_DATA.put(room.getId(), room);
    }
}
