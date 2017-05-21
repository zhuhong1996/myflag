package com.example.sdu.myflag.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.R;
import com.example.sdu.myflag.util.BaseTools;
import com.example.sdu.myflag.util.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Response;


public class SearchFriendActivity extends BaseActivity implements SearchView.OnQueryTextListener {
    private SearchView sv;
    private TextView empty_layout;
    String user;

    @Override
    public int getLayoutId() {
        return R.layout.activity_searchfriend;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        init();
        sv.setOnQueryTextListener(this);
    }

    private void init() {
        sv = (SearchView) findViewById(R.id.searchView);
        empty_layout = (TextView) findViewById(R.id.no_user_tv);
        int tv_id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView tv = (TextView) sv.findViewById(tv_id);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    private void searchFriend(String str) {
        if (!BaseTools.isNetworkAvailable(SearchFriendActivity.this)) {
            Toast.makeText(SearchFriendActivity.this, "当前网络不可用！", Toast.LENGTH_LONG).show();
            return;
        }

        SearchFriendResult searchFriendResult = new SearchFriendResult();
        List<NetUtil.Param> params = new LinkedList<NetUtil.Param>();

        if (str.length() == 11) { // 手机号
            params.add(new NetUtil.Param("phone", str));
        } else { // id
            params.add(new NetUtil.Param("id", str));
        }

        try {
            NetUtil.getResult(NetUtil.findUserUrl, params, searchFriendResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchFriend(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private class SearchFriendResult implements NetUtil.CallBackForResult {

        @Override
        public void onFailure(final IOException e) {
            SearchFriendActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SearchFriendActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onSuccess(Response response) {
            if (response.isSuccessful()) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    user = jsonObject.getString("user");
                    JSONObject userObj = jsonObject.getJSONObject("user");
                    int uid = userObj.getInt("uid");
                    if (uid == 0) {
                        SearchFriendActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                empty_layout.setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        //查找成功，跳转到添加界面
                        //json解析在添加界面进行
                        SearchFriendActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                empty_layout.setVisibility(View.GONE);
                                Intent intent = new Intent();
                                intent.setClass(SearchFriendActivity.this, AddFriendMessageActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void searchFriendBack(View view){
        this.finish();
    }
}