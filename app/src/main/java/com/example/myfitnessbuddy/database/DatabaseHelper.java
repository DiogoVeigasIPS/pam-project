package com.example.myfitnessbuddy.database;

import android.content.Context;

import com.example.myfitnessbuddy.database.models.Day;
import com.example.myfitnessbuddy.database.models.Food;
import com.example.myfitnessbuddy.database.models.Meal;
import com.example.myfitnessbuddy.database.models.QuantifiedFood;
import com.example.myfitnessbuddy.database.models.QuickAddition;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseHelper {

    private static AppDatabase appDatabase;
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public static void init(Context context) {
        appDatabase = AppDatabase.getInstance(context);
    }

    public static void executeInBackground(Runnable runnable) {
        executorService.execute(runnable);
    }

    // Food-related methods
    public static class FoodHelper {
        public static List<Food> getAllFoods() {
            return appDatabase.foodDao().getAll();
        }

        public static void addNewFood(Food food) {
            executorService.execute(() -> appDatabase.foodDao().insert(food));
        }

        public static void addNewFoods(Food[] foods) {
            executorService.execute(() -> appDatabase.foodDao().insertAll(foods));
        }

        public static Food getFoodById(int foodId) {
            return appDatabase.foodDao().findById(foodId);
        }

        public static void updateFood(Food food) {
            executorService.execute(() -> appDatabase.foodDao().update(food));
        }

        public static List<Food> getFoodsByName(String searchQuery) {
            return appDatabase.foodDao().searchByName("%" + searchQuery + "%");
        }

        public static void deleteFood(Food food) {
            executorService.execute(() -> appDatabase.foodDao().delete(food));
        }
    }

    // Day-related methods
    public static class DayHelper {
        public static List<Day> getAllDays() {
            return appDatabase.dayDao().getAll();
        }

        public static Day getDayById(int dayId) {
            return appDatabase.dayDao().findById(dayId);
        }

        public static List<Meal> getMealsForDay(int dayId) {
            return appDatabase.dayDao().getMealsForDay(dayId);
        }

        public static Day getToday() {
            return appDatabase.dayDao().getToday(LocalDate.now());
        }

        public static Day getYesterday() {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            return appDatabase.dayDao().getToday(yesterday);
        }

        public static List<Integer> getCaloriesList(int dayId) {
            return appDatabase.dayDao().getCaloriesList(dayId);
        }

        public static int getTotalCalories(int dayId) {
            return appDatabase.dayDao().getTotalCalories(dayId);
        }

        public static int getLastMonthAverageCalories(){
            return appDatabase.dayDao().getLastMonthAverageWeight();
        }

        public static int getLastWeight(){
            Integer weight = appDatabase.dayDao().getLastWeight();

            return weight != null ? (int) weight : 0;
        }

        public static int getDayCount(){
            return appDatabase.dayDao().getDayCount();
        }

        public static long addNewDay(Day day) {
            long insertedId = appDatabase.dayDao().insert(day);
            return insertedId;
        }

        public static void updateDay(Day day) {
            executorService.execute(() -> appDatabase.dayDao().update(day));
        }

        public static void deleteDay(Day day) {
            executorService.execute(() -> appDatabase.dayDao().delete(day));
        }
    }

    // Meal-related methods
    public static class MealHelper {
        public static List<Meal> getAllMeals() {
            return appDatabase.mealDao().getAll();
        }

        public static Meal getMealById(int mealId) {
            return appDatabase.mealDao().findById(mealId);
        }

        public static int getCalories(int mealId) {
            return appDatabase.mealDao().getCalories(mealId);
        }

        public static void addNewMeal(Meal meal) {
            executorService.execute(() -> appDatabase.mealDao().insert(meal));
        }

        public static void addNewMeals(List<Meal> meals) {
            if (meals.isEmpty()) {
                return; // No meals to insert
            }

            Meal[] mealArray = meals.toArray(new Meal[0]);
            executorService.execute(() -> appDatabase.mealDao().insert(mealArray));
        }

        public static void updateMeal(Meal meal) {
            executorService.execute(() -> appDatabase.mealDao().update(meal));
        }

        public static void deleteMeal(Meal meal) {
            executorService.execute(() -> appDatabase.mealDao().delete(meal));
        }
    }

    // QuickAddition-related methods
    public static class QuickAdditionHelper {
        public static List<QuickAddition> getAllQuickAdditions() {
            return appDatabase.quickAdditionDao().getAll();
        }

        public static QuickAddition getQuickAdditionById(int quickAdditionId) {
            return appDatabase.quickAdditionDao().findById(quickAdditionId);
        }

        public static void addNewQuickAddition(QuickAddition quickAddition) {
            executorService.execute(() -> appDatabase.quickAdditionDao().insert(quickAddition));
        }

        public static void updateQuickAddition(QuickAddition quickAddition) {
            executorService.execute(() -> appDatabase.quickAdditionDao().update(quickAddition));
        }

        public static void deleteQuickAddition(QuickAddition quickAddition) {
            executorService.execute(() -> appDatabase.quickAdditionDao().delete(quickAddition));
        }
    }

    // QuantifiedFood-related methods
    public static class QuantifiedFoodHelper {
        public static List<QuantifiedFood> getAllQuantifiedFoods() {
            return appDatabase.quantifiedFoodDao().getAll();
        }

        public static QuantifiedFood getQuantifiedFoodById(int quickAdditionId) {
            return appDatabase.quantifiedFoodDao().findById(quickAdditionId);
        }

        public static void addNewQuantifiedFood(QuantifiedFood quantifiedFood) {
            executorService.execute(() -> appDatabase.quantifiedFoodDao().insert(quantifiedFood));
        }

        public static void updateQuantifiedFood(QuantifiedFood quantifiedFood) {
            executorService.execute(() -> appDatabase.quantifiedFoodDao().update(quantifiedFood));
        }

        public static void deleteQuantifiedFood(QuantifiedFood quantifiedFood) {
            executorService.execute(() -> appDatabase.quantifiedFoodDao().delete(quantifiedFood));
        }
    }
}
