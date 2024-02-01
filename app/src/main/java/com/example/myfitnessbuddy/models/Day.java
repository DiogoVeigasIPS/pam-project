package com.example.myfitnessbuddy.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.myfitnessbuddy.database.Converters;

import java.time.LocalDate;

@Entity(tableName = "day", indices = {@Index("date")})
@TypeConverters(Converters.class)
public class Day {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "date")

    private LocalDate date;
    @ColumnInfo(name = "weight")
    private int weight;
    @ColumnInfo(name = "calorieGoal")
    private int calorieGoal;

    @Ignore
    public Day(LocalDate date){
        this.date = date;
        weight = 0;
        calorieGoal = 0;
    }

    @Ignore
    public Day() {
        this(LocalDate.now());
    }

    public Day(int calorieGoal) {
        this();
        this.calorieGoal = calorieGoal;
    }

    @Ignore
    public Day(int weight, int calorieGoal) {
        this(calorieGoal);
        this.weight = weight;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getWeight() {
        return weight;
    }

    public int getCalorieGoal() {
        return calorieGoal;
    }

    // Setter methods
    public void setId(int id){
        this.id = id;
    }

    public void setWeight(int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("Weight must be a non-negative integer");
        }
        this.weight = weight;
    }

    public void setCalorieGoal(int calorieGoal) {
        if (calorieGoal < 0) {
            throw new IllegalArgumentException("Calorie goal must be a non-negative integer");
        }
        this.calorieGoal = calorieGoal;
    }

    public void setDate(LocalDate date){
        if(date == null){
            throw new IllegalArgumentException("Date cannot be null");
        }
        this.date = date;
    }
}
