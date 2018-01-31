package org.room76.apollo.signin;

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
    }

    interface UserActionsListener {

        void signInWithEmail(String email, String password);

        void createAccountWithEmail(String email, String password);

        void signOut();

        boolean checkRegistrated();

        void waitForStateChange();

        void removeStateChangeWaiting();
    }

}
