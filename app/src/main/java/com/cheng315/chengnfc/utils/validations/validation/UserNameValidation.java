package com.cheng315.chengnfc.utils.validations.validation;

import android.content.Context;
import android.widget.Toast;

import com.cheng315.chengnfc.utils.validations.ValidationExecutor;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/9/1.
 * 用户名校验
 */

public class UserNameValidation extends ValidationExecutor{


    /**
     *  手机号校验
     */
    @Override
    public boolean doValidate(Context context, String content) {

        if (content.isEmpty()) {
            Toast.makeText(context, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        //手机号正则表达式
        String regex = "^((13[0-9])|(15[^4,\\D])|(18[0,2,5-9]))\\d{8}$";

        boolean result = Pattern.compile(regex).matcher(content).find();


        if(!result) {
            Toast.makeText(context, "手机的格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
