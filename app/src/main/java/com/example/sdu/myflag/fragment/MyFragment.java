package com.example.sdu.myflag.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.activity.FriendActivity;
import com.example.sdu.myflag.activity.LookInfoActivity;
import com.example.sdu.myflag.activity.MyMessageApplySuperviseActivity;
import com.example.sdu.myflag.activity.MyMessageFriendActivity;
import com.example.sdu.myflag.activity.MyMessageSuperViseActivity;
import com.example.sdu.myflag.activity.MySuperViseActivity;
import com.example.sdu.myflag.activity.SearchFriendActivity;
import com.example.sdu.myflag.activity.SettingActivity;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.base.BaseFragment;

/**
 * 我的界面
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {

    RelativeLayout messageLayout, addFriendLayout, watchLayout, settingLayout, userInfoLayout, superviseMsgLayout, apply_message_layout;
    TextView userNameTv, userIntroTv;
    private String nickname, information, sex;
    int iconIndex;
    ImageView fragment_my_icon;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences("User", Context.MODE_PRIVATE);
        nickname = sp.getString("nickname", "");
        information = sp.getString("information", "");
        sex = sp.getString("sex", "");
        iconIndex = sp.getInt("photo", 0);

        userNameTv.setText(nickname);
        userIntroTv.setText(information);
        fragment_my_icon.setImageDrawable(getResources().getDrawable(BaseApplication.HeadIcon[iconIndex]));

        addFriendLayout.setOnClickListener(this);
        userInfoLayout.setOnClickListener(this);
        messageLayout.setOnClickListener(this);
        superviseMsgLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
        watchLayout.setOnClickListener(this);
        apply_message_layout.setOnClickListener(this);
    }

    @Override
    protected void init() {
        addFriendLayout = (RelativeLayout) mRootView.findViewById(R.id.add_friend_layout);
        userNameTv = (TextView) mRootView.findViewById(R.id.user_name_tv);
        userIntroTv = (TextView) mRootView.findViewById(R.id.user_intro_tv);
        userInfoLayout = (RelativeLayout) mRootView.findViewById(R.id.user_info_layout);
        messageLayout = (RelativeLayout) mRootView.findViewById(R.id.message_layout);
        superviseMsgLayout = (RelativeLayout) mRootView.findViewById(R.id.supervise_message_layout);
        settingLayout = (RelativeLayout) mRootView.findViewById(R.id.setting_layout);
        watchLayout = (RelativeLayout) mRootView.findViewById(R.id.watch_layout);
        apply_message_layout = (RelativeLayout) mRootView.findViewById(R.id.apply_message_layout);
        fragment_my_icon = (ImageView) mRootView.findViewById(R.id.fragment_my_icon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_friend_layout:
                startNewActivity(SearchFriendActivity.class);
                break;

            case R.id.user_info_layout:
                Intent intent = new Intent(MyFragment.this.getActivity(), LookInfoActivity.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("sex", sex);
                intent.putExtra("info", information);
                getActivity().startActivityForResult(intent, 0);
                break;

            case R.id.message_layout:
                startNewActivity(MyMessageFriendActivity.class);
                break;

            case R.id.supervise_message_layout:
                startNewActivity(MyMessageSuperViseActivity.class);
                break;

            case R.id.setting_layout:
                startNewActivity(SettingActivity.class);
                break;

            case R.id.watch_layout:
                startNewActivity(MySuperViseActivity.class);
                break;
            case R.id.apply_message_layout:
                startNewActivity(MyMessageApplySuperviseActivity.class);
                break;
        }
    }

    public void setHeadIcon(int id){
        fragment_my_icon.setImageDrawable(getResources().getDrawable(id));
    }

    public void setNickName(String nickName) {
        userNameTv.setText(nickName);
    }

    public void setIntro(String information) {
        userIntroTv.setText(information);
    }
}
