package org.room76.apollo.signin;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by a.zatsepin on 03/02/2018.
 */

public class SignInState {
    private static SignInState instance = new SignInState();

    private FirebaseUser mUser;

    private SignInState() {
    }

    public static SignInState getInstance() {
        return instance;
    }

    public FirebaseUser getUser() {
        return mUser;
    }

    public void setUser(FirebaseUser mUser) {
        this.mUser = mUser;
    }
}
