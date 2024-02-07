package com.example.myfitnessbuddy.database.models.enums;

import android.content.Context;
import com.example.myfitnessbuddy.R;

public enum Goal {
    NOT_DEFINED(R.string.not_defined),
    LOSE(R.string.lose_weight),
    MAINTAIN(R.string.maintain_weight),
    GAIN(R.string.gain_weight);

    private final int textResId;

    Goal(int textResId) {
        this.textResId = textResId;
    }

    public int getTextResId() {
        return textResId;
    }

    public String getText(Context context) {
        return context.getString(textResId);
    }
}
