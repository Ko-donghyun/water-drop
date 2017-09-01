package com.waterdrop.k.waterdrop;

import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.waterdrop.k.waterdrop.DataBase.MyRegion;
import com.waterdrop.k.waterdrop.DataBase.Region;
import com.waterdrop.k.waterdrop.Dialog.ItemDeleteDialog;
import com.waterdrop.k.waterdrop.Dialog.RegionAddDialog;
import com.waterdrop.k.waterdrop.ListViewAdapter.RegionListViewAdapter;
import com.waterdrop.k.waterdrop.ListViewAdapter.TextListViewAdapter;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegionSettingActivity extends Activity {

    MyRegion myRegionDataBase;
    final String myRegionDataBaseName = "myregion.db";
    final int myRegionDataBaseVersion = 1;

    Region regionDataBase;
    final String regionDataBaseName = "region.db";
    final int regionDataBaseVersion = 1;

    ListView myRegionListView;
    Button myRegionAddButton;
    RegionAddDialog regionAddDialog;
    RegionListViewAdapter myRegionListViewAdapter;

    String city1;
    String city2;
    String city3;

    TextListViewAdapter city1ListViewAdapter;
    TextListViewAdapter city2ListViewAdapter;
    TextListViewAdapter city3ListViewAdapter;

    ItemDeleteDialog itemDeleteDialog;

    int deleteItemPosition;

    public static SharedPreferences tokenPreference;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.region_setting);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }
        myRegionListView = (ListView) findViewById(R.id.my_region_list);
        myRegionAddButton = (Button) findViewById(R.id.my_region_add_button);

        myRegionDataBase = new MyRegion(this, myRegionDataBaseName, null, myRegionDataBaseVersion);
        regionDataBase = new Region(this, regionDataBaseName, null, regionDataBaseVersion);

        myRegionListViewAdapter = new RegionListViewAdapter(getApplicationContext(), R.layout.region_list_item);
        getMyRegionData();
        myRegionListView.setAdapter(myRegionListViewAdapter);
        myRegionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        myRegionListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteItemPosition = i;
                itemDeleteDialog = new ItemDeleteDialog(RegionSettingActivity.this,
                        myRegionListViewAdapter.getItem(i).getId(),
                        "'" + myRegionListViewAdapter.getItem(i).getCity1() + " " + myRegionListViewAdapter.getItem(i).getCity2() + " " + myRegionListViewAdapter.getItem(i).getCity3() + "'이(가) 삭제 됩니다.",
                        dialogDeleteCancelClickListener, dialogDeleteDoneClickListener);
                itemDeleteDialog.show();
                return false;
            }
        });

        setCityListViewAdapter();
        setCity1ListViewAdapter();

        myRegionAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regionAddDialog = new RegionAddDialog(RegionSettingActivity.this, okayClickListener, cancelClickListener, city1ClickListener, city2ClickListener, city3ClickListener, city1ListViewAdapter, city2ListViewAdapter, city3ListViewAdapter);
                regionAddDialog.show();
            }
        });

    }

    private void getMyRegionData() {
        SQLiteDatabase sqLiteDatabase = myRegionDataBase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM myregion", null);

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                myRegionListViewAdapter.addItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            } while (cursor.moveToNext());

            cursor.close();
        }
        sqLiteDatabase.close();
    }


    private void setCityListViewAdapter() {
        city1ListViewAdapter = new TextListViewAdapter();
        city2ListViewAdapter = new TextListViewAdapter();
        city3ListViewAdapter = new TextListViewAdapter();
    }

    private void setCity1ListViewAdapter() {
        String citys[] = {"강원도", "경기도", "경상남도", "경상북도", "광주광역시", "대구광역시", "대전광역시", "부산광역시",
                "서울특별시", "세종특별자치시", "울산광역시", "인천광역시", "전라남도", "전라북도", "제주특별자치도", "충청남도", "충청북도"};

        for (int i = 0; i < citys.length; i++)
        city1ListViewAdapter.addItem(i, citys[i]);
    }

    private void setCity2ListViewAdapter() {
        city2ListViewAdapter = new TextListViewAdapter();
        SQLiteDatabase sqLiteDatabase = regionDataBase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT city2 FROM region WHERE city1 = ?", new String [] {city1});

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                city2ListViewAdapter.addItem(1, cursor.getString(0));
            } while (cursor.moveToNext());

            cursor.close();
        }
        sqLiteDatabase.close();

        regionAddDialog.updateCity2Adapter(city2ListViewAdapter);
    }

    private void setCity3ListViewAdapter() {
        city3ListViewAdapter = new TextListViewAdapter();
        SQLiteDatabase sqLiteDatabase = regionDataBase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT _id, city3 FROM region WHERE city1 = ? AND city2 = ?", new String [] {city1, city2});

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                city3ListViewAdapter.addItem(cursor.getInt(0), cursor.getString(1));
            } while (cursor.moveToNext());

            cursor.close();
        }
        sqLiteDatabase.close();

        regionAddDialog.updateCity3Adapter(city3ListViewAdapter);
    }


    private View.OnClickListener cancelClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            regionAddDialog.dismiss();
            if (regionAddDialog.getRegionListViewFlipperDisplayedChild() == 1) {
                regionAddDialog.previousViewFlipper();
            } else if (regionAddDialog.getRegionListViewFlipperDisplayedChild() == 2) {
                regionAddDialog.nextViewFlipper();
            }
            city1 = "";
            city2 = "";
            city3 = "";
        }
    };

    private AdapterView.OnItemClickListener city1ClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            // city2 리스트 조회, 텍스트 설정
            city1 = city1ListViewAdapter.getItem(i).getText();
            setCity2ListViewAdapter();

            regionAddDialog.updateCity2Adapter(city2ListViewAdapter);
            regionAddDialog.updateCityText(city1);
            regionAddDialog.nextViewFlipper();
        }
    };

    private AdapterView.OnItemClickListener city2ClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            // city3 리스트 조회, 텍스트 설정
            city2 = city2ListViewAdapter.getItem(i).getText();
            setCity3ListViewAdapter();

            regionAddDialog.updateCity3Adapter(city3ListViewAdapter);
            regionAddDialog.updateCityText(city1 + " " + city2);
            regionAddDialog.nextViewFlipper();
        }
    };
    private AdapterView.OnItemClickListener city3ClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            // 텍스트 설정
            city3 = city3ListViewAdapter.getItem(i).getText();

            regionAddDialog.updateCityText(city1 + " " + city2 + " " + city3);
        }
    };

    private View.OnClickListener okayClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (city1.equals("") || city2.equals("") || city3.equals("")) {
                Toast.makeText(RegionSettingActivity.this, "설정이 완료되지 않았습니다.", Toast.LENGTH_SHORT).show();
            } else {
                long id = saveMyRegionItem(city1, city2, city3);
                myRegionListViewAdapter.addItem(id, city1, city2, city3);
                myRegionListViewAdapter.notifyDataSetChanged();
                city1 = "";
                city2 = "";
                city3 = "";
                regionAddDialog.dismiss();
            }
        }
    };

    private long saveMyRegionItem(String city1, String city2, String city3) {
        SQLiteDatabase myregionDB = myRegionDataBase.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("city1", city1);
        values.put("city2", city2);
        values.put("city3", city3);
        long id = myregionDB.insert("myregion", null, values);

        myregionDB.close();


        tokenPreference = getSharedPreferences("token", Activity.MODE_PRIVATE);
        token = tokenPreference.getString("token", "null");

        Log.d("token", token);

        // 서버 통신
        OkHttpHelper ok = new OkHttpHelper();
        ok.updateUrl("http://10.10.96.155:8080/");
        ok.get("api/user/addLocation?si=" + city1 + "&gu=" + city2 + "&dong=" + city3 + "&device_token=" + token, new Callback() {
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

        return id;
    }
    private void removeMyRegionItem(long id) {
        SQLiteDatabase myregionDB = myRegionDataBase.getWritableDatabase();
        myregionDB.delete("myregion", "_id=" + id, null);
        myregionDB.close();
    }

    private View.OnClickListener dialogDeleteCancelClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            itemDeleteDialog.dismiss();
        }
    };

    private View.OnClickListener dialogDeleteDoneClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            removeMyRegionItem(itemDeleteDialog.getItemId());
            myRegionListViewAdapter.removeItem(deleteItemPosition);
            myRegionListViewAdapter.notifyDataSetChanged();
            itemDeleteDialog.dismiss();
        }
    };

//    private void getCity2Data(String city1) {
//        SQLiteDatabase sqLiteDatabase = regionDataBase.getReadableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM region WHERE city1 = ?)",  new String[] {
//                city1
//        });
//
//        if (cursor.getCount() != 0) {
//            cursor.moveToFirst();
//            do {
//                re.addItem(cursor.getInt(0), cursor.getString(1));
//            } while (cursor.moveToNext());
//
//            cursor.close();
//        }
//        sqLiteDatabase.close();
//    }
}
