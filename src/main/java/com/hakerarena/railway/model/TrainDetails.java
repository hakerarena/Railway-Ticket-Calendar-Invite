package com.hakerarena.railway.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainDetails {
    private String pnr;
    private String trainNumber;
    private String fromStation;
    private String toStation;
    private String departureDate;
    private String departureTime;
    private String arrivalDate;
    private String arrivalTime;

    @Override
    public String toString() {
        return "TrainDetails{" +
                "PNR='" + pnr + '\'' +
                ", Train Number='" + trainNumber + '\'' +
                ", From='" + fromStation + '\'' +
                ", To='" + toStation + '\'' +
                ", Departure Time='" + departureTime + '\'' +
                ", Arrival Time='" + arrivalTime + '\'' +
                '}';
    }
}
