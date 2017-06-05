package com.example.sdu.myflag.activity;

import android.os.Bundle;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2016/8/16.
 */
public class GuideActivity extends BaseActivity {

    GifDrawable gifDrawable;
    GifImageView gifImageView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        gifImageView = (GifImageView) findViewById(R.id.gif_guide);
        gifDrawable = (GifDrawable) gifImageView.getDrawable();
        gifDrawable.setLoopCount(1);
        Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startNewActivity(LoginActivity.class);
                GuideActivity.this.finish();
            }
        };
        timer.schedule(task, 6000);

    }
}
