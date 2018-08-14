package com.example.wanhao.aclassapp.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;

/**
 * Created by wanhao on 2017/8/9.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, "mySQLite.db", null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table COURSE (" +
                "ID text" +
                ", USER text" +
                ", NAME text" +
                ",MAJOR text" +
                ",COUNT text" +
                ",PICTURE text" +
                ",PRIMARY KEY(USER,ID))");

        db.execSQL("create table DOCUMENT (" +
                "DOCUMENTID text" +
                ",USERID text" +
                ",COURSEID text" +
                ",TYPE text" +
                ",TITLE text" +
                ",DATE text" +
                ",SIZE text" +
                ",AUTHOR text" +
                ",PRIMARY KEY(USERID,DOCUMENTID,COURSEID))");


        db.execSQL("create table CHAT (" +
                "CHATID int " +
                ",USERID text" +
                ",COURSEID text" +
                ",CONTENT text" +
                ",DATE text" +
                ",MESSAGETYPE int" +
                ",NICENAME text" +
                ",AVATAR text" +
                ",ROLE text" +
                ",PRIMARY KEY(CHATID,COURSEID,USERID))");

        Log.i(TAG, "onCreate: "+"create table CHAT (CHATID int ,USERID text,COURSEID text,CONTENT text,DATE text,MESSAGETYPE int,PRIMARY KEY(CHATID,COURSEID,USERID))");
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
