package org.room76.apollo.model;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDataRoomRepository implements Repository<Room> {
    private static final String TAG = FirebaseDataRoomRepository.class.getClass().getSimpleName();
    private static final String TABLE_NAME = "Rooms";

    private List<Room> mList = new ArrayList<>();
    private DatabaseReference DATABASE = FirebaseDatabase.getInstance().getReference();

    public FirebaseDataRoomRepository() {
        refresh();
    }

    private void refresh(){
        Query q = DATABASE.child(TABLE_NAME);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mList.clear();
                for (DataSnapshot roomSnapshot: dataSnapshot.getChildren()) {
                    Room room = roomSnapshot.getValue(Room.class);
                    mList.add(room);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG,databaseError.getMessage());
            }
        });
    }

    @Override
    public void updateItem(Room item) {
        DATABASE.child(TABLE_NAME).child(item.getId()).setValue(item);
    }

    @Override
    public void getItems(@NonNull final LoadCallback<Room> callback) {
        callback.onLoaded(mList);
    }

    @Override
    public void getItem(@NonNull final String roomId, @NonNull final GetCallback<Room> callback) {
        for (Room room: mList) {
            if (room.getId().equals(roomId)) callback.onLoaded(room);
        }
    }

    @Override
    public synchronized void saveItem(@NonNull Room item) {
        DATABASE.child(TABLE_NAME).child(item.getId()).setValue(item);
        mList.add(item);
    }

    @Override
    public void refreshData() {
        refresh();
    }

    @Override
    public boolean contains(Room item) {
        return mList.contains(item);
    }
}
