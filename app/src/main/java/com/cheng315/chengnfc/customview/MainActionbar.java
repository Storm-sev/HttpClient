package com.cheng315.chengnfc.customview;

import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheng315.chengnfc.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/9/6.
 * 自定义actionbar
 */

public class MainActionbar extends RelativeLayout {


    @BindView(R.id.main_menu)
    ImageView mainMenu;
    @BindView(R.id.main_title)
    TextView mainTitle;
    private View mRootView;
    private Context mContext;


    public MainActionbar(Context context) {

        this(context, null);
    }

    public MainActionbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mRootView = View.inflate(context, R.layout.main_actionbar, MainActionbar.this);
        ButterKnife.bind(mRootView);
        initView();

        setUpListener();


    }

    private void setUpListener() {



    }

    /**
     * 初始化视图
     */
    private void initView() {

         //主标题字体加粗
        TextPaint paint =
                mainTitle.getPaint();

        paint.setFakeBoldText(true);

    }



    public ImageView getMainMenu(){

        return mainMenu;
    }




}
