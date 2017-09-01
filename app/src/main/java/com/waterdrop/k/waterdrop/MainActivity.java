package com.waterdrop.k.waterdrop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.waterdrop.k.waterdrop.DataBase.CheckList;
import com.waterdrop.k.waterdrop.ListViewAdapter.CheckListViewAdapter;
import com.waterdrop.k.waterdrop.SpinnerAdapter.MyCheckListSpinnerAdapter;

public class MainActivity extends Activity{
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

    /**
     * 체크리스트 페이지
     */
    public static SharedPreferences lastSelectedCheckListInventoryIdPreference;
    public static SharedPreferences.Editor lastSelectedCheckListInventoryIdEditor;
    Long lastSelectedCheckListInventoryId;

    TextView checkListItemAddButton;

    CheckList myCheckListDataBase;
    final String myCheckListDataBaseName = "mychecklist.db";
    final int myCheckListDataBaseVersion = 1;

    CheckListViewAdapter checkListViewAdapter;
    ListView myCheckListView;

    Spinner myCheckListSpinner;
    MyCheckListSpinnerAdapter myCheckListSpinnerAdapter;

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
                checkListPageTab.setBackgroundColor(Color.parseColor("#47baa1"));
                chatBotPageTab.setBackgroundColor(Color.parseColor("#358675"));
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
                chatBotPageTab.setBackgroundColor(Color.parseColor("#47baa1"));
                checkListPageTab.setBackgroundColor(Color.parseColor("#358675"));
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



        /*
         * 체크리스트 페이지
         */
        lastSelectedCheckListInventoryIdPreference = getSharedPreferences("lastSelectedCheckListInventoryId", Activity.MODE_PRIVATE);
        lastSelectedCheckListInventoryId = lastSelectedCheckListInventoryIdPreference.getLong("lastSelectedCheckListInventoryId", 1);


        checkListItemAddButton = (TextView) findViewById(R.id.check_list_item_add_button);
        myCheckListDataBase = new CheckList(this, myCheckListDataBaseName, null, myCheckListDataBaseVersion);

        checkListViewAdapter = new CheckListViewAdapter(MainActivity.this);
        myCheckListSpinnerAdapter = new MyCheckListSpinnerAdapter();

        myCheckListView = (ListView) findViewById(R.id.my_check_list_view);
        myCheckListSpinner = (Spinner)findViewById(R.id.my_check_list_spinner);

        getMyCheckListSpinnerData();
        getMyCheckList(lastSelectedCheckListInventoryId);

        myCheckListSpinner.setAdapter(myCheckListSpinnerAdapter);
        myCheckListView.setAdapter(checkListViewAdapter);

        int spinnerPosition = myCheckListSpinnerAdapter.getIndex(myCheckListSpinner, lastSelectedCheckListInventoryId);
        myCheckListSpinner.setSelection(spinnerPosition);

        myCheckListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lastSelectedCheckListInventoryId = myCheckListSpinnerAdapter.getItem(i).getId();

                lastSelectedCheckListInventoryIdEditor = lastSelectedCheckListInventoryIdPreference.edit();
                lastSelectedCheckListInventoryIdEditor.putLong("lastSelectedCheckListInventoryId", lastSelectedCheckListInventoryId);
                lastSelectedCheckListInventoryIdEditor.apply();
                getMyCheckList(lastSelectedCheckListInventoryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getMyCheckListSpinnerData() {
        SQLiteDatabase sqLiteDatabase = myCheckListDataBase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM checklistinventory", null);

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                myCheckListSpinnerAdapter.addItem(cursor.getInt(0), cursor.getString(1));
            } while (cursor.moveToNext());

            cursor.close();
        }
        sqLiteDatabase.close();
    }

    private void getMyCheckList(long checkListInventoryId) {
        checkListViewAdapter = new CheckListViewAdapter(MainActivity.this);

        SQLiteDatabase sqLiteDatabase = myCheckListDataBase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM checklist WHERE checklistinventory_id = ?", new String[] {Long.toString(checkListInventoryId)});

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                checkListViewAdapter.addItem(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getInt(3));
            } while (cursor.moveToNext());

            cursor.close();
        }
        sqLiteDatabase.close();

        myCheckListView.setAdapter(checkListViewAdapter);
    }


}
