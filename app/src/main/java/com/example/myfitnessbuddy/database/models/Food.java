package com.example.myfitnessbuddy.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.DecimalFormat;

@Entity(tableName = "food")
public class Food{
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "icon")
    private int icon;
    @ColumnInfo(name = "portionSize")
    private double portionSize;
    @ColumnInfo(name = "caloriesPerPortion")
    private double caloriesPerPortion;
    @ColumnInfo(name = "units")
    private String units;

    public Food(String name, String description, int icon, double portionSize, double caloriesPerPortion, String units) {
        setName(name);
        setDescription(description);
        setIcon(icon);
        setPortionSize(portionSize);
        setCaloriesPerPortion(caloriesPerPortion);
        setUnits(units);
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getIcon() {
        return icon;
    }

    public double getPortionSize() {
        return portionSize;
    }

    public double getCaloriesPerPortion() {
        return caloriesPerPortion;
    }

    public String getUnits() {
        return units;
    }

    // Setter methods
    public void setId(int id){
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setPortionSize(double portionSize) {
        if (portionSize <= 0) {
            throw new IllegalArgumentException("Portion size must be greater than 0");
        }
        this.portionSize = portionSize;
    }

    public void setCaloriesPerPortion(double caloriesPerPortion) {
        if (caloriesPerPortion <= 0) {
            throw new IllegalArgumentException("Calories per portion must be greater than 0");
        }
        this.caloriesPerPortion = caloriesPerPortion;
    }

    public void setUnits(String units) {
        if (units == null || units.trim().isEmpty()) {
            throw new IllegalArgumentException("Units cannot be null or empty");
        }
        this.units = units;
    }

    // Methods
    public double getCaloriesPerPortionUnit(){
        return caloriesPerPortion / portionSize;
    }

    public String getCaloriesLabel(){
        return new DecimalFormat("#").format(caloriesPerPortion) + " kcal";
    }

    public String getPortionLabel(){
        return new DecimalFormat("#").format(portionSize) + " " + units;
    }

    public String getDetailsLabel(){
        return getCaloriesLabel() + " p/ " + getPortionLabel();
    }

    public String getCompoundName(){
        return String.format("%s %s", this.name, this.description);
    }
}

