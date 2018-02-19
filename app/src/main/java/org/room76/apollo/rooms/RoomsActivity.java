package org.room76.apollo.rooms;

import android.content.Intent;
import android.os.Bundle;

import org.room76.apollo.BaseNavigationActivity;


public class RoomsActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFragment(RoomsFragment.newInstance());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent); //TODO think about update
    }
}
