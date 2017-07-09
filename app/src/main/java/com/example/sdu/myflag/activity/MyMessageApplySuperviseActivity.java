package com.example.sdu.myflag.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.adapter.ApplySuperviseAdapter;
import com.example.sdu.myflag.adapter.FriendMessageAdapter;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.bean.ApplySuperviseBean;
import com.example.sdu.myflag.bean.TempFriendBean;
import com.example.sdu.myflag.util.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 *申请监督消息界面
 */
public class MyMessageApplySuperviseActivity extends BaseActivity {

    SwipeRefreshLayout apply_supervise_swipe_layout;
    ListView applySuperviseListView;
    ApplySuperviseAdapter applySuperviseAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mymessage_apply_supervise;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        apply_supervise_swipe_layout = (SwipeRefreshLayout) findViewById(R.id.apply_supervise_swipe_layout);
        applySuperviseListView = (ListView) findViewById(R.id.applySuperviseListView);

        apply_supervise_swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apply_supervise_swipe_layout.setRefreshing(false);
                getMessage();
                Toast.makeText(MyMessageApplySuperviseActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
        getMessage();
    }

    //此处与服务器通信，获取信息
    private boolean getMessage() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences("User", MODE_PRIVATE);
        String id = sp.getString("uid", "");

        List<NetUtil.Param> params = new ArrayList<>();
        params.add(new NetUtil.Param("uid", id));

        MyMessageCallBack myMessageCallBack = new MyMessageCallBack();
        try {
            NetUtil.getResult(NetUtil.getApplySuperviseUrl, params, myMessageCallBack);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void myMessageApply(View view) {
        this.finish();
    }

    class MyMessageCallBack implements NetUtil.CallBackForResult {

        ArrayList<ApplySuperviseBean> list;

        public MyMessageCallBack() {
            list = new ArrayList<>();
        }

        @Override
        public void onFailure(final IOException e) {
            MyMessageApplySuperviseActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MyMessageApplySuperviseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess(Response response) {
            if (response.isSuccessful()) {
                try {
                    String res = response.body().string();
                    JSONObject request = new JSONObject(res);
                    JSONArray jsonArray = request.getJSONArray("request");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String nickname = jsonObject.optString("nickname");
                        String fid = jsonObject.optString("fid");
                        String applyUid = jsonObject.optString("applyUid");
                        String agree = jsonObject.optString("agree");
                        int iconId = jsonObject.optInt("photo");

                        ApplySuperviseBean applySuperviseBean = new ApplySuperviseBean();
                        applySuperviseBean.agree = agree;
                        applySuperviseBean.fid = fid;
                        applySuperviseBean.applyUid = applyUid;
                        applySuperviseBean.nickname = nickname;
                        applySuperviseBean.iconId = iconId;
                        list.add(applySuperviseBean);
                    }
                    MyMessageApplySuperviseActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            applySuperviseAdapter = new ApplySuperviseAdapter(MyMessageApplySuperviseActivity.this, list);
                            applySuperviseListView.setAdapter(applySuperviseAdapter);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        public ArrayList<ApplySuperviseBean> getList() {
            return list;
        }
    }
}
