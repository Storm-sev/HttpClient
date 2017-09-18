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
     * log 打印
     */
    private HttpLoggingInterceptor mHttpLoggingInterceptor;

    /**
     * 缓存
     */
    private CacheInterceptor mCacheInterceptor;


    private volatile OkHttpClient mClient;

    private static volatile OkHttpClientManager mInstance;
    private final Cache mCache;


    private OkHttpClientManager() {
        mHttpLoggingInterceptor = new HttpLoggingInterceptor();

        mHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mCacheInterceptor = new CacheInterceptor();

        mCache = new Cache(new File(AppUtils.getAppContext().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);

    }

    public static OkHttpClientManager getInstance() {

        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }

        return mInstance;

    }


    public OkHttpClient init() {

        if (mClient == null) {

            mClient = new OkHttpClient.Builder()
                    .cache(mCache)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(mHttpLoggingInterceptor)
                    .addInterceptor(mCacheInterceptor)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

        }

        return mClient;
    }


    public OkHttpClient getDownLoadClient() {

        if (mClient != null) {
            mClient.newBuilder().addNetworkInterceptor(new ProgressInterceptor()).build();
        }

        return mClient;

    }
}
