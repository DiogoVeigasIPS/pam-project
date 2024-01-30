package com.example.myfitnessbuddy.activities.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myfitnessbuddy.R;

public class QuickAddActivity extends AppCompatActivity {

    private static final String TITLE = "TITLE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add);

        ((ImageButton)findViewById(R.id.bt_back)).setOnClickListener(v -> finish());

        Bundle extras = getIntent().getExtras();

        if(extras == null) return;
        int stringResource = extras.getInt(TITLE);

        setActivityTitle(stringResource);
    }

    private void setActivityTitle(int stringResource) {
        TextView title = findViewById(R.id.title);
        String newTitle = getString(R.string.quick) + " " + getString(stringResource);
        title.setText(newTitle);
    }
}