package com.waterdrop.k.waterdrop;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.waterdrop.k.waterdrop.DataBase.Region;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyService extends Service {
    LocationManager lm;

    public static SharedPreferences positionLongitudePreference;
    public static SharedPreferences.Editor positionLongitudePreferenceEditor;
    public static SharedPreferences positionLatitudePreference;
    public static SharedPreferences.Editor positionLatitudePreferenceEditor;

    @Override
    public IBinder onBind(Intent intent) {
        // Service 객체와 (화면단 Activity 사이에서)
        // 통신(데이터를 주고받을) 때 사용하는 메서드
        // 데이터를 전달할 필요가 없으면 return null;
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        Log.d("test", "서비스의 onCreate");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
//        Log.d("test", "서비스의 onStartCommand");
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
                    3600000, // 통지사이의 최소 시간간격 (miliSecond)
//                    10000, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                    3600000, // 통지사이의 최소 시간간격 (miliSecond)
//                10000, // 통지사이의 최소 시간간격 (miliSecond), // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실행
        lm.removeUpdates(mLocationListener);
//        positionPreference = getSharedPreferences("longitude", Activity.MODE_PRIVATE);
//        Log.d("test", "서비스의 onDestroy");
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


            SharedPreferences tokenPreference;
            tokenPreference = getSharedPreferences("token", Activity.MODE_PRIVATE);
            String token = tokenPreference.getString("token", "null");

            Log.d("token", token);

            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            String city1 = addresses.get(0).getLocality();
            String city2 = "";
            String city3 = addresses.get(0).getThoroughfare();

            Log.d("city1", city1);
            Log.d("city3", city3);

            Region regionDataBase;
            final String regionDataBaseName = "region.db";
            final int regionDataBaseVersion = 1;

            regionDataBase = new Region(getApplicationContext(), regionDataBaseName, null, regionDataBaseVersion);

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


            // 서버 통신
            OkHttpHelper ok = new OkHttpHelper();
            //ok.updateUrl("http://10.10.96.155:8080/");
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