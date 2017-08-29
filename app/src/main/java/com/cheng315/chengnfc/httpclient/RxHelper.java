package com.cheng315.chengnfc.httpclient;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/28.
 * rx 工具类
 */

public class RxHelper {


    /**
     * rx线程切换
     */
    public static <T> Observable.Transformer<T, T> IO_Main() {

        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {

                return tObservable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }

        };
    }

}
