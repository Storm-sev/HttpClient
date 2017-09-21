package com.cheng315.chengnfc.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.cheng315.chengnfc.MApplication;
import com.cheng315.chengnfc.R;
import com.cheng315.chengnfc.utils.AppUtils;
import com.cheng315.lib.httpclient.HttpManager;
import com.cheng315.lib.httpclient.downloadfile.FileCallBack;
import com.cheng315.lib.utils.RxBus;
import com.cheng315.lib.utils.LogUtils;

import java.io.File;

import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/8/28.
 * 更新版本的服务
 */

public class UpdateService extends Service {

    private static final String TAG = UpdateService.class.getSimpleName();

    public static final String APK_URL = "apk_url";

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;


    private RemoteViews mRemoteViews;

    private int preProgress;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotification();

        downLoadApk(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     *
     */
    private void downLoadApk(Intent intent) {

        if (intent != null) {

            final String apkUrl = intent.getStringExtra(UpdateService.APK_URL);

            HttpManager.load(apkUrl, new FileCallBack<ResponseBody>(
                    Environment.getExternalStorageDirectory().getAbsolutePath(),
                    AppUtils.getNameFromUrl(apkUrl)) {

                @Override
                public void onSuccess(ResponseBody responseBody) {


                }

                @Override
                public void onStart() {


                }

                @Override
                public void onComplete() {
                    File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

                    File file = new File(fileDir, AppUtils.getNameFromUrl(apkUrl));

                    if (file.exists()) {

//                        AppUtils.installApp(UpdateService.this, file);
                    }


                    File cacheFile = new File(MApplication.appContext.getCacheDir(), "HttpCache");
                    if (cacheFile.exists()) {
                        LogUtils.d(TAG, "获取的文件存在");
                    } else {
                        LogUtils.d(TAG, " 获取的缓存文件不存在 ");
                    }


                    LogUtils.d(TAG, "检测 rxbus 背压是否 解除订阅  :  " + RxBus.getInstance().hasSubscribers(true));

                    stopSelf();
                }

                @Override
                public void onError(Throwable e) {


                }

                @Override
                public void onProgress(float progress, long total) {

//                    LogUtils.d(TAG, "获取的消息 : " + (int) (progress * 100) + "%" + "文件的总长度 : " + total);

                    updateUI(progress);


                }
            });
        } else {

            // xzish
            stopSelf();

        }

    }

    /**
     * 更新ui
     *
     * @param progress
     */
    private void updateUI(float progress) {

        int curProgress = (int) (progress * 100);


        if (preProgress < curProgress) {

            mRemoteViews.setProgressBar(R.id.pb_download, 100, curProgress, false);
            mRemoteViews.setTextViewText(R.id.tv_tool__title, "诚品宝:" + curProgress + "%");

            mNotificationManager.notify(0, mBuilder.build());
        }

        preProgress = curProgress;

    }


    /**
     * 创建 通知栏信息
     */
    private void createNotification() {

        mBuilder = new NotificationCompat.Builder(this);
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        mRemoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_update);
        mRemoteViews.setProgressBar(R.id.pb_download, 100, 0, false);
        mRemoteViews.setTextViewText(R.id.tv_tool__title, "诚品宝: 0%");

        mBuilder.setContent(mRemoteViews);
        mBuilder.setSmallIcon(R.drawable.icon_logo);

        mNotificationManager.notify(0, mBuilder.build());

    }


}


