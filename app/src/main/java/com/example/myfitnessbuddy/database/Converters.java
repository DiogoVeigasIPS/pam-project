package com.example.myfitnessbuddy.database;

import androidx.room.TypeConverter;

import com.example.myfitnessbuddy.models.enums.MealType;

import java.time.LocalDate;

public class Converters {
    @TypeConverter
    public static LocalDate fromTimestamp(Long value) {
        return value == null ? null : LocalDate.ofEpochDay(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(LocalDate date) {
        return date == null ? null : date.toEpochDay();
    }

    @TypeConverter
    public static MealType fromString(String value) {
        return value == null ? null : MealType.valueOf(value);
    }

    @TypeConverter
    public static String mealTypeToString(MealType mealType) {
        return mealType == null ? null : mealType.name();
    }
}
