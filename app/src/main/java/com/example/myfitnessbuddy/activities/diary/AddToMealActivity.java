package com.example.myfitnessbuddy.activities.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myfitnessbuddy.R;

public class AddToMealActivity extends AppCompatActivity {

    public static final String TITLE = "TITLE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_meal);

        ImageButton btBack = findViewById(R.id.bt_back);
        btBack.setOnClickListener(v -> finish());

        setTitle();
    }

    private void setTitle() {
        Bundle extras = getIntent().getExtras();
        if(extras == null) return;

        TextView title = findViewById(R.id.title);
        title.setText(extras.getInt(TITLE));
    }
}