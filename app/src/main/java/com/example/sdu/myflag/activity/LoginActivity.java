package com.example.sdu.myflag.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.view.View.OnClickListener;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.util.BaseTools;
import com.example.sdu.myflag.util.NetUtil;

import org.json.*;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Response;

/**
 * 登陆界面
 */
public class LoginActivity extends BaseActivity {
    private EditText accountEditText, passwordEditText;
    private String account, password;
    private SharedPreferences preferences;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        //获取各组件id
        preferences = BaseApplication.getInstance().getSharedPreferences("User", Context.MODE_PRIVATE);
        accountEditText = (EditText) findViewById(R.id.loginAccountEditText);
        passwordEditText = (EditText) findViewById(R.id.loginPasswordEditText);

        account = preferences.getString("account", null);
        password = preferences.getString("password", null);

        if (account != null && password != null)
            doLogin();
    }

    public void goToRegister(View v) {
        startNewActivity(RegisterActivity.class);
    }

    public void login(View v) {
        dialog.show();
        if (getText()) {
            ArrayList<NetUtil.Param> params = new ArrayList<>();
            params.add(new NetUtil.Param("phone", account));
            params.add(new NetUtil.Param("password", password));

            LoginResult loginResult = new LoginResult();
            try {
                NetUtil.getResult(NetUtil.loginUrl, params, loginResult);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doLogin() {
        dialog.show();
        ArrayList<NetUtil.Param> params = new ArrayList<>();
        params.add(new NetUtil.Param("phone", account));
        params.add(new NetUtil.Param("password", password));

        LoginResult loginResult = new LoginResult();
        try {
            NetUtil.getResult(NetUtil.loginUrl, params, loginResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取各EditText中的值，并进行合法性校验,合法返回true
    //不合法返回false，并用Toast进行提醒
    private boolean getText() {
        account = accountEditText.getText().toString();
        password = passwordEditText.getText().toString();
        if (!BaseTools.isNetworkAvailable(LoginActivity.this)) {
            Toast.makeText(this, "当前网络不可用！", Toast.LENGTH_SHORT).show();
            return false;
        } else if (account.isEmpty()) {
            Toast.makeText(this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.isEmpty()) {
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private class LoginResult implements NetUtil.CallBackForResult {

        @Override
        public void onFailure(final IOException e) {
            LoginActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onSuccess(Response response) {
            if (response.isSuccessful()) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String user = jsonObject.optString("user");
                    if (user == null || user.equals("")) {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        JSONObject userJson = new JSONObject(user);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("uid", userJson.optInt("uid") + "").apply();
                        editor.putString("phone", userJson.optString("phone")).apply();
                        editor.putString("nickname", userJson.optString("nickname")).apply();
                        editor.putString("information", userJson.optString("information")).apply();
                        editor.putString("email", userJson.optString("email")).apply();
                        editor.putString("account", account).apply();
                        editor.putString("password", password).apply();
                        editor.putString("sex", userJson.optString("sex")).apply();
                        editor.putInt("photo", userJson.optInt("photo")).apply();
                        dialog.dismiss();
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                            }
                        });
                        startNewActivity(MainActivity.class);
                        LoginActivity.this.finish();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
