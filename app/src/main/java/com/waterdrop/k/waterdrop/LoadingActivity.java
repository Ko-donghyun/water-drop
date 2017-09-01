package com.waterdrop.k.waterdrop;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LoadingActivity extends Activity {
    Intent targetIntent;

    public static SharedPreferences firstTimePreference;
    public static SharedPreferences.Editor firstTimeEditor;

    public static SharedPreferences positionLongitudePreference;
    public static SharedPreferences.Editor positionLongitudePreferenceEditor;
    public static SharedPreferences positionLatitudePreference;
    public static SharedPreferences.Editor positionLatitudePreferenceEditor;
    int firstTime;

    String longitude;
    String latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }


        firstTimePreference = getSharedPreferences("firstTime", Activity.MODE_PRIVATE);
        firstTime = firstTimePreference.getInt("firstTime", 0);

        positionLongitudePreference = getSharedPreferences("longitude", Activity.MODE_PRIVATE);
        longitude = positionLongitudePreference.getString("longitude", "126.976930");

        positionLatitudePreference = getSharedPreferences("latitude", Activity.MODE_PRIVATE);
        latitude = positionLatitudePreference.getString("latitude", "37.574515");

        if (firstTime == 0) {
            // 토큰 값 받기

            firstTimeEditor = firstTimePreference.edit();
            firstTimeEditor.putInt("firstTime", 1);
            firstTimeEditor.apply();
        }
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        startService(intent);

        startActivityWithDelay();

    }


    public void startActivityWithDelay() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            public void run() {
                targetIntent = new Intent(LoadingActivity.this, MainActivity.class);
                startActivity(targetIntent);
                finish();
            }
        }, 1000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }
}
