package com.cheng315.chengnfc;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cheng315.lib.base.BaseActivity;

import butterknife.BindView;

/**
 *
 *  @author storm_
 *  @date 2017/9/8
 *  @address zq329051@outlook.com
 *  @describe : 防伪码验证界面　
 */
public class InputScanActivity extends BaseActivity {


    private static final String TAG = "InputScanActivity";


    @BindView(R.id.scan_back)
    ImageView scanBack;
    @BindView(R.id.scan_title)
    TextView scanTitle;
    @BindView(R.id.btn_input_submit)
    Button btnInputSubmit;
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.rl_scan)
    RelativeLayout rlScan;

    @Override
    protected void setUpListener() {


        rlScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideSoftInput(v);

            }
        });


        scanBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnInputSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 获取 edittext 输入的12位防伪码

                String trim = etInput.getText().toString().trim();

                if (trim != null && trim.length() == 12) {

                    Toast.makeText(InputScanActivity.this, "获取的12位防伪码 :  " + trim, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 隐藏输入键盘
     */
    private void hideSoftInput(View v) {
        InputMethodManager imm
                = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        scanTitle.setText(R.string.scan_input);

    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_input_scan;
    }



}
