package com.cheng315.chengnfc.httpclient;

import com.cheng315.chengnfc.MApplication;
import com.cheng315.chengnfc.httpclient.downloadfile.ProgressInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/8/24.
 */

public class HttpClientManager {

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

                    ProgressInterceptor progressInterceptor = new ProgressInterceptor();


                    Cache cache = new Cache(new File(MApplication.appContext.getCacheDir(), "HttpCahe")
                            , 1024 * 1024 * 100);

                    httpClient = new OkHttpClient.Builder()
//                            .cache(cache)
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
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

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
        }
        return mHttpClientService;
    }


    /**
     * 图偏请求
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
