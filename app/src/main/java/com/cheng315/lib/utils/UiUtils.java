package com.cheng315.lib.utils;

import android.app.KeyguardManager;
import android.content.Context;

import static com.cheng315.lib.utils.AppUtils.getAppContext;

/**
 * Created by storm on 2017/9/15.
 * 屏幕相关工具类
 */

public class UiUtils {


    /**
     * 获取屏幕的宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        return AppUtils.getAppContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕的高度
     *
     * @return
     */
    public static int getScreenHeight() {

        return AppUtils.getAppContext().getResources().getDisplayMetrics().heightPixels;
    }


    /**
     * 屏幕是否在锁屏状态
     *
     * @return
     */
    public static boolean isScreenLock() {

        KeyguardManager km =
                (KeyguardManager) AppUtils.getAppContext().getSystemService(Context.KEYGUARD_SERVICE);

        return km.inKeyguardRestrictedInputMode();

    }

    /**
     * dp 转化px
     *
     * @param dpValue dp 转化px
     * @return
     */
    public static int dip2px(float dpValue) {
        final float scale = AppUtils.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * px 转化 dp
     *
     * @param pxValue
     * @return
     */
    public static int px2dip(float pxValue) {
        final float scale = getAppContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


}
