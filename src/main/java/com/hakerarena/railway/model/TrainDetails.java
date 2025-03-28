package com.hakerarena.railway.model;

import lombok.Data;

@Data
public class TrainDetails {
    private String pnr;
    private String trainNumber;
    private String trainName;
    private String bookedFromStation;
    private String departureStation;
    private String arrivalStation;
    private String departureDate;
    private String arrivalDate;
    private String classCode;
    private String className;
}
