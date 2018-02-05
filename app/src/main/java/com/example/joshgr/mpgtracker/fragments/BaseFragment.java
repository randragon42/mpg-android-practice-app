package com.example.joshgr.mpgtracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.joshgr.mpgtracker.R;


public abstract class BaseFragment extends Fragment {
    protected abstract String getTitle();

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Update Title
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

    }
}
