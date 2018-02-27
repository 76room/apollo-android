package org.room76.apollo.rooms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import org.room76.apollo.BaseNavigationActivity;


public class RoomsActivity extends BaseNavigationActivity {

    private RoomsFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFragment = RoomsFragment.newInstance();
        setFragment(mFragment);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mFragment.update(intent.getAction()!=null);
    }
}
