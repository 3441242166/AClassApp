package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.ChatHistoryActivity;
import com.example.wanhao.aclassapp.activity.DocumentActivity;
import com.example.wanhao.aclassapp.activity.HomeWorkActivity;
import com.example.wanhao.aclassapp.activity.RemarkActivity;
import com.example.wanhao.aclassapp.activity.SignActivity;
import com.example.wanhao.aclassapp.adapter.GridAdapter;
import com.example.wanhao.aclassapp.backService.CourseService;
import com.example.wanhao.aclassapp.base.IBasePresenter;
import com.example.wanhao.aclassapp.bean.GridBean;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.config.Constant;
import com.example.wanhao.aclassapp.db.ChatDB;
import com.example.wanhao.aclassapp.db.CourseDB;
import com.example.wanhao.aclassapp.util.GsonUtils;
import com.example.wanhao.aclassapp.util.PopupUtil;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.CourseView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;

import static com.example.wanhao.aclassapp.config.ApiConstant.COURSE_ID;

public class CoursePresenter implements IBasePresenter {
    private static final String TAG = "CoursePresenter";
    private static int LOAD_NUM = 15;

    private Context context;
    private CourseView view;
    private Realm realm;

    private CourseDB course;
    private List<ChatDB> chatAllList;
    private LinkedList<ChatDB> showList;
    int chatSum = 0;

    public CoursePresenter(Context context, CourseView view){
        this.context  = context;
        this.view = view;
    }

    public void init(String courseId){
        realm = Realm.getDefaultInstance();
        EventBus.getDefault().register(this);

        realm.beginTransaction();
        course = realm.where(CourseDB.class)
                .equalTo("courseID",courseId)
                .findFirst();
        //未读消息设为0
        course.setUnRead(0);

        realm.commitTransaction();

        view.initView(course);

        getHistoryList();

    }

    private void getHistoryList(){
        showList = new LinkedList<>();

        realm.beginTransaction();

        chatAllList = realm.where(ChatDB.class)
                .equalTo("courseID",course.getCourseID())
                .findAll();

        String count = SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_COUNT);
//        if(list == null || list.size()==0){
//            Log.i(TAG, "getHistoryList: 无历史记录");
//            realm.commitTransaction();
//            return;
//        }
        for(ChatDB bean:chatAllList){
            if(bean.getUser().getCount().equals(count)){
                bean.setItemType(ChatDB.USER_ME);
            }else {
                bean.setItemType(ChatDB.USER_OTHER);
            }
        }

        realm.commitTransaction();

        Log.i(TAG, "getHistoryList: list.size = "+chatAllList.size());

        loadMore();
        view.loadDataSuccess(showList);
    }

    public void loadMore(){
        Log.i(TAG, "loadMore");

        int startPos = chatAllList.size() - chatSum - 1;
        if(startPos < 0){
            view.errorMessage("没有更多咯");
            return;
        }

        for(int x = 0;x<LOAD_NUM && startPos >=0 ;x++,startPos--){
            showList.addFirst(chatAllList.get(startPos));
        }
        chatSum +=LOAD_NUM;

        view.notifyDataChange(false);
    }

    public void sendMessage(String message){
        if(TextUtils.isEmpty(message)){
            return;
        }
        HashMap<String,String> map = new HashMap<>();
        map.put("content", message);

        String msg = GsonUtils.toJson(map);

        Intent intent = new Intent(context,CourseService.class);
        intent.putExtra(ApiConstant.IM_ACTION ,ApiConstant.MESSAGE_CHAT);
        intent.putExtra(ApiConstant.COURSE_ID ,course.getCourseID());
        intent.putExtra(ApiConstant.COURSE_MESSAGE ,msg);

        context.startService(intent);
    }

    @Subscribe(priority = 999)
    public void handleIMMessage(ChatDB bean) {
        Log.i(Constant.TAG_EVENTBUS, "CoursePresenter: content = "+bean.getContent());

        if((bean.getCourseID()).equals(course.getCourseID())){

            showList.add(bean);
            view.notifyDataChange(true);

        }else {
            realm.beginTransaction();
            //设置未读消息
            CourseDB course = realm.where(CourseDB.class)
                    .equalTo("courseID",bean.getCourseID())
                    .findFirst();
            course.setUnRead(course.getUnRead()+1);

            realm.commitTransaction();
        }

        EventBus.getDefault().cancelEventDelivery(bean);
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

    private static final String[] OTHER_TITLE =
            {"留言版", "课后作业", "课堂文件", "公告",
                    "课程信息", "聊天纪录", "课堂签到"};
    private static final int[] OTHER_IMG =
            {R.mipmap.gv_animation, R.mipmap.gv_multipleltem, R.mipmap.gv_header_and_footer, R.mipmap.gv_pulltorefresh,
                    R.mipmap.gv_section, R.mipmap.gv_empty, R.mipmap.gv_drag_and_swipe};
    private static final Class[] CLASSES =
            {RemarkActivity.class,HomeWorkActivity.class,DocumentActivity.class,RemarkActivity.class,
                    RemarkActivity.class,ChatHistoryActivity.class,SignActivity.class};

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
            //this.view.startActivity(CLASSES[position],null);
            Intent intent = new Intent(context ,CLASSES[position]);
            intent.putExtra(COURSE_ID ,course.getCourseID());
            context.startActivity(intent);
            popupWindow.dismiss();
        });
        recyclerView.setAdapter(adapter);
        View parent = LayoutInflater.from(context).inflate(R.layout.content_course, null);
        //显示PopupWindow
        popupWindow.setAnimationStyle(R.style.animTranslate);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }


}
