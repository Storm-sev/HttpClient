package com.cheng315.chengnfc.httpclient.downloadfile;

import rx.Subscriber;

/**
 * Created by Administrator on 2017/8/25.
 */

public class FileSubscriber<T> extends Subscriber<T> {



    private FileCallBack fileCallBack ;

    public FileSubscriber(FileCallBack fileCallBack) {

        this.fileCallBack = fileCallBack;

    }

    @Override
    public void onStart() {
        super.onStart();
        if (fileCallBack != null) {
            fileCallBack.onStart();

        }

    }

    @Override
    public void onCompleted() {

        if (fileCallBack != null) {
            fileCallBack.onComplete();

        }

    }

    @Override
    public void onError(Throwable e) {

        if (fileCallBack != null) {
            fileCallBack.onError(e);

        }

    }

    @Override
    public void onNext(T t) {

        if (fileCallBack != null) {
            fileCallBack.onSuccess(t);
        }
        
    }
    

}
