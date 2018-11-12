package com.example.wanhao.aclassapp.view;

import com.example.wanhao.aclassapp.base.IBaseTokenView;
import com.example.wanhao.aclassapp.bean.Course;
import com.example.wanhao.aclassapp.db.CourseDB;

import java.util.List;

/**
 * Created by wanhao on 2018/2/23.
 */

public interface ICourseFgView extends IBaseTokenView<List<CourseDB>> {
    void updateData(int index);
}
