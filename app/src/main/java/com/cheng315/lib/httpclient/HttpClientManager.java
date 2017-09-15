package com.cheng315.lib.httpclient;

import com.cheng315.chengnfc.MApplication;
import com.cheng315.lib.utils.LogUtils;
import com.cheng315.lib.httpclient.downloadfile.ProgressInterceptor;
import com.cheng315.lib.httpclient.interceptor.CacheInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/8/24.
 */

public class HttpClientManager {

    private static final String TAG = HttpClientManager.class.getSimpleName();

    public static OkHttpClient httpClient;
    public static volatile Retrofit mRetrofit;
    public static HttpClientService mHttpClientService;
    public static HttpClientImgService mHttpClientImgService;

    public static Retrofit initRetrofit() {

        if (mRetrofit == null) {
            synchronized (HttpClientManager.class) {
                if (mRetrofit == null) {
                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    CacheInterceptor cacheInterceptor = new CacheInterceptor();

                    ProgressInterceptor progressInterceptor = new ProgressInterceptor();

                    Cache cache = new Cache(new File(MApplication.appContext.getCacheDir(), "HttpCache")
                            , 1024 * 1024 * 100);


                    LogUtils.d(TAG, "okhttp 的缓存路径 " + MApplication.appContext.getCacheDir().toString());
                    httpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .addInterceptor(cacheInterceptor) // 添加网络缓存的 拦截器
                            .retryOnConnectionFailure(true)
                            .addInterceptor(httpLoggingInterceptor)
                            .addNetworkInterceptor(progressInterceptor)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .build();

                    mRetrofit = new Retrofit.Builder()
                            .baseUrl(Api.BASE_URL)
                            .client(httpClient)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();


                    LogUtils.d(TAG, "retrofit实例 : " + mRetrofit);


                }
            }
        }
        return mRetrofit;
    }


    /**
     * 获取连接请求 根据不同的模块创建不同的请求service
     *
     * @return
     */
    public static HttpClientService getHttpClientService() {
        if (mHttpClientService == null) {
            mHttpClientService = initRetrofit().create(HttpClientService.class);
            LogUtils.d(TAG, "接口的实例 :  " + mHttpClientImgService);
        }
        return mHttpClientService;
    }


    /**
     * 图片请求
     *
     * @return
     */
    public static HttpClientImgService getHttpClientImgService() {

        if (mHttpClientImgService == null) {
            mHttpClientImgService = initRetrofit()
                    .newBuilder()
                    .baseUrl(Api.BASE_IMG_URL)
                    .build()
                    .create(HttpClientImgService.class);
        }

        return mHttpClientImgService;
    }


}
