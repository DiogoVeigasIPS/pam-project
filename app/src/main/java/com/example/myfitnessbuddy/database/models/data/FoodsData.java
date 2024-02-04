package com.example.myfitnessbuddy.database.models.data;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.database.models.Food;

public class FoodsData {
    public static Food[] populateFoodsData(){
        return new Food[] {
                new Food("Chicken Breast", "Grilled", R.drawable.steak, 100, 165, "g"),
                new Food("Salmon", "Baked", R.drawable.steak, 100, 206, "g"),
                new Food("Broccoli", "Steamed", R.drawable.apple, 100, 55, "g"),
                new Food("Rice", "White", R.drawable.apple, 100, 120, "g"),
                new Food("Sweet Potato", "Baked", R.drawable.cookie, 100, 90, "g")
        };
    }
}
