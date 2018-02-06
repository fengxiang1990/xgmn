package com.fxa.xgmn;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by 30315 on 2018/2/6.
 */

public class MyApplication extends Application{

    SQLiteDatabase sqLiteDatabase;
    @Override
    public void onCreate() {
        super.onCreate();
        sqLiteDatabase = DBManager.initDB(this);
    }
}
