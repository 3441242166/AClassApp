package com.example.wanhao.aclassapp.config;

/**
 * Created by wanhao on 2018/1/31.
 */

public class ApiConstant {
    public static final String BASE_URL = "https://api.fc.xd.style/";

    public final static String RETURN_SUCCESS ="SUCCESS";
    //用户token
    public static final String USER_TOKEN = "token";
    public static final String USER_ROLE = "role";
    public static final String COUNT = "count";
    public static final String PASSWORD = "password";
    public static final String USER_NAME = "username";
    /*********************文件命名相关********************************/
    public static final String  FILE_NAME = "fckt";

    public static final int ADD_SUCCESS = 1;
    public static final int ADD_ERROR = 0;
    /**********************ActivityForResult的请求码************************/
    public static final int CAMERA_CODE = 1;
    public static final int GALLERY_CODE = 2;

    /**********************用户上传的头像的名称************************/
    public static final String USER_AVATAR_NAME = "avatar.jpg";
    public static final String AVATAR_IMG_PATH = "classroom";

    /**********************储存token的时间************************/
    public static final String TOKEN_TIME = "token_time";

    /**********************Course有关Key************************/
    public static final String COURSE_NAME = "course_name";
    public static final String COURSE_ID = "course_id";
}
