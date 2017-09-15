package com.cheng315.lib.httpclient;

/**
 * Created by Administrator on 2017/8/28.
 * 通用网络请求回调
 */

public interface CommonCallBack<T> {

    void onStart();

    void onError(Throwable e);

    void onComplete();


    void onNext(T t);

}
