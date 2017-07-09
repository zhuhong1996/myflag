package com.example.sdu.myflag.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.activity.FriendFlagActivity;
import com.example.sdu.myflag.adapter.FriendListAdapter;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.base.BaseFragment;
import com.example.sdu.myflag.bean.FriendBean;
import com.example.sdu.myflag.util.BaseTools;
import com.example.sdu.myflag.util.CharacterParser;
import com.example.sdu.myflag.util.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Response;


public class FriendFragment extends BaseFragment {

    private ListView listView;
    private FriendListAdapter friendListAdapter;
    ArrayList<FriendBean> list;

    public FriendFragment() {
        // Required empty public constructor
    }

    protected int getLayoutId() {
        return R.layout.fragment_friend;
    }

    protected void init() {
        listView = (ListView) mRootView.findViewById(R.id.friendListView);
        list = new ArrayList<>();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        getData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FriendFragment.this.getActivity(), FriendFlagActivity.class);
                FriendBean bean = list.get(position);

                intent.putExtra("uid", bean.getId());
                intent.putExtra("headIndex", bean.getIconId());
                intent.putExtra("nickname", bean.getName());

                FriendFragment.this.startActivity(intent);
            }
        });
    }

    private void getData() {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences("User", Context.MODE_PRIVATE);
        String id = sp.getString("uid", "");
        ArrayList<NetUtil.Param> params = new ArrayList<>();
        params.add(new NetUtil.Param("id", id));
        try {
            NetUtil.getResult(NetUtil.getFriendListUrl, params, new FriendListCallBack());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class FriendListCallBack implements NetUtil.CallBackForResult {

        @Override
        public void onFailure(IOException e) {
            FriendFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FriendFragment.this.getActivity(), "获取好友列表失败", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess(final Response response) {
            if (response.isSuccessful()) {
                try {
                    final String res = response.body().string();
                    FriendFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONArray jsonArray = null;
                            try {
                                JSONObject friend = new JSONObject(res);
                                jsonArray = friend.getJSONArray("friend");

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

                                friendListAdapter = new FriendListAdapter(FriendFragment.this.getContext(), list, 0);
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
