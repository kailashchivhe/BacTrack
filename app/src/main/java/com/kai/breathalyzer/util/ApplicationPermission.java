package com.kai.breathalyzer.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;


public class ApplicationPermission {
    public static final int REQUEST_CODE_LOCATION_PERMISSIONS = 1002;

    private Activity mActivity;

    public ApplicationPermission(Activity activity) {
        mActivity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    public void verifyPermissions() {
        if (!locationPermissionsEnabled()) {
            ActivityCompat.requestPermissions(mActivity, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN}, REQUEST_CODE_LOCATION_PERMISSIONS);
        }
        if(!checkLocationPermissions()){
            showLocationServicesAlert();
        }
        BacTrackSingleton.getInstance( mActivity.getApplicationContext() ).initBacTrackSDK();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private boolean locationPermissionsEnabled() {
        return ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mActivity, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mActivity, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mActivity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mActivity, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults){
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    verifyPermissions();
                }
            }
        }
    }

    private final DialogInterface.OnClickListener cancelClickListener = (dialog, which) -> dialog.cancel();

    private void showLocationServicesAlert() {
        new AlertDialog.Builder(mActivity, android.R.style.Theme_Material_Light_Dialog_Alert)
                .setMessage("This app requires Location Services to run. Would you like to enable Location Services now?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mActivity.startActivity(intent);
                    }
                })
                .setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) cancelClickListener)
                .create()
                .show();
    }

    private boolean checkLocationPermissions(){
        LocationManager locationManager = (LocationManager)mActivity.getSystemService(Context.LOCATION_SERVICE);
        boolean bGPSEnabled = false;
        boolean bNetworkEnabled = false;

        try {
            bGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            bNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        return bGPSEnabled && bNetworkEnabled;
    }
}
