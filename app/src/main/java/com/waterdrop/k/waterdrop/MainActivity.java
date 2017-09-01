package com.waterdrop.k.waterdrop;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
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
import com.waterdrop.k.waterdrop.DataBase.Region;
import com.waterdrop.k.waterdrop.Dialog.CheckListAddDialog;
import com.waterdrop.k.waterdrop.ListViewAdapter.ChatBotListViewAdapter;
import com.waterdrop.k.waterdrop.ListViewAdapter.CheckListViewAdapter;
import com.waterdrop.k.waterdrop.ListViewAdapter.CheckListViewAdapter3;
import com.waterdrop.k.waterdrop.ListViewAdapter.TextListViewAdapter;
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

    Region regionDataBase;
    final String regionDataBaseName = "region.db";
    final int regionDataBaseVersion = 1;

    public static SharedPreferences positionLongitudePreference;
    public static SharedPreferences.Editor positionLongitudePreferenceEditor;
    public static SharedPreferences positionLatitudePreference;
    public static SharedPreferences.Editor positionLatitudePreferenceEditor;

    ListView chatBotListView;
    Button sendButton;
    EditText questionEdit;

    ChatBotListViewAdapter chatBotListViewAdapter;

    String longitude;
    String latitude;

    String address;
    String city1;
    String city2;
    String city3;

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
        regionDataBase = new Region(this, regionDataBaseName, null, regionDataBaseVersion);

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

                    positionLongitudePreference = getSharedPreferences("longitude", Activity.MODE_PRIVATE);
                    longitude = positionLongitudePreference.getString("longitude", "126");

                    positionLatitudePreference = getSharedPreferences("latitude", Activity.MODE_PRIVATE);
                    latitude = positionLatitudePreference.getString("latitude", "37");

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

        positionLongitudePreference = getSharedPreferences("longitude", Activity.MODE_PRIVATE);
        longitude = positionLongitudePreference.getString("longitude", "126.976930");

        positionLatitudePreference = getSharedPreferences("latitude", Activity.MODE_PRIVATE);
        latitude = positionLatitudePreference.getString("latitude", "37.574515");

        getLocation();

        ok.get("api/chatbot/welcome?si=" + city1 + "&gu=" + city2 + "&dong=" + city3, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendable = true;
//                            Toast.makeText(MainActivity.this, "서버 통신 실패했습니다.", Toast.LENGTH_SHORT).show();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        questionEdit.setText("");
                        chatBotListViewAdapter.addItem(1, "현재 위치가 올바르지 않습니다.", 1);
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

                        if (responseResult.isSuccessful()) {
                            try {
                                chatBotListViewAdapter.addItem(1, responseStr, 1);

                                chatBotListViewAdapter.notifyDataSetChanged();
                                chatBotListView.setAdapter(chatBotListViewAdapter);
                                chatBotListView.setSelection(chatBotListViewAdapter.getCount() - 1);

                                sendable = true;
                                questionEdit.setText("");
                            } catch (final Exception e) {
                                System.out.print(e.toString());
                            }
                        } else {
                            questionEdit.setText("");
                            chatBotListViewAdapter.addItem(1, "서버 통신 실패했습니다.", 1);
                            chatBotListViewAdapter.notifyDataSetChanged();
                            chatBotListView.setAdapter(chatBotListViewAdapter);
                            chatBotListView.setSelection(chatBotListViewAdapter.getCount() - 1);
                        }
                    }
                });

            }
        });
    }

    public void getLocation() {
        getCurrentLocation();
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
//        double latitude = 37.2;
//        double longitude = 127.23;
        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            addresses = geocoder.getFromLocation(latitude, longitude), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

//        p1 = new GeoPoint((int) (location.getLatitude() * 1E6),
//                (int) (location.getLongitude() * 1E6));
//

        address = addresses.get(0).getAddressLine(0);
        System.out.println(addresses);
        city1 = addresses.get(0).getLocality();
        city3 = addresses.get(0).getThoroughfare();

        Log.d("city1", city1);
        Log.d("city3", city3);

        SQLiteDatabase sqLiteDatabase = regionDataBase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT _id, city1, city2, city3 FROM region WHERE city1 = ? AND city3 = ?", new String [] {city1, city3});

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                city2 = cursor.getString(2);
                Log.d("city2", city2);
            } while (cursor.moveToNext());

            cursor.close();
        }
        sqLiteDatabase.close();


        SharedPreferences tokenPreference;
        tokenPreference = getSharedPreferences("token", Activity.MODE_PRIVATE);
        String token = tokenPreference.getString("token", "null");

        // 서버 통신
        OkHttpHelper ok = new OkHttpHelper();
        ok.updateUrl("http://10.10.96.155:8080/");
        ok.get("api/user/addcurr_Location?si=" + city1 + "&gu=" + city2 + "&dong=" + city3 + "&device_token=" + token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("err", e.getStackTrace().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(response.body().toString());
                    } catch (final Exception e) {
                        System.out.print(e.toString());
                    }
                }
            }
        });
//        Log.d("adsfd", addresses.get(0).getLocality());
//        Log.d("adsfd", addresses.get(0).getUrl());
//        Log.d("adsfd", addresses.get(0).ge);
//        Log.d("adsfd", addresses.get(0).getAddressLine(2));
    }

    public void getCurrentLocation() {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                100, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        } else {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
        }
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
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

//            Log.d("test", "onLocationChanged, location:" + location);
            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude();   //위도
//            double altitude = location.getAltitude();   //고도
//            float accuracy = location.getAccuracy();    //정확도
//            String provider = location.getProvider();   //위치제공자
            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
//            Log.d("longitude", Double.toString(longitude));
//            Log.d("latitude", Double.toString(latitude));
//            Log.d("provider", provider);
//
            positionLongitudePreference = getSharedPreferences("longitude", Activity.MODE_PRIVATE);
            positionLongitudePreferenceEditor = positionLongitudePreference.edit();
            positionLongitudePreferenceEditor.putString("longitude", Double.toString(longitude));
            positionLongitudePreferenceEditor.apply();

            positionLatitudePreference = getSharedPreferences("latitude", Activity.MODE_PRIVATE);
            positionLatitudePreferenceEditor = positionLatitudePreference.edit();
            positionLatitudePreferenceEditor.putString("latitude", Double.toString(latitude));
            positionLatitudePreferenceEditor.apply();
        }

        public void onProviderDisabled(String provider) {
            // Disabled시
//            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
//            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
//            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };
}
