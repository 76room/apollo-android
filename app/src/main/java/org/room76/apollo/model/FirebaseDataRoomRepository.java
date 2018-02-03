package org.room76.apollo.model;


import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDataRoomRepository implements RoomsRepository {
    private DatabaseReference REFERENCE = FirebaseDatabase.getInstance().getReference();

    @Override
    public void getRooms(@NonNull final LoadRoomsCallback callback) {

    }

    @Override
    public void getRoom(@NonNull final String roomId, @NonNull final GetRoomCallback callback) {
        Query q = REFERENCE.child("Rooms").child(roomId);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Object roomParmas =  dataSnapshot.getValue();
//                if (roomParmas instanceof Map) {
//                    roomParmas =  (Map<String, String>) roomParmas;
//                    Room room = getRoomFromMap(roomParmas);
//            }
                callback.onRoomLoaded(dataSnapshot.getValue(Room.class));


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

    }
}
