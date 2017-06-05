package com.example.sdu.myflag.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.adapter.SuperViseMsgAdapter;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.bean.SuperviseBean;
import com.example.sdu.myflag.util.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Response;

/**
 * 监督消息界面
 */
public class MyMessageSuperViseActivity extends BaseActivity {

    private ListView listView;
    private SuperViseMsgAdapter adapter;
    private SharedPreferences sp;
    private SwipeRefreshLayout supervise_swipe_layout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mymessage_supervise;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        listView = (ListView) findViewById(R.id.myMessageSuperViseListView);
        supervise_swipe_layout = (SwipeRefreshLayout) findViewById(R.id.supervise_swipe_layout);
        getMessage();
        supervise_swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                supervise_swipe_layout.setRefreshing(false);
                getMessage();
                Toast.makeText(MyMessageSuperViseActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMessage() {
        sp = BaseApplication.getInstance().getSharedPreferences("User", MODE_PRIVATE);
        String id = sp.getString("uid", "");
        ArrayList<NetUtil.Param> params = new ArrayList<>();
        params.add(new NetUtil.Param("id", id));
        try {
            NetUtil.getResult(NetUtil.getSuperViseMsgUrl, params, new SuperViseMsgCallBack());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void myMessageSuperviseBack(View view) {
        this.finish();
    }

    class SuperViseMsgCallBack implements NetUtil.CallBackForResult {

        private ArrayList<SuperviseBean> mList;

        public SuperViseMsgCallBack(){
            mList = new ArrayList<>();
        }

        @Override
        public void onFailure(final IOException e) {
            MyMessageSuperViseActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MyMessageSuperViseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess(Response response) {
            if (response.isSuccessful()) {
                try {
                    String res = response.body().string();
                    JSONObject jo = new JSONObject(res);
                    JSONArray jsonArray = jo.getJSONArray("request");

                    int size = jsonArray.length();
                    for (int i = 0; i < size; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String uid = jsonObject.optString("uid");
                        String agree = jsonObject.optString("agree");
                        String fid = jsonObject.optString("fid");
                        String nickname = jsonObject.optString("nickname");
                        int iconId = jsonObject.optInt("photo");
                        SuperviseBean superviseBean = new SuperviseBean(fid, agree, uid);
                        superviseBean.iconId = iconId;
                        superviseBean.nickname = nickname;
                        mList.add(superviseBean);
                    }
                    MyMessageSuperViseActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new SuperViseMsgAdapter(MyMessageSuperViseActivity.this, mList);
                            listView.setAdapter(adapter);
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
}
