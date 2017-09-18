package com.cheng315.lib.httpclient;

import com.cheng315.chengnfc.bean.VersionBean;
import com.cheng315.lib.httpclient.downloadfile.FileCallBack;
import com.cheng315.lib.httpclient.downloadfile.FileSubscriber;
import com.cheng315.lib.utils.LogUtils;
import com.cheng315.lib.utils.RxBus;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


/**
 * Created by Administrator on 2017/8/25.
 *
 */

public class HttpManager {


    private static final String TAG = HttpManager.class.getSimpleName();


    /**
     * 下载执行请求
     */
    public static void load(String url, final FileCallBack<ResponseBody> fileCallBack) {

        Observable<ResponseBody> responseBodyObservable
                = HttpClientManager.getDownLadService().downLoadNewApk(url);

        responseBodyObservable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
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
     * 新版本 检查版本
     */
    public static void checkVersion(String curVersionCode, final CommonCallBack commonCallBack) {

        Observable<VersionBean> versionBeanObservable =
                HttpClientManager.getHttpClientService().checkVersionService(curVersionCode);
        versionBeanObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VersionBean>() {
                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        this.mDisposable = d;

                        LogUtils.d(TAG, "检查版本  : onSubscribe");


                    }

                    @Override
                    public void onNext(@NonNull VersionBean versionBean) {
                        if (commonCallBack != null) {
                            commonCallBack.onNext(versionBean);

                        }
                        RxBus.getInstance().send("发送数据");
                        LogUtils.d(TAG, "检查版本 : onNext ");

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (commonCallBack != null) {
                            commonCallBack.onError(e);

                        }

                        LogUtils.d(TAG, "检查版本   onError: " + e.toString());


                    }

                    @Override
                    public void onComplete() {

                        LogUtils.d(TAG, "检查版本 :  onComplete");

                        if (commonCallBack != null) {
                            commonCallBack.onComplete();

                        }


                    }
                });

    }


    /**
     * 检查版本是否一致 api
     */
    public static void checkVersionCode(String curVersionCode, final CommonCallBack commonCallBack) {

        Observable<VersionBean> versionBeanObservable =
                HttpClientManager.getHttpClientService().checkVersionService(curVersionCode);


        versionBeanObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VersionBean>() {
                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        this.mDisposable = d;

                        LogUtils.d(TAG, "检查版本  : onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull VersionBean versionBean) {
                        if (commonCallBack != null) {
                            commonCallBack.onNext(versionBean);

                        }
                        RxBus.getInstance().send("发送数据");
                        LogUtils.d(TAG, "检查版本 : onNext ");

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (commonCallBack != null) {
                            commonCallBack.onError(e);

                        }

                        LogUtils.d(TAG, "检查版本   onError: " + e.toString());

                    }

                    @Override
                    public void onComplete() {

                        LogUtils.d(TAG, "检查版本 :  onComplete");

                        if (commonCallBack != null) {
                            commonCallBack.onComplete();

                        }


                    }
                });
    }


}
