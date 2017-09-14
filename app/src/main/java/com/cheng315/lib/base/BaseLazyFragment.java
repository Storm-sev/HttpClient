package com.cheng315.lib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * @author storm_
 * @date 2017/9/11
 * @address zq329051@outlook.com
 * @describe : viewpager 和fragment 实现懒加载
 */

public abstract class BaseLazyFragment extends BaseFragment {

    private static final String TAG = BaseLazyFragment.class.getSimpleName();

    protected boolean isVisibleToUser; //  是否显示在界面上

    protected boolean isPrepared; // 视图初始化标志位

    protected boolean isFirst; // 是否第一次加载


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isFirst = true;

    }

    /**
     *
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 此方法 有可能在生命周期之外调用.

        if (getUserVisibleHint()) {
            isVisibleToUser = true;
            lazyLoad();
        } else {
            isVisibleToUser = false;
            onInvisible();
        }

    }

    /**
     * 当前fragment 不可见的时候回调此方法
     */
    protected abstract void onInvisible();

    private void lazyLoad() {

        if (!isPrepared || !isVisibleToUser || !isFirst) {
            return;
        }


        lazyLoadData();

        // 保证懒加载只执行一次
        isFirst = false;


    }

    // 实现懒加载数据
    protected abstract void lazyLoadData();


}
