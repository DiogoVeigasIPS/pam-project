package com.example.myfitnessbuddy.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.myfitnessbuddy.activities.foods.AddFoodActivity;
import com.example.myfitnessbuddy.database.DatabaseHelper;
import com.example.myfitnessbuddy.database.models.Day;
import com.example.myfitnessbuddy.database.models.Food;
import com.example.myfitnessbuddy.database.models.Meal;
import com.example.myfitnessbuddy.database.models.QuantifiedFood;
import com.example.myfitnessbuddy.main_fragments.FragmentPanel;

import org.w3c.dom.Text;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<Food> foods;
    private ActionType actionType;
    private int mealId;

    public FoodAdapter(List<Food> foods, ActionType actionType, int mealId) {
        this.foods = foods;
        this.actionType = actionType;
        this.mealId = mealId;
    }

    public FoodAdapter(List<Food> foods) {
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
        Food food = foods.get(position);
        holder.setValues(food);
        ImageButton actionButton = holder.itemView.findViewById(R.id.bt_action);

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
            QuantityDialogFragment quantityDialogFragment = new QuantityDialogFragment(mealId, food);
            FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
            quantityDialogFragment.show(fragmentManager, "QuantityDialogFragmentTag");
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public void setFoods(List<Food> newFoods) {
        this.foods = newFoods;
        notifyDataSetChanged();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        private TextView foodName, foodDescription;
        private ImageView foodImage;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodDescription = itemView.findViewById(R.id.food_description);
            foodImage = itemView.findViewById(R.id.food_image);
        }

        public void setValues(Food food){
            this.foodName.setText(food.getCompoundName());
            this.foodDescription.setText(food.getDetailsLabel());
            this.foodImage.setImageResource(food.getIcon());
        }
    }

    public static class QuantityDialogFragment extends DialogFragment {
        private final int mealId;
        private final Food food;

        public QuantityDialogFragment(int mealId, Food food){
            this.mealId = mealId;
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

            builder.setView(dialogView)
                    .setPositiveButton(R.string.update, null) // Set a null click listener initially
                    .setNegativeButton(R.string.cancel, (dialog, id) -> FoodAdapter.QuantityDialogFragment.this.getDialog().cancel());

            AlertDialog dialog = builder.create();

            // Override the onShow method to customize the positive button's behavior
            dialog.setOnShowListener(dialogInterface -> {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(view -> {
                    // Your custom logic here
                    EditText quantityInput = dialogView.findViewById(R.id.quantity_input);
                    String quantityStr = quantityInput.getText().toString();

                    if(quantityStr.trim().equals("")){
                        Toast.makeText(getActivity(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        double quantity = Double.parseDouble(quantityStr);
                        DatabaseHelper.executeInBackground(() -> {
                            QuantifiedFood quantifiedFood = new QuantifiedFood(quantity, food);
                            quantifiedFood.setMealId(mealId);

                            DatabaseHelper.QuantifiedFoodHelper.addNewQuantifiedFood(quantifiedFood);

                            requireActivity().runOnUiThread(() -> {
                                if(isAdded()){
                                    Toast.makeText(getActivity(), R.string.food_added, Toast.LENGTH_SHORT).show();
                                    ((AddToMealActivity) getActivity()).updateMealData();
                                    dialog.dismiss();
                                }
                            });
                        });

                    } catch (NumberFormatException numberFormatException){
                        Log.e("QuantityDialogFragment", "Error parsing quantity", numberFormatException);
                        Toast.makeText(getActivity(), R.string.invalid_quantity_format, Toast.LENGTH_SHORT).show();
                    }
                });
            });

            return dialog;
        }
    }
}