package com.example.myfitnessbuddy.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.AddFoodActivity;
import com.example.myfitnessbuddy.models.Food;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<Food> foods;

    public FoodAdapter(List<Food> foods) {
        this.foods = foods;
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
        holder.itemView.findViewById(R.id.bt_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                Intent intent = new Intent(context, AddFoodActivity.class);

                intent.putExtra(AddFoodActivity.FOOD_ID, food.getId());
                context.startActivity(intent);
            }
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