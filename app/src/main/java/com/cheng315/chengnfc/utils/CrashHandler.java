package com.cheng315.chengnfc.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/30.
 *  捕获全局的系统异常处理
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {


    private static final String TAG = "CrashHandler ";


    private static final boolean DEBUG = true;

    /**
     * 异常文件的存储路径
     */
    private static final String PATH = Environment.getExternalStorageDirectory()
            .getPath() + "/cheng_nfc/log/";

    /**
     * 文件名
     */
    private static final String FILE_NAME = "Crash";

    /**
     * 文件的后缀名
     */
    private static final String FILE_NAME_SUFFIX = ".log";


    private static CrashHandler mInstance = new CrashHandler();

    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    private Context mContext;

    /**
     * 时间
     */
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * 存储设备的参数信息
     */
    private Map<String, String> phoneInfo = new HashMap<>();


    private CrashHandler() {

    }


    public static CrashHandler getInstance() {
        return mInstance;

    }

    /**
     * 初始化
     */
    public void init(Context context) {
        this.mContext = context.getApplicationContext();
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //当前实例作为系统默认的异常处理
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {


        if (!handlerException(ex) && mDefaultCrashHandler != null) {

            mDefaultCrashHandler.uncaughtException(thread, ex);

        } else {

            SystemClock.sleep(3000);

            // kill progress
            // 退出
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);


        }


    }

    /**
     * 自定义错误异常处理 搜集错误的异常信息;
     */
    private boolean handlerException(Throwable ex) {

        Log.d(TAG, "handlerException .... start");
        if (ex == null) {
            return false;
        }

        new Thread() {
            @Override
            public void run() {

                Looper.prepare();
                Toast.makeText(mContext, "程序出现错误.即将退出", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        // 获取设备的信息
        saveException2sdCard(ex);

        return true;

    }

    private void saveException2sdCard(final Throwable ex) {

        Log.d(TAG, "saveException to sdCard is  ... start: ");

        String filePath = null;

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            Log.d(TAG, " sdCard is unmounted ");

            filePath = mContext.getFilesDir().getPath() + "/cheng_nfc/log/";
        } else {

            Log.d(TAG, " sdCart is mounted ... ");
            filePath = Environment.getExternalStorageDirectory().getPath() + "/cheng_nfc/log/";
        }


        // 获取存储的文件路径
        File fileDir = new File(filePath);
        if (!fileDir.exists()) {

            Log.d(TAG, "LOG file is not exists , now ready create");
            fileDir.mkdirs();
        }


        long currentTime = System.currentTimeMillis();
        //当前时间
        final String curTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTime));

        final File file = new File(filePath + FILE_NAME + FILE_NAME_SUFFIX);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                    pw.print(curTime);

                    dumpPhoneInfo(pw,ex);

                    pw.close();

                    Log.d(TAG, "exception file is complete . ");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }

    /**
     * 获取当前设备的信息
     */
    private void dumpPhoneInfo(PrintWriter pw, Throwable ex) {

        Log.d(TAG, "dumpPhoneInfo ...... start ");

        try {
            PackageManager pm =
                    mContext.getPackageManager();

            PackageInfo pi
                    = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);


            pw.print("App Version :　");
            pw.print(pi.versionName);
            //
            pw.print("_");
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
        } catch (PackageManager.NameNotFoundException e) {

            Log.d(TAG, "Error while collect package info ;", e);

        }

    }


}
