package com.kai.breathalyzer.model;

public class UserHistory {
    String measurements;
    String date;

    public UserHistory(String measurements, String date) {
        this.measurements = measurements;
        this.date = date;
    }

    public String getMeasurements() {
        return measurements;
    }

    public void setMeasurements(String measurements) {
        this.measurements = measurements;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
