package com.cheng315.lib.utils;


import org.reactivestreams.Subscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Storm on 2017/6/1
 * rxbus 数据传递工具类.
 */

public class RxBus {

    private static final String TAG = RxBus.class.getSimpleName();

    private static volatile RxBus INSTANCE;

    private Subject<Object> _mBus;
    /**
     * 有背压处理的rxbus
     */
    private FlowableProcessor<Object> _mBackpressureBus;


    // 类似于粘性广播
    private static Map<Class<?>, Object> mStrckyEventMap;

    //rxbus 订阅管理 CompositeSubscription 管理多个
    private HashMap<String, CompositeDisposable> mSubscriptionmMap;

    private RxBus() {
        //ConcurrentHashMap 是线程安全的
        _mBus = PublishSubject.create().toSerialized();
        _mBackpressureBus = PublishProcessor.create().toSerialized();
        mStrckyEventMap = new ConcurrentHashMap<>();
    }


    public static RxBus getInstance() {
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
     * 发送普通的数据
     *
     * @param event
     */
    public void send(Object event) {
        _mBus.onNext(event);

    }

    /**
     * 发送 有背压的数据
     *
     * @param event
     */
    public void sendBackpressure(Object event) {

        _mBackpressureBus.onNext(event);
    }


    /**
     * 获取普通数据传递的数据
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {

        return _mBus.ofType(eventType);
    }


    /**
     * 获取 背压处理数据
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Flowable<T> toFlowable(Class<T> eventType) {
        return _mBackpressureBus.ofType(eventType);
    }


    /**
     * 背压的处理
     *
     * @return
     */
    public <T> Disposable doFlowable(Class<T> eventType, Subscriber<T> subscriber) {
//
//        Flowable<T> tFlowable = toFlowable(eventType)
//                .onBackpressureLatest()
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .doOnCancel(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        LogUtils.d(TAG, "Flowable --------------------取消订阅");
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread());
//
//        tFlowable
//                .subscribeWith(subscriber);

        // 处理背压的情况下添加参数 根据不同的背压策略来定制不同的请求方式.

        return (Disposable) toFlowable(eventType)
                .onBackpressureLatest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber);


    }

    public <T> Disposable doSubscribe(Class<T> eventType, Consumer<T> next, Consumer<Throwable> error) {
        return toObservable(eventType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next, error);

    }


    /**
     * 添加订阅者到集合
     */
    public void addSubscription(Object o, Disposable disposable) {

        if (mSubscriptionmMap == null) {
            mSubscriptionmMap = new HashMap<>();

        }

        String key = o.getClass().getName();

        if (mSubscriptionmMap.get(key) != null) {
            mSubscriptionmMap.get(key).add(disposable);

        } else {
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(disposable);
            mSubscriptionmMap.put(key, compositeDisposable);
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


    public boolean hasFlowable() {
        return _mBackpressureBus.hasSubscribers();
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

            mSubscriptionmMap.get(key).dispose();
        }

        mSubscriptionmMap.remove(key);


    }
}
