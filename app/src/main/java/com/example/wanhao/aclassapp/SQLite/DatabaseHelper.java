package com.example.wanhao.aclassapp.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wanhao on 2017/8/9.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "mySQLite.db", null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table COURSE (ID text primary key" +
                ", NAME text" +
                ",MAJOR text" +
                ",COUNT text" +
                ",PICTURE text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //用户生词库
//        if(newVersion == 8) {
//            db.execSQL("drop table TIMETASK");
//            db.execSQL("create table TIMETASK (datetime text primary key, image text,title INTEGER,time text)");
//        }
    }
}
