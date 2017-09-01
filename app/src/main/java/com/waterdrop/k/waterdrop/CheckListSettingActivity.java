package com.waterdrop.k.waterdrop;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.waterdrop.k.waterdrop.DataBase.CheckList;
import com.waterdrop.k.waterdrop.Dialog.CheckListAddDialog;
import com.waterdrop.k.waterdrop.Dialog.CheckListInventoryEditDialog;
import com.waterdrop.k.waterdrop.Dialog.ItemDeleteDialog;
import com.waterdrop.k.waterdrop.ListViewAdapter.CheckListViewAdapter2;
import com.waterdrop.k.waterdrop.ListViewAdapter.TextListViewAdapter;
import com.waterdrop.k.waterdrop.SpinnerAdapter.MyCheckListSpinnerAdapter;

public class CheckListSettingActivity extends Activity {

    public static SharedPreferences lastSelectedCheckListInventoryIdPreference;
    public static SharedPreferences.Editor lastSelectedCheckListInventoryIdEditor;
    Long lastSelectedCheckListInventoryId;

    TextView checkListItemAddButton;
    ImageView checkListInventoryEditButton;

    CheckListAddDialog checkListAddDialog;
    CheckListInventoryEditDialog checkListInventoryEditDialog;

    CheckList myCheckListDataBase;
    final String myCheckListDataBaseName = "mychecklist.db";
    final int myCheckListDataBaseVersion = 1;

    TextListViewAdapter myCheckListViewInventoryAdapter;
    CheckListViewAdapter2 checkListViewAdapter;
    ListView myCheckListView;

    Spinner myCheckListSpinner;
    MyCheckListSpinnerAdapter myCheckListSpinnerAdapter;

    ItemDeleteDialog itemDeleteDialog;
    int deleteItemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_list_setting);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }
        lastSelectedCheckListInventoryIdPreference = getSharedPreferences("lastSelectedCheckListInventoryId", Activity.MODE_PRIVATE);
        lastSelectedCheckListInventoryId = lastSelectedCheckListInventoryIdPreference.getLong("lastSelectedCheckListInventoryId", 1);


        checkListItemAddButton = (TextView) findViewById(R.id.check_list_item_add_button);
        checkListInventoryEditButton = (ImageView) findViewById(R.id.check_list_inventory_edit_button);
        myCheckListDataBase = new CheckList(this, myCheckListDataBaseName, null, myCheckListDataBaseVersion);

        checkListViewAdapter = new CheckListViewAdapter2();
        myCheckListViewInventoryAdapter = new TextListViewAdapter();
        myCheckListSpinnerAdapter = new MyCheckListSpinnerAdapter();

        myCheckListView = (ListView) findViewById(R.id.my_check_list_view);
        myCheckListSpinner = (Spinner)findViewById(R.id.my_check_list_spinner);

        getMyCheckListInventoryData();
        getMyCheckList(lastSelectedCheckListInventoryId);


        myCheckListSpinner.setAdapter(myCheckListSpinnerAdapter);
        myCheckListView.setAdapter(checkListViewAdapter);

        checkListItemAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkListAddDialog = new CheckListAddDialog(CheckListSettingActivity.this, checkListAddDialogOkayClickListener, checkListAddDialogCancelClickListener);
                checkListAddDialog.show();
            }
        });
        checkListInventoryEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkListInventoryEditDialog = new CheckListInventoryEditDialog(CheckListSettingActivity.this, checkListInventoryEditDialogOkayClickListener, checkListInventoryEditDialogCancelClickListener, myCheckListViewInventoryAdapter, checkListInventoryEditDialogListViewClickListener);
                checkListInventoryEditDialog.show();
            }
        });

        int spinnerPosition = myCheckListSpinnerAdapter.getIndex(myCheckListSpinner, lastSelectedCheckListInventoryId);
        myCheckListSpinner.setSelection(spinnerPosition);

        myCheckListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lastSelectedCheckListInventoryId = myCheckListSpinnerAdapter.getItem(i).getId();

                getMyCheckList(lastSelectedCheckListInventoryId);
                Toast.makeText(CheckListSettingActivity.this, Long.toString(lastSelectedCheckListInventoryId), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void getMyCheckListInventoryData() {
        SQLiteDatabase sqLiteDatabase = myCheckListDataBase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM checklistinventory", null);

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                myCheckListSpinnerAdapter.addItem(cursor.getInt(0), cursor.getString(1));
                myCheckListViewInventoryAdapter.addItem(cursor.getInt(0), cursor.getString(1));
            } while (cursor.moveToNext());

            cursor.close();
        }
        sqLiteDatabase.close();
    }

    private void getMyCheckList(long checkListInventoryId) {
        checkListViewAdapter = new CheckListViewAdapter2();

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


    private View.OnClickListener checkListAddDialogOkayClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            checkListAddDialog.dismiss();
        }
    };
    private View.OnClickListener checkListAddDialogCancelClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            checkListAddDialog.dismiss();

        }
    };


    private View.OnClickListener checkListInventoryEditDialogOkayClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (checkListInventoryEditDialog.getInventoryEditText().equals("")) {
                Toast.makeText(CheckListSettingActivity.this, "값이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
            } else {
                long id = saveMyCheckListInventoryItem(checkListInventoryEditDialog.getInventoryEditText());
                myCheckListSpinnerAdapter.addItem(id, checkListInventoryEditDialog.getInventoryEditText());
                myCheckListViewInventoryAdapter.addItem(id, checkListInventoryEditDialog.getInventoryEditText());
                checkListInventoryEditDialog.updateInventoryListViewAdapter(myCheckListViewInventoryAdapter);
                myCheckListSpinnerAdapter.notifyDataSetChanged();
            }
        }
    };

    private long saveMyCheckListInventoryItem(String name) {
        SQLiteDatabase myregionDB = myCheckListDataBase.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);
        long id = myregionDB.insert("checklistinventory", null, values);

        myregionDB.close();
        return id;
    }

    private View.OnClickListener checkListInventoryEditDialogCancelClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            checkListInventoryEditDialog.dismiss();
        }
    };

    private AdapterView.OnItemLongClickListener checkListInventoryEditDialogListViewClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

            deleteItemPosition = i;
            itemDeleteDialog = new ItemDeleteDialog(CheckListSettingActivity.this,
                    myCheckListViewInventoryAdapter.getItem(i).getId(),
                    "'" + myCheckListViewInventoryAdapter.getItem(i).getText() + "'와(과) 관련된 체크리스트 내용 전체가 삭제 됩니다.",
                    dialogDeleteCancelClickListener, dialogDeleteDoneClickListener);
            itemDeleteDialog.show();
            checkListInventoryEditDialog.updateInventoryListViewAdapter(myCheckListViewInventoryAdapter);

            return false;
        }
    };

    private View.OnClickListener dialogDeleteCancelClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            itemDeleteDialog.dismiss();
        }
    };

    private View.OnClickListener dialogDeleteDoneClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            removeMyCheckListViewInventoryItem(itemDeleteDialog.getItemId());
            myCheckListViewInventoryAdapter.removeItem(deleteItemPosition);
            myCheckListSpinnerAdapter.removeItem(deleteItemPosition);
            checkListInventoryEditDialog.updateInventoryListViewAdapter(myCheckListViewInventoryAdapter);
            myCheckListSpinnerAdapter.notifyDataSetChanged();
            itemDeleteDialog.dismiss();
        }
    };
    private void removeMyCheckListViewInventoryItem(long id) {
        SQLiteDatabase checkListInventoryDB = myCheckListDataBase.getWritableDatabase();
        checkListInventoryDB.delete("checklistinventory", "_id=" + id, null);
        checkListInventoryDB.delete("checklist", "checklistinventory_id=" + id, null);

        checkListInventoryDB.close();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:

                Intent intent = new Intent(CheckListSettingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
