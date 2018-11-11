package com.example.wanhao.aclassapp.db;

import com.example.wanhao.aclassapp.bean.Course;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CourseDB extends RealmObject {
    @PrimaryKey
    private String courseID;
    private String userCount;

    private String name;
    private String major;
    private String picture;
    private String code;
    private String studentSum;
    private String college;
    private int priority;
    private int unRead;

    public CourseDB() {
    }

    public CourseDB(Course course) {
        courseID = course.getId();
        name = course.getName();
        major = course.getMajor();
        picture = course.getImgUrl();
        code = course.getCode();
        studentSum = course.getNum();
        college = course.getParent();
        priority = 0;
        unRead = 0;
    }

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStudentSum() {
        return studentSum;
    }

    public void setStudentSum(String studentSum) {
        this.studentSum = studentSum;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getUnRead() {
        return unRead;
    }

    public void setUnRead(int unRead) {
        this.unRead = unRead;
    }
}
