package org.room76.apollo.model;

import android.support.v4.util.ArrayMap;

/**
 * This is the endpoint for your data source. Typically, it would be a SQLite db and/or a server
 * API. In this example, we fake this by creating the data on the fly.
 */
public final class RoomsServiceApiEndpoint {

    static {
        DATA = new ArrayMap(3);
        addRoom("Oh yes!", "I demand trial by Unit testing", null);
        addRoom("Image Room", "Room with image for testing", "http://www.fonstola.ru/large/201604/230397.jpg");
        addRoom("Espresso", "UI Testing for Android", null);
    }

    private final static ArrayMap<String, Room> DATA;

    private static void addRoom(String title, String description, String imageUrl) {
        Room newRoom = new Room(title, description, imageUrl);
        DATA.put(newRoom.getId(), newRoom);
    }

    /**
     * @return the Rooms to show when starting the app.
     */
    public static ArrayMap<String, Room> loadPersistedRooms() {
        return DATA;
    }
}
