package com.cheng315.chengnfc.httpclient;

import com.cheng315.chengnfc.bean.VersionBean;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2017/8/24.
 */

public interface HttpClientService {


    /**
     * 检查版本
     */
    @GET("appVersion/upgrade?")
    Observable<VersionBean> checkVersionService(@Query("versionCode") String versionCode);


    /**
     * 下载最新apk
     */
    @Streaming
    @POST
    Observable<ResponseBody> downLoadNewApk(@Url String url);


}
