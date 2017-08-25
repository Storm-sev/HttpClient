package com.cheng315.chengnfc.httpclient;

/**
 * Created by Administrator on 2017/8/25.
 */

public interface DownLoadListener {


    /**
     * @param progress   进度
     * @param total      总进度
     * @param isComplete 是否完成
     */
    void onProgress(long progress, long total, boolean isComplete);
}
