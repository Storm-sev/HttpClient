package com.cheng315.chengnfc.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2017/8/30.
 * 系统崩溃处理
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {


    private static final String TAG = "CrashHandler ";


    private static final boolean DEBUG = true;

    /**
     * 异常文件的存储路径
     */
    private static final String PATH = Environment.getExternalStorageDirectory()
            .getPath() + "/cheng_nfc/log/";


    // 文件名
    private static final String FILE_NAME = "Crash";

    /**
     * 文件的后缀名
     */
    private static final String FILE_NAME_SUFFIX = ".log";


    private static CrashHandler mInstance = new CrashHandler();

    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    private Context mContext;


    private CrashHandler() {

    }


    public static CrashHandler getInstance() {
        return mInstance;

    }


    /**
     * 这里做初始化操作
     */
    public void init(Context context) {

        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //当前实例作为系统默认的异常处理
        Thread.setDefaultUncaughtExceptionHandler(mDefaultCrashHandler);
        this.mContext = context.getApplicationContext();

    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        // 异常信息保存到本地
        try {
            saveExceptionToSDCard(ex);
        } catch (IOException e) {
        }
        // 把异常上传到服务器


    }


    /**
     *
     */
    private void saveExceptionToSDCard(final Throwable ex) throws  IOException{

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_BAD_REMOVAL)) {
            if (DEBUG) {
                Log.w(TAG, "sdcard unmounted, skip dump exception");
                return;
            }

        }

        File dir = new File(PATH);

        if (!dir.exists()) {
            Log.d(TAG, "log dir not exists,ready create");
            dir.mkdirs();
        }


        //获取时间
        long currentMillis = System.currentTimeMillis();

        final String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentMillis));

        final File file = new File(PATH + FILE_NAME + FILE_NAME_SUFFIX);

        /**
         * 开启一个子线程写入
         */
        new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                    pw.println(time);

                    dumpPhoneInfo(pw,ex);
                    pw.close();

                } catch (Exception e) {

                    Log.e(TAG, "dump crash info filed : " + Log.getStackTraceString(ex));


                }


            }
        }).start();


    }


    /**
     * 获取当前设备的信息
     */
    private void dumpPhoneInfo(PrintWriter pw, Throwable ex) throws PackageManager.NameNotFoundException {

        Log.d(TAG, "dumpPhoneInfo ...... start ");

        PackageManager pm =
                mContext.getPackageManager();

        PackageInfo pi
                = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);


        pw.print("App Version :　");
        pw.print(pi.versionName);
        //
        pw.print("_" );
        pw.println(pi.versionCode);


        //获取android 的版本号码
        pw.print("OS Version : ");
        pw.print(Build.VERSION.RELEASE);
        pw.print(" _SDK : ");
        pw.println(Build.VERSION.SDK_INT);

        // 获取手机制造商

        pw.print("Model : ");
        pw.print(Build.MODEL);


        // 获取的CPu 架构
        pw.print("CPU ABI : ");
        pw.println(Build.CPU_ABI);
        pw.println();

        // 异常信息
        ex.printStackTrace(pw);

    }


}
