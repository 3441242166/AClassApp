package com.example.wanhao.aclassapp.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeworkResponse {
    @SerializedName("status")
    String status;
    @SerializedName("quizList")
    List<Homework> quizList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Homework> getQuizList() {
        return quizList;
    }

    public void setQuizList(List<Homework> quizList) {
        this.quizList = quizList;
    }
}
