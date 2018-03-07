package com.example.wanhao.aclassapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import com.example.wanhao.aclassapp.base.BaseApplication;
import com.example.wanhao.aclassapp.config.ApiConstant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static android.os.Environment.DIRECTORY_PICTURES;

public class FileConvertUtil {

    //文件保存的路径
    public static final String FILE_PATH = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) +
            "/" + SaveDataUtil.getValueFromSharedPreferences(BaseApplication.getContext(), ApiConstant.USER_NAME)+
            "/classroom";

    /**
     * 删除一个目录下的所有文件
     * @param dir
     * @return
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 向本地SD卡写网络图片
     *
     * @param bitmap
     */
    public static Uri saveBitmapToLocal(String fileName, Bitmap bitmap) {
        try {
            // 创建文件流，指向该路径，文件名叫做fileName
            File file = new File(FILE_PATH, fileName);
            // file其实是图片，它的父级File是文件夹，判断一下文件夹是否存在，如果不存在，创建文件夹
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                // 文件夹不存在
                fileParent.mkdirs();// 创建文件夹
            }
            // 将图片保存到本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(file));

            return Uri.fromFile(file);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从本地SD卡获取缓存的bitmap
     */
    public static Bitmap getBitmapFromLocal(String fileName) {
        try {
            File file = new File(FILE_PATH, fileName);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
                        file));
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
