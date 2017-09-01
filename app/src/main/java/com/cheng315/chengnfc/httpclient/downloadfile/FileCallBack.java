package com.cheng315.chengnfc.httpclient.downloadfile;

import com.cheng315.chengnfc.utils.LogUtils;
import com.cheng315.chengnfc.utils.RxBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/8/25.
 * <p>
 * 下载文件的回调
 */

public abstract class FileCallBack<T> {

    private static final String TAG = "FileCallBack";


    private String fileDir;// 文件的存储路径
    private String fileName; // 文件的名字


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

        RxBus.getInstace().unSubscribe(this);


    }


    //订阅加载进度条
    public void subscribeLoadProgress() {

        RxBus.getInstace().doSubscribe(FileLoadEvent.class, new Action1<FileLoadEvent>() {
            @Override
            public void call(FileLoadEvent fileLoadEvent) {

                long progress = fileLoadEvent.getProgress();
                long total = fileLoadEvent.getTotal();

                onProgress(progress * 1.0f / total, total);

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });


    }

}



