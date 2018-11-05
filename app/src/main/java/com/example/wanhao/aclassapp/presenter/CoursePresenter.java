package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.DocumentActivity;
import com.example.wanhao.aclassapp.activity.HomeWorkActivity;
import com.example.wanhao.aclassapp.activity.RemarkActivity;
import com.example.wanhao.aclassapp.activity.SignActivity;
import com.example.wanhao.aclassapp.adapter.GridAdapter;
import com.example.wanhao.aclassapp.backService.CourseService;
import com.example.wanhao.aclassapp.backService.DownDocumentService;
import com.example.wanhao.aclassapp.bean.ChatBean;
import com.example.wanhao.aclassapp.bean.ChatResult;
import com.example.wanhao.aclassapp.bean.Course;
import com.example.wanhao.aclassapp.bean.GridBean;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.util.PopupUtil;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.CourseView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.realm.Realm;
import okhttp3.WebSocket;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompHeader;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompCommand;
import ua.naiksoftware.stomp.client.StompMessage;

import static com.example.wanhao.aclassapp.bean.ChatBean.ME;
import static com.example.wanhao.aclassapp.bean.ChatBean.OTHER;
import static com.example.wanhao.aclassapp.config.ApiConstant.BASE_URL;
import static com.example.wanhao.aclassapp.config.ApiConstant.CHAT_URL;

public class CoursePresenter {
    private static final String TAG = "CoursePresenter";

    private Context context;
    private CourseView view;
    private Realm realm;

    private String courseID;

    public CoursePresenter(Context context, CourseView view,String courseId){
        this.context  = context;
        this.view = view;
        this.courseID = courseId;
        realm = Realm.getDefaultInstance();
    }

    public void init(){
        getHistoryList();
        clearUnReadMessage();
    }

    private void getHistoryList(){
        realm.beginTransaction();
        List<ChatBean> list = realm.where(ChatBean.class)
                .equalTo("courseID",Integer.valueOf(courseID))
                .findAllAsync();
        String count = SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_COUNT);
        for(ChatBean bean:list){
            if(bean.getUser().getCount().equals(count)){
                bean.setItemType(ME);
            }else {
                bean.setItemType(OTHER);
            }
        }
        realm.commitTransaction();


        view.getHistoryMessage(list);

    }

    private void  clearUnReadMessage(){
        realm.beginTransaction();
        Course course = realm.where(Course.class)
                .equalTo("id",courseID)
                .findFirst();
        course.setUnReadNum(0);
        realm.commitTransaction();
    }

    public void sendMessage(String message){
        if(TextUtils.isEmpty(message)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("content", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(context,CourseService.class);
        intent.putExtra(ApiConstant.COURSE_ID,courseID);
        intent.putExtra(ApiConstant.COURSE_MESSAGE,jsonObject.toString());
        context.startService(intent);

    }

    public void handleMessage(ChatBean bean){
        view.getMessage(bean);
    }

    private static final String[] OTHER_TITLE =
            {"留言版", "课后作业", "课堂文件", "公告",
                    "课程信息", "聊天纪录", "课堂签到"};
    private static final int[] OTHER_IMG =
            {R.mipmap.gv_animation, R.mipmap.gv_multipleltem, R.mipmap.gv_header_and_footer, R.mipmap.gv_pulltorefresh,
                    R.mipmap.gv_section, R.mipmap.gv_empty, R.mipmap.gv_drag_and_swipe};
    private static final Class[] CLASSES =
            {RemarkActivity.class,HomeWorkActivity.class,DocumentActivity.class,RemarkActivity.class,
                    RemarkActivity.class,RemarkActivity.class,SignActivity.class};

    public void openSelectAvatarDialog(){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_course_more, null);
        final PopupWindow popupWindow = PopupUtil.getPopupWindow(context,view);
        //设置点击事件
        RecyclerView recyclerView = view.findViewById(R.id.dialog_course_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        ArrayList<GridBean> otherList = new ArrayList<>();
        for(int x=0;x<OTHER_TITLE.length;x++){
            GridBean bean= new GridBean(OTHER_IMG[x],OTHER_TITLE[x]);
            otherList.add(bean);
        }
        GridAdapter adapter = new GridAdapter(otherList,context);
        adapter.setOnItemClickListener((adapter1, view1, position) -> {
            Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show();
            this.view.startActivity(CLASSES[position],null);
            popupWindow.dismiss();
        });
        recyclerView.setAdapter(adapter);
        View parent = LayoutInflater.from(context).inflate(R.layout.content_course, null);
        //显示PopupWindow
        popupWindow.setAnimationStyle(R.style.animTranslate);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }
}
