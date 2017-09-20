package com.cheng315.lib.utils;

/**
 * Created by storm on 2017/9/19.
 * string 操作工具类
 */

public class StirngUtils {

    /**
     * 获取 文件名
     */

    public static String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

}
