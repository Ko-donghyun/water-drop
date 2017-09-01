package com.waterdrop.k.waterdrop.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.waterdrop.k.waterdrop.R;

public class CheckListAddDialog extends Dialog {

//    private View.OnClickListener settingThemeClickListener;
    private View.OnClickListener okayClickListener;
    private View.OnClickListener cancelClickListener;
//    private View.OnClickListener settingMyTimeZoneClickListener;
//    private View.OnClickListener settingListViewThemeClickListener;
//    private CompoundButton.OnCheckedChangeListener settingAlarmClickListener;
//    private int editableTime;
//    private boolean alarm;
    private Context context;

    String behaviorLocationGeographValue = "0";
    String behaviorAfterDisasterValue = "0";
    String behaviorLocationBuildingValue = "0";
    String behaviorContentValue = "";
    EditText behaviorContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.check_list_item_add_dialog);

        Spinner behaviorLocationGeograph = (Spinner) findViewById(R.id.behavior_location_geograph);
        behaviorLocationGeograph.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                behaviorLocationGeographValue = Integer.toString(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Spinner behaviorAfterDisaster = (Spinner)findViewById(R.id.behavior_after_disaster);
        behaviorAfterDisaster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                behaviorAfterDisasterValue = Integer.toString(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Spinner behaviorLocationBuilding = (Spinner)findViewById(R.id.behavior_location_building);
        behaviorLocationBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                behaviorLocationBuildingValue = Integer.toString(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        behaviorContent = (EditText) findViewById(R.id.behavior_content);
//        TextView myEditableTime = (TextView) findViewById(R.id.my_editable_time);
//        myEditableTime.setText("(현재 " + Integer.toString(editableTime) + "시)");
//
//        LinearLayout settingTheme = (LinearLayout) findViewById(R.id.setting_theme);
//        LinearLayout settingListViewTheme = (LinearLayout) findViewById(R.id.setting_layout_theme);
//        LinearLayout settingMyTimeZone = (LinearLayout) findViewById(R.id.setting_my_time_zone);
        Button okayButton = (Button) findViewById(R.id.dialog_okay);
        okayButton.setOnClickListener(okayClickListener);
        Button cancelButton = (Button) findViewById(R.id.dialog_cancel);
        cancelButton.setOnClickListener(cancelClickListener);
//        pushSwitch.setOnCheckedChangeListener(settingAlarmClickListener);
    }

    public CheckListAddDialog(Context context, View.OnClickListener okayClickListener, View.OnClickListener cancelClickListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
//        this.editableTime = editableTime;
//        this.alarm = alarm;
//        this.settingThemeClickListener = settingThemeClickListener;
//        this.settingListViewThemeClickListener = settingListViewThemeClickListener;
//        this.settingMyTimeZoneClickListener = settingMyTimeZoneClickListener;
//        this.settingAlarmClickListener = settingAlarmClickListener;
        this.okayClickListener = okayClickListener;
        this.cancelClickListener = cancelClickListener;
    }

    public String getBehaviorLocationGeographValue() {
        return behaviorLocationGeographValue;
    }
    public String getBehaviorAfterDisasterValue() {
        return behaviorAfterDisasterValue;
    }
    public String getBehaviorLocationBuildingValue() {
        return behaviorLocationBuildingValue;
    }
    public String getBehaviorContentValue() {
        behaviorContentValue = behaviorContent.getText().toString();
        return behaviorContentValue;
    }
}