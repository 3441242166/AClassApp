package com.example.wanhao.aclassapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.wanhao.aclassapp.base.BaseApplication;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.DownService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileConvertUtil {
    private static final String TAG = "FileConvertUtil";

    //文件保存的路径
    public static final String FILE_IMAGE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +
            "/classroom" +
            "/" + SaveDataUtil.getValueFromSharedPreferences(BaseApplication.getContext(), ApiConstant.COUNT);

    public static final String FILE_DOCUMENT_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +
            "/classroom" +
            "/" + SaveDataUtil.getValueFromSharedPreferences(BaseApplication.getContext(), ApiConstant.COUNT);

    /**
     * 向本地SD卡写网络图片
     *
     * @param bitmap
     */
    public static Uri saveBitmapToLocal(String fileName, Bitmap bitmap) {
        try {
            // 创建文件流，指向该路径，文件名叫做fileName
            File file = new File(FILE_IMAGE_PATH +"/image", fileName);
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
            File file = new File(FILE_IMAGE_PATH +"/image", fileName);
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

    public static String getDocumentFilePath(){
        return FILE_DOCUMENT_PATH +"/document";
    }

    public static void saveDocument(InputStream is,long size,String title, DownService.DownloadListener downloadListener){
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        // 储存下载文件的目录
        String savePath = FileConvertUtil.getDocumentFilePath();
        try {
            long total = size;
            File file = new File(savePath, title);
            fos = new FileOutputStream(file);
            long sum = 0;
            int progress = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                sum += len;
                progress = (int) (sum * 1.0f / total * 100);
                Log.i(TAG, "accept: progress "+progress);

            }
            fos.flush();

        } catch (Exception e) {
            Log.i(TAG, "saveDocument: "+e.toString());
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                Log.i(TAG, "saveDocument: "+e.toString());
            }
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                Log.i(TAG, "saveDocument: "+e.toString());
            }
        }
    }

}
