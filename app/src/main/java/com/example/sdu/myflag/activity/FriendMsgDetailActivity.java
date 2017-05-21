package com.example.sdu.myflag.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.bean.TempFriendBean;
import com.example.sdu.myflag.util.NetUtil;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Response;

/**
 * 加好友消息详情
 */
public class FriendMsgDetailActivity extends BaseActivity {

    TextView nicknameTv, markTv, detail_bar_nickname_tv;
    Button agreeBtn, refuseBtn;
    TextView agreeOrNot, remark_setting_tv;
    String requestUid, requestName, mark, agree, remark;
    int iconId;
    EditText remarkEdt;
    ImageView msg_icon_img;

    @Override
    public int getLayoutId() {
        return R.layout.activity_friend_msg_detail;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        requestName = "";
        requestUid = "";
        mark = "";
        remark = "";
        agree = "0";
        nicknameTv = (TextView) findViewById(R.id.nickName_tv);
        markTv = (TextView) findViewById(R.id.mark_msg_tv);
        agreeBtn = (Button) findViewById(R.id.agree_btn);
        refuseBtn = (Button) findViewById(R.id.refuse_btn);
        agreeOrNot = (TextView) findViewById(R.id.agree_ornot_tv);
        remarkEdt = (EditText) findViewById(R.id.remark_edt);
        remark_setting_tv = (TextView) findViewById(R.id.remark_setting_tv);
        detail_bar_nickname_tv = (TextView) findViewById(R.id.detail_bar_nickname_tv);
        msg_icon_img = (ImageView) findViewById(R.id.msg_icon_img);

        Intent intent = getIntent();
        TempFriendBean bean = (TempFriendBean) intent.getExtras().get("bean");
        if (bean != null) {
            requestUid = bean.requestUid;
            requestName = bean.nickname;
            mark = bean.message;
            agree = bean.agree;
            iconId = bean.iconId;
        }
        msg_icon_img.setImageDrawable(getResources().getDrawable(BaseApplication.HeadIcon[iconId]));
        detail_bar_nickname_tv.setText(requestName);
        nicknameTv.setText(requestName);
        markTv.setText(mark);
        if (agree.equals("0")) {
            agreeOrNot.setVisibility(View.GONE);
        } else if (agree.equals("1")) {
            agreeBtn.setVisibility(View.GONE);
            refuseBtn.setVisibility(View.GONE);
            remark_setting_tv.setVisibility(View.GONE);
            agreeOrNot.setText("已拒绝");
            agreeOrNot.setTextColor(Color.RED);
        } else {
            agreeBtn.setVisibility(View.GONE);
            refuseBtn.setVisibility(View.GONE);
            remark_setting_tv.setVisibility(View.GONE);
            agreeOrNot.setText("已同意");
        }
    }

    public void friendMsgAgree(View view) {
        ArrayList<NetUtil.Param> params = new ArrayList<>();
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences("User", MODE_PRIVATE);
        String id = sp.getString("uid", "");
        agree = "2";
        remark = remarkEdt.getText().toString();
        params.add(new NetUtil.Param("id", id));
        params.add(new NetUtil.Param("requestId", requestUid));
        params.add(new NetUtil.Param("agree", agree));
        params.add(new NetUtil.Param("remark", remark));
        try {
            NetUtil.getResult(NetUtil.confirmFriendUrl, params, new FriendMsgDetailCallBack());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void friendMsgRefuse(View view) {
        ArrayList<NetUtil.Param> params = new ArrayList<>();
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences("User", MODE_PRIVATE);
        String id = sp.getString("uid", "");
        agree = "1";
        params.add(new NetUtil.Param("id", id));
        params.add(new NetUtil.Param("requestId", requestUid));
        params.add(new NetUtil.Param("agree", agree));
        try {
            NetUtil.getResult(NetUtil.confirmFriendUrl, params, new FriendMsgDetailCallBack());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void settingRemark(View view) {
        remarkEdt.setVisibility(View.VISIBLE);
        remarkEdt.setText(nicknameTv.getText());
        nicknameTv.setVisibility(View.GONE);
    }

    public void FriendMsgDetailBack(View view) {
        this.finish();
    }

    class FriendMsgDetailCallBack implements NetUtil.CallBackForResult {

        @Override
        public void onFailure(final IOException e) {
            FriendMsgDetailActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FriendMsgDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess(Response response) {
            if (response.isSuccessful()) {
                try {
                    String res = response.body().string();
                    if (res.equals("1")) {
                        FriendMsgDetailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FriendMsgDetailActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                                FriendMsgDetailActivity.this.finish();
                            }
                        });
                    } else if (res.equals("0")) {
                        FriendMsgDetailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FriendMsgDetailActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
