package com.example.wanhao.aclassapp.SQLite;

/**
 * Created by wanhao on 2017/8/9.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wanhao.aclassapp.bean.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseDao {
    private DatabaseHelper mMyDBHelper;

    public CourseDao(Context context) {
        mMyDBHelper=new DatabaseHelper(context);
    }
    // 增加的方法吗，返回的的是一个long值
    public void addCourseList(List<Course> list){

        SQLiteDatabase sqLiteDatabase =  mMyDBHelper.getWritableDatabase();

        for(int x=0;x<list.size();x++) {
            Course course = list.get(x);
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", course.getId());
            contentValues.put("PICTURE", course.getImgUrl());
            contentValues.put("NAME", course.getName());
            contentValues.put("COUNT", course.getNum());
            contentValues.put("MAJOR", course.getParent());

            long rowid = sqLiteDatabase.insert("COURSE", null, contentValues);
        }
        sqLiteDatabase.close();
    }

    // 删除方法，返回值是int
    public int deleteCourse(String id){
        SQLiteDatabase sqLiteDatabase = mMyDBHelper.getWritableDatabase();
        int deleteResult = sqLiteDatabase.delete("COURSE","ID=?", new String[]{id});
        sqLiteDatabase.close();
        return deleteResult;
    }

    public List<Course> alterAllCoursse(){

        SQLiteDatabase readableDatabase = mMyDBHelper.getReadableDatabase();
        // 查询比较特别,涉及到 cursor
        Cursor cursor = readableDatabase.rawQuery("select * from COURSE ", null);

        List<Course> list =new ArrayList<Course>();

        while (cursor.moveToNext()) {
            Course task = new Course();
            task.setId(cursor.getString(cursor.getColumnIndex("ID")));
            task.setImgUrl(cursor.getString(cursor.getColumnIndex("PICTURE")));
            task.setName(cursor.getString(cursor.getColumnIndex("NAME")));
            task.setNum(cursor.getString(cursor.getColumnIndex("COUNT")));
            task.setParent(cursor.getString(cursor.getColumnIndex("MAJOR")));

            list.add(task);
        }

        cursor.close(); // 记得关闭 corsor
        readableDatabase.close(); // 关闭数据库
        return list;
    }
}
