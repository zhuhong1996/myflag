package com.example.sdu.myflag.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.base.BaseApplication;

/**
 * 修改头像界面
 */
public class EditHeadIconActivity extends BaseActivity {

    int select = BaseApplication.getInstance().getSharedPreferences("User", Context.MODE_PRIVATE).getInt("photo", 0);
    ImageView head_icon_check[];

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_head_icon;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        head_icon_check = new ImageView[9];

        head_icon_check[0] = (ImageView) findViewById(R.id.head_icon_check1);
        head_icon_check[1] = (ImageView) findViewById(R.id.head_icon_check2);
        head_icon_check[2] = (ImageView) findViewById(R.id.head_icon_check3);
        head_icon_check[3] = (ImageView) findViewById(R.id.head_icon_check4);
        head_icon_check[4] = (ImageView) findViewById(R.id.head_icon_check5);
        head_icon_check[5] = (ImageView) findViewById(R.id.head_icon_check6);
        head_icon_check[6] = (ImageView) findViewById(R.id.head_icon_check7);
        head_icon_check[7] = (ImageView) findViewById(R.id.head_icon_check8);
        head_icon_check[8] = (ImageView) findViewById(R.id.head_icon_check9);
    }

    public void editIconBack(View view) {
        setResult(RESULT_CANCELED);
        this.finish();
    }

    // 确认修改按钮点击事件
    public void confirmChangeIcon(View view) {
        if(select == 0)
            Toast.makeText(EditHeadIconActivity.this, "未选择头像！", Toast.LENGTH_SHORT).show();
        else {
            setResult(select);
            this.finish();
        }
    }

    public void onClick_head_icon1(View view) {
        for(int i = 0; i < 9; i++){
            if(i == 0)
                head_icon_check[i].setVisibility(View.VISIBLE);
            else
                head_icon_check[i].setVisibility(View.GONE);
        }
        select = 1;
    }

    public void onClick_head_icon2(View view) {
        for(int i = 0; i < 9; i++){
            if(i == 1)
                head_icon_check[i].setVisibility(View.VISIBLE);
            else
                head_icon_check[i].setVisibility(View.GONE);
        }
        select = 2;
    }

    public void onClick_head_icon3(View view) {
        for(int i = 0; i < 9; i++){
            if(i == 2)
                head_icon_check[i].setVisibility(View.VISIBLE);
            else
                head_icon_check[i].setVisibility(View.GONE);
        }
        select = 3;
    }

    public void onClick_head_icon4(View view) {
        for(int i = 0; i < 9; i++){
            if(i == 3)
                head_icon_check[i].setVisibility(View.VISIBLE);
            else
                head_icon_check[i].setVisibility(View.GONE);
        }
        select = 4;
    }

    public void onClick_head_icon5(View view) {
        for(int i = 0; i < 9; i++){
            if(i == 4)
                head_icon_check[i].setVisibility(View.VISIBLE);
            else
                head_icon_check[i].setVisibility(View.GONE);
        }
        select = 5;
    }

    public void onClick_head_icon6(View view) {
        for(int i = 0; i < 9; i++){
            if(i == 5)
                head_icon_check[i].setVisibility(View.VISIBLE);
            else
                head_icon_check[i].setVisibility(View.GONE);
        }
        select = 6;
    }

    public void onClick_head_icon7(View view) {
        for(int i = 0; i < 9; i++){
            if(i == 6)
                head_icon_check[i].setVisibility(View.VISIBLE);
            else
                head_icon_check[i].setVisibility(View.GONE);
        }
        select = 7;
    }

    public void onClick_head_icon8(View view) {
        for(int i = 0; i < 9; i++){
            if(i == 7)
                head_icon_check[i].setVisibility(View.VISIBLE);
            else
                head_icon_check[i].setVisibility(View.GONE);
        }
        select = 8;
    }

    public void onClick_head_icon9(View view) {
        for(int i = 0; i < 9; i++){
            if(i == 8)
                head_icon_check[i].setVisibility(View.VISIBLE);
            else
                head_icon_check[i].setVisibility(View.GONE);
        }
        select = 9;
    }
}
