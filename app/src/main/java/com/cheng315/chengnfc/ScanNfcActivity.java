package com.cheng315.chengnfc;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheng315.chengnfc.utils.LogUtils;
import com.cheng315.lib.base.BaseActivity;

import butterknife.BindView;

/**
 *
 *  @author storm_
 *  @date 2017/9/8
 *  @address zq329051@outlook.com
 *  @describe :　nfc 扫描页面
 */
public class ScanNfcActivity extends BaseActivity {


    private static final String TAG = "ScanNfcActivity";
    @BindView(R.id.scan_back)
    ImageView scanBack;
    @BindView(R.id.tv_scan)
    TextView tvScan;
    @BindView(R.id.iv_pre_scan)
    ImageView ivPreScan;
    @BindView(R.id.iv_scan_loading)
    ImageView ivScanLoading;
    @BindView(R.id.iv_scaning_anim)
    ImageView ivScaningAnim;
    @BindView(R.id.rl_scan)
    ImageView rlScan;
    @BindView(R.id.activity_scan_nfc)
    RelativeLayout activityScanNfc;
    @BindView(R.id.tv_scan_explain)
    TextView tvScanExplain;


    /**
     * 按钮手势判断
     */
    private GestureDetector mDetector;
    private ObjectAnimator alphaAnimator;


    @Override
    protected void setUpListener() {


        scanBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        rlScan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mDetector != null) {
                    mDetector.onTouchEvent(event);
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    LogUtils.d(TAG, "手势检测 _action up ");
                    stopScanStateOrAnim();

                }
                return true;
            }

        });

    }


    /**
     * 停止扫描状态
     */
    private void stopScanStateOrAnim() {
        tvScan.setText("长按开始扫描");
        rlScan.setBackgroundResource(R.drawable.btn_scaning_normal);
        ivPreScan.setVisibility(View.VISIBLE);
        tvScanExplain.setVisibility(View.VISIBLE);
        ivScanLoading.setVisibility(View.GONE);
        ivScaningAnim.setVisibility(View.GONE);

        alphaAnimator.end();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        alphaAnimator = ObjectAnimator.ofFloat(ivScaningAnim, "alpha", 1f, 0f, 1f);
        alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
        alphaAnimator.setRepeatMode(ObjectAnimator.RESTART);
        alphaAnimator.setDuration(800);
        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                startScaningStateOrAnim();

                return super.onDown(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {

                LogUtils.d(TAG, "手势识别 + onlongPress");

                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    LogUtils.d(TAG, "手势检测 _action down ");
                }
                if (e.getAction() == MotionEvent.ACTION_MOVE) {

                    LogUtils.d(TAG, "手势检测 _action move ");
                }


                //
                startScaningStateOrAnim();

            }
        });

    }


    /**
     * 开始扫描状态
     */
    private void startScaningStateOrAnim() {

        ivPreScan.setVisibility(View.GONE);
        ivScanLoading.setVisibility(View.VISIBLE);
        ivScaningAnim.setVisibility(View.VISIBLE);
        tvScanExplain.setVisibility(View.GONE);

        alphaAnimator.start();

        rlScan.setBackgroundResource(R.drawable.btn_scaning_press);
        tvScan.setText("扫描中....");

    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_scan_nfc;
    }



}
