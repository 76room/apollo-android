package org.room76.apollo.rooms;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.NavigationView;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.room76.apollo.R;
import org.room76.apollo.signin.SignInActivity;
import org.room76.apollo.util.EspressoIdlingResource;

public class RoomsActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private ImageView mUserImage;
    private TextView mUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        // Set up the toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Set up the navigation drawer.
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.drawable.dr_drawer_background);
        NavigationView navigationView = findViewById(R.id.nav_view);
        findViewById(R.id.footer_feedback).setOnClickListener(this);
        findViewById(R.id.footer_settings).setOnClickListener(this);
        findViewById(R.id.footer_sigh_out).setOnClickListener(this);
        View headerview = navigationView.getHeaderView(0);
        mUserEmail = headerview.findViewById(R.id.user_email);
        mUserImage = headerview.findViewById(R.id.user_image);
        navigationView.setItemIconTintList(null);
        setupDrawerContent(navigationView);

        if (null == savedInstanceState) {
            initFragment(RoomsFragment.newInstance());
        }
        mUserEmail.setText("Please login in your account");
        mUserImage.setOnClickListener(this);
    }

    private void initFragment(Fragment roomsFragment) {
        // Add the RoomsFragment to the layout
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, roomsFragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.my_rooms_navigation_menu_item:
                        Toast.makeText(getApplicationContext(), "My rooms", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.find_rooms_navigation_menu_item:
                        Toast.makeText(getApplicationContext(), "Find rooms", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.find_music_navigation_menu_item:
                        Toast.makeText(getApplicationContext(), "Find music", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.find_place_navigation_menu_item:
                        Toast.makeText(getApplicationContext(), "Find place", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                // Close the navigation drawer when an item is selected.
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.user_image:
                startActivity(new Intent(this, SignInActivity.class));
                break;
            case R.id.footer_feedback:
                Toast.makeText(getApplicationContext(),"footer click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.footer_settings:
                Toast.makeText(getApplicationContext(),"footer click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.footer_sigh_out:
                startActivityForResult(new Intent(this, SignInActivity.class), SignInActivity.SIGN_OUT);
                break;
            default:
                break;
        }
        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == RESULT_OK && requestCode == SignInActivity.SIGN_OUT) {
           Log.i("result","user signed out");
           mUserEmail.setText("Please login in your account");
           mUserImage.setImageDrawable(getDrawable(R.drawable.ic_default_user_image));
       }
    }
}
