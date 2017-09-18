package com.cheng315.lib.httpclient.http;

import com.cheng315.lib.httpclient.Api;
import com.cheng315.lib.httpclient.HttpClientService;

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


    public RetrofitManager init() {


        mRetrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClientManager.getGlobalClient())
                .build();

        return this;
    }


    /**
     * 全局的 retrofit
     *
     * @return
     */
    public Retrofit getGlobalRetrofit() {

        return mRetrofit.newBuilder()
                .client(OkHttpClientManager.getGlobalClient())
                .build();
    }


    /**
     * 下载文件 retrofit
     *
     * @return
     */
    public Retrofit getDownLoadRetrofit() {
        return mRetrofit
                .newBuilder()
                .client(OkHttpClientManager.getDownLoadOkHttpClient())
                .build();
    }


    /**
     * 上传文件 retrofit
     *
     * @return
     */
    public Retrofit getUpdateRetrofit() {
        return mRetrofit
                .newBuilder()
                .client(OkHttpClientManager.getUpdateClnt())
                .build();
    }


}
