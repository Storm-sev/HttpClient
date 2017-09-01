package com.cheng315.chengnfc.utils.validations;

import android.content.Context;

import com.cheng315.chengnfc.utils.validations.utils.ValidationUtils;

/**
 * Created by Administrator on 2017/9/1.
 * 校验执行者
 */

public abstract class ValidationExecutor extends ValidationUtils{


    /**
     * 处理校验
     * @param context
     * @param content
     * @return
     */
    public abstract boolean doValidate(Context context, String content);



}
