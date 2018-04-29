package com.example.wanhao.aclassapp.bean;

/**
 * Created by wanhao on 2018/4/8.
 */

public class Homework {

    int id;
    String content;
    String answer;

    int choose = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getChoose() {
        return choose;
    }

    public void setChoose(int choose) {
        this.choose = choose;
    }

    public String getUserChoose(){
        switch (choose){
            case 0:
                return "A";
            case 1:
                return "B";
            case 2:
                return "C";
            case 3:
                return "D";
        }

        return null;
    }
}
