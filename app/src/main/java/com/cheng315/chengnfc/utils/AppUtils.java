package com.cheng315.chengnfc.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by Administrator on 2017/8/28.
 * 工具类
 */

public class AppUtils {



    /**
     * 获取下载文件链接中获取文件名
     */
    public static String getNameFromUrl(String url) {

        return url.substring(url.lastIndexOf("/") + 1);


    }

    /**
     * install app
     * 更新app
     */
    public static void installApp(Context context, File file) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.getApplicationContext().startActivity(intent);

    }



}
