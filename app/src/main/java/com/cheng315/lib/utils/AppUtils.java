package com.cheng315.lib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.cheng315.chengnfc.MApplication;

import java.io.File;

/**
 * Created by Administrator on 2017/9/4.
 * app 相关工具咧
 */

public class AppUtils {

    private static final String TAG = "AppUtils";


    /**
     * 获取 Application 的context
     *
     * @return
     */
    public static Context getAppContext(){

        return MApplication.appContext;
    }

    /**
     * install apk
     */
    public static void installApk(Context context, File file) {

        Intent intent = new Intent();

        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.getApplicationContext().startActivity(intent);
    }




}
