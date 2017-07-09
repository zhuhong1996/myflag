package com.example.sdu.myflag.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.sdu.myflag.R;
import com.john.waveview.WaveView;

public class MyFlagDetailActivity extends AppCompatActivity {
    private WaveView waveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_flag_detail);

        waveView = (WaveView) findViewById(R.id.wave_view);
        waveView.setProgress(50); //这里的参数放置一个1-100的参数   参数=100*（currentTime-startTime）/(endTime-startTime)   修改波浪的波动程度去xml文件里面修改

    }
}
