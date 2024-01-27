package com.example.myfitnessbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.myfitnessbuddy.fragments.FragmentDiary;
import com.example.myfitnessbuddy.fragments.FragmentFoods;
import com.example.myfitnessbuddy.fragments.FragmentPanel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentContainer = findViewById(R.id.fragment_container);

        setFragmentNavigation();
    }

    private void updateFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(fragmentContainer.getId(), fragment);
        ft.commit();
    }

    private void setFragmentNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.bt_home) {
                updateFragment(FragmentPanel.newInstance());
                return true;
            }else if(item.getItemId() == R.id.bt_diary) {
                updateFragment(FragmentDiary.newInstance());
                return true;
            }else if(item.getItemId() == R.id.bt_foods){
                updateFragment(FragmentFoods.newInstance());
                return true;
            }else{
                return false;
            }
        });
    }
}
