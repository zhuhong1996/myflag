package com.example.sdu.myflag.activity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.base.BaseApplication;

/**
 * 查看个人信息
 */
public class LookInfoActivity extends BaseActivity {

    private TextView nickNameTv, infoTv, sex_tv;
    private String nickname, sex, info;
    private boolean isEdit;
    private int select = BaseApplication.getInstance().getSharedPreferences("User", Context.MODE_PRIVATE).getInt("photo", 0);

    @Override
    public int getLayoutId() {
        return R.layout.activity_lookinfo;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        nickNameTv = (TextView) findViewById(R.id.nick_name_tv);
        infoTv = (TextView) findViewById(R.id.info_tv);
        sex_tv = (TextView) findViewById(R.id.sex_tv);
        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        sex = intent.getStringExtra("sex");
        info = intent.getStringExtra("info");
        isEdit = false;
        sex_tv.setText(sex);
        nickNameTv.setText(nickname);
        infoTv.setText(info);
    }

    public void editInfo(View view) {
        Intent intent = new Intent(LookInfoActivity.this, EditInfoActivity.class);
        intent.putExtra("nickname", nickNameTv.getText());
        intent.putExtra("info", infoTv.getText());
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                String nicknameStr = data.getStringExtra("nickname");
                String infoStr = data.getStringExtra("information");
                select = data.getIntExtra("photo", 0);
                nickNameTv.setText(nicknameStr);
                infoTv.setText(infoStr);
                nickname = nicknameStr;
                info = infoStr;
                isEdit = true;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isEdit) {
            Intent intent = new Intent();
            intent.putExtra("nickname", nickname);
            intent.putExtra("info", info);
            intent.putExtra("photo", select);
            setResult(1, intent);
            LookInfoActivity.this.finish();
        } else
            LookInfoActivity.this.finish();
    }

    public void lookInfoBackTo(View view) {
        onBackPressed();
    }
}
