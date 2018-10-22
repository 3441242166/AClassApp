package com.example.wanhao.aclassapp.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wanhao on 2018/4/9.
 */

public class Question implements MultiItemEntity {

    public static final int SIGLE = 0;
    public static final int MORE = 1;
    @SerializedName("homework_id")
    private int id;
    @SerializedName("homework_content")
    private String content;
    @SerializedName("homework_answer")
    private String answer;
    @SerializedName("question_type")
    private String questionType;

    private int type;
    private String[] answers;
    private boolean[] chooses;
    private String qusetion;

    public void init(){
        if(questionType.equals("单选题")){
            type = SIGLE;
        }else {
            type = MORE;
        }

        answers = new String[4];
        chooses = new boolean[4];

        String[] ar = content.split("##");
        qusetion = ar[0];

        String[] temp = ar[1].split("\\$\\$");
        for(int x=0;x<temp.length;x++){
            answers[x] = temp[x];
        }
    }

    public void setAnswer(int pos){
        if(type == MORE){
            chooses[pos] = !chooses[pos];
        }else {
            for(int x=0;x<chooses.length;x++){
                chooses[x] = false;
            }
            chooses[pos] = true;
        }
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public boolean[] getChooses() {
        return chooses;
    }

    public void setChooses(boolean[] chooses) {
        this.chooses = chooses;
    }

    public String getQusetion() {
        return qusetion;
    }

    public void setQusetion(String qusetion) {
        this.qusetion = qusetion;
    }

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

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
