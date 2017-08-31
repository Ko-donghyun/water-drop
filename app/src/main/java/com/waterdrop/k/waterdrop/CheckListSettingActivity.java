package com.waterdrop.k.waterdrop;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.waterdrop.k.waterdrop.DataBase.CheckList;
import com.waterdrop.k.waterdrop.Dialog.CheckListAddDialog;
import com.waterdrop.k.waterdrop.ListViewAdapter.CheckListViewAdapter;
import com.waterdrop.k.waterdrop.SpinnerAdapter.MyCheckListSpinnerAdapter;

public class CheckListSettingActivity extends Activity {

    public static SharedPreferences lastSelectedCheckListInventoryIdPreference;
    public static SharedPreferences.Editor lastSelectedCheckListInventoryIdEditor;
    Long lastSelectedCheckListInventoryId;

    TextView checkListItemAddButton;
    CheckListAddDialog checkListAddDialog;

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
        setContentView(R.layout.check_list_setting);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }
        lastSelectedCheckListInventoryIdPreference = getSharedPreferences("lastSelectedCheckListInventoryId", Activity.MODE_PRIVATE);
        lastSelectedCheckListInventoryId = lastSelectedCheckListInventoryIdPreference.getLong("lastSelectedCheckListInventoryId", 1);


//        lastGetInTimeEditor = lastGetInTimePreference.edit();
//        lastGetInTimeEditor.putLong("lastGetInTime", currentTime);
//        lastGetInTimeEditor.apply();

        checkListItemAddButton = (TextView) findViewById(R.id.check_list_item_add_button);
        myCheckListDataBase = new CheckList(this, myCheckListDataBaseName, null, myCheckListDataBaseVersion);

        checkListViewAdapter = new CheckListViewAdapter();
        myCheckListSpinnerAdapter = new MyCheckListSpinnerAdapter();

        myCheckListView = (ListView) findViewById(R.id.my_check_list_view);
        myCheckListSpinner = (Spinner)findViewById(R.id.my_check_list_spinner);

        getMyCheckListSpinnerData();
        getMyCheckList(lastSelectedCheckListInventoryId);


        myCheckListSpinner.setAdapter(myCheckListSpinnerAdapter);
        myCheckListView.setAdapter(checkListViewAdapter);

        checkListItemAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkListAddDialog = new CheckListAddDialog(CheckListSettingActivity.this, okayClickListener);
                checkListAddDialog.show();
            }
        });

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
                Toast.makeText(CheckListSettingActivity.this, Long.toString(lastSelectedCheckListInventoryId), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private View.OnClickListener okayClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            checkListAddDialog.dismiss();
        }
    };

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
        checkListViewAdapter = new CheckListViewAdapter();

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
