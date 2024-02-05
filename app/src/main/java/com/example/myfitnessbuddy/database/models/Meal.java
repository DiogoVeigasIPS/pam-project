package com.example.myfitnessbuddy.database.models;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.myfitnessbuddy.database.Converters;
import com.example.myfitnessbuddy.database.models.enums.MealType;

@Entity(tableName = "meal",
        foreignKeys = @ForeignKey(
                entity = Day.class,
                parentColumns = "dayId",
                childColumns = "dayId",
                onDelete = CASCADE
        ), indices = {@Index("dayId")}
)
@TypeConverters({Converters.class})
public class Meal {
    @PrimaryKey(autoGenerate = true)
    private int mealId;
    @ColumnInfo(name = "type")
    private MealType type;
    @ColumnInfo(name = "dayId")
    private int dayId;

    public Meal(MealType type, int dayId) {
        setType(type);
        setDayId(dayId);
    }

    // Getter methods
    public int getMealId() {
        return mealId;
    }

    public MealType getType() {
        return type;
    }

    public int getDayId() {
        return dayId;
    }

    // Setter methods
    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public void setType(MealType type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        this.type = type;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }
}
