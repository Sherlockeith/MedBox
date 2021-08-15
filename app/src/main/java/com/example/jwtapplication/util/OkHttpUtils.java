package com.example.jwtapplication.util;


import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;
public class OkHttpUtils {
    private static final String TAG = "OkHttp3Utils";
    public OkHttpClient okHttpClient = null;
    private String SEVER_URL = "http://3.0.17.207:4000";
    public String responseToString = "";
    private MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType FORM_CONTENT_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    public OkHttpUtils() {

    }

    public String getResponseToString() {
        while (responseToString.equals("")) {

        }
        return responseToString;
    }

    private OkHttpClient getOkHttpClient() {
        synchronized (OkHttpUtils.class) {
            if (okHttpClient == null) {
                okHttpClient = new OkHttpClient().newBuilder().connectTimeout(60*1000, TimeUnit.MILLISECONDS)
                        .readTimeout(60*1000, TimeUnit.MILLISECONDS).build();
            }
        }
        return okHttpClient;
    }

    public String PostResult(String url, HashMap<String, String> map) throws IOException {
        url = SEVER_URL + url;
        okHttpClient = getOkHttpClient();

        JSONObject json = new JSONObject();
        StringBuffer sb = new StringBuffer();
        //Setup the para
        for (String key: map.keySet()) {
            try {
                json.put(key, map.get(key));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        RequestBody body = RequestBody.create(JSON, String.valueOf(json));
        //Setup the request method
        Request request = new Request.Builder().url(url).post(body).build();

        //Execute the request
        Response response = okHttpClient.newCall(request).execute();
        responseToString = response.body().string();

        return responseToString;
    }
    public String PostResult(String url, HashMap<String, String> map,HashMap<String,String> headMsg) throws IOException {
        url = SEVER_URL + url;
        okHttpClient = getOkHttpClient();

        JSONObject json = new JSONObject();
        StringBuffer sb = new StringBuffer();
        //Setup the date
        for (String key: map.keySet()) {
            try {
                json.put(key, map.get(key));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        RequestBody body = RequestBody.create(JSON, String.valueOf(json));
        //Setup the request
        Request request = new Request.Builder().addHeader("jwt",headMsg.get("jwt")).url(url).post(body).build();
        //Execute the request
        Response response = okHttpClient.newCall(request).execute();
        responseToString = response.body().string();


        return responseToString;
    }
}