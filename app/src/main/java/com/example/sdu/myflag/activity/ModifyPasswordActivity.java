package com.example.sdu.myflag.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.util.NetUtil;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Response;

/**
 * 修改密码界面
 */
public class ModifyPasswordActivity extends BaseActivity {

    EditText confirm_new_password, new_password, prev_password;
    String prePass, newPass, confirmPass;

    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_password;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        confirm_new_password = (EditText) findViewById(R.id.confirm_new_password);
        new_password = (EditText) findViewById(R.id.new_password);
        prev_password = (EditText) findViewById(R.id.prev_password);
    }

    public void modifyPasswordBack(View view) {
        this.finish();
    }

    private boolean getText() {
        prePass = prev_password.getText().toString();
        newPass = new_password.getText().toString();
        confirmPass = confirm_new_password.getText().toString();

        if (prePass.length() == 0) {
            Toast.makeText(this, "原密码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        } else if (newPass.length() == 0) {
            Toast.makeText(this, "新密码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        } else if (confirmPass.length() == 0) {
            Toast.makeText(this, "确认新密码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (prePass.equals(newPass)) {
            Toast.makeText(this, "原密码不能和新密码相同！", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "两次输入的密码不匹配！", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void modifyPasswordAction(View view) {
        if (getText()) {
            ArrayList<NetUtil.Param> params = new ArrayList<>();
            String uid = BaseApplication.getInstance().getSharedPreferences("User", MODE_PRIVATE).getString("uid", "");
            params.add(new NetUtil.Param("id", uid));
            params.add(new NetUtil.Param("newPassword", newPass));
            params.add(new NetUtil.Param("oldPassword", prePass));
            try {
                NetUtil.getResult(NetUtil.modifyPassWordUrl, params, new NetUtil.CallBackForResult() {
                    @Override
                    public void onFailure(final IOException e) {
                        ModifyPasswordActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ModifyPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(Response response) {
                        if (response.isSuccessful()) {
                            try {
                                final String res = response.body().string();
                                ModifyPasswordActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (res.equals("1")) {
                                            Toast.makeText(ModifyPasswordActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
                                            ModifyPasswordActivity.this.finish();
                                        } else
                                            Toast.makeText(ModifyPasswordActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
