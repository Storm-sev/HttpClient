package com.cheng315.chengnfc.utils;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Storm on 2017/6/1
 * rxbus 数据传递工具类.
 */

public class RxBus {

    private static volatile RxBus INSTANCE;
    private Subject<Object, Object> _mBus = new SerializedSubject<Object, Object>(PublishSubject.create());
    // 类似于粘性广播
    private static Map<Class<?>, Object> mStrckyEventMap;

    //rxbus 订阅管理 CompositeSubscription 管理多个
    private HashMap<String, CompositeSubscription> mSubscriptionmMap;


    private RxBus() {
        //ConcurrentHashMap 是线程安全的
        mStrckyEventMap = new ConcurrentHashMap<>();
    }


    public static RxBus getInstace() {
        if (INSTANCE == null) {
            synchronized (RxBus.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RxBus();
                }
            }

        }
        return INSTANCE;
    }


    /**
     * 发送普通数据
     *
     * @param event
     */
    public void send(Object event) {
        _mBus.onNext(event);
    }


    /**
     * 接受普通传递的数据8
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {

        return _mBus.ofType(eventType);
    }


    public <T> Subscription doSubscribe(Class<T> eventType, Action1<T> next, Action1<Throwable> error) {


        return toObservable(eventType)
                .onBackpressureBuffer()// 背压处理
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next, error);

    }


    /**
     * 添加订阅者到集合
     */
    public void addSubscription(Object o, Subscription subscription) {

        if (mSubscriptionmMap == null) {
            mSubscriptionmMap = new HashMap<>();

        }

        String key = o.getClass().getName();

        if (mSubscriptionmMap.get(key) != null) {
            mSubscriptionmMap.get(key).add(subscription);

        } else {
            CompositeSubscription compositeSubscription = new CompositeSubscription();
            compositeSubscription.add(subscription);
            mSubscriptionmMap.put(key, compositeSubscription);
        }


    }

    /**
     * 判断是否有订阅者
     *
     * @return
     */
    public boolean hasObservers() {
        return _mBus.hasObservers();
    }


    // 发送一个 Sticky 事件

    /**
     * 发送消息
     *
     * @param event
     */
    public void sendSticky(Object event) {
        synchronized (mStrckyEventMap) {
            mStrckyEventMap.put(event.getClass(), event);
        }
        send(event);
    }


    /**
     * 发送粘性
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservableSticky(Class<T> eventType) {
        synchronized (mStrckyEventMap) {
            Observable<T> tObservable = _mBus.ofType(eventType);
            Object event = mStrckyEventMap.get(eventType);

            if (event != null) {
                return tObservable.mergeWith(Observable.just(eventType.cast(event)));
            } else {

                return tObservable;
            }
        }

    }


    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (mStrckyEventMap) {
            return eventType.cast(mStrckyEventMap.remove(eventType));
        }
    }

    /**
     * 移除所有的sticky事件
     */
    public void removeAllStickyEvent() {
        synchronized (mStrckyEventMap) {
            mStrckyEventMap.clear();

        }
    }


    public void unSubscribe(Object o) {

        if (mSubscriptionmMap == null) {
            return;
        }

        String key = o.getClass().getName();

        if (!mSubscriptionmMap.containsKey(key)) {
            return;
        }

        if (mSubscriptionmMap.get(key) != null) {

            mSubscriptionmMap.get(key).unsubscribe();
        }

        mSubscriptionmMap.remove(key);


    }
}
