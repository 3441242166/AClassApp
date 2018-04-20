package com.example.wanhao.aclassapp.bean.sqlbean;

import com.example.wanhao.aclassapp.bean.sqlbean.Course;

import java.util.List;

/**
 * Created by wanhao on 2018/2/25.
 */

public class CourseResult {

    private String status;

    private List<Course> courses;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
