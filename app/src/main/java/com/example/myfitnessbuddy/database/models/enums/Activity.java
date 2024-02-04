package com.example.myfitnessbuddy.database.models.enums;

public enum Activity {
    NOT_DEFINED("Not Defined"),
    NOT_ACTIVE("Not Active"),
    SLIGHTLY_ACTIVE("Slightly Active"),
    ACTIVE("Active"),
    VERY_ACTIVE("Very Active");

    private final String text;

    Activity(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}