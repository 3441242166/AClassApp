package com.example.wanhao.aclassapp.config;

/**
 * 网络请求常量
 */

public class ApiConstant {
    public static final String BASE_URL = "http://toppest.ink:8086/";
    public static final String DOCUMENT_URL = "http://toppest.ink:8085/";

    public static final String HEAD_URL = BASE_URL+"auth/avatar";
    public static final String CHAT_URL = "ws://toppest.ink:8086/im/websocket";

    public final static String RETURN_SUCCESS ="200";
    public final static String RETURN_ERROR ="FAILED";
    /**********************储存token的时间************************/
    public static final String TOKEN_TIME = "token_time";
    public static final String USER_TOKEN = "token";
    public static final String USER_ROLE = "role";
    public static final String USER_COUNT = "count";
    public static final String USER_PASSWORD = "password";
    public static final String USER_NAME = "username";
    public static final String USER_SIGNATURE = "signature";
    /*********************文件命名相关********************************/
    public static final String  FILE_NAME = "fzkt";
    public static final int ADD_COURSE = 1;
    public static final String RESULT_ADD = "add_result";
    public static final int ADD_SUCCESS = 1;
    public static final int ADD_ERROR = 0;
    /**********************ActivityForResult的请求码************************/
    public static final String ACTIVITY_DATA = "data";
    public static final int CAMERA_CODE = 1;
    public static final int GALLERY_CODE = 2;

    /**********************用户上传的头像的名称************************/
    public static final String USER_AVATAR_NAME = "avatar.jpg";
    public static final String AVATAR_IMG_PATH = "classroom";

    /**********************Course有关Key************************/
    public static final String COURSE_NAME = "course_name";
    public static final String COURSE_BEAN = "course_bean";
    public static final String COURSE_ID = "course_id";
    public static final String COURSE_MESSAGE = "course_message";
    public static final String COURSE_ACTION= "com.example.wanhao.aclassapp.im";
    /**********************Document有关Key************************/
    public static final String DOCUMENT = "document";
    public static final String DOCUMENT_ID = "document_id";
    public static final String DOCUMENT_TYPE = "document_type";
    public static final String DOCUMENT_EDATA = "edata";
    public static final String DOCUMENT_PREVIEW = "preview";
    /**********************下载有关Key************************/
    public static final String DOWNLOAD_ID = "download_id";
    public static final String DOWNLOAD_ACTION = "com.example.wanhao.aclassapp.download_action";
    /**********************Notification 有关************************/
    public static final String CHANNE_DOWNED_NAME = "下载通知信息";
    public static final String CHANNE_DOWNED_ID = "1";

    public static final String CHANNE_IM_NAME = "聊天通知信息";
    public static final String CHANNE_IM_ID = "2";
    /**********************broadcast 有关************************/
    public static final String DOWNED_BEAN= "downloadBean";
    public static final String DOWNED_STATE = "state";
    public static final String DOWNED_PROCESS = "process";
    public static final String DOWNED_FINISH = "finish";
    public static final String DOWNED_ING = "ing";
    public static final String DOWNED_BEGIN = "begin";
    public static final String DOWNED_ID = "id";

    public enum DOWNLOAD_STATE{
        NONE,ING,STOP,FINISH
    }
}
