package com.kai.breathalyzer.listener;

public interface TestListener {
    void onResultReceived(float measuredBac);
    void blowProgress(float breathVolumeRemaining);
    void testCountdown(int currentCountdownCount);
}
