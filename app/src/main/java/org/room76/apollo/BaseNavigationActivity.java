package org.room76.apollo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.firebase.auth.FirebaseUser;

import org.room76.apollo.mymusic.MyMusicActivity;
import org.room76.apollo.rooms.RoomsActivity;
import org.room76.apollo.signin.SignInActivity;
import org.room76.apollo.signin.SignInState;
import org.room76.apollo.util.BorderTransform;
import org.room76.apollo.util.CircleTransform;
import org.room76.apollo.util.EspressoIdlingResource;
import org.room76.apollo.util.ShadowTransform;

/**
 * Created by a.zatsepin on 03/02/2018.
 */

public abstract class BaseNavigationActivity extends AppCompatActivity implements View.OnClickListener {
    protected DrawerLayout mDrawerLayout;
    protected ImageView mUserImage;
    protected TextView mUserEmail;
    protected Fragment mFragment;
    protected FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_core);

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
        headerview.setOnClickListener(this);
        mUserEmail = headerview.findViewById(R.id.user_email);
        mUserImage = headerview.findViewById(R.id.user_image);
        mFab = findViewById(R.id.fab_add_rooms);
        navigationView.setItemIconTintList(null);
        setupDrawerContent(navigationView);

        if (null == savedInstanceState && mFragment != null) {
            initFragment(mFragment);
        }
    }

    protected void setFragment(Fragment fragment){
        this.mFragment = fragment;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUserData();
    }

    protected void initFragment(Fragment roomsFragment) {
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
                        startActivity(new Intent(getApplicationContext(), RoomsActivity.class));
                        break;
                    case R.id.find_rooms_navigation_menu_item:
                        Toast.makeText(getApplicationContext(), "Find rooms", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.find_music_navigation_menu_item:
                        startActivity(new Intent(getApplicationContext(), MyMusicActivity.class));
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header:
                if (SignInState.getInstance().getUser() == null) {
                    startActivityForResult(new Intent(this, SignInActivity.class), SignInActivity.SIGN_IN);
                }
                break;
            case R.id.footer_feedback:
                Toast.makeText(getApplicationContext(), "footer click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.footer_settings:
                Toast.makeText(getApplicationContext(), "footer click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.footer_sigh_out:
                if (SignInState.getInstance().getUser() != null) {
                    Intent intent = new Intent(this, SignInActivity.class);
                    intent.setAction(String.valueOf(SignInActivity.SIGN_OUT));
                    startActivityForResult(intent, SignInActivity.SIGN_OUT);
                }
                break;
            default:
                break;
        }
        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_OK && requestCode == SignInActivity.SIGN_OUT) {
            setupUserData();
        }
    }

    private void setupUserData() {
        if (SignInState.getInstance().getUser() == null) {
            mUserEmail.setText("Please login in your account");
            mUserImage.setImageDrawable(getDrawable(R.drawable.ic_default_user_image));
        } else if (SignInState.getInstance().getUser() != null) {
            FirebaseUser user = SignInState.getInstance().getUser();
            mUserEmail.setText(user.getDisplayName() == null || user.getDisplayName().isEmpty()
                    ? user.getEmail() : user.getDisplayName());

            EspressoIdlingResource.increment();

            Glide.with(getApplicationContext())
                    .load(user.getPhotoUrl())
                    .error(R.drawable.ic_default_user_image)
                    .transform(new CircleTransform(this))
                    .into(new GlideDrawableImageViewTarget(mUserImage) {
                        @Override
                        public void onResourceReady(GlideDrawable resource,
                                                    GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            EspressoIdlingResource.decrement(); // App is idle.
                        }
                    });
        }
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
