package com.example.myfitnessbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.FrameLayout;

import com.example.myfitnessbuddy.fragments.FragmentPainel;

public class MainActivity extends AppCompatActivity {
    FrameLayout fragment_container;
    ImageButton bt_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment_container = findViewById(R.id.fragment_container);
        bt_home = findViewById(R.id.bt_home);

        // Pass the instance to updateFragment
        updateFragment(FragmentPainel.newInstance("abc", "def"));
    }

    private void updateFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(fragment_container.getId(), fragment);
        ft.commit();
    }
}
