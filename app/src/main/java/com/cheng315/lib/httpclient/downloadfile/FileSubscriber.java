package com.cheng315.lib.httpclient.downloadfile;


import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/8/25.
 *
 */

public class FileSubscriber<T> implements Observer<T> {


    private static final String TAG = FileSubscriber.class.getSimpleName();

    private FileCallBack fileCallBack;

    private Disposable mDisposable;


    public FileSubscriber(FileCallBack fileCallBack) {
        this.fileCallBack = fileCallBack;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.mDisposable = d;

        if (fileCallBack != null) {
            fileCallBack.onStart();

        }
    }

    @Override
    public void onNext(T t) {
        if (fileCallBack != null) {
            fileCallBack.onSuccess(t);

        }

    }

    @Override
    public void onError(Throwable t) {

        if (fileCallBack != null) {
            fileCallBack.onError(t);

        }

    }

    @Override
    public void onComplete() {
        if (fileCallBack != null) {
            fileCallBack.onComplete();
        }
    }

}
