package com.example.myfitnessbuddy.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.diary.AddToMealActivity;
import com.example.myfitnessbuddy.activities.diary.QuickAddActivity;
import com.example.myfitnessbuddy.activities.foods.AddDishActivity;
import com.example.myfitnessbuddy.activities.foods.AddFoodActivity;
import com.example.myfitnessbuddy.activities.foods.AddToDishActivity;
import com.example.myfitnessbuddy.database.DatabaseHelper;
import com.example.myfitnessbuddy.database.models.QuickAddition;
import com.example.myfitnessbuddy.database.models.associatios.DishMealCrossRef;
import com.example.myfitnessbuddy.database.models.associatios.DishWithQuantifiedFoods;
import com.example.myfitnessbuddy.database.models.Food;
import com.example.myfitnessbuddy.database.models.associatios.ListableFood;
import com.example.myfitnessbuddy.database.models.QuantifiedFood;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<ListableFood> foods;
    private ActionType actionType;
    private SearchType searchType;
    private int givenId;
    private int stringResource;

    public FoodAdapter(List<ListableFood> foods, ActionType actionType, int givenId, SearchType searchType) {
        this.foods = foods;
        this.actionType = actionType;
        this.givenId = givenId;
        this.searchType = searchType;
    }

    public FoodAdapter(List<ListableFood> foods, ActionType actionType, int givenId) {
        this(foods, actionType, givenId, SearchType.FOODS);
    }

    public FoodAdapter(List<ListableFood> foods, ActionType actionType, int givenId, int stringResource) {
        this(foods, actionType, givenId, SearchType.FOODS);
        this.stringResource = stringResource;
    }

    public FoodAdapter(List<ListableFood> foods) {
        this(foods, ActionType.DETAILS, -1);
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_food, parent, false);

        return new FoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        ListableFood listableFood = foods.get(position);
        holder.setValues(listableFood);
        // The button
        ImageButton actionButton = holder.itemView.findViewById(R.id.bt_action);

        // To help the user click in the button (he might have not good aim)
        holder.itemView.setOnClickListener(v -> actionButton.performClick());

        if(actionType == ActionType.EDIT_IN_MEAL){
            actionButton.setImageResource(R.drawable.edit);

            if(listableFood instanceof QuickAddition){
                QuickAddition quickAddition = (QuickAddition) listableFood;

                actionButton.setOnClickListener(v -> {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, QuickAddActivity.class);

                    intent.putExtra(QuickAddActivity.QUICK_ADDITION_ID, quickAddition.getId());
                    intent.putExtra(QuickAddActivity.TITLE, stringResource);
                    intent.putExtra(QuickAddActivity.MEAL_ID, givenId);

                    context.startActivity(intent);
                });
            }else if(listableFood instanceof QuantifiedFood){
                QuantifiedFood quantifiedFood = (QuantifiedFood) listableFood;

                actionButton.setOnClickListener(v -> {
                    QuantityDialogFragment quantityDialogFragment = new QuantityDialogFragment(givenId, actionType, quantifiedFood);
                    FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                    quantityDialogFragment.show(fragmentManager, "QuantityDialogFragmentTag");
                });
            }else if(listableFood instanceof DishWithQuantifiedFoods){
                DishWithQuantifiedFoods dishWithQuantifiedFoods = (DishWithQuantifiedFoods) listableFood;

                actionButton.setImageResource(R.drawable.delete);
                actionButton.setOnClickListener(v -> {
                    DatabaseHelper.executeInBackground(() -> {
                        DishMealCrossRef dishMealCrossRef = new DishMealCrossRef(dishWithQuantifiedFoods.getDish().getDishId(), givenId);
                        DatabaseHelper.MealHelper.removeDishFromMeal(dishMealCrossRef);

                        v.post(() -> {
                            Toast.makeText(v.getContext(), R.string.dish_deleted, Toast.LENGTH_SHORT).show();
                            ((AddToMealActivity) v.getContext()).updateFoodList();
                        });
                    });
                });
            }
            return;
        }

        if(listableFood instanceof Food){
            Food food = (Food) listableFood;
            if(this.actionType == ActionType.DETAILS){
                actionButton.setOnClickListener(v -> {
                    Context context = v.getContext();

                    Intent intent = new Intent(context, AddFoodActivity.class);

                    intent.putExtra(AddFoodActivity.FOOD_ID, food.getId());
                    context.startActivity(intent);
                });
                return;
            }

            actionButton.setImageResource(R.drawable.add_black);
            actionButton.setOnClickListener(v -> {
                QuantityDialogFragment quantityDialogFragment = new QuantityDialogFragment(givenId, actionType, food);
                FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                quantityDialogFragment.show(fragmentManager, "QuantityDialogFragmentTag");
            });

        }else if(listableFood instanceof DishWithQuantifiedFoods){
            DishWithQuantifiedFoods dish = (DishWithQuantifiedFoods) listableFood;

            if(this.actionType == ActionType.DETAILS){
                actionButton.setOnClickListener(v -> {
                    Context context = v.getContext();

                    Intent intent = new Intent(context, AddDishActivity.class);

                    intent.putExtra(AddDishActivity.DISH_ID, dish.getDish().getDishId());
                    context.startActivity(intent);
                });
            }else if (this.actionType == ActionType.ADD_TO_MEAL){
                actionButton.setOnClickListener(v -> {
                    DatabaseHelper.executeInBackground(() -> {
                        boolean isDuplicate =  DatabaseHelper.MealHelper.dishIsDuplicateInMeal(givenId, dish.getId());

                        if(isDuplicate){
                            v.post(() -> {
                                Toast.makeText(v.getContext(), R.string.dish_duplicated, Toast.LENGTH_LONG).show();
                            });
                            return;
                        }

                        DatabaseHelper.MealHelper.insertDishInMeal(new DishMealCrossRef(dish.getId(), givenId));
                        v.post(() -> {
                            ((AddToMealActivity) v.getContext()).updateMealData();
                            Toast.makeText(v.getContext(), R.string.dish_added_meal, Toast.LENGTH_SHORT).show();
                        });
                    });
                });
            }
        }else if(listableFood instanceof QuantifiedFood){
            QuantifiedFood quantifiedFood = (QuantifiedFood) listableFood;

            actionButton.setImageResource(R.drawable.edit);
            actionButton.setOnClickListener(v -> {
                QuantityDialogFragment quantityDialogFragment = new QuantityDialogFragment(givenId, actionType, quantifiedFood);
                FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                quantityDialogFragment.show(fragmentManager, "QuantityDialogFragmentTag");
            });
        }
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public void setFoods(List<ListableFood> newFoods) {
        this.foods = newFoods;
        notifyDataSetChanged();
    }

    public void setFoods(List<ListableFood> newFoods, int givenId) {
        setFoods(newFoods);
        this.givenId = givenId;
    }

    public void setFoods(List<ListableFood> newFoods, ActionType actionType) {
        setFoods(newFoods);
        this.actionType = actionType;
    }

    public void setFoods(List<ListableFood> newFoods, SearchType searchType) {
        setFoods(newFoods);
        this.searchType = searchType;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        private final TextView foodName;
        private final TextView foodDescription;
        private final ImageView foodImage;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodDescription = itemView.findViewById(R.id.food_description);
            foodImage = itemView.findViewById(R.id.food_image);
        }

        public void setValues(ListableFood food){
            this.foodName.setText(food.getCompoundName());
            this.foodDescription.setText(food.getDetailsLabel());
            this.foodImage.setImageResource(food.getIcon());
        }
    }

    public static class QuantityDialogFragment extends DialogFragment {
        private final int givenId;
        private final ListableFood food;
        private final ActionType actionType;

        public QuantityDialogFragment(int givenId, ActionType actionType, ListableFood food){
            this.givenId = givenId;
            this.actionType = actionType;
            this.food = food;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_quantity, null);

            TextView unitTextView = dialogView.findViewById(R.id.unit_label);
            unitTextView.setText(food.getUnits());

            int positiveButtonResource = R.string.add;

            if(food instanceof QuantifiedFood){
                EditText quantityInput = dialogView.findViewById(R.id.quantity_input);
                quantityInput.setText(String.valueOf((int)((QuantifiedFood) food).getQuantity()));
                positiveButtonResource = R.string.update;
            }

            builder.setView(dialogView)
                    .setPositiveButton(positiveButtonResource, null) // Set a null click listener initially
                    .setNegativeButton(R.string.cancel, (dialog, id) -> FoodAdapter.QuantityDialogFragment.this.getDialog().cancel());

            if(food instanceof QuantifiedFood)
                builder.setNeutralButton(R.string.remove, null);

            AlertDialog dialog = builder.create();

            // Override the onShow method to customize the positive button's behavior
            dialog.setOnShowListener(dialogInterface -> {
                // Positive button (update or add)
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(view -> {
                    EditText quantityInput = dialogView.findViewById(R.id.quantity_input);
                    String quantityStr = quantityInput.getText().toString();

                    if(quantityStr.trim().equals("")){
                        Toast.makeText(getActivity(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(food instanceof Food){
                        addQuantityWithFood(quantityStr, (Food) food, dialog);
                    } else if (food instanceof QuantifiedFood) {
                        updateQuantityInFood(quantityStr, (QuantifiedFood) food, dialog);
                    }
                });

                // Neutral button (delete)
                Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                neutralButton.setTextColor(getResources().getColor(R.color.danger));
                neutralButton.setOnClickListener(v -> {
                    DatabaseHelper.executeInBackground(() -> {
                        DatabaseHelper.QuantifiedFoodHelper.deleteQuantifiedFood((QuantifiedFood) food);

                        requireActivity().runOnUiThread(() -> {
                            if(isAdded()){
                                Toast.makeText(getActivity(), R.string.food_removed, Toast.LENGTH_SHORT).show();

                                if(actionType == ActionType.EDIT_IN_DISH)
                                    ((AddDishActivity) getActivity()).updateFoodList();
                                else if(actionType == ActionType.ADD_TO_DISH)
                                    ((AddToDishActivity) getActivity()).updateDishData();

                                dialog.dismiss();
                            }
                        });
                    });
                });
            });

            return dialog;
        }

        private void updateQuantityInFood(String quantityStr, QuantifiedFood food, AlertDialog dialog) {
            try {
                double quantity = Double.parseDouble(quantityStr);
                DatabaseHelper.executeInBackground(() -> {
                    food.setQuantity(quantity);

                    DatabaseHelper.QuantifiedFoodHelper.updateQuantifiedFood(food);

                    requireActivity().runOnUiThread(() -> {
                        if(isAdded()){
                            Toast.makeText(getActivity(), R.string.food_updated, Toast.LENGTH_SHORT).show();

                            if(actionType == ActionType.EDIT_IN_DISH)
                                ((AddDishActivity) getActivity()).updateFoodList();
                            if(actionType == ActionType.EDIT_IN_MEAL)
                                ((AddToMealActivity) getActivity()).updateFoodList();

                            dialog.dismiss();
                        }
                    });
                });

            } catch (NumberFormatException numberFormatException){
                Log.e("QuantityDialogFragment", "Error parsing quantity", numberFormatException);
                Toast.makeText(getActivity(), R.string.invalid_quantity_format, Toast.LENGTH_SHORT).show();
            }
        }

        private void addQuantityWithFood(String quantityStr, Food food, AlertDialog dialog) {
            try {
                double quantity = Double.parseDouble(quantityStr);
                DatabaseHelper.executeInBackground(() -> {
                    QuantifiedFood quantifiedFood = new QuantifiedFood(quantity, food);

                    if(actionType == ActionType.ADD_TO_MEAL)
                        quantifiedFood.setMealId(givenId);
                    else if(actionType == ActionType.ADD_TO_DISH)
                        quantifiedFood.setDishId(givenId);

                    DatabaseHelper.QuantifiedFoodHelper.addNewQuantifiedFood(quantifiedFood);

                    requireActivity().runOnUiThread(() -> {
                        if(isAdded()){
                            Toast.makeText(getActivity(), R.string.food_added, Toast.LENGTH_SHORT).show();

                            if(actionType == ActionType.ADD_TO_MEAL)
                                ((AddToMealActivity) getActivity()).updateMealData();
                            else if(actionType == ActionType.ADD_TO_DISH)
                                ((AddToDishActivity) getActivity()).updateDishData();

                            dialog.dismiss();
                        }
                    });
                });

            } catch (NumberFormatException numberFormatException){
                Log.e("QuantityDialogFragment", "Error parsing quantity", numberFormatException);
                Toast.makeText(getActivity(), R.string.invalid_quantity_format, Toast.LENGTH_SHORT).show();
            }
        }
    }
}