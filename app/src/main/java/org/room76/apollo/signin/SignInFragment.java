package org.room76.apollo.signin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.room76.apollo.R;

public class SignInFragment extends Fragment implements SignInContract.View, View.OnClickListener {

    private SignInContract.UserActionsListener mActionsListener;

    private View mProviderContainer;
    private View mLoginFormContainer;

    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    public static Fragment newInstance() {
        return new SignInFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mProviderContainer = root.findViewById(R.id.select_provider_container);
        root.findViewById(R.id.email).setOnClickListener(this);
        root.findViewById(R.id.google).setOnClickListener(this);
        root.findViewById(R.id.facebook).setOnClickListener(this);

        mLoginFormContainer = root.findViewById(R.id.login_form);
        root.findViewById(R.id.next).setOnClickListener(this);
        mEmailEditText = root.findViewById(R.id.edit_email);
        mPasswordEditText = root.findViewById(R.id.edit_password);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        mActionsListener = new SignInPresenter(this);
    }

    @Override
    public void setProgressIndicator(boolean active) {

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
                if (mActionsListener.checkRegistrated()) {
                    hideProviderSelector();
                    showLoginForm();
                } else {
                    hideProviderSelector();
                }
                break;
            case R.id.google:
                break;
            case R.id.facebook:
                break;
            case R.id.next:
                mActionsListener.signInWithEmail(mEmailEditText.getText().toString(),mPasswordEditText.getText().toString());
                break;
            default:
                break;
        }
    }
}
