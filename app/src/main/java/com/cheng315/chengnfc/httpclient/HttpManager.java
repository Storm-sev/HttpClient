package com.cheng315.chengnfc.httpclient;

import com.cheng315.chengnfc.bean.VersionBean;
import com.cheng315.chengnfc.httpclient.downloadfile.FileCallBack;
import com.cheng315.chengnfc.httpclient.downloadfile.FileSubscriber;
import com.cheng315.chengnfc.utils.LogUtils;
import com.cheng315.chengnfc.utils.RxBus;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FileSubscriber<ResponseBody>(fileCallBack));


    }


    /**
     * 文件上传
     */
    public static void upLoadFile(String filePath) {


        // 构造需要上传的文件
        File file = new File(filePath);

        //创建RequestBody
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//      MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        /*
         createFormData 参数 和后台约定好的key
          file 是上传时候的key , 根据需要改成自己的
         */
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        /*
         添加描述

         */
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), "file");


        Observable<ResponseBody> responseBodyObservable =

                HttpClientManager.getHttpClientService().uploadFile(description, body);
    }


    /**
     * 检查版本是否一致 api
     */
    public static void checkVersionCode(String curVersionCode, final CommonCallBack commonCallBack) {

        Observable<VersionBean> versionBeanObservable =
                HttpClientManager.getHttpClientService().checkVersionService(curVersionCode);


        versionBeanObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VersionBean>() {
                    @Override
                    public void onCompleted() {

                        LogUtils.d(TAG, "onCompleted : 请求完成");

                        if (commonCallBack != null) {
                            commonCallBack.onComplete();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        LogUtils.d(TAG, "onError : " + e.toString());

                        if (commonCallBack != null) {
                            commonCallBack.onError(e);

                        }
                    }

                    @Override
                    public void onNext(VersionBean versionBean) {


                        int code = Integer.valueOf(versionBean.getCode());

                        if (commonCallBack != null) {
                            commonCallBack.onNext(versionBean);

                        }

                        RxBus.getInstace().send("发送数据到 rxbus");

                    }
                });

    }


}
