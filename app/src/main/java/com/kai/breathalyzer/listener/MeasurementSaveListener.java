package com.kai.breathalyzer.listener;


public interface MeasurementSaveListener {
    void measurementSaveSuccessful();
    void measurementSaveFailure(String message);
}
