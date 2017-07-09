package com.example.sdu.myflag.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdu.myflag.R;
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

public class FriendFlagActivity extends BaseActivity {

    ListView listView;
    FlagListAdapter listAdapter;
    String uid;
    ArrayList<FlagBean> list;

    LinearLayout emptyLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    String nickname;
    TextView fragment_main_nickname_tv;
    ImageView main_head_icon;
    int headIndex;

    public int getLayoutId() {
        return R.layout.activity_friend_flag;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        fragment_main_nickname_tv = (TextView) findViewById(R.id.friend_flag_nickname_tv);
        main_head_icon = (ImageView) findViewById(R.id.friend_flag_head_icon);
        listView = (ListView) findViewById(R.id.friend_flag_lv);
        emptyLayout = (LinearLayout) findViewById(R.id.empty_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.friend_flag_swipe);

        uid = getIntent().getExtras().getString("uid");
        nickname = getIntent().getExtras().getString("nickname");
        fragment_main_nickname_tv.setText(nickname);
        headIndex = getIntent().getExtras().getInt("headIndex", 0);
        main_head_icon.setImageDrawable(getResources().getDrawable(BaseApplication.HeadIcon[headIndex]));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getFlag();
                Toast.makeText(FriendFlagActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FriendFlagActivity.this, SuperViseDetailActivity.class);
                intent.putExtra("bean", list.get(position));
                intent.putExtra("code", 3);
                FriendFlagActivity.this.startActivity(intent);
            }
        });

        getFlag();
    }

    private void getFlag() {

        Date date = new Date();
        long t = date.getTime() / 1000;
        String time = Long.toString(t);
        time = time.substring(time.length() - 10);

        GetFlagResult getFlagResult = new GetFlagResult();

        List<NetUtil.Param> params = new LinkedList<>();
        params.add(new NetUtil.Param("id", uid));
        params.add(new NetUtil.Param("time", time));

        try {
            NetUtil.getResult(NetUtil.getMyFlagUrl, params, getFlagResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class GetFlagResult implements NetUtil.CallBackForResult {
        @Override
        public void onFailure(final IOException e) {
            FriendFlagActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FriendFlagActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onSuccess(Response response) {
            if (response.isSuccessful()) {
                try {
                    String res = response.body().string();
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
                        String startTime = stampToDate(js.optString("startTime"));
                        long end = js.optLong("endTime");
                        String endTime = stampToDate(end + "");
                        String createTime = js.optString("createTime");
                        String teamOrNot = js.optString("isTeam").equals("true") ? "[团队]" : "[个人]";
                        String isFinish = "false";
                        if (BaseTools.isFlagOverdue(end))
                            isFinish = "true";
                        FlagBean flagBean = new FlagBean(fid, nickname, content, startTime, endTime, SuperviseBriefList, userBriefList, award, teamOrNot, isFinish, BriefList, achieve);
                        flagBean.iconId = headIndex;
                        list.add(flagBean);
                    }
                    FriendFlagActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listAdapter = new FlagListAdapter(FriendFlagActivity.this, list);
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

    public void setIcon(int id) {
        main_head_icon.setImageDrawable(getResources().getDrawable(BaseApplication.HeadIcon[id]));
    }


    /*
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        FriendFlagActivity.this.getWindow().setAttributes(lp);
    }

}
