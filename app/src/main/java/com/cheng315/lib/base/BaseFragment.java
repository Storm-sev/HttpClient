package com.cheng315.lib.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/9/4.
 *
 */

public abstract class BaseFragment extends Fragment{


    protected Context mContext;
    protected View mRootView;

    /**
     * butterknife
     */
    protected Unbinder unbinder;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mContext = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(attachLayoutRes(), null);
            unbinder = ButterKnife.bind(this, mRootView);
            initViews();


        }

        return mRootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        setUpListener();
    }


    /**
     * 设置监听
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
     * 加载布局
     */
    protected abstract int attachLayoutRes();


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(unbinder != null) {
            unbinder.unbind();

        }
    }
}
