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
        addRoom(new User("Queen", "https://www.hellomagazine.com/imagenes/royalty/2017102443429/the-queen-horse-earnings-revealed/0-221-286/the-queen-state-opening-crown-t.jpg"), "Oh yes!", "I demand trial by Unit testing", true, null);
        addRoom(new User("Alabama", "https://peopledotcom.files.wordpress.com/2017/07/jennifer-aniston-fragrance-2.jpg"), "Unit Cafe", "Сервіс замовлення їжі онлайн в Unit.Cafe, найкращому кафе інноваційного містечка Unit City.", true, "https://images.adsttc.com/media/images/594b/94d4/b22e/3898/a700/0492/large_jpg/10.jpg?1498125515");
        addRoom(new User("Gandalf", "https://coubsecure-s.akamaihd.net/get/b180/p/coub/simple/cw_image/07837940028/8e1dc6c1ed147af871c8e/med_1501967527_00032.jpg"), "Espresso", "UI Testing for Android", false, null);
    }

    private final static ArrayMap<String, Room> DATA;

    private static void addRoom(User author, String title, String description, boolean isOpen, String imageUrl) {
        Room newRoom = new Room(author, title, description, isOpen, imageUrl);
        List<Track> tracks = new ArrayList<>();
        tracks.add(new Track("Demons", "Imagine", 200000, "http://test"));
        tracks.add(new Track("Rape me", "Nirvana", 400000, "http://test"));
        tracks.add(new Track("Выхода нет", "Сплин", 50000, "http://test"));
        List<User> users = new ArrayList<>();
        users.add(author);

        users.add(new User("Alba", "http://v1.popcornnews.ru/upload/jsOT7y.jpg"));
        users.add(new User("Alba", "http://lichnosti.net/photos/4393/main.jpg"));
        users.add(new User("Hulk", "https://vokrug.tv/pic/person/4/c/3/8/4c385380340d56acd318cf77d7777924.jpeg"));

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
