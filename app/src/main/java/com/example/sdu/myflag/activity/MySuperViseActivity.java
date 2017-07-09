package com.example.sdu.myflag.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.adapter.FlagFriendListAdapter;
import com.example.sdu.myflag.adapter.FlagListAdapter;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.bean.FlagBean;
import com.example.sdu.myflag.bean.SuperViseBriefBean;
import com.example.sdu.myflag.util.BaseTools;
import com.example.sdu.myflag.util.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Response;

/**
 * 我的监督界面
 */
public class MySuperViseActivity extends BaseActivity {
    ListView listView;
    SwipeRefreshLayout community_swipe_layout;
    FlagFriendListAdapter listAdapter;
    ArrayList<FlagBean> list;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_supervise;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        community_swipe_layout = (SwipeRefreshLayout) findViewById(R.id.my_supervise_swipe_layout);
        listView = (ListView) findViewById(R.id.my_supervise_lv);
        community_swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                community_swipe_layout.setRefreshing(false);
                getFlag();
                Toast.makeText(MySuperViseActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
        getFlag();
    }

    private void getFlag() {
        SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences("User", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("uid", null);

        if (id == null) {
            Toast.makeText(MySuperViseActivity.this, "获取用户ID失败！", Toast.LENGTH_SHORT).show();
            return;
        }

        Date date = new Date();
        long t = date.getTime() / 1000;
        String time = Long.toString(t);
        time = time.substring(time.length() - 10);

        GetFlagResult getFlagResult = new GetFlagResult();

        List<NetUtil.Param> params = new LinkedList<>();
        params.add(new NetUtil.Param("id", id));
        params.add(new NetUtil.Param("time", time));

        try {
            NetUtil.getResult(NetUtil.mySuperViseUrl, params, getFlagResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mySuperviseBack(View view) {
        this.finish();
    }

    private class GetFlagResult implements NetUtil.CallBackForResult {
        @Override
        public void onFailure(final IOException e) {
            MySuperViseActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MySuperViseActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onSuccess(Response response) {
            if (response.isSuccessful()) {
                try {
                    final String res = response.body().string();
                    JSONObject jo = new JSONObject(res);
                    JSONArray jsonArray = jo.getJSONArray("flags");
                    int size = jsonArray.length();
                    list = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        JSONObject js = jsonArray.getJSONObject(i).getJSONObject("flag");
                        JSONArray SuperviseBrief = jsonArray.getJSONObject(i).getJSONArray("friendsJudge");
                        ArrayList<String> SuperviseBriefList = new ArrayList<>();
                        ArrayList<SuperViseBriefBean> BriefList = new ArrayList<>();
                        JSONArray userBrief = jsonArray.getJSONObject(i).getJSONArray("member");
                        ArrayList<String> userBriefList = new ArrayList<>();
                        for (int j = 0; j < SuperviseBrief.length(); j++) {
                            JSONObject jb = SuperviseBrief.getJSONObject(j);
                            String uid = jb.optString("uid");
                            String nickname = jb.optString("nickname");
                            String evaluate = jb.optString("evaluate");
                            String achieve = jb.optString("achieve");
                            int iconId = jb.optInt("photo");
                            SuperviseBriefList.add(nickname);
                            BriefList.add(new SuperViseBriefBean(uid, nickname, achieve, evaluate, iconId));
                        }
                        for (int j = 0; j < userBrief.length(); j++) {
                            JSONObject jb = userBrief.getJSONObject(j);
                            String nickname = jb.optString("nickname");
                            userBriefList.add(nickname);
                        }

                        String content = js.optString("content");
                        String award = js.optString("award");
                        String achieve = js.optString("achieve");
                        String fid = js.optString("fid");
                        String id = js.optString("id");
                        String nickname = js.optString("nickname");
                        String startTime = stampToDate(js.optString("startTime"));
                        long end = js.optLong("endTime");
                        int iconId = js.optInt("photo");
                        String endTime = stampToDate(end + "");
                        String createTime = js.optString("createTime");
                        String teamOrNot = js.optString("isTeam").equals("true") ? "[团队]" : "[个人]";
                        String isFinish = "false";
                        if (BaseTools.isFlagOverdue(end))
                            isFinish = "true";
                        FlagBean flagBean = new FlagBean(fid, nickname, content, startTime, endTime, SuperviseBriefList, userBriefList, award, teamOrNot, isFinish, BriefList, achieve);
                        flagBean.iconId = iconId;
                        list.add(flagBean);
                    }
                    MySuperViseActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listAdapter = new FlagFriendListAdapter(MySuperViseActivity.this, list);
                            listView.setAdapter(listAdapter);
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
