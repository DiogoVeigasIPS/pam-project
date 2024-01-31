package com.example.myfitnessbuddy.models.enums;


public enum Goal {
    NOT_DEFINED("Not Defined"),
    LOSE("Lose Weight"),
    MAINTAIN("Maintain Weight"),
    GAIN("Gain Weight");

    private final String text;

    Goal(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

