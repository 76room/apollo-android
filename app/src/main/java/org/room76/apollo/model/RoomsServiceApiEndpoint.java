package org.room76.apollo.model;

import android.support.v4.util.ArrayMap;

/**
 * This is the endpoint for your data source. Typically, it would be a SQLite db and/or a server
 * API. In this example, we fake this by creating the data on the fly.
 */
public final class RoomsServiceApiEndpoint {

    static {
        DATA = new ArrayMap<>(3);
        addRoom(new User("Queen", "https://www.hellomagazine.com/imagenes/royalty/2017102443429/the-queen-horse-earnings-revealed/0-221-286/the-queen-state-opening-crown-t.jpg"), "Oh yes!", "I demand trial by Unit testing", true, null);
        addRoom(new User("Alabama"), "Image Room", "Room with image for testing", true, "http://www.fonstola.ru/large/201604/230397.jpg");
        addRoom(new User("Gandalf", "https://coubsecure-s.akamaihd.net/get/b180/p/coub/simple/cw_image/07837940028/8e1dc6c1ed147af871c8e/med_1501967527_00032.jpg"), "Espresso", "UI Testing for Android", false, null);
    }

    private final static ArrayMap<String, Room> DATA;

    private static void addRoom(User author, String title, String description, boolean isOpen, String imageUrl) {
        Room newRoom = new Room(author, title, description, isOpen, imageUrl);
        DATA.put(newRoom.getId(), newRoom);
    }

    /**
     * @return the Rooms to show when starting the app.
     */
    public static ArrayMap<String, Room> loadPersistedRooms() {
        return DATA;
    }
}
