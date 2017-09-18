package com.cheng315.lib.httpclient.downloadfile;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by storm on 2017/9/18.
 */

public interface DownLoadService {


    /**
     * 下载最新apk
     */
    @Streaming
    @POST
    Observable<ResponseBody> downLoadNewApk(@Url String url);
}
