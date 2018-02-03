package org.room76.apollo.signin;

import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInPresenter implements SignInContract.UserActionsListener {

    private final SignInContract.View mSignInContractView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public SignInPresenter(final SignInContract.View mSignInContractView) {
        this.mSignInContractView = mSignInContractView;
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mSignInContractView.navigateToMainPage(user);
                } else {
                    mSignInContractView.showProviderSelector();
                }
            }
        };
    }

    @Override
    public void signInWithEmail(String email, String password) {
        mSignInContractView.setProgressIndicator(true);
        if (email.isEmpty() || password.isEmpty()) {
            mSignInContractView.showError("Field can't be empty");
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                mSignInContractView.navigateToMainPage(task.getResult().getUser());
                            } else {
                                mSignInContractView.showError(task.getException().getMessage());
                            }
                            mSignInContractView.setProgressIndicator(false);
                        }
                    });
        }

    }

    @Override
    public void signInWithGoogle(GoogleSignInAccount account) {
        mSignInContractView.setProgressIndicator(true);
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            mSignInContractView.navigateToMainPage(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            mSignInContractView.showError(task.getException().getMessage());
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
                        if (task.isSuccessful()) {
                            mSignInContractView.navigateToMainPage(task.getResult().getUser());
                        }
                        mSignInContractView.setProgressIndicator(false);
                    }
                });
    }

    @Override
    public void signOut() {
        mAuth.signOut();
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
