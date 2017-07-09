package com.example.sdu.myflag.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.example.sdu.myflag.R;
import com.example.sdu.myflag.menupath.*;
import com.example.sdu.myflag.util.BaseTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    public Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        dialog = BaseTools.showLoadingDialog(this);
        afterCreate(savedInstanceState);
    }

    public void startNewActivity(Class<?> cl){
        startActivity(new Intent(this, cl));
    }

    public abstract int getLayoutId();
    public abstract void afterCreate(Bundle savedInstanceState);
}
