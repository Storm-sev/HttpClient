package com.cheng315.lib.httpclient;

/**
 * Created by Administrator on 2017/8/28.
 * Rxbus 消息操作类
 */

public class MsgEvent<T> {


    int code;

    T content;


    public MsgEvent(int code, T content) {
        this.code = code;
        this.content = content;
    }


    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }


}
