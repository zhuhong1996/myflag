package com.example.sdu.myflag.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.adapter.ClockListAdapter;
import com.example.sdu.myflag.adapter.FlagFriendListAdapter;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.base.BaseFragment;
import com.example.sdu.myflag.bean.ClockBean;
import com.example.sdu.myflag.bean.FlagBean;
import com.example.sdu.myflag.bean.SuperViseBriefBean;
import com.example.sdu.myflag.util.BaseTools;
import com.example.sdu.myflag.util.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Response;

/**
 * 好友打卡界面
 */
public class ClockFragment extends BaseFragment {

    ListView clockListView;
    ClockListAdapter adapter;
    ArrayList<ClockBean> list;
    String uid;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_clock;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                refreshData();
                Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
        refreshData();
    }

    @Override
    protected void init() {
        clockListView = (ListView) mRootView.findViewById(R.id.clock_list);
        swipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.clock_swipe_layout);
        SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences("User", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", null);
        list = new ArrayList<>();
    }

    private void refreshData(){
        GetClockResult getClockResult = new GetClockResult();

        List<NetUtil.Param> params = new LinkedList<>();
        String time = BaseTools.getParamTime();
        params.add(new NetUtil.Param("uid", "28"));
       // params.add(new NetUtil.Param("time", time));

        try {
            NetUtil.getResult(NetUtil.getClockUrl, params, getClockResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class GetClockResult implements NetUtil.CallBackForResult {
        @Override
        public void onFailure(final IOException e) {
            ClockFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ClockFragment.this.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onSuccess(Response response) {
            if (response.isSuccessful()) {
                try {
                    final String res = response.body().string();
                    JSONObject jo = new JSONObject(res);
                    JSONArray jsonArray = jo.getJSONArray("clocks");
                    int size = jsonArray.length();
                    list = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        JSONObject Clock = jsonArray.getJSONObject(i).getJSONObject("clock");
                        String cid = Clock.optString("cid");
                        int iconId = Clock.optInt("headPhoto");
                        String content = Clock.optString("content");
                        String fid = Clock.optString("fid");
                        String uid = Clock.optString("uid");
                        String nickname = Clock.optString("nickname");
                        String photo = Clock.optString("photo");
                        String time = BaseTools.stampToDate(Clock.optString("time"));
                        ClockBean clockBean = new ClockBean(cid, uid, fid, content, time, photo, iconId, nickname);
                        list.add(clockBean);
                    }
                    ClockFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ClockListAdapter(ClockFragment.this.getContext(), list);
                            clockListView.setAdapter(adapter);
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
