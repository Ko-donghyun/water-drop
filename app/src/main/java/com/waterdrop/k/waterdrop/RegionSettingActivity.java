package com.waterdrop.k.waterdrop;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

public class RegionSettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.region_setting);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }
    }
}
