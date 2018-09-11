package com.example.wanhao.aclassapp.presenter;

import android.content.Context;

import com.example.wanhao.aclassapp.bean.Assignment;
import com.example.wanhao.aclassapp.util.DateUtil;
import com.example.wanhao.aclassapp.view.AssignmentView;

import java.util.ArrayList;
import java.util.List;

public class AssignmentPresenter {

    Context context;
    AssignmentView view;

    public AssignmentPresenter(Context context, AssignmentView view){
        this.context = context;
        this.view = view;
    }

    public void getData(){
        view.showProgress();

        List<Assignment> list = new ArrayList<>();

        for(int x=0;x<15;x++){
            Assignment assignment = new Assignment();

            assignment.setTitle("+"+x);
            assignment.setContent("content+"+x);
            assignment.setcDate(DateUtil.getNowDateTimeString());
            assignment.seteDate(DateUtil.getNowDateString());
            assignment.setPostMan("万浩");

            list.add(assignment);
        }
        view.loadDataSuccess(list);
        view.dismissProgress();
    }



}
