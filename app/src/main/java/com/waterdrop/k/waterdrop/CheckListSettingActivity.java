package com.waterdrop.k.waterdrop;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.waterdrop.k.waterdrop.DataBase.CheckList;
import com.waterdrop.k.waterdrop.Dialog.CheckListAddDialog;
import com.waterdrop.k.waterdrop.Dialog.CheckListInventoryEditDialog;
import com.waterdrop.k.waterdrop.Dialog.ItemDeleteDialog;
import com.waterdrop.k.waterdrop.ListViewAdapter.CheckListViewAdapter;
import com.waterdrop.k.waterdrop.ListViewAdapter.CheckListViewAdapter2;
import com.waterdrop.k.waterdrop.ListViewAdapter.CheckListViewAdapter3;
import com.waterdrop.k.waterdrop.ListViewAdapter.TextListViewAdapter;
import com.waterdrop.k.waterdrop.SpinnerAdapter.MyCheckListSpinnerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Array;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckListSettingActivity extends Activity {
    OkHttpHelper ok = new OkHttpHelper();

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

    CheckListViewAdapter3 serverCheckListViewAdapter;
    ListView serverCheckListView;

    Spinner myCheckListSpinner;
    MyCheckListSpinnerAdapter myCheckListSpinnerAdapter;

    ItemDeleteDialog itemDeleteDialog;
    int deleteItemPosition;
    private Handler mHandler;
    String responseStr;
    Response responseResult;

    String behaviorLocationGeographValue = "0";
    String behaviorAfterDisasterValue = "0";
    String behaviorLocationBuildingValue = "0";
    String behaviorContentValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_list_setting);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }
        // 서버 통신 서버 디비 값 가져오고, 추천 알고리즘

        mHandler = new Handler(Looper.getMainLooper());

        lastSelectedCheckListInventoryIdPreference = getSharedPreferences("lastSelectedCheckListInventoryId", Activity.MODE_PRIVATE);
        lastSelectedCheckListInventoryId = lastSelectedCheckListInventoryIdPreference.getLong("lastSelectedCheckListInventoryId", 1);


        checkListItemAddButton = (TextView) findViewById(R.id.check_list_item_add_button);
        checkListInventoryEditButton = (ImageView) findViewById(R.id.check_list_inventory_edit_button);
        myCheckListDataBase = new CheckList(this, myCheckListDataBaseName, null, myCheckListDataBaseVersion);

        checkListViewAdapter = new CheckListViewAdapter2(true);
        serverCheckListViewAdapter = new CheckListViewAdapter3();
        myCheckListViewInventoryAdapter = new TextListViewAdapter();
        myCheckListSpinnerAdapter = new MyCheckListSpinnerAdapter();

        myCheckListView = (ListView) findViewById(R.id.my_check_list_view);
        serverCheckListView = (ListView) findViewById(R.id.server_check_list_view);
        myCheckListSpinner = (Spinner)findViewById(R.id.my_check_list_spinner);

        getMyCheckListInventoryData();
        getMyCheckList(lastSelectedCheckListInventoryId);
        getServerCheckListData();

        myCheckListSpinner.setAdapter(myCheckListSpinnerAdapter);
        myCheckListView.setAdapter(checkListViewAdapter);
        serverCheckListView.setAdapter(serverCheckListViewAdapter);

        checkListItemAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkListAddDialog = new CheckListAddDialog(CheckListSettingActivity.this, checkListAddDialogOkayClickListener, checkListAddDialogCancelClickListener);
                checkListAddDialog.show();
                InputMethodManager ime = null;
                ime = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                ime.showSoftInputFromInputMethod(((EditText)checkListAddDialog.findViewById(R.id.behavior_content)).getWindowToken(), InputMethodManager.SHOW_FORCED);
                checkListAddDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
        serverCheckListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 내 로컬 디비에 추가
                long id = saveMyCheckList(serverCheckListViewAdapter.getItem(i).getTodo());
                checkListViewAdapter.addItem(id, lastSelectedCheckListInventoryId.intValue(), serverCheckListViewAdapter.getItem(i).getTodo(), 0);
                checkListViewAdapter.notifyDataSetChanged();
            }
        });
        myCheckListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                return false;
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
        checkListViewAdapter = new CheckListViewAdapter2(true);

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
            behaviorLocationGeographValue = checkListAddDialog.getBehaviorLocationGeographValue();
            behaviorAfterDisasterValue = checkListAddDialog.getBehaviorAfterDisasterValue();
            behaviorLocationBuildingValue = checkListAddDialog.getBehaviorLocationBuildingValue();
            behaviorContentValue = checkListAddDialog.getBehaviorContentValue();

            saveServerCheckList();

            checkListAddDialog.dismiss();
            // 서버 통신
        }
    };

    public void getServerCheckListData() {
        ok.get("api/behavior/allBehaviors", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(CheckListSettingActivity.this, "서버 통신 실패했습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseStr = response.body().string();
                responseResult = response;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (responseResult.isSuccessful()) {
                            try {
                                serverCheckListViewAdapter = new CheckListViewAdapter3();
                                JSONArray jsonArray = new JSONArray(responseStr);

                                for (int i = 0 ; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());

                                    String behaviorContent = jsonObject.getString("behavior_content");
                                    serverCheckListViewAdapter.addItem(i, 0, behaviorContent, 0);
                                }

                                serverCheckListViewAdapter.notifyDataSetChanged();
                                serverCheckListView.setAdapter(serverCheckListViewAdapter);

                            } catch (final Exception e) {
                                System.out.print(e.toString());
                            }
                        }
                    }
                });

            }
        });
    }

    public long saveMyCheckList(String behaviorContent) {
        SQLiteDatabase myCheckListDB = myCheckListDataBase.getWritableDatabase();

        ContentValues values = new ContentValues();

        lastSelectedCheckListInventoryIdPreference = getSharedPreferences("lastSelectedCheckListInventoryId", Activity.MODE_PRIVATE);
        lastSelectedCheckListInventoryId = lastSelectedCheckListInventoryIdPreference.getLong("lastSelectedCheckListInventoryId", 1);

        values.put("checklistinventory_id", lastSelectedCheckListInventoryId);
        values.put("todo", behaviorContent);
        values.put("ischecked", 0);
        long id = myCheckListDB.insert("checklist", null, values);

        myCheckListDB.close();



        return id;
    }

    public void saveServerCheckList() {
        ok.get("api/behavior/newBehavior?behavior_after_disaster=" + behaviorAfterDisasterValue + "&behavior_location_geography= " + behaviorLocationGeographValue + " &behavior_location_building=" + behaviorLocationBuildingValue + " &behavior_content=" + behaviorContentValue, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(CheckListSettingActivity.this, "서버 통신 실패했습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseStr = response.body().string();
                responseResult = response;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (responseResult.isSuccessful()) {
                            System.out.println(responseStr);
                            long id = saveMyCheckList(behaviorContentValue);
                            checkListViewAdapter.addItem(id, lastSelectedCheckListInventoryId.intValue(), behaviorContentValue, 1);
                            checkListViewAdapter.notifyDataSetChanged();
                            updateServerCheckList();
                        } else {

                        }
                    }
                });
            }
        });
    }

    private void updateServerCheckList() {
        serverCheckListViewAdapter.addItem(1, lastSelectedCheckListInventoryId.intValue(), behaviorContentValue, 0);
        serverCheckListViewAdapter.notifyDataSetChanged();
    }


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
                checkListInventoryEditDialog.setEmptyText();
                checkListInventoryEditDialog.dismiss();
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
