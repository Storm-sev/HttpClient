package com.cheng315.lib.httpclient.http;

import com.cheng315.lib.httpclient.Api;
import com.cheng315.lib.httpclient.HttpClientService;
import com.cheng315.lib.httpclient.downloadfile.DownLoadService;
import com.cheng315.lib.httpclient.downloadfile.ProgressInterceptor;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by storm on 2017/9/15.
 */
public class RetrofitManager {

    private static final String TAG = RetrofitManager.class.getSimpleName();

    private static volatile RetrofitManager mInstance;

    private volatile Retrofit mRetrofit;

    private HttpClientService mHttpClientService;

    private RetrofitManager() {

    }

    public static RetrofitManager getInstance() {

        if (mInstance == null) {
            synchronized (RetrofitManager.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitManager();
                }
            }
        }
        return mInstance;
    }


    public Retrofit init() {

        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .client(OkHttpClientManager.getInstance().init())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return mRetrofit;
    }


    /**
     *  获取下载的接口.
     *
     * @return
     */
    public DownLoadService getDownLoadService() {

        return init().newBuilder()
                .client(OkHttpClientManager
                        .getInstance()
                        .init()
                        .newBuilder()
                        .addNetworkInterceptor(new ProgressInterceptor())
                        .build())
                .build().create(DownLoadService.class);
    }


    /**
     * @return
     */
    public HttpClientService getHttpClientService() {

        return init().create(HttpClientService.class);

    }



}
