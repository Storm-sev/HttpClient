package com.cheng315.chengnfc.utils.validations;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by Administrator on 2017/9/1.
 *
 *  EditText的校验模型
 *
 */

public class ValidationModel {

    /**
     * 传入的 editText控件
     */
    private EditText editText;

    /**
     * 执行校验的处理类
     */
    private ValidationExecutor validationExecutor;


    public ValidationModel(EditText editText, ValidationExecutor validationExecutor) {
        this.editText = editText;
        this.validationExecutor = validationExecutor;
    }


    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public ValidationExecutor getValidationExecutor() {
        return validationExecutor;
    }

    public void setValidationExecutor(ValidationExecutor validationExecutor) {
        this.validationExecutor = validationExecutor;
    }


    /**
     * edittext是否为空
     */
    public boolean isTextEmpty() {

        if (editText == null || TextUtils.isEmpty(editText.getText().toString())) {
            return true;
        }
        return false;
    }

}
