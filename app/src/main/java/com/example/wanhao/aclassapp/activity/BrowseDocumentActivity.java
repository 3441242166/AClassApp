package com.example.wanhao.aclassapp.activity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.broadcast.DownloadReceiver;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.BrowseDocumentPresenter;
import com.example.wanhao.aclassapp.util.FileSizeUtil;
import com.example.wanhao.aclassapp.view.BrowseDocumentView;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class BrowseDocumentActivity extends TopBarBaseActivity implements BrowseDocumentView{
    private static final String TAG = "BrowseDocumentActivity";

    @BindView(R.id.ac_browse_button)
    Button button;
    @BindView(R.id.ac_browse_image)
    ImageView imageView;
    @BindView(R.id.ac_browse_text)
    TextView textView;

    private DownloadReceiver mBroadcastReceiver;
    private BrowseDocumentPresenter presenter;
    private int documentID;
    private Document document;

    public enum STATE{
        NONE,ING,FINISH
    }

    private STATE nowState = STATE.NONE;

    @Override
    protected int getContentView() {
        return R.layout.activity_browse_document;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter = new BrowseDocumentPresenter(this,this);
        documentID = getIntent().getIntExtra(ApiConstant.Document_ID,-1);
        presenter.checkDocument(String.valueOf(documentID));

        initView();
        initEvent();

    }

    private void initView() {
        setTitle(document.getTitle());
        textView.setText(document.getTitle());
        String last = document.getTitle().substring(document.getTitle().length()-3);

        if(last.equals("pdf")){
            Glide.with(this).load(R.drawable.icon_pdf).into(imageView);
        }
        if(last.equals("txt")){
            Glide.with(this).load(R.drawable.icon_txt).into(imageView);
        }

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
         * ING      点击取消
         * FINISH   点击打开文件
         */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (nowState){
                    case NONE:
                        presenter.startDownload();
                        break;
                    case ING:
                        //暂停文件
                        new SweetAlertDialog(BrowseDocumentActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("确定?")
                                .setContentText("你确定要取消下载吗？")
                                .setConfirmText("是，取消!")
                                .setCancelText("不，点错了")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Log.i(TAG, "SweetAlertDialog onClick: ");
                                        presenter.cancalDownload();
                                    }
                                })
                                .show();
                        break;
                    case FINISH:
                        // 打开文件
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
            public void onDownloadStateChange(String state,int ID) {
                //  检测是否和下载中文件ID相同
                if(documentID==ID){
                    button.setText(state);
                    if(state.equals("打开")){
                        nowState = STATE.FINISH;
                    }else{
                        nowState = STATE.ING;
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

    @Override
    public void setDocument(Document document) {
        this.document = document;
    }

    @Override
    public void documentState(STATE state) {
        Log.i(TAG, "setState: ");
        nowState = state;
        switch (state){
            case NONE:
                button.setText("下载 ("+ FileSizeUtil.FormetFileSize(Integer.valueOf(document.getSize()))+")");
                break;
            case FINISH:
                button.setText("打开");
                break;
            default:
                button.setText("default");
                break;
        }
    }

}
