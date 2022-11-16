package com.kai.breathalyzer.listener;

public interface ProfileUpdateListener {
    void profileUpdateSuccessful();
    void profileUpdateFailure(String message);
}

