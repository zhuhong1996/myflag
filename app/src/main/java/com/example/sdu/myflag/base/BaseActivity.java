package com.example.sdu.myflag.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        afterCreate(savedInstanceState);
    }

    public void startNewActivity(Class<?> cl){
        startActivity(new Intent(this, cl));
    }

    public abstract int getLayoutId();
    public abstract void afterCreate(Bundle savedInstanceState);
}
