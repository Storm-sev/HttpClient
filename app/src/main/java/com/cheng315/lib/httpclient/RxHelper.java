package com.cheng315.lib.httpclient;


import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/8/28.
 * rx 工具类
 */

public class RxHelper {
    /**
     * rx observable 线程切换
     */
    public static <T> ObservableTransformer<T, T> IO_Main() {

        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull io.reactivex.Observable<T> upstream) {

                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };

    }


    /**
     * rx Flowable 线程
     */
    public static <T> FlowableTransformer<T, T> IO_Main_Flowable() {


        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
