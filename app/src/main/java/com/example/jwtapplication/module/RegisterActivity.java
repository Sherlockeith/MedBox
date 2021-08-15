package com.example.jwtapplication.module;


import android.app.Activity;
import android.app.AlertDialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.example.jwtapplication.R;
import com.example.jwtapplication.dao.Data;
import com.example.jwtapplication.dao.DataDao;
import com.example.jwtapplication.dao.LoginDao;
import com.example.jwtapplication.dao.Token;
import com.example.jwtapplication.util.OkHttpUtils;

import java.util.HashMap;

public class RegisterActivity extends Activity {
    ProgressDialog progressDialog;
    String ResponseString;
    DataDao loginDao;
    EditText mAccount;
    EditText email;
    EditText mPassword;
    EditText rPassword;
    Button register;
    OkHttpUtils okHttpUtils = new OkHttpUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Registering...");
        mAccount = (EditText) findViewById(R.id.username1);
        email = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password1);
        rPassword = (EditText) findViewById(R.id.repeatPassword);
        register = (Button) findViewById(R.id.register1);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rPassword.getText().toString().equals(mPassword.getText().toString())) {
                    if(mAccount.getText().toString().length()==0||email.getText().toString().length()==0
                            ||mPassword.getText().toString().length()==0){
                        new AlertDialog.Builder(RegisterActivity.this).setMessage("Missing required field!")
                                .show();
                    }else {
                        new NetWork().execute();
                    }

                } else {
                    new AlertDialog.Builder(RegisterActivity.this).setMessage("Password doesn't match!")
                            .show();
                }
            }
        });
    }



    class NetWork extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            HashMap<String, String> map = new HashMap<>();
            map.put("username", mAccount.getText().toString());
            map.put("password", mPassword.getText().toString());
            map.put("email", email.getText().toString());


            try {
                ResponseString = okHttpUtils.PostResult("/auth/register", map);

            } catch (Exception e) {

            }
            loginDao = JSON.parseObject(ResponseString, DataDao.class);

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            if (loginDao.success.equals("1")) {
                new AlertDialog.Builder(RegisterActivity.this).setMessage("Register Success!")
                        .show();
                finish();
            } else {
                new AlertDialog.Builder(RegisterActivity.this).setMessage("Register Failed!")
                        .show();
            }

            progressDialog.dismiss();

        }
    }
}