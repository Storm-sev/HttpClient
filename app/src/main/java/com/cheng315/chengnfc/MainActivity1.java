package com.cheng315.chengnfc;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheng315.chengnfc.customview.MainActionbar;
import com.cheng315.lib.base.BaseActivity;
import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import io.reactivex.functions.Consumer;


public class MainActivity1 extends BaseActivity {


    @BindView(R.id.banner)
    ImageView banner;
    @BindView(R.id.scan_qr_code)
    TextView scanQrCode;
    @BindView(R.id.scan_nfc)
    TextView scanNfc;
    @BindView(R.id.scan_input)
    TextView scanInput;
    @BindView(R.id.bottom_navigation)
    LinearLayout bottomNavigation;
//    @BindView(R.id.main_right)
//    RelativeLayout mainRight;
    @BindView(R.id.main_actionbar)
    MainActionbar mainActionbar;
    @BindView(R.id.activity_main)
    DrawerLayout activityMain;
    @BindView(R.id.main_right)
    NavigationView mainRight;

    @Override
    protected void setUpListener() {


        RxView.clicks(scanInput)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Intent intent = new Intent(MainActivity1.this, InputScanActivity.class);
                        startActivity(intent);
                    }
                });

        RxView.clicks(scanNfc)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Intent intent = new Intent(MainActivity1.this, ScanNfcActivity.class);
                        startActivity(intent);
                    }
                });





        mainActionbar.getMainMenu().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!activityMain.isDrawerOpen(mainRight)) {
                    activityMain.openDrawer(mainRight);

                }
            }
        });


        activityMain.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //打开手势滑动
                activityMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // 关闭手势滑动
                activityMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

        activityMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main1;
    }



}
