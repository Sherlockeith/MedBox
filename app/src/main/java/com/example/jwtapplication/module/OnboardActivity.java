package com.example.jwtapplication.module;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.example.jwtapplication.dao.DataDao;
import com.example.jwtapplication.dao.LoginDao;
import com.example.jwtapplication.dao.Token;
import com.example.jwtapplication.util.OkHttpUtils;

import java.util.HashMap;
import com.example.jwtapplication.R;
public class OnboardActivity extends AppCompatActivity {

    String account ;
    String jwt;
    String id;
    String code;
    EditText verification;
    EditText verification1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);
        progressDialog = new ProgressDialog(OnboardActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Authenticating...");
        verification = (EditText) findViewById(R.id.medid);
        verification1 = (EditText) findViewById(R.id.medcode);
        Button check = (Button) findViewById(R.id.confirm);
        Intent intent = getIntent();
        account=intent.getStringExtra("account");
        jwt=intent.getStringExtra("jwt");
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = String.valueOf(verification.getText());
                code = String.valueOf(verification1.getText());
                if(id.length()>0&&code.length()>0){
                    new NetWork1().execute();
                }else {
                    new AlertDialog.Builder(OnboardActivity.this).setMessage("Code/ID cannot be empty!")
                            .show();
                }

            }
        });
    }
    ProgressDialog progressDialog;
    OkHttpUtils okHttpUtils= new OkHttpUtils();
    LoginDao loginDao;
    String ResponseString;
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
            map.put("password", code);
            map.put("medboxID",id);
            System.out.println(JSON.toJSONString(map));
            HashMap<String,String> head=new HashMap<>()
                    ;
            head.put("jwt",jwt);

            try {
                ResponseString = okHttpUtils.PostResult("/onboard/authenticate", map, head);
            } catch (Exception e) {

            }

            loginDao = JSON.parseObject(ResponseString, LoginDao.class);
            if (loginDao.success.equals("1")) {
                Token token = new Token();
                token.setUsername(account);
                jwt=loginDao.data.jwt;
                token.setKey(loginDao.data.jwt);
                token.setSelectStr("TEST");
                Token.savetOKEN(token);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (loginDao.success.equals("1")) {
                Intent intent = new Intent(OnboardActivity.this, WelcomeActivity.class);
                intent.putExtra("username", account);
                intent.putExtra("medid", id);
                intent.putExtra("jwt",jwt);
                startActivity(intent);
            } else {
                new AlertDialog.Builder(OnboardActivity.this).setMessage("Incorrect Code/ID!")
                        .show();
            }
            progressDialog.dismiss();

        }
    }
}