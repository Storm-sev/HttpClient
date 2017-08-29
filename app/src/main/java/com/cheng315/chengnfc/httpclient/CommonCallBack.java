package com.cheng315.chengnfc.httpclient;

/**
 * Created by Administrator on 2017/8/28.
 * 通用网络请求回调
 */

public interface CommonCallBack<T> {

    // 请求之前
    void onStart();

    // 发生错虚
    void onError(Throwable e);


    // 请求成功
    void onComplete();


    /**
     * 获取请求的数据
     */
    void onNext(T t);

}
