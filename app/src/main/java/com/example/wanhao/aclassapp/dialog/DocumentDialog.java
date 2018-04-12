package com.example.wanhao.aclassapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.wanhao.aclassapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanhao on 2018/4/9.
 */

public class DocumentDialog extends Dialog{
    private static final String TAG = "DocumentDialog";

    @BindView(R.id.dialog_document_type)
    RadioButton btType;
    @BindView(R.id.dialog_document_date)
    RadioButton btDate;
    @BindView(R.id.dialog_document_teacher)
    RadioButton btTeacher;
    @BindView(R.id.dialog_document_size)
    RadioButton btSize;
    @BindView(R.id.dialog_document_time)
    RadioButton btTime;

    @BindView(R.id.dialog_document_sort)
    RadioGroup groupSort;
    @BindView(R.id.dialog_document_group)
    RadioGroup groupGrop;

    @BindView(R.id.dialog_document_enter)
    Button btEnter;
    @BindView(R.id.dialog_document_replace)
    Button btReplace;

    private onSelectListener onSelectListener;

    private int chackGroup;
    private int chackSort;

    public DocumentDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_document);
        ButterKnife.bind(this);
        //初始化界面控件
        initView();
        //初始化界面控件的事件
        initEvent();
    }

    private void initView() {

    }

    private void initEvent() {
        groupSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.i(TAG, "onCheckedChanged: i = "+i);
                chackSort = i;
            }
        });

        groupGrop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.i(TAG, "onCheckedChanged: i = "+i);
                chackGroup = i;
            }
        });

        btEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chackSort = 0;
                chackGroup = 0;
            }
        });

        btReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onSelectListener!=null){
                    onSelectListener.onSelect(chackGroup,chackSort);
                }
            }
        });
    }

    public void setOnSelectListener(onSelectListener onSelectListener){
        this.onSelectListener = onSelectListener;
    }

    public interface onSelectListener{
        void onSelect(int i,int j);
    }
}
