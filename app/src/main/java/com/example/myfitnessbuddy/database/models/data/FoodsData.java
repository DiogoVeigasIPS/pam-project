package com.example.myfitnessbuddy.database.models.data;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.database.models.Food;

public class FoodsData {
    public static Food[] populateFoodsData(){
        return new Food[] {
                new Food("Chicken Breast", "Grilled", R.drawable.steak, 100, 165, "g"),
                new Food("Salmon", "Baked", R.drawable.steak, 100, 206, "g"),
                new Food("Broccoli", "Steamed", R.drawable.apple, 100, 55, "g"),
                new Food("Quinoa", "Cooked", R.drawable.cookie, 100, 120, "g"),
                new Food("Sweet Potato", "Baked", R.drawable.cookie, 100, 90, "g"),
                new Food("Spinach", "Raw", R.drawable.apple, 100, 23, "g"),
                new Food("Egg", "Boiled", R.drawable.cookie, 50, 68, "g"),
                new Food("Oats", "Cooked", R.drawable.cookie, 100, 68, "g"),
                new Food("Greek Yogurt", "Plain", R.drawable.cookie, 150, 100, "g"),
                new Food("Almonds", "Raw", R.drawable.cookie, 30, 160, "g"),
                new Food("Blueberries", "Fresh", R.drawable.apple, 100, 57, "g"),
                new Food("Avocado", "Sliced", R.drawable.apple, 100, 160, "g"),
                new Food("Cottage Cheese", "Low Fat", R.drawable.cookie, 100, 72, "g"),
                new Food("Brown Rice", "Cooked", R.drawable.cookie, 100, 111, "g"),
                new Food("Turkey", "Ground", R.drawable.steak, 100, 165, "g"),
                new Food("Green Beans", "Steamed", R.drawable.apple, 100, 31, "g"),
                new Food("Apple", "Sliced", R.drawable.apple, 100, 52, "g"),
                new Food("Cauliflower", "Roasted", R.drawable.apple, 100, 25, "g"),
                new Food("Chickpeas", "Canned", R.drawable.cookie, 100, 164, "g"),
                new Food("Cucumber", "Sliced", R.drawable.apple, 100, 16, "g")
        };

    }
}
