package com.cheng315.lib.httpclient.downloadfile;

import com.cheng315.lib.utils.RxBus;

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


    private static final String TAG = ProgressResponseBody.class.getSimpleName();

    private ResponseBody responseBody;
    private BufferedSource bufferedSource;

    private FileLoadEvent fileLoadEvent;


    public ProgressResponseBody(ResponseBody responseBody) {

        this.responseBody = responseBody;
        this.fileLoadEvent = new FileLoadEvent();

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
//                LogUtils.d(TAG,"获取的文件进度 ：　" + bytesReaded);
                // 背压
                fileLoadEvent.setTotal(responseBody.contentLength());
                fileLoadEvent.setProgress(bytesReaded);

                RxBus.getInstance().sendByBackPressure(fileLoadEvent);
                return bytesRead;
            }

        };


    }
}
