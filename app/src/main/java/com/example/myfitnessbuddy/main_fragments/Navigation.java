package com.example.myfitnessbuddy.main_fragments;

import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myfitnessbuddy.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Navigation {
    private static FrameLayout fragmentContainer;
    private static FragmentManager fragmentManager;
    private static int currentTab;
    private static BottomNavigationView bottomNavigationView;

    public static void setFragmentManager(FragmentManager manager) {
        Navigation.fragmentManager = manager;
    }

    public static FrameLayout getFragmentContainer() {
        return fragmentContainer;
    }

    public static void setFragmentContainer(FrameLayout fragmentContainer) {
        Navigation.fragmentContainer = fragmentContainer;
    }

    public static void setBottomNavigationView(BottomNavigationView bottomNavigationView){
        Navigation.bottomNavigationView = bottomNavigationView;
    }

    public static void updateFragment(Fragment fragment) {
        if (fragmentManager == null) {
            throw new IllegalStateException("FragmentManager is not set. Call setFragmentManager() first.");
        }

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(fragmentContainer.getId(), fragment);
        ft.commit();
    }

    public static void setFragmentNavigation(){
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.bt_home && currentTab != 0) {
                Navigation.updateFragment(FragmentPanel.newInstance());
                currentTab = 0;
                return true;
            }else if(item.getItemId() == R.id.bt_diary && currentTab != 1) {
                Navigation.updateFragment(FragmentDiary.newInstance());
                currentTab = 1;
                return true;
            }else if(item.getItemId() == R.id.bt_foods && currentTab != 2){
                Navigation.updateFragment(FragmentFoods.newInstance());
                currentTab = 2;
                return true;
            }else{
                return false;
            }
        });
    }

    public static void navigateToPanel() {
        currentTab = 0;
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.bt_home);
        item.setChecked(true);
        updateFragment(FragmentPanel.newInstance());
    }
}
