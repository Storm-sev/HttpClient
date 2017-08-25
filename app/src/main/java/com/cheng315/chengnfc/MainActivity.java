package com.cheng315.chengnfc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cheng315.chengnfc.httpclient.HttpManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initData();
    }

    private void initData() {

        HttpManager.checkVersionCode("V1_1.1");

    }
}
