package com.cheng315.lib.httpclient.downloadfile;

import com.cheng315.chengnfc.utils.RxBus;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by Administrator on 2017/8/25.
 * 显示下载进度 重写 responsebody
 */

public class ProgressResponseBody extends ResponseBody {


    private ResponseBody responseBody;
    private BufferedSource bufferedSource;



    public ProgressResponseBody(ResponseBody responseBody) {

        this.responseBody = responseBody;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));

        }

        return bufferedSource;
    }

    private Source source(Source source) {


        return new ForwardingSource(source) {
            long bytesReaded = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {

                long bytesRead = super.read(sink, byteCount);
                bytesReaded += bytesRead == -1 ? 0 : bytesRead;
                //实时发送当前的字节数 和总长度

                // 使用RxbuS 发送数据
                RxBus.getInstace().send(new FileLoadEvent(responseBody.contentLength(), bytesReaded));

                return bytesRead;
            }

        };


    }
}
