package com.mpgtracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mpgtracker.R;

public abstract class BaseFragment extends Fragment {
    protected abstract String getTitle();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Update Title
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update Title
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }
}
