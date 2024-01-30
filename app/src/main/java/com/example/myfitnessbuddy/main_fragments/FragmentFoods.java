package com.example.myfitnessbuddy.main_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myfitnessbuddy.DatabaseHelper;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.foods.AddFoodActivity;
import com.example.myfitnessbuddy.adapters.FoodAdapter;
import com.example.myfitnessbuddy.models.Food;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentFoods#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFoods extends Fragment {

    private RecyclerView foodsList;
    private TextView emptyList;
    private FoodAdapter foodAdapter;

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

        setNavigationalButtons();

        setTabNavigation();

        foodsList = getView().findViewById(R.id.foods_list);
        emptyList = getView().findViewById(R.id.empty_list);
        setListAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(foodAdapter != null) updateFoodList();
    }

    private void setNavigationalButtons(){
        ImageButton btBack = getView().findViewById(R.id.bt_back);
        btBack.setOnClickListener(v -> {
            Log.d(getClass().getSimpleName(), "Someone clicked me!");
            Navigation.updateFragment(FragmentPanel.newInstance());
        });

        FloatingActionButton btAdd = getView().findViewById(R.id.bt_add);
        btAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddFoodActivity.class);
            startActivity(intent);
        });

        EditText foodsSearch = getView().findViewById(R.id.foods_search);
        foodsSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String search = foodsSearch.getText().toString();
                updateFoodList(search);
                return true;
            }
            return false;
        });
    }

    private void setListAdapter() {
        DatabaseHelper.executeInBackground(() -> {
            List<Food> foods = DatabaseHelper.getAllFoods();

            requireActivity().runOnUiThread(() -> {
                foodAdapter = new FoodAdapter(foods);
                foodsList.setAdapter(foodAdapter);
                foodsList.setLayoutManager(new LinearLayoutManager(requireActivity()));

                if (foods.isEmpty()) {
                    showEmptyListMessage(R.string.this_list_seems_to_be_empty, foodsList);
                    return;
                }
                hideEmptyListMessage();
            });
        });
    }

    private void updateFoodList() {
        DatabaseHelper.executeInBackground(() -> {
            List<Food> foods = DatabaseHelper.getAllFoods();

            requireActivity().runOnUiThread(() -> {
                if(foods.isEmpty()){
                    showEmptyListMessage(R.string.this_list_seems_to_be_empty, foodsList);
                    return;
                }

                hideEmptyListMessage();
                Log.d("ATAOPA", "updateFoodList: ");
                foodAdapter.setFoods(foods);
            });
        });
    }

    private void updateFoodList(String search) {
        DatabaseHelper.executeInBackground(() -> {
            List<Food> foods = DatabaseHelper.getFoodsByName(search);

            requireActivity().runOnUiThread(() -> {
                if(foods.isEmpty()){
                    showEmptyListMessage(R.string.not_found_in_list, foodsList);
                    return;
                }else{
                    hideEmptyListMessage();
                }

                foodAdapter.setFoods(foods);
                hideKeyboard();
            });
        });
    }

    private void hideEmptyListMessage() {
        emptyList.setVisibility(View.GONE);
        foodsList.setVisibility(View.VISIBLE);
    }

    private void showEmptyListMessage(int message, RecyclerView foodsList){
        emptyList.setText(message);
        emptyList.setVisibility(View.VISIBLE);
        foodsList.setVisibility(View.GONE);
    }

    private void hideKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void updateDishList(){
        // Do not touch
    }

    private void setTabNavigation(){
        TabLayout tabSelector = getView().findViewById(R.id.tab_selector);

        tabSelector.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedTabPosition = tab.getPosition();
                switch (selectedTabPosition){
                    case 0:
                        updateFoodList();
                        break;
                    case 1:
                        updateDishList();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselected
            }
        });
    }
}