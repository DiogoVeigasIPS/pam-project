package com.example.myfitnessbuddy.database;

import android.content.Context;

import com.example.myfitnessbuddy.database.models.Day;
import com.example.myfitnessbuddy.database.models.Dish;
import com.example.myfitnessbuddy.database.models.associatios.AllFoodsInMeal;
import com.example.myfitnessbuddy.database.models.associatios.DishMealCrossRef;
import com.example.myfitnessbuddy.database.models.associatios.DishWithQuantifiedFoods;
import com.example.myfitnessbuddy.database.models.associatios.DishesInMeal;
import com.example.myfitnessbuddy.database.models.Food;
import com.example.myfitnessbuddy.database.models.Meal;
import com.example.myfitnessbuddy.database.models.QuantifiedFood;
import com.example.myfitnessbuddy.database.models.QuickAddition;

import java.time.LocalDate;
import java.util.ArrayList;
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
            List<Integer> mealIds = appDatabase.dayDao().getIdMealsByDay(dayId);

            List<Integer> caloriesList = new ArrayList<>();

            for(Integer mealId : mealIds){
                caloriesList.add(MealHelper.getCalories(mealId));
            }

            return caloriesList;

            //return appDatabase.dayDao().getCaloriesList(dayId);
        }

        public static int getTotalCalories(int dayId) {
            return getCaloriesList(dayId).stream().mapToInt(Integer::intValue).sum();
            //return appDatabase.dayDao().getTotalCalories(dayId);
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
            return appDatabase.dayDao().insert(day);
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
            AllFoodsInMeal allFoodsInMeal = getAllFoodsInMeal(mealId);

            return allFoodsInMeal.getCalorieSum();
            //return appDatabase.mealDao().getCalories(mealId);
        }

        public static AllFoodsInMeal getAllFoodsInMeal(int mealId){
            List<QuantifiedFood> quantifiedFoodList = appDatabase.mealDao().getQuantifiedFoodsInMeal(mealId);
            List<QuickAddition> quickAdditions = appDatabase.mealDao().getQuickAdditionsInMeal(mealId);
            DishesInMeal dishesInMeal = appDatabase.mealDao().getDishesInMeal(mealId);

            List<Integer> dishIds = new ArrayList<>();

            for(Dish dish : dishesInMeal.getDishes()){
                dishIds.add(dish.getDishId());
            }

            List<DishWithQuantifiedFoods> dishWithQuantifiedFoods = appDatabase.mealDao().getDishesWithQuantifiedFoods(dishIds);

            return new AllFoodsInMeal(dishesInMeal.getMeal(), quantifiedFoodList, quickAdditions, dishWithQuantifiedFoods);
        }

        public static List<DishesInMeal> getDishesInMeals(){
            return appDatabase.mealDao().getDishesInMeals();
        }

        public static DishesInMeal getDishesInMeal(int mealId){
            return appDatabase.mealDao().getDishesInMeal(mealId);
        }

        public static void insertDishInMeal(DishMealCrossRef dishMealCrossRef){
            executorService.execute(() -> appDatabase.mealDao().insertDishInMeal(dishMealCrossRef));
        }

        public static void removeDishFromMeal(DishMealCrossRef dishMealCrossRef){
            executorService.execute(() -> appDatabase.mealDao().removeDishFromMeal(dishMealCrossRef));
        }

        public static boolean dishIsDuplicateInMeal(int mealId, int dishId){
            return appDatabase.mealDao().dishIsDuplicateInMeal(mealId, dishId) > 0;
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

    // Dish-related methods
    public static class DishHelper {
        public static List<DishWithQuantifiedFoods> getAllDishes() {
            return appDatabase.dishDao().getAll();
        }

        public static DishWithQuantifiedFoods getDishById(int dishId) {
            return appDatabase.dishDao().findById(dishId);
        }

        public static long addNewDish(Dish dish) {
            return appDatabase.dishDao().insert(dish);
        }

        public static void updateDish(Dish dish) {
            executorService.execute(() -> appDatabase.dishDao().update(dish));
        }

        public static void deleteDish(Dish dish) {
            executorService.execute(() -> appDatabase.dishDao().delete(dish));
        }

        public static List<DishWithQuantifiedFoods> searchDishesByName(String searchQuery) {
            return appDatabase.dishDao().searchByName("%" + searchQuery + "%");
        }
    }
}
