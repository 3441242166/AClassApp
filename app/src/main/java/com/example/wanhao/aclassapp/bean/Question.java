package com.example.wanhao.aclassapp.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wanhao on 2018/4/9.
 */

public class Question {

    private String question;

    private String answer;

    private List<String> list;

    private int choose;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public int getChoose() {
        return choose;
    }

    public void setChoose(int choose) {
        this.choose = choose;
    }

    public void transfrom(){
        String[] ar = answer.split(":");
        list = Arrays.asList(ar);
    }

}
