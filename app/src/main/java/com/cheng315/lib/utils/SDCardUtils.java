package com.cheng315.lib.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by storm on 2017/9/15.
 * SDCard 操作相关工具类
 */

public class SDCardUtils {


    /**
     * 判断Sd卡是否挂载 .
     * @return ture 可用
     */
    public static boolean isSDCardEnable(){

        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());

    }

    /**
     * 获取 sdcard 路径
     * @return path
     */
    public static String  getSDCardPath(){

        if(!isSDCardEnable()) return null;

        return Environment.getExternalStorageDirectory().getPath() + File.separator;
    }

    /**
     * 获取 SD卡 data 路径
     * @return
     */
    public static String getDataPath(){

        if(!isSDCardEnable()) return null;

        return Environment.getExternalStorageDirectory().getPath() + File.separator + "data" + File.separator;
    }


}
