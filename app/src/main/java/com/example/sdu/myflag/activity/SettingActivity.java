package com.example.sdu.myflag.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.base.BaseApplication;

/**
 * 设置界面
 */
public class SettingActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_setup;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
    }

    public void modifyPassword(View view) {
        startNewActivity(ModifyPasswordActivity.class);
    }

    public void aboutUs(View view) {
        startNewActivity(AboutUsActivity.class);
    }

    public void exitLogin(View view) {
        SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("User", Context.MODE_PRIVATE);
        preferences.edit().remove("account").apply();
        preferences.edit().remove("password").apply();
        startNewActivity(LoginActivity.class);
        MainActivity.getInstance().finish();
        finish();
    }

    public void suggestion(View view) {
        startNewActivity(SuggestionActivity.class);
    }

    public void settingBack(View view) {
        this.finish();
    }
}
