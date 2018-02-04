package org.room76.apollo.mymusic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.room76.apollo.R;

/**
 * Created by a.zatsepin on 04/02/2018.
 */

public class ErrorFragment extends Fragment{

    public static ErrorFragment newInstance() {
        Bundle args = new Bundle();
        ErrorFragment fragment = new ErrorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_error, container, false);
        return root;
    }
}
