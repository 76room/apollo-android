package org.room76.apollo.mymusic;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import org.room76.apollo.BaseNavigationActivity;
import org.room76.apollo.R;

public class MyMusicActivity extends BaseNavigationActivity {

    private static final int PERMISSION_REQUEST_READ_MUSIC = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (null == savedInstanceState) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_READ_MUSIC);
        }
        super.onCreate(savedInstanceState);
        mFab.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_READ_MUSIC) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initFragment(MyMusicFragment.newInstance());
            } else {
                Toast.makeText(this, "Read storage permission request was denied.", Toast.LENGTH_LONG).show();
                initFragment(ErrorFragment.newInstance());
            }
        }
    }
}
