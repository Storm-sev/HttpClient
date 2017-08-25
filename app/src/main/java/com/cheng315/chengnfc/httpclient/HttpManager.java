package com.cheng315.chengnfc.httpclient;

import com.cheng315.chengnfc.bean.VersionBean;
import com.cheng315.chengnfc.httpclient.downloadfile.FileCallBack;
import com.cheng315.chengnfc.httpclient.downloadfile.FileSubscriber;
import com.cheng315.chengnfc.utils.LogUtils;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/25.
 */

public class HttpManager {


    private static final String TAG = "HttpManager";


    /**
     * 下载执行请求
     *
     * @param url
     * @param fileCallBack
     */
    public static void load(String url, final FileCallBack<ResponseBody> fileCallBack) {


        Observable<ResponseBody> responseBodyObservable
                = HttpClientManager.getHttpClientService().downLoadNewApk(url);

        responseBodyObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        fileCallBack.saveFile(responseBody);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FileSubscriber<ResponseBody>(fileCallBack));


    }

    public static void checkVersionCode(String curVersionCode) {

        Observable<VersionBean> versionBeanObservable =
                HttpClientManager.getHttpClientService().checkVersionService(curVersionCode);


        versionBeanObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VersionBean>() {
                    @Override
                    public void onCompleted() {


                        LogUtils.d(TAG, "onCompleted : 请求完成");
                    }

                    @Override
                    public void onError(Throwable e) {

                        LogUtils.d(TAG, "onError : " + e.toString());

                    }

                    @Override
                    public void onNext(VersionBean versionBean) {


                        LogUtils.d(TAG, " onNext 获取链接的版本 判断 :  " + versionBean.getCode());

                        int code = Integer.valueOf(versionBean.getCode());

                        if (code == 0) {
                            LogUtils.d(TAG, "版本有更新 : " + versionBean.getData().getApkUrl());

                        } else if (code == 7) {
                            LogUtils.d(TAG, "当前为最新版本 ;　" + versionBean.getDescription());
                        }


                    }
                });

    }


}
