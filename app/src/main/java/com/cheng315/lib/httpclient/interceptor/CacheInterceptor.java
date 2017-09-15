package com.cheng315.lib.httpclient.interceptor;

import com.cheng315.lib.utils.NetWorkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by storm on 2017/9/12.
 *  配置缓存库
 */

public class CacheInterceptor implements Interceptor {

    private static final String TAG = CacheInterceptor.class.getSimpleName();


    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request =
                chain.request();

        if (!NetWorkUtils.isConnected()) {
            request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }


        Response response = chain.proceed(request);

        if (NetWorkUtils.isConnected()) {

            return response.newBuilder()
                    //有网络时 设置在线缓存为60s
                    .header("Cache-Control", "public, max-age=" + 60)
                    .removeHeader("Pragma")
                    .build();

        } else {
            // 没有网络时候
            int maxTime = 24 * 60 * 60; // 离线缓冲的时间

            return response.newBuilder()
                    //无网络时, 设置缓存的时间为 一天的时间 (...)
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxTime)
                    .removeHeader("Pragma")
                    .build();
        }

    }
}
