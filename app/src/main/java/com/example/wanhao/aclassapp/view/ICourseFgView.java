package com.example.wanhao.aclassapp.view;

import com.example.wanhao.aclassapp.base.IBaseView;
import com.example.wanhao.aclassapp.bean.Course;

import java.util.List;

/**
 * Created by wanhao on 2018/2/23.
 */

public interface ICourseFgView extends IBaseView<List<Course>> {

    void deleteSucess();

}