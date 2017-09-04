package com.cheng315.lib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2017/9/4.
 *
 */

public  abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(arrachLayoutRes());
        initViews();
        initData();
        setUpListener();


    }

    /**
     * 添加监听
     */
    protected abstract void setUpListener();


    /**
     * 加载数据
     */
    protected abstract void initData();




    /**
     * 初始化控件
     */
    protected abstract void initViews();


    /**
     * init layout
     */
    protected abstract int arrachLayoutRes();







}

