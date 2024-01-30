package com.example.myfitnessbuddy.activities.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

        Bundle extras = getIntent().getExtras();

        if(extras == null) return;
        int stringResource = extras.getInt(TITLE);

        setActivityTitle(stringResource);
        setQuickAddNavigation(stringResource);
    }

    private void setQuickAddNavigation(int stringResource) {
        ((Button)findViewById(R.id.bt_quick_addition)).setOnClickListener(v -> {
            Intent intent = new Intent(AddToMealActivity.this, QuickAddActivity.class);
            intent.putExtra(TITLE, stringResource);
            startActivity(intent);
        });
    }

    private void setActivityTitle(int stringResource) {
        TextView title = findViewById(R.id.title);
        title.setText(stringResource);
    }
}