package com.cheng315.chengnfc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.cheng315.chengnfc.bean.VersionBean;
import com.cheng315.chengnfc.httpclient.CommonCallBack;
import com.cheng315.chengnfc.httpclient.HttpManager;
import com.cheng315.chengnfc.service.UpdateService;
import com.cheng315.chengnfc.utils.LogUtils;
import com.cheng315.chengnfc.utils.RxBus;
import com.jakewharton.rxbinding.view.RxView;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button download_apk;
    private TextView textView;

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

//        download_apk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (mVersionBean == null) {
//                    return;
//
//                }
//                // 直接启动下载的service
//                startToUpdate();
//
//            }
//        });


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
        textView = (TextView) findViewById(R.id.textView);

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

                //
                if (!"0".equals(versionBean.getCode())) {
                    return;

                }

                RxBus.getInstace().send(versionBean);

            }


        });
    }


}
