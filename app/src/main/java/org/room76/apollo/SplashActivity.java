package org.room76.apollo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.room76.apollo.rooms.RoomsActivity;
import org.room76.apollo.signin.SignInActivity;
import org.room76.apollo.util.Injection;

import static org.room76.apollo.mymusic.MyMusicActivity.PERMISSION_REQUEST_READ_MUSIC;

/**
 * Created by a.zatsepin on 25/02/2018.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView tv = findViewById(R.id.version);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        tv.setText(String.format("%s %s", getString(R.string.version_name), BuildConfig.VERSION_NAME));

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_READ_MUSIC);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivityForResult(new Intent(getApplicationContext(), SignInActivity.class), SignInActivity.SIGN_IN);
            }
        },2000);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_READ_MUSIC) {
            Injection.init();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SignInActivity.SIGN_IN) {
            startActivity(new Intent(getApplicationContext(), RoomsActivity.class));
        }
    }
}
