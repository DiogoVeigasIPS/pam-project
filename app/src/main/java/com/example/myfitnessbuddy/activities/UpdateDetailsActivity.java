package com.example.myfitnessbuddy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

import com.example.myfitnessbuddy.R;

public class UpdateDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        ImageButton btBack = findViewById(R.id.bt_back);
        btBack.setOnClickListener(v -> finish());
    }
}