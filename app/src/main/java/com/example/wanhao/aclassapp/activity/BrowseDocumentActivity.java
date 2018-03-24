package com.example.wanhao.aclassapp.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.broadcast.DownloadReceiver;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.DownService;

import butterknife.BindView;

public class BrowseDocumentActivity extends TopBarBaseActivity {
    private static final String TAG = "BrowseDocumentActivity";

    @BindView(R.id.ac_browse_button)
    Button button;
    @BindView(R.id.ac_browse_image)
    ImageView imageView;
    @BindView(R.id.ac_browse_text)
    TextView textView;

    private DownloadReceiver mBroadcastReceiver;

    private int documentID;

    public enum STATE{
        NONE,ING,STOP,FINISH
    }

    private STATE nowState = STATE.NONE;

    @Override
    protected int getContentView() {
        return R.layout.activity_browse_document;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle("文件名");
        documentID = getIntent().getIntExtra(ApiConstant.Document_ID,-1);
        initView();
        initEvent();
    }

    private void initView() {

    }

    private void initEvent() {
        setTopLeftButton(new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        /**
         * NONE     启动下载
         * ING      点击暂停
         * STOP     点击取消
         * FINISH   点击打开文件
         */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (nowState){
                    case NONE:
                        Intent intent = new Intent(BrowseDocumentActivity.this, DownService.class);
                        intent.putExtra(ApiConstant.Document_ID,documentID);
                        startService(intent);
                        break;
                    case ING:
                        break;
                    case FINISH:
                        break;
                    case STOP:
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        // 1. 实例化BroadcastReceiver子类 &  IntentFilter
        mBroadcastReceiver = new DownloadReceiver();
        IntentFilter intentFilter = new IntentFilter();

        // 2. 设置接收广播的类型
        intentFilter.addAction(ApiConstant.DOWNLOAD_STATE);

        // 3. 动态注册：调用Context的registerReceiver（）方法
        registerReceiver(mBroadcastReceiver, intentFilter);

        mBroadcastReceiver.setDownloadStateChangeLinser(new DownloadReceiver.onDownloadStateChangeLinser() {
            @Override
            public void onDownloadStateChange(int state,int ID) {
                //  检测是否和下载中文件ID相同
                if(documentID==ID){
                    switch (state){
                        case ApiConstant.DOWNLOAD_STATE_NONE:
                            butonState("下载",true);
                            break;
                        case ApiConstant.DOWNLOAD_STATE_STOP:
                            butonState("暂停",true);
                            break;
                        case ApiConstant.DOWNLOAD_STATE_ING:
                            butonState("下载中...",false);
                            break;
                        case ApiConstant.DOWNLOAD_STATE_FINISH:
                            butonState("打开",true);
                            break;
                    }
                }

            }
        });

    }


    // 注册广播后，要在相应位置记得销毁广播
// 即在onPause() 中unregisterReceiver(mBroadcastReceiver)
// 当此Activity实例化时，会动态将MyBroadcastReceiver注册到系统中
// 当此Activity销毁时，动态注册的MyBroadcastReceiver将不再接收到相应的广播。
    @Override
    protected void onPause() {
        super.onPause();
        //销毁在onResume()方法中的广播
        unregisterReceiver(mBroadcastReceiver);
    }

    private void butonState(String contant,boolean clickable){
        button.setText(contant);
        button.setClickable(clickable);
    }

}
