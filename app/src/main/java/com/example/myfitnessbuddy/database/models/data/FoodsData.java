package com.example.myfitnessbuddy.database.models.data;

import android.content.Context;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.database.models.Food;

public class FoodsData {
    public static Food[] populateFoodsData(Context context){
        return new Food[] {
                new Food(context.getString(R.string.chicken_breast), context.getString(R.string.grilled), R.drawable.steak, 100, 165, "g"),
                new Food(context.getString(R.string.salmon), context.getString(R.string.baked), R.drawable.steak, 100, 206, "g"),
                new Food(context.getString(R.string.brocollis), context.getString(R.string.steamed), R.drawable.apple, 100, 55, "g"),
                new Food(context.getString(R.string.rice), context.getString(R.string.white), R.drawable.apple, 100, 120, "g"),
                new Food(context.getString(R.string.sweet_potato), context.getString(R.string.baked), R.drawable.cookie, 100, 90, "g")
        };
    }
}
