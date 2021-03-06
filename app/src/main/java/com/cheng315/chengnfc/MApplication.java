package com.cheng315.chengnfc;

import android.app.Application;
import android.content.Context;

import com.cheng315.chengnfc.utils.validations.EditTextValidator;
import com.cheng315.lib.utils.LogUtils;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Administrator on 2017/8/24.
 */

public class MApplication extends Application {


    public static Context appContext;
    public static LogUtils.Builder mBuilder;

    private static EditTextValidator editTextValidator;


//    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this;
        mBuilder = new LogUtils.Builder()
                .setLogSwitch(true)
                .setGlobalTag("LJY")// 设置log全局标签，默认为空
                // 当全局标签不为空时，我们输出的log全部为该tag，
                // 为空时，如果传入的tag为空那就显示类名，否则显示tag
                .setLog2FileSwitch(false)// 打印log时是否存到文件的开关，默认关
                .setBorderSwitch(true)// 输出日志是否带边框开关，默认开
                .setLogFilter(LogUtils.V);// log过滤器，和logcat过滤器同理，默认Verbose

        editTextValidator = new EditTextValidator(this);

        setUpLeakCanary();
        // 获取全局异常信息
//        CrashHandler.getInstance().init(this);


        // 内存泄漏检测
//        refWatcher = LeakCanary.install(this);

    }

    /**
     * 内存泄漏检测
     */
    private void setUpLeakCanary() {

        if (LeakCanary.isInAnalyzerProcess(this)) {
         return;
        }

        LeakCanary.install(this);
    }


    public static EditTextValidator getSingleEditTextValidator(){
        return editTextValidator;
    }


}
