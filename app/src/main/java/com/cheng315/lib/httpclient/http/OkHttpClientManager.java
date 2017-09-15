package com.cheng315.lib.httpclient.http;

import com.cheng315.lib.httpclient.downloadfile.ProgressInterceptor;
import com.cheng315.lib.httpclient.interceptor.CacheInterceptor;
import com.cheng315.lib.utils.AppUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by storm on 2017/9/15.
 */

public class OkHttpClientManager {

    private static final String TAG = OkHttpClientManager.class.getSimpleName();

    /**
     * 获取下载的  OkHttpClient
     *
     * @return
     */
    public static OkHttpClient getDownLoadOkHttpClient() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        CacheInterceptor cacheInterceptor = new CacheInterceptor();
        Cache cache = new Cache(new File(AppUtils.getAppContext().getCacheDir(), "HttpCache"),
                1024 * 1024 * 100);

        ProgressInterceptor progressInterceptor = new ProgressInterceptor();

        return new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor)
                .retryOnConnectionFailure(true)
                .addInterceptor(cacheInterceptor)
                .addNetworkInterceptor(progressInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }


    /**
     * 全局的  OkHttpClient
     *
     * @return
     */
    public static OkHttpClient getGlobalClient() {


        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        CacheInterceptor cacheInterceptor = new CacheInterceptor();
        Cache cache = new Cache(new File(AppUtils.getAppContext().getCacheDir(), "HttpCache"),
                1024 * 1024 * 100);

        return new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(cacheInterceptor)
                .retryOnConnectionFailure(true)
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 上传 OkHttpClient
     *
     * @return
     */
    public static OkHttpClient getUpdateClnt() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        CacheInterceptor cacheInterceptor = new CacheInterceptor();
        Cache cache = new Cache(new File(AppUtils.getAppContext().getCacheDir(), "HttpCache"),
                1024 * 1024 * 100);
        return new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(cacheInterceptor)
                .retryOnConnectionFailure(true)
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }


}
