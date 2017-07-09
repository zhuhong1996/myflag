package com.example.sdu.myflag.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.util.BaseTools;
import com.example.sdu.myflag.util.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;


public class RegisterActivity extends BaseActivity {
    private EditText nameEditText, passwordEditText, passwordAgainEditText, phoneNumberEditText, emailEditText, personalInformationEditText;
    private String name, password, passwordAgain, phoneNumber, email, personalInformation;
    private Button completeButton;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        //获取各组件id
        nameEditText = (EditText) findViewById(R.id.registerNameEditText);
        passwordEditText = (EditText) findViewById(R.id.registerPasswordEditText);
        passwordAgainEditText = (EditText) findViewById(R.id.registerPasswordAgainEditText);
        phoneNumberEditText = (EditText) findViewById(R.id.registerPhoneNumberEditText);
        emailEditText = (EditText) findViewById(R.id.registerEmailEditText);
        personalInformationEditText = (EditText) findViewById(R.id.registerPersonalInformationEditText);

        completeButton = (Button) findViewById(R.id.registerCompleteButton);

        //设置“完成 ”按钮事件监听
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getText()) {
                    List<NetUtil.Param> params = new ArrayList<>();
                    params.add(new NetUtil.Param("nickname", name));
                    params.add(new NetUtil.Param("password", password));
                    params.add(new NetUtil.Param("phone", phoneNumber));
                    params.add(new NetUtil.Param("email", email));
                    params.add(new NetUtil.Param("information", personalInformation));
                    RegisterResult registerResult = new RegisterResult();
                    try {
                        NetUtil.getResult(NetUtil.registerUrl, params, registerResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void backTo(View v) {
        RegisterActivity.this.finish();
    }


    //获取各EditText中的值，并进行合法性校验,合法返回true
    //不合法返回false，并用Toast进行提醒
    private boolean getText() {
        name = nameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        passwordAgain = passwordAgainEditText.getText().toString();
        phoneNumber = phoneNumberEditText.getText().toString();
        email = emailEditText.getText().toString();
        personalInformation = personalInformationEditText.getText().toString();

        if (!BaseTools.isNetworkAvailable(RegisterActivity.this)) {
            Toast.makeText(this, "当前网络不可用！", Toast.LENGTH_SHORT).show();
            return false;
        } else if (name.isEmpty() || password.isEmpty() || passwordAgain.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "信息不完整！", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.equals(passwordAgain)) {
            Toast.makeText(this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phoneNumber.length() != 11) {
            Toast.makeText(this, "手机号应为11位！", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            for (int i = 0; i < phoneNumber.length(); i++) {
                char c = phoneNumber.charAt(i);
                if (c < '0' || c > '9') {
                    Toast.makeText(this, "手机号应为纯数字！", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }

        return true;
    }

    private class RegisterResult implements NetUtil.CallBackForResult {

        @Override
        public void onFailure(final IOException e) {
            RegisterActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onSuccess(Response response) {
            if (response.isSuccessful()) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                        }
                    });
                    RegisterActivity.this.finish();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
