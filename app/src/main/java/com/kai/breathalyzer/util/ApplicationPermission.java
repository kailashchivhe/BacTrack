package com.kai.breathalyzer.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

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
}
