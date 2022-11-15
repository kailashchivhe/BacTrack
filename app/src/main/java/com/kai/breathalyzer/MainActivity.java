package com.kai.breathalyzer;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kai.breathalyzer.util.ApplicationPermission;
import com.kai.breathalyzer.util.BacTrackSingleton;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "BacTrack";
    private ApplicationPermission applicationPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyBluetoothPermissions();
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