package com.example.wanhao.aclassapp.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.wanhao.aclassapp.bean.sqlbean.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanhao on 2018/3/20.
 */

public class DocumentDao {
    private static final String TAG = "DocumentDao";

    private DatabaseHelper mMyDBHelper;

    public DocumentDao(Context context) {
        mMyDBHelper=new DatabaseHelper(context);
    }
    // 增加的方法吗，返回的的是一个long值
    public void addDocumentList(List<Document> list, String userID, String courseID,String type){

        SQLiteDatabase sqLiteDatabase =  mMyDBHelper.getWritableDatabase();

        for(int x=0;x<list.size();x++) {
            Log.i(TAG, "addDocumentList: courseID "+courseID);
            Document course = list.get(x);

            ContentValues contentValues = new ContentValues();
            contentValues.put("DOCUMENTID", course.getId());
            contentValues.put("TYPE", type);
            contentValues.put("USERID", userID);
            contentValues.put("COURSEID", courseID);
            contentValues.put("TITLE", course.getTitle());
            contentValues.put("DATE", course.getDate());
            contentValues.put("SIZE", course.getSize());
            contentValues.put("AUTHOR", course.getUser());

            long rowd = sqLiteDatabase.insert("DOCUMENT", null, contentValues);
        }
        sqLiteDatabase.close();
    }

    // 删除方法，返回值是int
    public int deleteDocument(String documentID,String userID,String courseID){
        SQLiteDatabase sqLiteDatabase = mMyDBHelper.getWritableDatabase();
        int deleteResult = sqLiteDatabase.delete("DOCUMENT","documentID=?,userID=?,courseID=?", new String[]{documentID,userID,courseID});
        sqLiteDatabase.close();
        return deleteResult;
    }

    public List<Document> alterAllDocument(String user){

        SQLiteDatabase readableDatabase = mMyDBHelper.getReadableDatabase();
        // 查询比较特别,涉及到 cursor
        Cursor cursor = readableDatabase.rawQuery("select * from DOCUMENT WHERE USERID=?", new String[]{user});

        List<Document> list =new ArrayList<Document>();

        while (cursor.moveToNext()) {
            Document task = new Document();
            task.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex("DOCUMENTID"))));
            task.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
            task.setSize(cursor.getString(cursor.getColumnIndex("SIZE")));
            task.setDate(cursor.getString(cursor.getColumnIndex("DATE")));
            task.setUser(cursor.getString(cursor.getColumnIndex("AUTHOR")));
            task.setCourseID(cursor.getString(cursor.getColumnIndex("COURSEID")));
            Log.i(TAG, "alterAllDocument: courseID "+task.getCourseID()+" documentID "+task.getId());
            list.add(task);
        }

        cursor.close(); // 记得关闭 corsor
        readableDatabase.close(); // 关闭数据库
        return list;
    }

    public List<Document> alterAllTypeDocument(String user,String courseID,String type){
        Log.i(TAG, "alterAllTypeDocument: user ="+user+" courseID ="+courseID+" type ="+type);
        SQLiteDatabase readableDatabase = mMyDBHelper.getReadableDatabase();
        // 查询比较特别,涉及到 cursor
        Cursor cursor = readableDatabase.rawQuery("select * from DOCUMENT WHERE USERID=? AND COURSEID=? AND TYPE=?", new String[]{user,courseID,type});

        List<Document> list =new ArrayList<Document>();

        while (cursor.moveToNext()) {
            Document task = new Document();
            task.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex("DOCUMENTID"))));
            task.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
            task.setSize(cursor.getString(cursor.getColumnIndex("SIZE")));
            task.setDate(cursor.getString(cursor.getColumnIndex("DATE")));
            task.setUser(cursor.getString(cursor.getColumnIndex("AUTHOR")));
            task.setCourseID(cursor.getString(cursor.getColumnIndex("COURSEID")));
            Log.i(TAG, "alterAllDocument: courseID "+task.getCourseID()+" documentID "+task.getId());
            list.add(task);
        }

        cursor.close(); // 记得关闭 corsor
        readableDatabase.close(); // 关闭数据库
        return list;
    }

    public Document alterDocument(String documentID,String userID){
        Log.i(TAG, "alterDocument: userID  "+userID+"  documentID  "+documentID);
        SQLiteDatabase readableDatabase = mMyDBHelper.getReadableDatabase();
        // 查询比较特别,涉及到 cursor
        Cursor cursor = readableDatabase.rawQuery("select * from DOCUMENT WHERE USERID=? AND DOCUMENTID=?", new String[]{userID,documentID});
        Document task = new Document();

        while (cursor.moveToNext()) {

            task.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex("DOCUMENTID"))));
            task.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
            task.setSize(cursor.getString(cursor.getColumnIndex("SIZE")));
            task.setDate(cursor.getString(cursor.getColumnIndex("DATE")));
            task.setUser(cursor.getString(cursor.getColumnIndex("AUTHOR")));
            task.setCourseID(cursor.getString(cursor.getColumnIndex("COURSEID")));
            break;
        }
        Log.i(TAG, "alterDocument: courseID "+task.getCourseID());
        cursor.close(); // 记得关闭 corsor
        readableDatabase.close(); // 关闭数据库
        return task;
    }

}
