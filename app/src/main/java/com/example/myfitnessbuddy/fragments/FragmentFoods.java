package com.example.myfitnessbuddy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfitnessbuddy.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentFoods#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFoods extends Fragment {

    public FragmentFoods() {
        // Required empty public constructor
    }

    public static FragmentFoods newInstance() {
        FragmentFoods fragment = new FragmentFoods();
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
        return inflater.inflate(R.layout.fragment_foods, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}