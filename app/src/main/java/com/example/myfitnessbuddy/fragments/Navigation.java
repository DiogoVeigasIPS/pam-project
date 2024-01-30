package com.example.myfitnessbuddy.fragments;

import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myfitnessbuddy.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Navigation {
    private static FrameLayout fragmentContainer;
    private static FragmentManager fragmentManager;

    public static void setFragmentManager(FragmentManager manager) {
        Navigation.fragmentManager = manager;
    }

    public static FrameLayout getFragmentContainer() {
        return fragmentContainer;
    }

    public static void setFragmentContainer(FrameLayout fragmentContainer) {
        Navigation.fragmentContainer = fragmentContainer;
    }

    public static void updateFragment(Fragment fragment) {
        if (fragmentManager == null) {
            throw new IllegalStateException("FragmentManager is not set. Call setFragmentManager() first.");
        }

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(fragmentContainer.getId(), fragment);
        ft.commit();
    }

    public static void setFragmentNavigation(BottomNavigationView bottomNavigationView){
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
