package com.waterdrop.k.waterdrop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.waterdrop.k.waterdrop.DataBase.CheckList;
import com.waterdrop.k.waterdrop.Dialog.CheckListAddDialog;
import com.waterdrop.k.waterdrop.ListViewAdapter.ChatBotListViewAdapter;
import com.waterdrop.k.waterdrop.ListViewAdapter.CheckListViewAdapter;
import com.waterdrop.k.waterdrop.ListViewAdapter.CheckListViewAdapter3;
import com.waterdrop.k.waterdrop.SpinnerAdapter.MyCheckListSpinnerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends Activity {
    OkHttpHelper ok = new OkHttpHelper();
    InputMethodManager imm;
    private Handler mHandler;
    String responseStr;
    Response responseResult;

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

    /**
     * 챗봇 페이지
     */

    public static SharedPreferences positionPreference;

    ListView chatBotListView;
    Button sendButton;
    EditText questionEdit;

    ChatBotListViewAdapter chatBotListViewAdapter;

    String longitude;
    String latitude;

    String si;
    String gu;
    String dong;

    boolean sendable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

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
                    hideKeyboard(questionEdit);
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
                finish();
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

        /*
         * 챗봇 페이지
         */
        chatBotListViewAdapter = new ChatBotListViewAdapter();
        chatBotListView = (ListView) findViewById(R.id.chat_bot_list_view);
        chatBotListView.setAdapter(chatBotListViewAdapter);
        sendButton = (Button) findViewById(R.id.send_button);
        questionEdit = (EditText) findViewById(R.id.question);
        mHandler = new Handler(Looper.getMainLooper());
        // http://13.124.201.35:8080/api/chatbot/test?message=비 올 때 자동차에서 어떻게 해야돼?

        sendWelcome();

        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!questionEdit.getText().toString().equals("") && sendable) {
                    sendable = false;
                    chatBotListViewAdapter.addItem(0, questionEdit.getText().toString(), 0);
                    chatBotListViewAdapter.notifyDataSetChanged();

                    positionPreference = getSharedPreferences("longitude", Activity.MODE_PRIVATE);
                    longitude = positionPreference.getString("longitude", "126");

                    positionPreference = getSharedPreferences("latitude", Activity.MODE_PRIVATE);
                    latitude = positionPreference.getString("latitude", "37");

                    Log.d("longitude", longitude);
                    Log.d("latitude", latitude);

                    ok.get("api/chatbot?message=" + questionEdit.getText().toString() + "&longitude=" + longitude + "&latitude=" + latitude, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            sendable = true;
//                            Toast.makeText(MainActivity.this, "서버 통신 실패했습니다.", Toast.LENGTH_SHORT).show();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                questionEdit.setText("");
                                chatBotListViewAdapter.addItem(1, "서버 통신 실패했습니다.", 1);
                                chatBotListViewAdapter.notifyDataSetChanged();
                                chatBotListView.setAdapter(chatBotListViewAdapter);
                                chatBotListView.setSelection(chatBotListViewAdapter.getCount() - 1);
                                }
                            });
                            Log.d("err", e.getStackTrace().toString());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            responseStr = response.body().string();
                            responseResult = response;
                            Log.d("answer", responseStr);

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    if (responseResult.isSuccessful()) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(responseStr);
                                            if (jsonObject.getString("type").equals("weather")) {

                                                chatBotListViewAdapter.addItem(1, jsonObject.getString("message"), 1);

                                            } else if (jsonObject.getString("type").equals("behavior")) {

                                                JSONArray jsonArray = jsonObject.getJSONArray("message");

                                                for (int i = 0 ; i < jsonArray.length(); i++) {
                                                    JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());

                                                    String behaviorContent = jsonObject1.getString("behavior_content");
                                                    chatBotListViewAdapter.addItem(1, behaviorContent, 1);
                                                }

                                            } else if (jsonObject.getString("type").equals("shelter")) {
                                                chatBotListViewAdapter.addItem(1, jsonObject.getString("message"), 1);
                                            } else {
                                                chatBotListViewAdapter.addItem(1, jsonObject.getString("message"), 1);
                                            }

                                            chatBotListViewAdapter.notifyDataSetChanged();
                                            chatBotListView.setAdapter(chatBotListViewAdapter);
                                            chatBotListView.setSelection(chatBotListViewAdapter.getCount() - 1);

                                            sendable = true;
                                            questionEdit.setText("");
                                        } catch (final Exception e) {
                                            System.out.print(e.toString());
                                        }
                                    }
                                }
                            });

                        }
                    });
                }
            }
        });

    }

    private void sendWelcome() {
        sendable = false;

        positionPreference = getSharedPreferences("longitude", Activity.MODE_PRIVATE);
        longitude = positionPreference.getString("longitude", "126");

        positionPreference = getSharedPreferences("latitude", Activity.MODE_PRIVATE);
        latitude = positionPreference.getString("latitude", "37");

        getLocation();
        Log.d("longitude", longitude);
        Log.d("latitude", si);
        Log.d("latitude", gu);
        Log.d("latitude", dong);
        ok.get("api/chatbot/welcome&si=" + si + "&gu=" + gu + "&dong=" + dong, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendable = true;
//                            Toast.makeText(MainActivity.this, "서버 통신 실패했습니다.", Toast.LENGTH_SHORT).show();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        questionEdit.setText("");
                        chatBotListViewAdapter.addItem(1, "서버 통신 실패했습니다.", 1);
                        chatBotListViewAdapter.notifyDataSetChanged();
                        chatBotListView.setAdapter(chatBotListViewAdapter);
                        chatBotListView.setSelection(chatBotListViewAdapter.getCount() - 1);
                    }
                });
                Log.d("err", e.getStackTrace().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseStr = response.body().string();
                responseResult = response;

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("aaa" , responseStr);
                        if (responseResult.isSuccessful()) {
                            try {
                                Log.d("aaa" , responseStr);
                                JSONObject jsonObject = new JSONObject(responseStr);

                                chatBotListViewAdapter.addItem(1, jsonObject.toString(), 1);

                                chatBotListViewAdapter.notifyDataSetChanged();
                                chatBotListView.setAdapter(chatBotListViewAdapter);
                                chatBotListView.setSelection(chatBotListViewAdapter.getCount() - 1);

                                sendable = true;
                                questionEdit.setText("");
                            } catch (final Exception e) {
                                System.out.print(e.toString());
                            }
                        }
                    }
                });

            }
        });
    }

    public void getLocation() {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
//        double latitude = 37.2;
//        double longitude = 127.23;
        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        si = addresses.get(0).getLocality().toString(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        gu = addresses.get(0).getSubLocality();
        dong = addresses.get(0).getThoroughfare();
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

    private void hideKeyboard(View view) {
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showKeyboard(View view) {
        imm.showSoftInput(view, 0);
    }

}
