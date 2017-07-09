package com.example.sdu.myflag.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.util.NetUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import carbon.widget.RadioButton;
import carbon.widget.RadioGroup;
import okhttp3.Response;

/**
 * 修改个人信息界面
 */
public class EditInfoActivity extends BaseActivity {

    private ImageView nicknameView,sexView,signalView;
    private EditText nickNameEdt, signatureEdt;
    private String nickName, signature, sex;
    private SharedPreferences sharedPreferences;
    private RadioButton male, female;
    private RadioGroup radioGroup;
    private int select = BaseApplication.getInstance().getSharedPreferences("User", Context.MODE_PRIVATE).getInt("photo", 0);;
    private ImageView headIcon;

    @Override
    public int getLayoutId() {
        return R.layout.activity_editinfo;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        Intent intent = this.getIntent();
        nickName = intent.getStringExtra("nickname");
        signature = intent.getStringExtra("info");

        nicknameView=(ImageView)findViewById(R.id.nicknameView);
        sexView=(ImageView)findViewById(R.id.sexView);
        signalView=(ImageView)findViewById(R.id.signalView);



        nickNameEdt = (EditText) findViewById(R.id.nick_name_edt);
        signatureEdt = (EditText) findViewById(R.id.signature_edt);
        radioGroup = (RadioGroup) findViewById(R.id.sex_radio_group);
        male = (RadioButton) findViewById(R.id.male_rb);
        female = (RadioButton) findViewById(R.id.female_rb);
        headIcon = (ImageView) findViewById(R.id.head_icon);

        headIcon.setImageDrawable(getResources().getDrawable(BaseApplication.HeadIcon[select]));

        if (male.isChecked())
            sex = "男";
        else if (female.isChecked())
            sex = "女";
        else
            sex = "";
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == male.getId()) {
                    sex = "男";
                } else if (checkedId == female.getId()) {
                    sex = "女";
                }
            }
        });
        nickNameEdt.setText(nickName);
        signatureEdt.setText(signature);
    }

    public void editInfoPost(View view) {
        if (nickNameEdt.getText().toString() == null || nickNameEdt.getText().toString().length() == 0) {
            Toast.makeText(EditInfoActivity.this, "请输入昵称", Toast.LENGTH_SHORT).show();
            return;
        } else if (sex.equals("")) {
            Toast.makeText(EditInfoActivity.this, "请选择性别", Toast.LENGTH_SHORT).show();
            return;
        }
        nickName = nickNameEdt.getText().toString();
        signature = signatureEdt.getText().toString();

        List<NetUtil.Param> params = new ArrayList<>();
        sharedPreferences = BaseApplication.getInstance().getSharedPreferences("User", Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");
        String email = sharedPreferences.getString("email", "");
        params.add(new NetUtil.Param("id", uid));
        params.add(new NetUtil.Param("nickname", nickName));
        params.add(new NetUtil.Param("information", signature));
        params.add(new NetUtil.Param("sex", sex));
        params.add(new NetUtil.Param("email", email));
        params.add(new NetUtil.Param("photo", select + ""));
        Log.v("resultkk", select + "");
        try {
            NetUtil.getResult(NetUtil.editInfoUrl, params, new EditInfoCallBack());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editHeadIcon(View view) {
        startActivityForResult(new Intent(EditInfoActivity.this, EditHeadIconActivity.class), 0);
    }

    class EditInfoCallBack implements NetUtil.CallBackForResult {
        @Override
        public void onFailure(final IOException e) {
            EditInfoActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(EditInfoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onSuccess(Response response) {
            if (response.isSuccessful()) {
                try {
                    String res = response.body().string();
                    if (res.equals("1")) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("nickname", nickName).apply();
                        editor.putString("information", signature).apply();
                        editor.putString("sex", sex).apply();
                        editor.putInt("photo", select).apply();
                        EditInfoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(EditInfoActivity.this, "修改信息成功", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent();
                                intent.putExtra("nickname", nickName);
                                intent.putExtra("information", signature);
                                intent.putExtra("sex", sex);
                                intent.putExtra("photo", select);
                                EditInfoActivity.this.setResult(1, intent);
                                EditInfoActivity.this.finish();
                            }
                        });
                    } else if (res.equals("0")) {
                        EditInfoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(EditInfoActivity.this, "修改信息失败", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void editInfoBackTo(View view) {
        setResult(0);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_CANCELED:
                break;
            default:
                select = resultCode;
                headIcon.setImageDrawable(getResources().getDrawable(BaseApplication.HeadIcon[select]));
        }
    }
}
