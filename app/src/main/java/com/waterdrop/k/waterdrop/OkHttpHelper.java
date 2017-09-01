package com.waterdrop.k.waterdrop;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpHelper {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();
    String RootUrl = "http://13.124.201.35:8080/";
//    String RootUrl = "http://10.10.96.155:8080/";

    public Call post(String url, String json, Callback callback) {
        String fullUrl = RootUrl + url;
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(fullUrl)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public Call get(String url, Callback callback) {
        String fullUrl = RootUrl + url;
        Request request = new Request.Builder()
                .url(fullUrl)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public void updateUrl(String rootUrl) {
        RootUrl = rootUrl;
    }

}