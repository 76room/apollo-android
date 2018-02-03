package org.room76.apollo.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import org.room76.apollo.R;
import org.room76.apollo.rooms.RoomsActivity;

import static android.app.Activity.RESULT_OK;

public class SignInFragment extends Fragment implements SignInContract.View, View.OnClickListener {
    private static final int RC_SIGN_IN = 100;

    private SignInContract.UserActionsListener mActionsListener;

    private LottieAnimationView mProgressGears;

    private View mProviderContainer;
    private View mLoginFormContainer;

    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sign_in, container, false);
        setRetainInstance(true);
        mActionsListener = new SignInPresenter(this);

        mProviderContainer = root.findViewById(R.id.select_provider_container);
        root.findViewById(R.id.email).setOnClickListener(this);
        root.findViewById(R.id.google).setOnClickListener(this);
        root.findViewById(R.id.facebook).setOnClickListener(this);

        mLoginFormContainer = root.findViewById(R.id.login_form);
        root.findViewById(R.id.next).setOnClickListener(this);
        mEmailEditText = root.findViewById(R.id.edit_email);
        mPasswordEditText = root.findViewById(R.id.edit_password);

        mProgressGears = root.findViewById(R.id.animation_view);
        return root;
    }

    @Override
    public void setProgressIndicator(boolean active) {
        mProgressGears.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showProviderSelector() {
        mProviderContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProviderSelector() {
        mProviderContainer.setVisibility(View.GONE);
    }

    @Override
    public void showLoginForm() {
        mLoginFormContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoginForm() {
        mLoginFormContainer.setVisibility(View.GONE);
    }

    @Override
    public void showRegistrationForm() {

    }

    @Override
    public void hideRegistrationForm() {

    }

    @Override
    public void navigateToMainPage(FirebaseUser user) {
        SignInState.getInstance().setUser(user);
        if (getActivity() != null) {
            getActivity().setResult(RESULT_OK);
            getActivity().finish();
        }
    }

    @Override
    public void showError(String exception) {
        Toast.makeText(getContext(), exception, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mActionsListener.waitForStateChange();
    }

    @Override
    public void onStop() {
        super.onStop();
        mActionsListener.removeStateChangeWaiting();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.email:
                setProgressIndicator(true);
                hideProviderSelector();
                showLoginForm();
                setProgressIndicator(false);
                break;
            case R.id.google:
                setProgressIndicator(true);
                Intent intent = buildGoogleClient().getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
                break;
            case R.id.facebook:
                break;
            case R.id.next:
                mActionsListener.signInWithEmail(mEmailEditText.getText().toString(), mPasswordEditText.getText().toString());
                break;
            default:
                break;
        }
    }

    public void handleSignOut() {
        if (mActionsListener != null) {
            mActionsListener.signOut();
            getActivity().setResult(RESULT_OK);
            getActivity().finish();
        }
    }

    private GoogleSignInClient buildGoogleClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        return mGoogleSignInClient;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                mActionsListener.signInWithGoogle(account);
            } catch (ApiException e) {
                showError("signInResult:failed code=" + e.getStatusCode());
            }
        }
    }
}
