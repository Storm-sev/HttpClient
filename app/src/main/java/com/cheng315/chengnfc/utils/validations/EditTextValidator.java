package com.cheng315.chengnfc.utils.validations;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/1.
 * EditText 校验器
 */

public class EditTextValidator {

    private Context context;

    private View button;

    private ArrayList<ValidationModel> validationModels;

    public EditTextValidator(Context context) {

        init(context, null);
    }

    public EditTextValidator(Context context, View button) {
        init(context, button);

    }

    private void init(Context context, View button) {

        this.context = context;
        this.button = button;
        validationModels = new ArrayList<>();

    }


    /**
     * 设置button 支持各种有点击事件的view
     */
    public EditTextValidator setButton(View button) {
        this.button = button;
        return this;
    }


    public  View getButton(){
        return button;
    }


    /**
     * 添加验证模型
     */
    public EditTextValidator add(ValidationModel validationModel) {
        validationModels.add(validationModel);
        return this;
    }


    public EditTextValidator excute(){

        for (ValidationModel validationModel : validationModels) {

            if (validationModel.getEditText() == null) {
                return this;
            }

            validationModel.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // text 发生改变的时候
                    setEnabled();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }

        setEnabled();

        return this;
    }


    /**
     * 确定button 是否是可点击状态
     */
    private void setEnabled() {

        for (ValidationModel validationModel : validationModels) {

            if (button != null) {
                if (validationModel.isTextEmpty()) {// 如果有一个是空的,
                    button.setEnabled(false); // 不可点击
                    return;
                } else {
                    if(!button.isEnabled()) {
                        button.setEnabled(true);

                    }
                }
            }
        }

    }


    /**
     * 处理器验证正则处理
     */
    public boolean validate(){

        for (ValidationModel validationModel : validationModels) {


            if (validationModel.getValidationExecutor() == null || validationModel.getEditText() == null) {
                // 没有验证处理器  那么返回true
                return true;
            }

            if (!validationModel.getValidationExecutor().doValidate(context, validationModel.getEditText().getText().toString())) {

                return false;

            }

        }
        return true;

    }



}
