package com.example.myfitnessbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.myfitnessbuddy.activities.panel.UserPreferences;
import com.example.myfitnessbuddy.fragments.FragmentDiary;
import com.example.myfitnessbuddy.fragments.FragmentFoods;
import com.example.myfitnessbuddy.fragments.FragmentPanel;
import com.example.myfitnessbuddy.fragments.Navigation;
import com.example.myfitnessbuddy.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout fragmentContainer = findViewById(R.id.fragment_container);

        Navigation.setFragmentContainer(fragmentContainer);
        Navigation.setFragmentManager(getSupportFragmentManager());

        setFragmentNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = UserPreferences.readUserPreferences(this);
        if(user == null) return;
        Log.d("SHARED_PREFERENCES", user.getActivity().name());
        Log.d("SHARED_PREFERENCES", user.getGoal().name());
        Log.d("SHARED_PREFERENCES", String.valueOf(user.getAge()));
        Log.d("SHARED_PREFERENCES", String.valueOf(user.getHeight()));
    }

    private void setFragmentNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.bt_home) {
                Navigation.updateFragment(FragmentPanel.newInstance());
                return true;
            }else if(item.getItemId() == R.id.bt_diary) {
                Navigation.updateFragment(FragmentDiary.newInstance());
                return true;
            }else if(item.getItemId() == R.id.bt_foods){
                Navigation.updateFragment(FragmentFoods.newInstance());
                return true;
            }else{
                return false;
            }
        });
    }
}
