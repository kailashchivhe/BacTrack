package com.kai.breathalyzer;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kai.breathalyzer.util.ApplicationPermission;

import BACtrackAPI.API.BACtrackAPI;
import BACtrackAPI.API.BACtrackAPICallbacks;
import BACtrackAPI.Constants.BACTrackDeviceType;
import BACtrackAPI.Constants.BACtrackUnit;
import BACtrackAPI.Exceptions.BluetoothLENotSupportedException;
import BACtrackAPI.Exceptions.BluetoothNotEnabledException;
import BACtrackAPI.Exceptions.LocationServicesNotEnabledException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "BacTrack";
    private ApplicationPermission applicationPermission;

    private final BACtrackAPICallbacks mCallbacks = new BACtrackAPICallbacks() {

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
        }

        @Override
        public void BACtrackDidConnect(String s) {
            Log.d(TAG, "BACtrackDidConnect: ");
        }

        @Override
        public void BACtrackDisconnected() {
            Log.d(TAG, "BACtrackDisconnected: ");
        }

        @Override
        public void BACtrackConnectionTimeout() {
            Log.d(TAG, "BACtrackConnectionTimeout: ");
        }

        @Override
        public void BACtrackFoundBreathalyzer(BACtrackAPI.BACtrackDevice device) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Log.d(TAG, "BACtrackFoundBreathalyzer: " + device.device.getName());
        }

        @Override
        public void BACtrackCountdown(int currentCountdownCount) {
            Log.d(TAG, "BACtrackCountdown: "+ currentCountdownCount);
        }

        @Override
        public void BACtrackStart() {
            Log.d(TAG, "BACtrackStart: ");
        }

        @Override
        public void BACtrackBlow(float breathVolumeRemaining) {
            Log.d(TAG, "BACtrackBlow: "+breathVolumeRemaining);
        }

        @Override
        public void BACtrackAnalyzing() {
            Log.d(TAG, "BACtrackAnalyzing: ");
        }

        @Override
        public void BACtrackResults(float measuredBac) {
            Log.d(TAG, "BACtrackResults: "+measuredBac);
        }

        @Override
        public void BACtrackFirmwareVersion(String version) {
            Log.d(TAG, "BACtrackFirmwareVersion: "+ version);
        }

        @Override
        public void BACtrackSerial(String serialHex) {
            Log.d(TAG, "BACtrackSerial: "+serialHex);
        }

        @Override
        public void BACtrackUseCount(int useCount) {
            Log.d(TAG, "BACtrackUseCount: "+useCount);
        }

        @Override
        public void BACtrackBatteryVoltage(float voltage) {
            Log.d(TAG, "BACtrackBatteryVoltage: "+voltage);
        }

        @Override
        public void BACtrackBatteryLevel(int level) {
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyBluetoothPermissions();
        try {
            BACtrackAPI mAPI = new BACtrackAPI( getApplicationContext(), mCallbacks, "29b31174a6d44a3d96ad3077a3e7c5");
        } catch (BluetoothLENotSupportedException | BluetoothNotEnabledException | LocationServicesNotEnabledException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            if (applicationPermission != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    applicationPermission.verifyPermissions();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (applicationPermission != null) {
            applicationPermission.onRequestPermissionResult(requestCode, permissions, grantResults);
        }
    }

    private void verifyBluetoothPermissions() {
        applicationPermission = new ApplicationPermission( this );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            applicationPermission.verifyPermissions();
        }
    }
}