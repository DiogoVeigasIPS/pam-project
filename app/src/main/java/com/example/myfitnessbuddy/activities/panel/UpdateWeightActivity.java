package com.example.myfitnessbuddy.activities.panel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

import com.example.myfitnessbuddy.R;

public class UpdateWeightActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_weight);

        ImageButton btBack = findViewById(R.id.bt_back);
        btBack.setOnClickListener(v -> finish());
    }
}