package com.cheng315.chengnfc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cheng315.chengnfc.bean.VersionBean;
import com.cheng315.chengnfc.service.UpdateService;
import com.cheng315.chengnfc.utils.validations.ValidationModel;
import com.cheng315.chengnfc.utils.validations.validation.UserNameValidation;
import com.cheng315.lib.httpclient.CommonCallBack;
import com.cheng315.lib.httpclient.HttpManager;
import com.cheng315.lib.utils.LogUtils;
import com.cheng315.lib.utils.RxBus;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.regex.Pattern;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button download_apk, btn_check, btn_send_sms, btn_to_main, btn_to_scan_nfc, btn_check_version;
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

        RxView.clicks(btn_to_scan_nfc)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                        Intent intent = new Intent(MainActivity.this, ScanNfcActivity.class);
                        startActivity(intent);

                    }
                });

        RxView.clicks(btn_to_main)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Intent intent = new Intent(MainActivity.this, MainActivity1.class);
                        startActivity(intent);
                    }
                });


//
        RxView.clicks(btn_check)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        checkIsNet();

                    }
                });

        RxView.clicks(download_apk)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (mVersionBean == null) {
                            return;
                        }
                        startToUpdate();

                    }
                });


        RxView.clicks(btn_check_version)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                        checkVersion();


                    }
                });


    }

    /**
     * 检查版本
     */
    private void checkVersion() {

        HttpManager.checkVersion("V1_1.0", new CommonCallBack<VersionBean>() {
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

                LogUtils.d(TAG, "检查版本是否有更新传递的json数据  : " + versionBean.getCode());

                LogUtils.d(TAG, "有新的版本更新 _ -------------");
                if (!"0".equals(versionBean.getCode())) {
                    return;
                }


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
        btn_to_main = (Button) findViewById(R.id.btn_to_main);
        btn_to_scan_nfc = (Button) findViewById(R.id.btn_to_scan_nfc);

        download_apk = (Button) findViewById(R.id.download_apk);
        btn_check = (Button) findViewById(R.id.btn_check);
        textView = (TextView) findViewById(R.id.textView);

        et_phone = (EditText) findViewById(R.id.et_phone);
        btn_send_sms = (Button) findViewById(R.id.btn_send_sms);

        btn_check_version = (Button) findViewById(R.id.btn_check_version);


    }

    private void initData() {

        checkVersionCode();
        RxBus.getInstance().doSubscribe(VersionBean.class, new Consumer<VersionBean>() {
            @Override
            public void accept(VersionBean versionBean) throws Exception {
                mVersionBean = versionBean;
            }


        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }

        });


//        RxView.clicks(btn_send_sms)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//
//
//                        sendValidationSms();
//
//                    }
//                });


    }

    /**
     * 短信验证
     */
    private void sendValidationSms() {

        MApplication.getSingleEditTextValidator()
                .add(new ValidationModel(et_phone, new UserNameValidation()))
                .excute();


        if (!MApplication.getSingleEditTextValidator().validate()) {

            LogUtils.d(TAG, " 获取的结果是 false");
            return;
        }


        LogUtils.d(TAG, "验证输入的手机号 +  是手机号");
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

                LogUtils.d(TAG, "检查版本是否有更新传递的json数据  : " + versionBean.getCode());

                if (!"0".equals(versionBean.getCode())) {
                    return;
                }
                RxBus.getInstance().send(versionBean);
            }


        });
    }


}
