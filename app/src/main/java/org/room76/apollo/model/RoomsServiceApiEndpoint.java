package org.room76.apollo.model;

import android.support.v4.util.ArrayMap;

import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the endpoint for your data source. Typically, it would be a SQLite db and/or a server
 * API. In this example, we fake this by creating the data on the fly.
 */
public final class RoomsServiceApiEndpoint {

    static {
        DATA = new ArrayMap<>(3);
        addRoom(new FirebaseUserMock("Queen", "https://www.hellomagazine.com/imagenes/royalty/2017102443429/the-queen-horse-earnings-revealed/0-221-286/the-queen-state-opening-crown-t.jpg"), "Oh yes!", "I demand trial by Unit testing", true, null);
        addRoom(new FirebaseUserMock("Alabama", "https://peopledotcom.files.wordpress.com/2017/07/jennifer-aniston-fragrance-2.jpg"), "Image Room", "Room with image for testing", true, "http://www.fonstola.ru/large/201604/230397.jpg");
        addRoom(new FirebaseUserMock("Gandalf", "https://coubsecure-s.akamaihd.net/get/b180/p/coub/simple/cw_image/07837940028/8e1dc6c1ed147af871c8e/med_1501967527_00032.jpg"), "Espresso", "UI Testing for Android", false, null);
    }

    private final static ArrayMap<String, Room> DATA;

    private static void addRoom(FirebaseUser author, String title, String description, boolean isOpen, String imageUrl) {
        Room newRoom = new Room(author, title, description, isOpen, imageUrl);
        List<Track> tracks = new ArrayList<>();
        tracks.add(new Track("track1", "Nirvana", 200000, "http://test"));
        tracks.add(new Track("track2", "Nirvana", 400000, "http://test"));
        tracks.add(new Track("track3", "Nirvana", 50000, "http://test"));
        tracks.add(new Track("track4", "Nirvana", 60000, "http://test"));
        List<FirebaseUser> users = new ArrayList<>();
        users.add(author);
        newRoom.setTracks(tracks);
        newRoom.setUsers(users);
        DATA.put(newRoom.getId(), newRoom);
    }

    /**
     * @return the Rooms to show when starting the app.
     */
    public static ArrayMap<String, Room> loadPersistedRooms() {
        return DATA;
    }
}
