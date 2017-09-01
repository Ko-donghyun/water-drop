package com.waterdrop.k.waterdrop.PushAlert;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.waterdrop.k.waterdrop.OkHttpHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    OkHttpHelper ok = new OkHttpHelper();
    private static final String TAG = "MyFirebaseIIDService";

    public static SharedPreferences tokenPreference;
    public static SharedPreferences.Editor tokenEditor;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        tokenPreference = getSharedPreferences("token", Activity.MODE_PRIVATE);
        tokenPreference.getString("token", refreshedToken);

        tokenEditor = tokenPreference.edit();
        tokenEditor.putString("token", refreshedToken);
        tokenEditor.apply();

        //ok.updateUrl("http://10.10.96.155:8080/");
        ok.get("api/user/saveToken?device_token=" + refreshedToken, new Callback() {
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
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }
    // [END refresh_token]
}