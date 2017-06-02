package com.example.sdu.myflag.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.bean.FlagBean;
import com.example.sdu.myflag.util.NetUtil;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Response;

/**
 * 监督详情界面
 */
public class SuperViseDetailActivity extends BaseActivity {

    Button judge_btn, apply_supervise_btn;
    TextView supervise_detail_award_tv, supervise_detail_time_tv, supervise_member_tv, supervise_detail_flagName_tv, supervise_detail_nickName_tv,
            isTeam_tv, supervise_ing_tv;
    String fid;
    LinearLayout supervise_mem_layout;
    FlagBean flagBean;
    String uid;
    ImageView msg_icon_img;

    @Override
    public int getLayoutId() {
        return R.layout.activity_supervise_detail;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences("User", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", null);

        supervise_detail_award_tv = (TextView) findViewById(R.id.supervise_detail_award_tv);
        supervise_detail_time_tv = (TextView) findViewById(R.id.supervise_detail_time_tv);
        supervise_member_tv = (TextView) findViewById(R.id.supervise_member_tv);
        supervise_detail_flagName_tv = (TextView) findViewById(R.id.supervise_detail_flagName_tv);
        isTeam_tv = (TextView) findViewById(R.id.isTeam_tv);
        judge_btn = (Button) findViewById(R.id.judge_btn);
        supervise_detail_nickName_tv = (TextView) findViewById(R.id.supervise_detail_nickName_tv);
        supervise_mem_layout = (LinearLayout) findViewById(R.id.supervise_mem_layout);
        apply_supervise_btn = (Button) findViewById(R.id.apply_supervise_btn);
        supervise_ing_tv = (TextView) findViewById(R.id.supervise_ing_tv);
        msg_icon_img = (ImageView) findViewById(R.id.msg_icon_img);

        Intent intent = getIntent();
        flagBean = (FlagBean) intent.getExtras().get("bean");
        int code = intent.getIntExtra("code", 0);
        if (code == 1) {
            if (flagBean.getIsFinish().equals("false"))     // 正在监督且flag未完成状态
            {
                judge_btn.setVisibility(View.GONE);
                apply_supervise_btn.setVisibility(View.GONE);
            } else {
                apply_supervise_btn.setVisibility(View.GONE);
                supervise_ing_tv.setVisibility(View.GONE);
            }
        } else if (code == 2) {
            judge_btn.setVisibility(View.GONE);
            if (flagBean.getIsSupervise())
                apply_supervise_btn.setVisibility(View.GONE);
            else
                supervise_ing_tv.setVisibility(View.GONE);
        } else {
            judge_btn.setVisibility(View.GONE);
            apply_supervise_btn.setVisibility(View.GONE);
            supervise_ing_tv.setVisibility(View.GONE);
        }
        supervise_detail_nickName_tv.setText(flagBean.getUser_name());
        supervise_detail_award_tv.setText(flagBean.getReward());
        supervise_detail_time_tv.setText(flagBean.getTime_begin() + "  -  " + flagBean.getTime_end());
        msg_icon_img.setImageDrawable(getResources().getDrawable(BaseApplication.HeadIcon[flagBean.getIconId()]));

        String member = "";
        for (int i = 0; i < flagBean.getMember().size(); i++) {
            member += (flagBean.getMember().get(i) + " ");
        }
        supervise_member_tv.setText(member);
        if (flagBean.getTeamOrNot().equals("[个人]"))
            supervise_mem_layout.setVisibility(View.GONE);
        supervise_detail_flagName_tv.setText(flagBean.getFlag());
        isTeam_tv.setText(flagBean.getTeamOrNot());
        fid = flagBean.getFid();
    }

    public void judgeBtnAction(View view) {
        Intent intent = new Intent(SuperViseDetailActivity.this, SuperViseJudgeActivity.class);
        intent.putExtra("fid", fid);
        startActivity(intent);
    }

    public void superviseDetailBack(View view) {
        SuperViseDetailActivity.this.finish();
    }

    public void lookJudge(View view) {
        Intent intent = new Intent(SuperViseDetailActivity.this, CommentActivity.class);
        intent.putExtra("briefBean", flagBean.getSuperViseBriefList());
        startActivity(intent);
    }

    public void applySuperVise(View view) {
        ArrayList<NetUtil.Param> params = new ArrayList<>();
        params.add(new NetUtil.Param("fid", fid));
        params.add(new NetUtil.Param("uid", uid));
        try {
            NetUtil.getResult(NetUtil.ApplySuperviseUrl, params, new ApplySuperviseUrlCallBack());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ApplySuperviseUrlCallBack implements NetUtil.CallBackForResult {
        @Override
        public void onFailure(final IOException e) {
            SuperViseDetailActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SuperViseDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess(Response response) {
            if (response.isSuccessful()) {
                try {
                    final String res = response.body().string();
                    SuperViseDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (res.equals("0")) {
                                Toast.makeText(SuperViseDetailActivity.this, "申请监督失败！", Toast.LENGTH_SHORT).show();
                                SuperViseDetailActivity.this.finish();
                            } else if (res.equals("1")) {
                                Toast.makeText(SuperViseDetailActivity.this, "申请监督成功！", Toast.LENGTH_SHORT).show();
                                SuperViseDetailActivity.this.finish();
                            } else if (res.equals("2")) {
                                Toast.makeText(SuperViseDetailActivity.this, "该Flag监督人数已满！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
