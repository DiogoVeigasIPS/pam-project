package com.example.myfitnessbuddy.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfitnessbuddy.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPanel#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPanel extends Fragment {

    public FragmentPanel() {
        // Required empty public constructor
    }

    public static FragmentPanel newInstance() {
        FragmentPanel fragment = new FragmentPanel();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_panel, container, false);
    }
}