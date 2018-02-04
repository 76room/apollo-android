package org.room76.apollo.rooms;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;

import org.room76.apollo.BaseNavigationActivity;
import org.room76.apollo.model.FirebaseDataRoomRepository;
import org.room76.apollo.model.FirebaseUserMock;
import org.room76.apollo.model.Room;
import org.room76.apollo.model.RoomsRepository;
import org.room76.apollo.model.Track;

import java.util.ArrayList;
import java.util.List;


public class RoomsActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setFragment(RoomsFragment.newInstance());
        super.onCreate(savedInstanceState);
    }
}
