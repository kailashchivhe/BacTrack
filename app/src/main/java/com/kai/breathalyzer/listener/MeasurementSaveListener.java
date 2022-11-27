package com.kai.breathalyzer.listener;


public interface MeasurementSaveListener {
    void measurementSaveSuccessfull();
    void measurementSaveFailure(String message);
}
