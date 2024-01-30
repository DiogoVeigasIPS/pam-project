package com.example.myfitnessbuddy.main_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.diary.AddToMealActivity;
import com.example.myfitnessbuddy.models.Meal;

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

        setNavigationalButtons();
    }

    private void setNavigationalButtons() {
        ImageButton btBack = getView().findViewById(R.id.bt_back);
        btBack.setOnClickListener(v -> Navigation.updateFragment(FragmentPanel.newInstance()));

        Button addBreakfast = getView().findViewById(R.id.add_breakfast);
        Button addLunch = getView().findViewById(R.id.add_lunch);
        Button addDinner = getView().findViewById(R.id.add_dinner);
        Button addSnack = getView().findViewById(R.id.add_snack);

        addBreakfast.setOnClickListener(v -> addFoodToMeal(R.string.breakfast));
        addLunch.setOnClickListener(v -> addFoodToMeal(R.string.lunch));
        addDinner.setOnClickListener(v -> addFoodToMeal(R.string.dinner));
        addSnack.setOnClickListener(v -> addFoodToMeal(R.string.snack));
    }

    // Send meal or something (like an id)
    private void addFoodToMeal(int title){
        Intent intent = new Intent(getContext(), AddToMealActivity.class);
        intent.putExtra(AddToMealActivity.TITLE, title);
        startActivity(intent);
    }
}