package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.example.wanhao.aclassapp.activity.BrowseDocumentActivity;
import com.example.wanhao.aclassapp.base.IBasePresenter;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.bean.HttpResult;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.DocumentService;
import com.example.wanhao.aclassapp.util.FileConvertUtil;
import com.example.wanhao.aclassapp.util.FileSizeUtil;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.IDocumentView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

import static com.example.wanhao.aclassapp.util.FileConvertUtil.getUriForFile;
import static com.example.wanhao.aclassapp.util.FileSizeUtil.SIZETYPE_KB;

/**
 * Created by wanhao on 2018/3/22.
 */

public class DocumentPresenter implements IBasePresenter {
    private static final String TAG = "DocumentPresenter";

    private IDocumentView view;
    private Context context;

    private Realm realm = Realm.getDefaultInstance();


    public DocumentPresenter(IDocumentView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void getListByCourse(){


    }

    @SuppressLint("CheckResult")
    public void getListByInternet(String courseID){
        String count = SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_COUNT);
        DocumentService service = RetrofitHelper.get(DocumentService.class);

        service.getDocumentList(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN),courseID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "getDocumentList body : "+body);

                    HttpResult<List<Document>> result = new Gson().fromJson(body,new TypeToken<HttpResult<List<Document>>>(){}.getType());

                    if(result.getCode().equals(ApiConstant.RETURN_SUCCESS)){
                        List<Document> temp = result.getData();

                        realm.beginTransaction();
                        for(Document document:temp){
                            document.setCourseID(courseID);
                            document.setUserCount(count);
                            realm.copyToRealmOrUpdate(document);
                        }
                        realm.commitTransaction();

                        view.loadDataSuccess(temp);
                    }else{
                        view.errorMessage(result.getMessage());
                        getListByDB(courseID);
                    }
                }, throwable -> {
                    getListByDB(courseID);
                    view.errorMessage("网络异常");
                });

    }

    public void getListByDB(String courseID){
        realm.beginTransaction();
        String count = SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_COUNT);
        List<Document> list;
        if(TextUtils.isEmpty(courseID)){
            list = realm.where(Document.class)
                    .equalTo("userCount",count)
                    .findAll();
        }else {
            list= realm.where(Document.class)
                    .equalTo("userCount",count)
                    .equalTo("courseID",courseID)
                    .findAll();
        }

        realm.commitTransaction();

        view.loadDataSuccess(list);
    }

    public void checkDocument(Document document){

        double size = FileSizeUtil.getFileOrFilesSize(FileConvertUtil.getFileDocumentPath()+document.getTitle(),SIZETYPE_KB);
        Log.i(TAG, "checkDocument: documentSize = "+size + "documentSize = "+document.getSize());

        if(size == Double.valueOf(document.getSize())){
            openDocument(document);
        }else {
            Intent intent = new Intent(context,BrowseDocumentActivity.class);

            intent.putExtra(ApiConstant.DOCUMENT,document);
            intent.putExtra(ApiConstant.COURSE_ID,document.getCourseID());

            context.startActivity(intent);
        }

    }

    private void openDocument(Document document){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(FileConvertUtil.getFileDocumentPath()+document.getTitle());
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(getUriForFile(context,file), FileConvertUtil.getMIMEType(file));//设置类型
        context.startActivity(intent);
    }

    @Override
    public void destroy() {

    }

    class DocumentList{

    }
}
