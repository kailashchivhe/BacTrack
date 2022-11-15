package com.kai.breathalyzer.ui.test;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.kai.breathalyzer.listener.BatteryListener;
import com.kai.breathalyzer.listener.ConnectionListener;
import com.kai.breathalyzer.listener.SerialListener;
import com.kai.breathalyzer.listener.TestListener;
import com.kai.breathalyzer.listener.UseCountListener;
import com.kai.breathalyzer.util.BacTrackSingleton;

public class TestViewModel extends AndroidViewModel implements BatteryListener, TestListener, SerialListener, UseCountListener, ConnectionListener {
    private BacTrackSingleton bacTrackSingleton;
    private MutableLiveData<Integer> batteryLiveData;
    private MutableLiveData<String> serialLiveData;
    private MutableLiveData<Integer> useCountLiveData;
    private MutableLiveData<Float> resultLiveData;
    private MutableLiveData<Integer> countdownLiveData;
    private MutableLiveData<Float> progressLiveData;
    private MutableLiveData<String> connectionLiveData;

    public TestViewModel(@NonNull Application application) {
        super(application);
        bacTrackSingleton = BacTrackSingleton.getInstance( application );
        batteryLiveData = new MutableLiveData<>();
        serialLiveData = new MutableLiveData<>();
        useCountLiveData = new MutableLiveData<>();
        resultLiveData = new MutableLiveData<>();
        countdownLiveData = new MutableLiveData<>();
        progressLiveData = new MutableLiveData<>();
        connectionLiveData = new MutableLiveData<>();
    }


    public void getBatteryPercentage() {
        bacTrackSingleton.getBatteryPercentage(this);
    }

    public void getSerialNumber() {
        bacTrackSingleton.getSerialNumber( this );
    }

    public void getUseCount() {
        bacTrackSingleton.getUseCounts( this );
    }

    public void startTest() {
        bacTrackSingleton.startTest( this );
    }

    public void disconnect(){
        bacTrackSingleton.disconnect( this );
    }

    @Override
    public void onBatteryLevelReceived(int level) {
        batteryLiveData.postValue(level);
    }

    @Override
    public void onSerialReceived(String serialHex) {
        serialLiveData.postValue(serialHex);
    }

    @Override
    public void onResultReceived(float measuredBac) {
        resultLiveData.postValue( measuredBac );
    }

    @Override
    public void blowProgress(float breathVolumeRemaining) {
        progressLiveData.postValue(breathVolumeRemaining);
    }

    @Override
    public void testCountdown(int currentCountdownCount) {
        countdownLiveData.postValue( currentCountdownCount );
    }

    @Override
    public void onUseCountReceived(int useCount) {
        useCountLiveData.postValue( useCount );
    }

    @Override
    public void connectionSuccess() {
        Log.d("TestViewModel", "connectionSuccess: ");
    }

    @Override
    public void connectionFailure(String message) {
        connectionLiveData.postValue(message);
    }

    public MutableLiveData<Integer> getBatteryLiveData() {
        return batteryLiveData;
    }

    public MutableLiveData<String> getSerialLiveData() {
        return serialLiveData;
    }

    public MutableLiveData<Integer> getUseCountLiveData() {
        return useCountLiveData;
    }

    public MutableLiveData<Float> getResultLiveData() {
        return resultLiveData;
    }

    public MutableLiveData<Integer> getCountdownLiveData() {
        return countdownLiveData;
    }

    public MutableLiveData<Float> getProgressLiveData() {
        return progressLiveData;
    }

    public MutableLiveData<String> getConnectionLiveData() {
        return connectionLiveData;
    }

    public void restrictExtraEvent(){
        bacTrackSingleton.setHomeBackFlag(true);
    }
}
