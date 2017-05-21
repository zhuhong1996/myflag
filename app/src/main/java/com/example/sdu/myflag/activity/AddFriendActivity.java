package com.example.sdu.myflag.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.util.BaseTools;
import com.example.sdu.myflag.util.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class AddFriendActivity extends BaseActivity {
    private String user, friendId, message;
    private Button sendButton;
    private EditText remarkEditText;
    private TextView nameTextView, informationTextView;
    private ImageView icon_img;
    Intent mIntent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_addfriend;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        init();
        getJson();
        setListener();
    }

    private void init() {
        sendButton = (Button) findViewById(R.id.addFriendSendButton);
        nameTextView = (TextView) findViewById(R.id.addFriendNameTextView);
        informationTextView = (TextView) findViewById(R.id.addFriendInformationTextView);
        remarkEditText = (EditText) findViewById(R.id.addFriendRemarkEditText);
        icon_img = (ImageView) findViewById(R.id.icon_img);

        mIntent = getIntent();
        Bundle bundle = mIntent.getExtras();
        user = bundle.getString("user");
        message = bundle.getString("message");
    }

    private void getJson() {
        try {
            JSONObject userJson = new JSONObject(user);
            int iconId = userJson.optInt("photo");
            friendId = userJson.optInt("uid") + "";
            String friendName = userJson.optString("nickname");

            icon_img.setImageDrawable(getResources().getDrawable(BaseApplication.HeadIcon[iconId]));
            if (friendName != null) {
                nameTextView.setText(friendName);
            }

            if (message != null) {
                informationTextView.setText(message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setListener() {

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BaseTools.isNetworkAvailable(AddFriendActivity.this)) {
                    Toast.makeText(AddFriendActivity.this, "当前网络不可用！", Toast.LENGTH_LONG).show();
                    return;
                }

                String remark = remarkEditText.getText().toString();
                AddFriendResult addFriendResult = new AddFriendResult();
                List<NetUtil.Param> params = new ArrayList<>();

                SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences("User", Context.MODE_PRIVATE);
                String requestId = sharedPreferences.getString("uid", null);
                if (requestId == null) {
                    Toast.makeText(AddFriendActivity.this, "获取用户ID失败！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (friendId == null) {
                    Toast.makeText(AddFriendActivity.this, "获取好友ID失败！", Toast.LENGTH_SHORT).show();
                    return;
                }

                params.add(new NetUtil.Param("id", friendId));
                params.add(new NetUtil.Param("requestId", requestId));
                params.add(new NetUtil.Param("message", message));
                params.add(new NetUtil.Param("remark", remark));

                try {
                    NetUtil.getResult(NetUtil.addFriendUrl, params, addFriendResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class AddFriendResult implements NetUtil.CallBackForResult {
        @Override
        public void onFailure(final IOException e) {
            AddFriendActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddFriendActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onSuccess(Response response) {
            if (response.isSuccessful()) {
                AddFriendActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddFriendActivity.this, "请求发送成功！", Toast.LENGTH_LONG).show();
                        setResult(RESULT_OK, mIntent);
                        AddFriendActivity.this.finish();
                    }
                });
            }
        }
    }

    public void addFriendBack(View view) {
        setResult(RESULT_CANCELED, mIntent);
        this.finish();
    }
}
