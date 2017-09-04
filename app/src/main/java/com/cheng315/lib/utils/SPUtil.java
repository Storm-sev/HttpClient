package com.cheng315.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SimpleArrayMap;

import com.cheng315.chengnfc.MApplication;

import java.util.Map;


/**
 * Created by storm 2017/9/4.orm
 * <p>
 * ap 存储
 */

public class SPUtil {


    private static final String TAG = "SPUtil";

    private static SimpleArrayMap<String, SPUtil> SP_UTILS_MAP = new SimpleArrayMap<>();
    private static SharedPreferences sp;

    private SPUtil(String spName) {

        sp = MApplication.appContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    public static SPUtil getInstance() {
        return getInstance("");
    }


    /**
     * 获取spUtil
     */
    private static SPUtil getInstance(String spName) {

        if (isSpace(spName)) {
            spName = "storm";
        }

        SPUtil spUtil = SP_UTILS_MAP.get(spName);
        if (spUtil == null) {
            spUtil = new SPUtil(spName);
            SP_UTILS_MAP.put(spName, spUtil);
        }


        return spUtil;
    }


    // ----------------存储或读取值

    /**
     * 存储 String 类型的值
     *
     * @param key
     * @param value
     */
    private void put(@NonNull String key, @Nullable String value) {

        sp.edit().putString(key, value).apply();
    }


    /**
     * 读取sp中String
     *
     * @param key
     */
    private String getString(@NonNull String key) {

        return sp.getString(key, "");
    }


    /**
     * 读取 sp 中的String 类型的值 如果不存在 返回默认的值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    private String getString(@NonNull String key, String defaultValue) {

        return sp.getString(key, defaultValue);

    }


    /**
     * 存 int  类型的值
     *
     * @param key
     * @param value
     */
    private void put(@NonNull String key, int value) {

        sp.edit().putInt(key, value).apply();

    }


    /**
     * 读取 int 值
     *
     * @param key
     */
    private int getInt(@NonNull String key) {

        return sp.getInt(key, -1);

    }


    /**
     * 读 int 类型的值 没有返回默认值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    private int getInt(@NonNull String key, int defaultValue) {

        return sp.getInt(key, defaultValue);

    }


    /**
     * 存 float 值
     *
     * @param key
     * @param value
     */
    private void put(@NonNull String key, float value) {

        sp.edit().putFloat(key, value).apply();

    }


    /**
     * 取 float 类型
     *
     * @param key
     * @return
     */
    private float getFloat(@NonNull String key) {

        return sp.getFloat(key, -1f);
    }


    /**
     * 取 float 类型的值  有 返回 没有 返回默认值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    private float getFloat(@NonNull String key, float defaultValue) {

        return sp.getFloat(key, defaultValue);
    }


    /**
     * 存 long 类型
     *
     * @param key
     * @param value
     */
    private void put(@NonNull String key, long value) {

        sp.edit().putLong(key, value).apply();

    }

    /**
     * 取 long 类型
     *
     * @param key
     * @return
     */
    private long getLong(@NonNull String key) {
        return sp.getLong(key, -1L);
    }

    /**
     * 取 long 类型 有 返回  没有返回 默认值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    private long getLong(@NonNull String key, long defaultValue) {

        return sp.getLong(key, defaultValue);
    }


    /**
     * 存 boolean 类型的值
     *
     * @param key
     * @param value
     */
    private void put(@NonNull String key, boolean value) {

        sp.edit().putBoolean(key, value).apply();

    }


    /**
     * 取 boolean 类型的值
     *
     * @param key
     * @return
     */
    private boolean getBoolean(@NonNull String key) {

        return sp.getBoolean(key, false);
    }


    /**
     * 取 boolean 类型的值 有 返回
     *
     * @param key
     * @param defaultValue
     * @return
     */
    private boolean getBoolean(@NonNull String key, boolean defaultValue) {

        return sp.getBoolean(key, defaultValue);
    }


    private Map<String, ?> getAll() {
        return sp.getAll();
    }

    /**
     * 移除当前key
     *
     * @param key
     */
    private void remove(@NonNull String key) {

        sp.edit().remove(key).apply();
    }


    /**
     * 移除 sp 中的所有数据
     */
    private void clear() {
        sp.edit().clear().apply();

    }


    /**
     * 判断字符串是否为null 或者全部为空吧字符
     */
    private static boolean isSpace(String str) {
        if (str == null) return true;
        for (int i = 0, len = str.length(); i < len; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }


        return true;
    }


}
