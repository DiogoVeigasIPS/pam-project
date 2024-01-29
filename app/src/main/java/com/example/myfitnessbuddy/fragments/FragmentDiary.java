package com.example.myfitnessbuddy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.myfitnessbuddy.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDiary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDiary extends Fragment {

    public FragmentDiary() {
        // Required empty public constructor
    }

    public static FragmentDiary newInstance() {
        FragmentDiary fragment = new FragmentDiary();
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
        return inflater.inflate(R.layout.fragment_diary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton btBack = getView().findViewById(R.id.bt_back);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(getClass().getSimpleName(), "Someone clicked me!");
                Navigation.updateFragment(FragmentPanel.newInstance());
            }
        });
    }
}