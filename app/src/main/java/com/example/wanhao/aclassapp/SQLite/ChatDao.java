package com.example.wanhao.aclassapp.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.wanhao.aclassapp.bean.sqlbean.ChatBean;
import com.example.wanhao.aclassapp.bean.sqlbean.Document;

import java.util.ArrayList;
import java.util.List;

public class ChatDao {
    private static final String TAG = "ChatDao";

    private DatabaseHelper mMyDBHelper;

    public ChatDao(Context context) {
        mMyDBHelper=new DatabaseHelper(context);
    }
    // 增加的方法吗，返回的的是一个long值
    public void addchat(ChatBean bean, String userID, String courseID){
        Log.i(TAG, "addchat: ");
        SQLiteDatabase sqLiteDatabase =  mMyDBHelper.getWritableDatabase();

            Log.i(TAG, "addDocumentList: courseID "+courseID);

            ContentValues contentValues = new ContentValues();

            contentValues.put("USERID", userID);
            contentValues.put("COURSEID", courseID);
            contentValues.put("CONTENT", bean.getContent());
            contentValues.put("DATE", bean.getDate());
            contentValues.put("MESSAGETYPE", bean.getItemType());
            contentValues.put("NICENAME", bean.getUser().getNickName());
        contentValues.put("AVATAR", bean.getUser().getAvatar());
        contentValues.put("ROLE", bean.getUser().getRole().getRole());

        long rowd = sqLiteDatabase.insert("CHAT", null, contentValues);

        sqLiteDatabase.close();
    }

    // 删除方法，返回值是int
    public int deleteChat(String chatID,String userID,String courseID){
        SQLiteDatabase sqLiteDatabase = mMyDBHelper.getWritableDatabase();
        int deleteResult = sqLiteDatabase.delete("CHAT","chatID=?,userID=?,courseID=?", new String[]{chatID,userID,courseID});
        sqLiteDatabase.close();
        return deleteResult;
    }

    public List<ChatBean> alterChatBean(String user,String courseID,int begin,int last){

        SQLiteDatabase readableDatabase = mMyDBHelper.getReadableDatabase();
        // 查询比较特别,涉及到 cursor
        Cursor cursor = readableDatabase.rawQuery("select * from CHAT WHERE USERID=? AND COURSEID=?", new String[]{user,courseID});

        List<ChatBean> list =new ArrayList<>();

        while (cursor.moveToNext()) {
            ChatBean task = new ChatBean();
            task.setSqlID(cursor.getInt(cursor.getColumnIndex("CHATID")));
            task.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));
            task.setDate(cursor.getString(cursor.getColumnIndex("DATE")));
            task.setItemType(cursor.getInt(cursor.getColumnIndex("MESSAGETYPE")));
            task.getUser().setNickName(cursor.getString(cursor.getColumnIndex("NICENAME")));
            task.getUser().setAvatar(cursor.getString(cursor.getColumnIndex("AVATAR")));
            task.getUser().getRole().setRole(cursor.getString(cursor.getColumnIndex("ROLE")));
            list.add(task);
        }

        cursor.close(); // 记得关闭 corsor
        readableDatabase.close(); // 关闭数据库
        return list;
    }
}
