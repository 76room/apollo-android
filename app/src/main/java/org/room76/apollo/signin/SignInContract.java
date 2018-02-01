package org.room76.apollo.signin;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

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

        void navigateToMainPage();

        void showError(String exception);
    }

    interface UserActionsListener {

        void signInWithEmail(String email, String password);

        void signInWithGoogle(GoogleSignInAccount account);

        void createAccountWithEmail(String email, String password);

        void signOut();

        boolean checkRegistrated();

        void waitForStateChange();

        void removeStateChangeWaiting();
    }

}
