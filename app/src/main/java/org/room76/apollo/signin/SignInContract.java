package org.room76.apollo.signin;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;

/**
 * This specifies the contract between the view and the presenter.
 */
public class SignInContract {

    interface View {
        void setProgressIndicator(boolean active);

        void showProviderSelector();

        void hideProviderSelector();

        void showLoginForm();

        void hideLoginForm();

        void showRegistrationForm();

        void hideRegistrationForm();

        void navigateToMainPage(FirebaseUser user);

        void showError(String exception);
    }

    interface UserActionsListener {

        void signInWithEmail(String email, String password);

        void signInWithGoogle(GoogleSignInAccount account);

        void createAccountWithEmail(String email, String password);

        void signOut();

        void waitForStateChange();

        void removeStateChangeWaiting();
    }

}
