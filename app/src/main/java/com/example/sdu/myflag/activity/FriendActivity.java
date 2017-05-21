package com.example.sdu.myflag.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.adapter.FriendListAdapter;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.bean.FriendBean;
import com.example.sdu.myflag.util.CharacterParser;
import com.example.sdu.myflag.util.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Response;

/**
 * 好友列表界面
 */
public class FriendActivity extends BaseActivity {

    private ListView listView;
    private FriendListAdapter friendListAdapter;
    int code;
    Button complete;

    @Override
    public int getLayoutId() {
        return R.layout.activity_friend;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        listView = (ListView) findViewById(R.id.listView);
        code = getIntent().getIntExtra("code", 0);
        complete = (Button) findViewById(R.id.complete_btn);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selected", friendListAdapter.getSelected());
                setResult(1, intent);
                FriendActivity.this.finish();
            }
        });
        if (code == 0)
            complete.setVisibility(View.GONE);

        getData();
    }

    private void getData() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences("User", MODE_PRIVATE);
        String id = sp.getString("uid", "");
        ArrayList<NetUtil.Param> params = new ArrayList<>();
        params.add(new NetUtil.Param("id", id));
        try {
            NetUtil.getResult(NetUtil.getFriendListUrl, params, new FriendListCallBack());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void FriendBack(View view) {
        this.finish();
    }

    class FriendListCallBack implements NetUtil.CallBackForResult {

        @Override
        public void onFailure(IOException e) {
            FriendActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FriendActivity.this, "获取好友列表失败", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess(final Response response) {
            if (response.isSuccessful()) {
                try {
                    final String res = response.body().string();
                    FriendActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONArray jsonArray = null;
                            try {
                                JSONObject friend = new JSONObject(res);
                                jsonArray = friend.getJSONArray("friend");

                                ArrayList<FriendBean> list = new ArrayList<>();
                                CharacterParser characterParser = new CharacterParser();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String nickname = jsonObject.optString("nickname");
                                    String remark = jsonObject.optString("remark");
                                    String uid = jsonObject.optString("uid");
                                    int iconId = jsonObject.optInt("photo");

                                    String pinYin = characterParser.getSelling(nickname);
                                    String sortString = pinYin.substring(0, 1).toUpperCase();
                                    String firstLetter = "";
                                    if (sortString.matches("[A-Z]")) {
                                        firstLetter = sortString.toUpperCase();
                                    } else {
                                        firstLetter = "#";
                                    }
                                    list.add(new FriendBean(uid, nickname, firstLetter, remark, iconId));
                                }
                                Collections.sort(list);

                                friendListAdapter = new FriendListAdapter(FriendActivity.this, list, code);
                                listView.setAdapter(friendListAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
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
