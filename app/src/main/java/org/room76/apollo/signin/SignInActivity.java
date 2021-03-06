package org.room76.apollo.signin;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.room76.apollo.R;
import org.room76.apollo.util.EspressoIdlingResource;

public class SignInActivity extends AppCompatActivity {

    public static int SIGN_OUT = 0;
    public static int SIGN_IN = 1;

    private SignInFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);
        if (null == savedInstanceState) {
            mFragment = SignInFragment.newInstance();
            initFragment(mFragment);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getAction() != null) {
            String intent = getIntent().getAction();
            if (intent.equals(String.valueOf(SignInActivity.SIGN_OUT))) {
                mFragment.handleSignOut();
            }
        }
    }

    private void initFragment(Fragment roomsFragment) {
        // Add the RoomsFragment to the layout
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, roomsFragment);
        transaction.commit();
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
