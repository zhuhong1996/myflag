package com.example.sdu.myflag.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.sdu.myflag.R;

/**
 * 关于我们界面
 */
public class AboutUsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }

    public void AboutUsBack(View view) {
        this.finish();
    }
}
