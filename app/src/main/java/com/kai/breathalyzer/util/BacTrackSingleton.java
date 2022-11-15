package com.kai.breathalyzer.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.kai.breathalyzer.listener.BatteryListener;
import com.kai.breathalyzer.listener.ConnectionListener;
import com.kai.breathalyzer.listener.SerialListener;
import com.kai.breathalyzer.listener.TestListener;
import com.kai.breathalyzer.listener.UseCountListener;

import BACtrackAPI.API.BACtrackAPI;
import BACtrackAPI.API.BACtrackAPICallbacks;
import BACtrackAPI.Constants.BACTrackDeviceType;
import BACtrackAPI.Constants.BACtrackUnit;
import BACtrackAPI.Exceptions.BluetoothLENotSupportedException;
import BACtrackAPI.Exceptions.BluetoothNotEnabledException;
import BACtrackAPI.Exceptions.LocationServicesNotEnabledException;

public class BacTrackSingleton implements BACtrackAPICallbacks{
    private Context mContext;
    private BACtrackAPI mAPI;
    private BatteryListener batteryListener;
    private UseCountListener useCountListener;
    private TestListener testListener;
    private SerialListener serialListener;
    private static final String TAG = "BacTrackSingleton";
    private static final String API_KEY = "29b31174a6d44a3d96ad3077a3e7c5";
    @SuppressLint("StaticFieldLeak")
    private static BacTrackSingleton INSTANCE;
    private ConnectionListener connectionListener;
    private boolean bBackFlag = false;

    private BacTrackSingleton(Context context){
        mContext = context;
    }

    public static BacTrackSingleton getInstance(Context context){
        if( INSTANCE == null ){
            INSTANCE = new BacTrackSingleton(context);
        }
        return INSTANCE;
    }

    public void initBacTrackSDK(){
        try {
            mAPI = new BACtrackAPI( mContext, this, API_KEY);
        } catch (BluetoothLENotSupportedException | BluetoothNotEnabledException | LocationServicesNotEnabledException e) {
            e.printStackTrace();
        }
    }

    public void connectToNearestBreathalyzer(ConnectionListener connectionListener){
        this.connectionListener = connectionListener;
          mAPI.connectToNearestBreathalyzer();
    }

    @Override
    public void BACtrackAPIKeyDeclined(String errorMessage) {
        Log.d(TAG, "BACtrackAPIKeyDeclined: ");
    }

    @Override
    public void BACtrackAPIKeyAuthorized() {
        Log.d(TAG, "BACtrackAPIKeyAuthorized: ");
    }

    @Override
    public void BACtrackConnected(BACTrackDeviceType bacTrackDeviceType) {
        Log.d(TAG, "BACtrackConnected: ");
        if( connectionListener != null ){
            connectionListener.connectionSuccess();
        }
    }

    @Override
    public void BACtrackDidConnect(String s) {
        Log.d(TAG, "BACtrackDidConnect: ");
    }

    @Override
    public void BACtrackDisconnected() {
        Log.d(TAG, "BACtrackDisconnected: ");
        if( connectionListener != null ){
            connectionListener.connectionFailure("Connection Failed");
        }
    }

    @Override
    public void BACtrackConnectionTimeout() {
        Log.d(TAG, "BACtrackConnectionTimeout: ");
    }

    @Override
    public void BACtrackFoundBreathalyzer(BACtrackAPI.BACtrackDevice device) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d(TAG, "BACtrackFoundBreathalyzer: " + device.device.getName());
    }

    @Override
    public void BACtrackCountdown(int currentCountdownCount) {
        testListener.testCountdown(currentCountdownCount);
        Log.d(TAG, "BACtrackCountdown: "+ currentCountdownCount);
    }

    @Override
    public void BACtrackStart() {
        Log.d(TAG, "BACtrackStart: ");
    }

    @Override
    public void BACtrackBlow(float breathVolumeRemaining) {
        testListener.blowProgress(100 - (int)(100.0 * breathVolumeRemaining));
        Log.d(TAG, "BACtrackBlow: "+breathVolumeRemaining);
    }

    @Override
    public void BACtrackAnalyzing() {
        Log.d(TAG, "BACtrackAnalyzing: ");
    }

    @Override
    public void BACtrackResults(float measuredBac) {
        testListener.onResultReceived(measuredBac);
        Log.d(TAG, "BACtrackResults: "+measuredBac);
    }

    @Override
    public void BACtrackFirmwareVersion(String version) {
        Log.d(TAG, "BACtrackFirmwareVersion: "+ version);
    }

    @Override
    public void BACtrackSerial(String serialHex) {
        serialListener.onSerialReceived(serialHex);
        Log.d(TAG, "BACtrackSerial: "+serialHex);
    }

    @Override
    public void BACtrackUseCount(int useCount) {
        useCountListener.onUseCountReceived(useCount);
        Log.d(TAG, "BACtrackUseCount: "+useCount);
    }

    @Override
    public void BACtrackBatteryVoltage(float voltage) {
        Log.d(TAG, "BACtrackBatteryVoltage: "+voltage);
    }

    @Override
    public void BACtrackBatteryLevel(int level) {
        batteryListener.onBatteryLevelReceived(level);
        Log.d(TAG, "BACtrackBatteryLevel: "+level);
    }

    @Override
    public void BACtrackError(int errorCode) {
        Log.d(TAG, "BACtrackError: "+errorCode);
    }

    @Override
    public void BACtrackUnits(BACtrackUnit units) {
        Log.d(TAG, "BACtrackUnits: "+units);
    }

    public void getBatteryPercentage(BatteryListener batteryListener) {
        this.batteryListener = batteryListener;
        mAPI.getBreathalyzerBatteryVoltage();
    }

    public void getSerialNumber(SerialListener serialListener) {
        this.serialListener = serialListener;
        mAPI.getSerialNumber();
    }

    public void getUseCounts(UseCountListener useCountListener){
        this.useCountListener = useCountListener;
        mAPI.getUseCount();
    }

    public void startTest(TestListener testListener){
        this.testListener = testListener;
        mAPI.startCountdown();
    }

    public void disconnect(ConnectionListener connectionListener){
        this.connectionListener = connectionListener;
        mAPI.disconnect();
    }

    public void setHomeBackFlag(boolean b) {
        bBackFlag = true;
    }

    public boolean getHomeBackFlag(){
        return  bBackFlag;
    }
}
