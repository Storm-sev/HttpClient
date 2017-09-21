package com.cheng315.lib.httpclient;

import com.cheng315.lib.utils.LogUtils;
import com.cheng315.lib.utils.NetWorkUtils;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by storm on 2017/9/15.
 * <p>
 * 观察者抽象类
 */

public abstract class CommonSubscriber<T> implements Observer<T> {


    private static final String TAG = CommonSubscriber.class.getSimpleName();

    private Disposable mDisposable;

    /**
     * 在使用之前调用
     */
    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.mDisposable = d;

        LogUtils.d(TAG, "base CommonSubscriber 　onSubscribe : ");

        if (!NetWorkUtils.isConnected()) {
            // 没有网络连接 直接退出
            return;
        }

    }

    @Override
    public void onNext(@NonNull T t) {
        LogUtils.d(TAG, "base CommonSubscriber 　onNext : ");

    }

    @Override
    public void onError(@NonNull Throwable e) {
        LogUtils.d(TAG, "base CommonSubscriber 　onError : " + e.toString());


    }

    @Override
    public void onComplete() {
        LogUtils.d(TAG, "base CommonSubscriber 　onComplete : ");


    }
}
