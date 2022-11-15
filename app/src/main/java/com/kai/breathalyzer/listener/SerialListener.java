package com.kai.breathalyzer.listener;

public interface SerialListener {
    void onSerialReceived(String serialHex);
}
