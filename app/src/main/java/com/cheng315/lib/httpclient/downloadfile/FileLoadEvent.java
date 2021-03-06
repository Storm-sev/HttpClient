package com.cheng315.lib.httpclient.downloadfile;

/**
 * Created by Administrator on 2017/8/25.
 *
 * 文件下载动作
 */

public class FileLoadEvent {


    long total; //下载的总长度
    long progress;  //下载的进度


    public FileLoadEvent() {
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getTotal() {
        return total;
    }

    public long getProgress() {
        return progress;
    }



    public FileLoadEvent(long total, long progress) {
        this.total = total;
        this.progress = progress;
    }

}
