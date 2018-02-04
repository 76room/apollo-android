package org.room76.apollo.model;


import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseDataRoomRepository implements RoomsRepository {
    private DatabaseReference REFERENCE = FirebaseDatabase.getInstance().getReference();

    @Override
    public void getRooms(@NonNull final LoadRoomsCallback callback) {
        Query q = REFERENCE.child("Rooms");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> roomsMap = (Map<String, Object>) dataSnapshot.getValue();
                List<Room> result = new ArrayList<>();
                for (String k : roomsMap.keySet()) {
                    Map<String, Object> roomMap = (Map<String, Object>) roomsMap.get(k);
                    Room room = Room.roomFromMap(roomMap);
                    result.add(room);
                }
                callback.onRoomsLoaded(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getRoom(@NonNull final String roomId, @NonNull final GetRoomCallback callback) {
        Query q = REFERENCE.child("Rooms").child(roomId);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onRoomLoaded(Room.roomFromMap((Map<String, Object>) dataSnapshot.getValue()));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void saveRoom(@NonNull Room room) {
        REFERENCE.child("Rooms").child(room.getId()).setValue(room);
    }

    @Override
    public void refreshData() {
        //pass
    }
}
