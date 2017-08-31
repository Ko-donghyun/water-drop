package com.waterdrop.k.waterdrop.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterdrop.k.waterdrop.R;

public class CheckListAddDialog extends Dialog {

//    private View.OnClickListener settingThemeClickListener;
    private View.OnClickListener okayClickListener;
//    private View.OnClickListener settingMyTimeZoneClickListener;
//    private View.OnClickListener settingListViewThemeClickListener;
//    private CompoundButton.OnCheckedChangeListener settingAlarmClickListener;
//    private int editableTime;
//    private boolean alarm;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.check_list_item_add_dialog);

//        TextView myEditableTime = (TextView) findViewById(R.id.my_editable_time);
//        myEditableTime.setText("(현재 " + Integer.toString(editableTime) + "시)");
//
//        LinearLayout settingTheme = (LinearLayout) findViewById(R.id.setting_theme);
//        LinearLayout settingListViewTheme = (LinearLayout) findViewById(R.id.setting_layout_theme);
//        LinearLayout settingMyTimeZone = (LinearLayout) findViewById(R.id.setting_my_time_zone);
        Button okayButton = (Button) findViewById(R.id.okay_button);
//        SwitchCompat pushSwitch = (SwitchCompat) findViewById(R.id.push_switch);
//        pushSwitch.setChecked(alarm);
//
//        settingMyTimeZone.setOnClickListener(settingMyTimeZoneClickListener);
//        settingTheme.setOnClickListener(settingThemeClickListener);
//        settingListViewTheme.setOnClickListener(settingListViewThemeClickListener);
        okayButton.setOnClickListener(okayClickListener);
//        pushSwitch.setOnCheckedChangeListener(settingAlarmClickListener);
    }

    public CheckListAddDialog(Context context, View.OnClickListener okayClickListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
//        this.editableTime = editableTime;
//        this.alarm = alarm;
//        this.settingThemeClickListener = settingThemeClickListener;
//        this.settingListViewThemeClickListener = settingListViewThemeClickListener;
//        this.settingMyTimeZoneClickListener = settingMyTimeZoneClickListener;
//        this.settingAlarmClickListener = settingAlarmClickListener;
        this.okayClickListener = okayClickListener;
    }
}