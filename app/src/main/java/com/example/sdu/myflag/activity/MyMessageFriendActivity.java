package com.example.sdu.myflag.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sdu.myflag.adapter.FriendMessageAdapter;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.bean.TempFriendBean;
import com.example.sdu.myflag.util.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;

public class MyMessageFriendActivity extends BaseActivity {

    private ListView listView;
    private FriendMessageAdapter friendMessageAdapter;
    private SwipeRefreshLayout friend_msg_swipe_layout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mymessage_friend;
    }

    //此处与服务器通信，获取信息
    private boolean getMessage() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences("User", MODE_PRIVATE);
        String id = sp.getString("uid", "");

        List<NetUtil.Param> params = new ArrayList<>();
        params.add(new NetUtil.Param("id", id));

        MyMessageCallBack myMessageCallBack = new MyMessageCallBack();
        try {
            NetUtil.getResult(NetUtil.getFriendMsgUrl, params, myMessageCallBack);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        listView = (ListView) findViewById(R.id.myMessageListView);
        getMessage();
        friend_msg_swipe_layout = (SwipeRefreshLayout) findViewById(R.id.friend_msg_swipe_layout);
        friend_msg_swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                friend_msg_swipe_layout.setRefreshing(false);
                getMessage();
                Toast.makeText(MyMessageFriendActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void myMessageFriendBack(View view) {
        this.finish();
    }

    class MyMessageCallBack implements NetUtil.CallBackForResult {

        ArrayList<TempFriendBean> list;

        public MyMessageCallBack() {
            list = new ArrayList<>();
        }

        @Override
        public void onFailure(final IOException e) {
            MyMessageFriendActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MyMessageFriendActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        String phone = jsonObject.optString("phone");
                        String message = jsonObject.optString("message");
                        String requestUid = jsonObject.optString("requestUid");
                        String agree = jsonObject.optString("agree");
                        int iconId = jsonObject.optInt("photo");

                        list.add(new TempFriendBean(nickname, phone, message, requestUid, agree, iconId));
                    }
                    MyMessageFriendActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            friendMessageAdapter = new FriendMessageAdapter(MyMessageFriendActivity.this, list);
                            listView.setAdapter(friendMessageAdapter);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        public ArrayList<TempFriendBean> getList() {
            return list;
        }
    }
}
