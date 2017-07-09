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
 * 意见反馈界面
 */
public class SuggestionActivity extends BaseActivity {

    EditText suggestion_edit_text;
    String suggestion, uid;

    @Override
    public int getLayoutId() {
        return R.layout.activity_suggestion;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        suggestion_edit_text = (EditText) findViewById(R.id.suggest_edit_text);
        suggestion = "";
        uid = BaseApplication.getInstance().getSharedPreferences("User", MODE_PRIVATE).getString("uid", "");
    }

    public void judgeFinishAction(View view) {
        if (uid.equals("")) {
            Toast.makeText(SuggestionActivity.this, "获取用户id失败", Toast.LENGTH_SHORT).show();
            return;
        }
        suggestion = suggestion_edit_text.getText().toString();

        if (suggestion.equals("")) {
            Toast.makeText(SuggestionActivity.this, "请输入您的意见", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<NetUtil.Param> params = new ArrayList<>();
        params.add(new NetUtil.Param("uid", uid));
        params.add(new NetUtil.Param("content", suggestion));
        try {
            NetUtil.getResult(NetUtil.suggestionUrl, params, new SuggestionCallBack());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void superViseJudgeBack(View view) {
        this.finish();
    }

    class SuggestionCallBack implements NetUtil.CallBackForResult {
        @Override
        public void onFailure(final IOException e) {
            SuggestionActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SuggestionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess(Response response) {
            if (response.isSuccessful()) {
                try {
                    final String res = response.body().string();
                    SuggestionActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (res.equals("1")) {
                                Toast.makeText(SuggestionActivity.this, "反馈成功", Toast.LENGTH_SHORT).show();
                                SuggestionActivity.this.finish();
                            }
                            else {
                                Toast.makeText(SuggestionActivity.this, "反馈失败", Toast.LENGTH_SHORT).show();
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
