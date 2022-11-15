package com.kai.breathalyzer.listener;

public interface ConnectionListener {
    void connectionSuccess();
    void connectionFailure(String message);
}
