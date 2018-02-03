package org.room76.apollo.rooms;

import android.os.Bundle;

import org.room76.apollo.BaseNavigationActivity;

public class RoomsActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFragment(RoomsFragment.newInstance());
        super.onCreate(savedInstanceState);
    }
}
