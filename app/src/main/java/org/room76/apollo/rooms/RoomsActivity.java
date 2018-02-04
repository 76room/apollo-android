package org.room76.apollo.rooms;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;

import org.room76.apollo.BaseNavigationActivity;
import org.room76.apollo.model.FirebaseDataRoomRepository;
import org.room76.apollo.model.FirebaseUserMock;
import org.room76.apollo.model.Room;
import org.room76.apollo.model.RoomsRepository;

import java.util.List;

public class RoomsActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setFragment(RoomsFragment.newInstance());
        super.onCreate(savedInstanceState);
        FirebaseDataRoomRepository dataRoomRepository = new FirebaseDataRoomRepository();
//        FirebaseUser user = new FirebaseUserMock("Test");
//        Room r = new Room(user, "Test room", "For testing", true, "imageUrl");
//        System.out.println(r.getTitle());
//        System.out.println(r.getId());
//        dataRoomRepository.saveRoom(r);
        dataRoomRepository.getRooms(new RoomsRepository.LoadRoomsCallback() {
            @Override
            public void onRoomsLoaded(List<Room> rooms) {
                for (Room r : rooms) {
                    System.out.println(r.getTitle());
                }
            }
        });
    }
}
