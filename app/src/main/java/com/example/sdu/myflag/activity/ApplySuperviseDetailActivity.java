package com.example.sdu.myflag.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.bean.ApplySuperviseBean;
import com.example.sdu.myflag.bean.SuperviseBean;
import com.example.sdu.myflag.util.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Response;

/**
 * 申请监督flag详情
 */
public class ApplySuperviseDetailActivity extends BaseActivity {
    TextView nickNameTv, flagNameTv, timeTv, awardTv, agree_ornot_tv, isTeam_tv;
    String flagName, startTime, endTime, award, agree, flagId, isTeam, applyUId;
    Button agreeBtn, refuseBtn;
    int iconId;
    ImageView msg_icon_img;

    @Override
    public int getLayoutId() {
        return R.layout.activity_applysupervise_detail;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        nickNameTv = (TextView) findViewById(R.id.applysupervise_detail_nickName_tv);
        flagNameTv = (TextView) findViewById(R.id.applysupervise_detail_flagName_tv);
        timeTv = (TextView) findViewById(R.id.supervise_detail_time_tv);
        awardTv = (TextView) findViewById(R.id.supervise_detail_award_tv);
        agreeBtn = (Button) findViewById(R.id.agree_apply_supervise_btn);
        refuseBtn = (Button) findViewById(R.id.refuse_apply_supervise_btn);
        agree_ornot_tv = (TextView) findViewById(R.id.agree_ornot_tv);
        isTeam_tv = (TextView) findViewById(R.id.isTeam_tv);
        msg_icon_img = (ImageView) findViewById(R.id.msg_icon_img);

        Intent intent = getIntent();
        ApplySuperviseBean bean = (ApplySuperviseBean) intent.getExtras().get("bean");
        if (bean != null) {
            flagId = bean.fid;
            nickNameTv.setText(bean.nickname);
            agree = bean.agree;
            iconId = bean.iconId;
            applyUId = bean.applyUid;
        }
        msg_icon_img.setImageDrawable(getResources().getDrawable(BaseApplication.HeadIcon[iconId]));

        if (agree.equals("0")) {
            agree_ornot_tv.setVisibility(View.GONE);
        } else if (agree.equals("1")) {
            agreeBtn.setVisibility(View.GONE);
            refuseBtn.setVisibility(View.GONE);
            agree_ornot_tv.setText("已拒绝");
            agree_ornot_tv.setTextColor(Color.RED);
        } else {
            agreeBtn.setVisibility(View.GONE);
            refuseBtn.setVisibility(View.GONE);
            agree_ornot_tv.setText("已同意");
        }

        ArrayList<NetUtil.Param> params = new ArrayList<>();
        params.add(new NetUtil.Param("id", flagId));
        try {
            NetUtil.getResult(NetUtil.getOneFlagUrl, params, new SuperViseDetailCallBack());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void applySuperviseDetailBack(View view) {
        this.finish();
    }

    public void agreeApply(View view) {
        ArrayList<NetUtil.Param> params = new ArrayList<>();
        agree = "2";
        params.add(new NetUtil.Param("uid", applyUId));
        params.add(new NetUtil.Param("agree", agree));
        params.add(new NetUtil.Param("fid", flagId));
        try {
            NetUtil.getResult(NetUtil.confirmApplySuperviseUrl, params, new ApplySuperViseActionCallBack());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refuseApply(View view) {
        ArrayList<NetUtil.Param> params = new ArrayList<>();
        agree = "1";
        params.add(new NetUtil.Param("uid", applyUId));
        params.add(new NetUtil.Param("agree", agree));
        params.add(new NetUtil.Param("fid", flagId));
        try {
            NetUtil.getResult(NetUtil.confirmApplySuperviseUrl, params, new ApplySuperViseActionCallBack());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class SuperViseDetailCallBack implements NetUtil.CallBackForResult {

        @Override
        public void onFailure(final IOException e) {
            ApplySuperviseDetailActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ApplySuperviseDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess(Response response) {
            if (response.isSuccessful()) {
                try {
                    final String res = response.body().string();
                    JSONObject jo = new JSONObject(res);
                    JSONObject jsonObject = jo.getJSONObject("flag");
                    flagName = jsonObject.optString("content");
                    award = jsonObject.optString("award");
                    startTime = stampToDate(jsonObject.optString("startTime"));
                    endTime = stampToDate(jsonObject.optString("endTime"));
                    String Team = jsonObject.optString("isTeam");
                    if (Team.equals("0"))
                        isTeam = "个人";
                    else
                        isTeam = "团体";
                    ApplySuperviseDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            flagNameTv.setText(flagName);
                            awardTv.setText(award);
                            timeTv.setText(startTime + " - " + endTime);
                            isTeam_tv.setText('[' + isTeam + "]：");
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ApplySuperViseActionCallBack implements NetUtil.CallBackForResult {
        @Override
        public void onFailure(final IOException e) {
            ApplySuperviseDetailActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ApplySuperviseDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess(final Response response) {
            if (response.isSuccessful()) {
                ApplySuperviseDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ApplySuperviseDetailActivity.this, "又有哦有哦到这儿了", Toast.LENGTH_SHORT).show();
                    }
                });
                try {
                    String res = response.body().string();
                    if (res.equals("1")) {
                        ApplySuperviseDetailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ApplySuperviseDetailActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                                ApplySuperviseDetailActivity.this.finish();
                            }
                        });
                    } else {
                        ApplySuperviseDetailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ApplySuperviseDetailActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
    * 将时间戳转换为时间
    */
    public String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = Long.valueOf(s) * 1000;
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
