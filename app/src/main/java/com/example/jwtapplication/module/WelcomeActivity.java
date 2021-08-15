package com.example.jwtapplication.module;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.example.jwtapplication.R;
import com.example.jwtapplication.dao.LoginDao;
import com.example.jwtapplication.dao.Token;
import com.example.jwtapplication.util.OkHttpUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class WelcomeActivity extends AppCompatActivity {
    String account ;
    String jwt;
    String id;
    String fcmtoken;
    ProgressDialog progressDialog;
    OkHttpUtils okHttpUtils= new OkHttpUtils();
    LoginDao loginDao;
    String ResponseString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        progressDialog = new ProgressDialog(WelcomeActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        Button check1 = (Button) findViewById(R.id.confirm1);
        account = getIntent().getStringExtra("username");
        id = getIntent().getStringExtra("medid");
        jwt = getIntent().getStringExtra("jwt");

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        // Get new FCM registration token
                        fcmtoken = task.getResult();
                        Log.d("test", fcmtoken);
                    }
                });

        check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NetWork1().execute();
            }
        });
    }

    class NetWork1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            HashMap<String, String> map = new HashMap<>();
            map.put("username", account);
            map.put("medboxID",id);
            map.put("registrationToken",fcmtoken);
            HashMap<String,String> head=new HashMap<>()
                    ;
            head.put("jwt",jwt);
            try {
                ResponseString = okHttpUtils.PostResult("/notification/register", map, head);

            } catch (Exception e) {

            }
            loginDao = JSON.parseObject(ResponseString, LoginDao.class);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (loginDao.success.equals("1")) {
                Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                intent.putExtra("username", account);
                intent.putExtra("medid", id);
                intent.putExtra("jwt",jwt);
                startActivity(intent);
            } else {
                new AlertDialog.Builder(WelcomeActivity.this).setMessage("Internal error!")
                        .show();
            }
            progressDialog.dismiss();

        }
    }
}
