package com.example.myfitnessbuddy.main_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.panel.UpdateDetailsActivity;
import com.example.myfitnessbuddy.activities.panel.UpdateGoalActivity;
import com.example.myfitnessbuddy.activities.panel.UpdateWeightActivity;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton calorieButton = getView().findViewById(R.id.mbt_calorie_add);
        ImageButton weightButton = getView().findViewById(R.id.mbt_weight_add);
        AppCompatButton personalDetailsButton = getView().findViewById(R.id.bt_personal_details);

        calorieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent caloryIntent = new Intent(getActivity(), UpdateGoalActivity.class);
                startActivity(caloryIntent);
            }
        });

        weightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent caloryIntent = new Intent(getActivity(), UpdateWeightActivity.class);
                startActivity(caloryIntent);
            }
        });

        personalDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent caloryIntent = new Intent(getActivity(), UpdateDetailsActivity.class);
                startActivity(caloryIntent);
            }
        });

    }
}