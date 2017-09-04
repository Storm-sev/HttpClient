package com.cheng315.chengnfc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cheng315.chengnfc.bean.VersionBean;
import com.cheng315.lib.httpclient.CommonCallBack;
import com.cheng315.lib.httpclient.HttpManager;
import com.cheng315.chengnfc.service.UpdateService;
import com.cheng315.chengnfc.utils.LogUtils;
import com.cheng315.chengnfc.utils.RxBus;
import com.cheng315.chengnfc.utils.validations.ValidationModel;
import com.cheng315.chengnfc.utils.validations.validation.UserNameValidation;
import com.jakewharton.rxbinding.view.RxView;

import java.util.regex.Pattern;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button download_apk,btn_check,btn_send_sms;
    private TextView textView;
    private EditText et_phone;

    private VersionBean mVersionBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initData();

        _setUpListener();

    }


    /**
     * 设置监听
     */

    private void _setUpListener() {


        /**
         *
         */
        RxView.clicks(btn_check)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        checkIsNet();

                    }
                });



        RxView.clicks(download_apk)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        if (mVersionBean == null) {
                            return;
                        }

                        startToUpdate();
                    }
                });

    }


    /**
     * 检查一个 url 地址是否为地址
     */
    private void checkIsNet() {

        String url = "http://www.bai.com";

        Pattern pattern = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        LogUtils.d(TAG, " 检查网站是不是网址" + url + " -----------------" + pattern.matcher(url).matches());
    }


    /**
     * 启动服务更新版本
     */
    private void startToUpdate() {

        Intent intent = new Intent(MainActivity.this, UpdateService.class);
        intent.putExtra(UpdateService.APK_URL, mVersionBean.getData().getApkUrl());
        startService(intent);
    }

    /**
     * init
     */
    private void initViews() {
        download_apk = (Button) findViewById(R.id.download_apk);
        btn_check = (Button)findViewById(R.id.btn_check);
        textView = (TextView) findViewById(R.id.textView);

        et_phone = (EditText)findViewById(R.id.et_phone);
        btn_send_sms = (Button)findViewById(R.id.btn_send_sms);


    }

    private void initData() {

        checkVersionCode();
        RxBus.getInstace().doSubscribe(VersionBean.class, new Action1<VersionBean>() {
            @Override
            public void call(VersionBean versionBean) {

                mVersionBean = versionBean;

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {


            }
        });


        RxView.clicks(btn_send_sms)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {


                        sendValidationSms();

                    }
                });



    }

    /**
     * 短信验证
     */
    private void sendValidationSms() {

        MApplication.getSingleEditTextValidator()
                .add(new ValidationModel(et_phone, new UserNameValidation()))
                .excute();


        if(!MApplication.getSingleEditTextValidator().validate()) {

            LogUtils.d(TAG," 获取的结果是 false");
            return;
        }


        LogUtils.d(TAG,"验证输入的手机号 +  是手机号");
        // 控制 60s 之后重新发送
        btn_send_sms.setTextColor(Color.BLUE);
        btn_send_sms.setEnabled(false);

    }


    /**
     * 检查版本状态
     */
    private void checkVersionCode() {
        HttpManager.checkVersionCode("V1_1.0", new CommonCallBack<VersionBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onNext(VersionBean versionBean) {


                LogUtils.d(TAG, "回调到activity界面的数据 : " + versionBean.getCode());

                if (!"0".equals(versionBean.getCode())) {
                    return;

                }

                RxBus.getInstace().send(versionBean);

            }


        });
    }


}
