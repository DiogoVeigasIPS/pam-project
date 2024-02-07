package com.example.myfitnessbuddy.database.models.enums;

import android.content.Context;
import com.example.myfitnessbuddy.R;

public enum Activity {
    NOT_DEFINED(R.string.not_defined),
    NOT_ACTIVE(R.string.not_active),
    SLIGHTLY_ACTIVE(R.string.slightly_active),
    ACTIVE(R.string.active),
    VERY_ACTIVE(R.string.very_active);

    private final int textResId;

    Activity(int textResId) {
        this.textResId = textResId;
    }

    public int getTextResId() {
        return textResId;
    }

    public String getText(Context context) {
        return context.getString(textResId);
    }
}
