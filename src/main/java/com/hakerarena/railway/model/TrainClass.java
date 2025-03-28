package com.hakerarena.railway.model;

public enum TrainClass {
    FIRST_AC("1A", "FIRST AC"),
    SECOND_AC("2A", "SECOND AC"),
    THIRD_AC("3A", "THIRD AC"),
    CHAIR_CAR("CC", "CHAIR CAR"),
    SLEEPER("SL", "SLEEPER"),
    EXECUTIVE_CHAIR_CAR("EC", "EXECUTIVE CHAIR CAR");

    private final String code;
    private final String name;

    TrainClass(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static TrainClass fromString(String input) {
        for (TrainClass tc : TrainClass.values()) {
            if (input.contains(tc.name) || input.contains(tc.code)) {
                return tc;
            }
        }
        return null; // Return null if no match found
    }
}
