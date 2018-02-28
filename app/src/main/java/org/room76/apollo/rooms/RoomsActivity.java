package org.room76.apollo.rooms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import org.room76.apollo.BaseNavigationActivity;
import org.room76.apollo.R;
import org.room76.apollo.model.Room;


public class RoomsActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateFragment(getIntent());
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras().getBoolean("my")){
            setTitle(R.string.my_rooms);
        } else setTitle(R.string.all_rooms);
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        updateFragment(intent);
////        ((RoomsFragment)mFragment).update();
//    }

    private void updateFragment(Intent intent) {
        mFragment = RoomsFragment.newInstance(intent.getExtras().getBoolean("my"));
        setFragment(mFragment);
    }
}
