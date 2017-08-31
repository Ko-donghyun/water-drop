package com.waterdrop.k.waterdrop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MainActivity extends Activity {
    TextView checkListPageTab;
    TextView chatBotPageTab;
    TextView checkListSetting;
    TextView regionSetting;
    ImageView sideDrawerButton;
    DrawerLayout sideDrawer;
    LinearLayout drawerLayout;
    TextView temp;


    ViewFlipper mainViewFlipper;
    boolean pageTabFlag = true;
    boolean isInDanger = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        mainViewFlipper = (ViewFlipper) findViewById(R.id.main_view_flipper);
        mainViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
        mainViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
        drawerLayout = (LinearLayout) findViewById(R.id.drawer_layout);
        sideDrawer = (DrawerLayout)findViewById(R.id.side_drawer);

        checkListPageTab = (TextView) findViewById(R.id.check_list_page_tab);
        chatBotPageTab = (TextView) findViewById(R.id.chat_bot_page_tab);
        checkListSetting = (TextView) findViewById(R.id.check_list_setting);
        regionSetting = (TextView) findViewById(R.id.region_setting);

        sideDrawerButton = (ImageView) findViewById(R.id.side_drawer_button);
        temp = (TextView) findViewById(R.id.temp);

        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInDanger) {
                    temp.setText("침수 위험이 없습니다.");
                    isInDanger = false;
                } else {
                    temp.setText("침수 위험이 있습니다.");
                    isInDanger = true;
                }
            }
        });

        sideDrawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sideDrawer.openDrawer(drawerLayout);
//                if ( sideDrawer.isDrawerOpen(mainLayout) ) {
//                    sideDrawer.closeDrawer(mainLayout);
//                }
            }
        });

        chatBotPageTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainViewFlipper.getDisplayedChild() == 1 && pageTabFlag) {
                    pageTabFlag = false;
                    mainViewFlipper.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push_right_in));
                    mainViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push_right_out));
                    mainViewFlipper.showPrevious();
                    pageTabFlag = true;
                }
                checkListPageTab.setBackgroundColor(Color.parseColor("#DDDDDD"));
                chatBotPageTab.setBackgroundColor(Color.parseColor("#CCCCCC"));
            }
        });

        checkListPageTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainViewFlipper.getDisplayedChild() == 0 && pageTabFlag) {
                    pageTabFlag = false;
                    mainViewFlipper.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push_left_in));
                    mainViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push_left_out));
                    mainViewFlipper.showNext();
                    pageTabFlag = true;
                }
                chatBotPageTab.setBackgroundColor(Color.parseColor("#DDDDDD"));
                checkListPageTab.setBackgroundColor(Color.parseColor("#CCCCCC"));
            }
        });

        checkListSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, CheckListSettingActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        regionSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, RegionSettingActivity.class);
                startActivity(intent);
//                finish();
            }
        });

    }
}
