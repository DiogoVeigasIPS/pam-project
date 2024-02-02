package com.example.myfitnessbuddy.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.foods.AddFoodActivity;
import com.example.myfitnessbuddy.database.models.Food;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<Food> foods;
    private ActionType actionType;

    public FoodAdapter(List<Food> foods, ActionType actionType) {
        this.foods = foods;
        this.actionType = actionType;
    }

    public FoodAdapter(List<Food> foods) {
        this(foods, ActionType.DETAILS);
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
            Toast.makeText(holder.itemView.getContext(), "I am adding", Toast.LENGTH_SHORT).show();
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
}