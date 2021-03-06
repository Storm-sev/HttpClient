package com.cheng315.lib.httpclient.downloadfile;

import com.cheng315.lib.utils.LogUtils;
import com.cheng315.lib.utils.RxBus;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/8/25.
 * <p>
 * 下载文件的回调
 */

public abstract class FileCallBack<T> {

    private static final String TAG = FileCallBack.class.getSimpleName();


    private String fileDir;// 文件的存储路径
    private String fileName; // 文件的名字
//    private Flowable flowable;

    public FileCallBack(String fileDir, String fileName) {
        this.fileDir = fileDir;
        this.fileName = fileName;
        subscribeLoadProgress();
    }


    //
    public abstract void onSuccess(T t);

    public abstract void onStart();

    public abstract void onComplete();

    public abstract void onError(Throwable e);

    public abstract void onProgress(float progress, long total);


    /**
     * @param body 响应内容
     */
    public void saveFile(ResponseBody body) {

        InputStream is = null;
        FileOutputStream fos = null;

        byte[] buff = new byte[2048];
        int len;

        File file = null;

        try {
            is = body.byteStream();

            File dir = new File(fileDir);
            if (!dir.exists()) {

                dir.mkdirs();
            }

            file = new File(dir, fileName);
            fos = new FileOutputStream(file);

            while ((len = is.read(buff)) != -1) {
                fos.write(buff, 0, len);
            }
            fos.flush();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (fos != null) fos.close();

            } catch (IOException e) {
                LogUtils.d(TAG, e.getMessage());
            }

            // 解除订阅关系
            unSubscribe();


        }

    }

    /**
     * 解除订阅
     */
    public void unSubscribe() {

        if (RxBus.getInstance().hasSubscribers(true)) {

            RxBus.getInstance().unSubscription();
        }



    }


    //订阅加载进度条
    public void subscribeLoadProgress() {

        final Flowable flowable = RxBus.getInstance().doFlowable(FileLoadEvent.class, new Subscriber<FileLoadEvent>() {
            Subscription sub;

            @Override
            public void onSubscribe(Subscription s) {
                LogUtils.d(TAG, "新Rxbus,数据 + onSubscribe ");
                this.sub = s;

                sub.request(1);

            }

            @Override
            public void onNext(FileLoadEvent fileLoadEvent) {

                long progress = fileLoadEvent.getProgress();
                long total = fileLoadEvent.getTotal();

                onProgress(progress * 1.0f / total, total);
                sub.request(1);

            }

            @Override
            public void onError(Throwable t) {
                LogUtils.d(TAG, "新Rxbus,数据 + onError");
            }

            @Override
            public void onComplete() {
                LogUtils.d(TAG, "新Rxbus,数据 .  + onComplete");


            }
        });


    }

}



