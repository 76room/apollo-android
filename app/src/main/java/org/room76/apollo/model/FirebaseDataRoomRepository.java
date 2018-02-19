package org.room76.apollo.model;


import android.support.annotation.NonNull;
import android.util.Log;

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

public class FirebaseDataRoomRepository implements Repository<Room> {
    private static final String TAG = FirebaseDataRoomRepository.class.getClass().getSimpleName();

    private List<Room> mList = new ArrayList<>();
    private DatabaseReference REFERENCE = FirebaseDatabase.getInstance().getReference();

    @Override
    public void getItems(@NonNull final LoadCallback<Room> callback) {
        Query q = REFERENCE.child("Rooms");
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mList.add(dataSnapshot.getValue(Room.class));
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                mList.remove(dataSnapshot.getValue(Room.class));
            }

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG,databaseError.getMessage());
            }
        });
        callback.onLoaded(mList);
    }

    @Override
    public void getItem(@NonNull final String roomId, @NonNull final GetCallback<Room> callback) {
        Query q = REFERENCE.child("Rooms").child(roomId);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onLoaded(dataSnapshot.getValue(Room.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG,databaseError.getMessage());
            }
        });
    }

    @Override
    public void saveItem(@NonNull Room item) {
        REFERENCE.child("Rooms").child(item.getId()).setValue(item);
    }

    @Override
    public void refreshData() {
        //pass
    }
}
