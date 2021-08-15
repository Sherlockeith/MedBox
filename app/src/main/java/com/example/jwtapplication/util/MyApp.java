package com.example.jwtapplication.util;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.jwtapplication.db.DaoMaster;
import com.example.jwtapplication.db.DaoSession;

public class MyApp extends Application {
    private DaoSession daoSession;
    private static MyApp myApp;
    public static MyApp getInstance(){
        return myApp;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        myApp=this;
        initGreenDao();
    }
    private void initGreenDao(){
        DaoMaster.DevOpenHelper helper=new DaoMaster.DevOpenHelper(this,"test.db");
        SQLiteDatabase db=helper.getWritableDatabase();
        DaoMaster daoMaster=new DaoMaster(db);
        daoSession=daoMaster.newSession();
    }
    public DaoSession getDaoSession(){
        return daoSession;
    }
}
