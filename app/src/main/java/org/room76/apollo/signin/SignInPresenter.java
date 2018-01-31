package org.room76.apollo.signin;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInPresenter implements SignInContract.UserActionsListener {

    private final SignInContract.View mSignInContractView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public SignInPresenter(SignInContract.View mSignInContractView) {
        this.mSignInContractView = mSignInContractView;
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth . AuthStateListener () {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null) {
                    // User is signed in
                } else {
                    // User is signed out
                }
            }
        };
    }

    @Override
    public void signInWithEmail(String email, String password) {
        mSignInContractView.setProgressIndicator(true);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                        }
                        mSignInContractView.setProgressIndicator(false);
                    }
                });
    }

    @Override
    public void createAccountWithEmail(String email, String password) {
        mSignInContractView.setProgressIndicator(true);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                        }
                        mSignInContractView.setProgressIndicator(false);
                    }
                });
    }

    @Override
    public void signOut() {

    }

    @Override
    public boolean checkRegistrated() {
        return true;
    }

    @Override
    public void waitForStateChange() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void removeStateChangeWaiting() {
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
