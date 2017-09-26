package com.cheng315.lib.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import static com.cheng315.lib.utils.AppUtils.getAppContext;

/**
 * Created by storm on 2017/9/19.
 * 手机操作类相关
 */

public class PhoneUtils {


    /**
     * 获取屏幕相关 displayMetrics
     */
    public static DisplayMetrics obtain(Context context) {
        WindowManager wm =
                (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics dm = new DisplayMetrics();

        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }


    /**
     * 获取手机屏幕宽度
     */
    public static int getPhoneScreenWidth(Context context) {
        DisplayMetrics outMetrics = obtain(context);
        return outMetrics.widthPixels;
    }

    /**
     * 获取手机屏幕的高度
     */
    public static int getPhoneScreenHeight(Context context) {

        return obtain(context).heightPixels;
    }


    /**
     * 获取屏幕的尺寸
     */
    public static int[] getPhoneScreenSize(Context context) {

        DisplayMetrics metrics = obtain(context);

        int[] size = new int[2];

        size[0] = metrics.widthPixels;

        size[1] = metrics.heightPixels;

        return size;
    }


    /**
     * 获取手机的IMEI 值
     * <p>Requires Permission:{@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
     */
    public static String getPhoneIMEI() {
        TelephonyManager tm =
                (TelephonyManager) getAppContext().getSystemService(Context.TELEPHONY_SERVICE);

        return tm.getDeviceId();
    }


    /**
     * 获取手机设备厂商
     */
    public static String getPhoneManufacturers() {

        return Build.MANUFACTURER;


    }


    /**
     * 获取手机型号
     */
    public static String getPhoneModel() {

        return Build.MODEL;

    }


    /**
     * 获取手机系统版本号
     */
    public static String getPhoneSystemVersion() {

        return Build.VERSION.RELEASE;


    }


    /**
     * 获取sdk的版本号
     */
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }


    /**
     * 获取 设备为mac 地址
     */
    public static String getMacAddress() {

        // 通过wifimanager 获取
        String macAddress = getMacAddressByWifiInfo();

        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }

        macAddress = getMacAddressByNetworkInterface();

        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }

        return null;
    }


    /**
     * 通过 wifi 获取mac 地址
     * 低版本的会返回 null 实际"02:00:00:00:00:00"
     * @return
     */
    public static String getMacAddressByWifiInfo() {

        try {
            WifiManager wm =
                    (WifiManager) AppUtils.getAppContext().getSystemService(Context.WIFI_SERVICE);

            if (wm != null) {
                WifiInfo wifiInfo = wm.getConnectionInfo();

                if (wifiInfo != null) {
                    return wifiInfo.getMacAddress();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "02:00:00:00:00:00";
    }


    /**
     * networkinfo 获取 mac地址
     * 7.0 即以上都可以获取到 wlan0 就是wifi mac地址
     */
    public static String getMacAddressByNetworkInterface() {

        try {
            List<NetworkInterface> nis
                    = Collections.list(NetworkInterface.getNetworkInterfaces());

            for (NetworkInterface ni : nis) {

                if (!ni.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macByte = ni.getHardwareAddress();
                if (macByte != null && macByte.length > 0) {
                    StringBuffer res1 = new StringBuffer();

                    for (byte b : macByte) {

                        res1.append(String.format("%02x:", b));
                    }
                    return res1.deleteCharAt(res1.length() - 1).toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "02:00:00:00:00:00";
    }



}
