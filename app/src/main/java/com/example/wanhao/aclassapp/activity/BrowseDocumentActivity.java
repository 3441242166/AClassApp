package com.example.wanhao.aclassapp.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import com.example.wanhao.aclassapp.util.FileConvertUtil;
import com.example.wanhao.aclassapp.view.IBrowseDocumentView;

import butterknife.BindView;

public class BrowseDocumentActivity extends TopBarBaseActivity implements IBrowseDocumentView {
    private static final String TAG = "BrowseDocumentActivity";

    @BindView(R.id.ac_browse_button)
    Button button;
    @BindView(R.id.ac_browse_image)
    ImageView imageView;
    @BindView(R.id.ac_browse_text)
    TextView textView;

    private BrowseDocumentPresenter presenter;
    private DownloadReceiver receiver;
    private Document document;
    private int downloadID;



    private ApiConstant.DOWNLOAD_STATE nowState = ApiConstant.DOWNLOAD_STATE.NONE;

    @Override
    protected int getContentView() {
        return R.layout.activity_browse_document;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter = new BrowseDocumentPresenter(this,this);
        Intent intent = getIntent();
        document = (Document) intent.getSerializableExtra(ApiConstant.DOCUMENT);
        //获取权限
        ActivityCompat.requestPermissions(this, new String[]{android
                .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        IntentFilter filter = new IntentFilter(ApiConstant.DOWNLOAD_ACTION);
        receiver = new DownloadReceiver();
        registerReceiver(receiver, filter);

        initView();
        initEvent();
    }

    private void initView() {
        setTitle(document.getTitle());
        textView.setText(document.getTitle());
        Glide.with(this).load(FileConvertUtil.getDocumentImageID(document.getTitle())).into(imageView);
    }

    private void initEvent() {
        setTopLeftButton(this::finish);

        button.setOnClickListener(view -> {
            switch (nowState){
                case NONE:
                    presenter.startDownload(document);
                    break;
                case ING:
                    //暂停文件
                    presenter.stopDownload(downloadID);
                    break;
                case STOP:
                    //暂停文件
                    presenter.startDownload(document);
                    break;
                case FINISH:
                    presenter.openDocument(document);
                    // 打开文件
                    break;
            }
        });

        receiver.setDownloadStateChangeListener(data -> {
            if (data.documentID == document.getId()) {
                nowState = data.state;
                downloadID = data.taskID;
                button.setText(data.message);
            }
        });

    }

    @Override
    public void tokenError(String msg) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        //销毁在onResume()方法中的广播
        //unregisterReceiver(receiver);
    }
}
