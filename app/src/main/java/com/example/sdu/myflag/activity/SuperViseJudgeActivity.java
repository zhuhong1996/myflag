package com.example.sdu.myflag.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.util.NetUtil;

import java.io.IOException;
import java.util.ArrayList;

import carbon.widget.RadioButton;
import carbon.widget.RadioGroup;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 监督评价界面
 */
public class SuperViseJudgeActivity extends BaseActivity {

    EditText judge_edit_text;
    RadioGroup judge_radio_group;
    String isFinish, evaluate, fid, uid;

    @Override
    public int getLayoutId() {
        return R.layout.activity_supervise_judge;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        judge_radio_group = (RadioGroup) findViewById(R.id.judge_radio_group);
        judge_edit_text = (EditText) findViewById(R.id.judge_edit_text);
        isFinish = "1";
        evaluate = "";
        fid = getIntent().getStringExtra("fid");
        uid = BaseApplication.getInstance().getSharedPreferences("User", MODE_PRIVATE).getString("uid", "");

        judge_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.judge_radio_ok:
                        isFinish = "2";
                        break;
                    case R.id.judge_radio_not:
                        isFinish = "1";
                        break;
                }
            }
        });
    }

    public void judgeFinishAction(View view) {
        if (uid.equals("")) {
            Toast.makeText(SuperViseJudgeActivity.this, "获取用户id失败", Toast.LENGTH_SHORT).show();
            return;
        }
        evaluate = judge_edit_text.getText().toString();
        ArrayList<NetUtil.Param> params = new ArrayList<>();
        params.add(new NetUtil.Param("fid", fid));
        params.add(new NetUtil.Param("uid", uid));
        params.add(new NetUtil.Param("achieve", isFinish));
        params.add(new NetUtil.Param("evaluate", evaluate));
        try {
            NetUtil.getResult(NetUtil.judgeSuperViseUrl, params, new JudgeCallBack());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void superViseJudgeBack(View view) {
        this.finish();
    }

    class JudgeCallBack implements NetUtil.CallBackForResult {
        @Override
        public void onFailure(final IOException e) {
            SuperViseJudgeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SuperViseJudgeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess(Response response) {
            if (response.isSuccessful()) {
                try {
                    final String res = response.body().string();
                    SuperViseJudgeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (res.equals("1")) {
                                Toast.makeText(SuperViseJudgeActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                                SuperViseJudgeActivity.this.finish();
                            }
                            else if(res.equals("2")){
                                Toast.makeText(SuperViseJudgeActivity.this, "你已评价过该FLAG，无法重复评价", Toast.LENGTH_SHORT).show();
                                SuperViseJudgeActivity.this.finish();
                            }
                            else {
                                Toast.makeText(SuperViseJudgeActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
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
