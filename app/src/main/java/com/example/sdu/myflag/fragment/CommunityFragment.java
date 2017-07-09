package com.example.sdu.myflag.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.adapter.SampleViewPagerAdapter;
import com.example.sdu.myflag.base.BaseFragment;
import com.example.sdu.myflag.widget.CustomViewPager;

import java.util.ArrayList;

/**
 * 社区界面
 */
public class CommunityFragment extends BaseFragment {

    ArrayList<Fragment> list;
    String id;
    LinearLayout friendFlagLayout, clockLayout;
    TextView friendFlagTv, clockFlagTv;
    CustomViewPager viewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_community;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        friendFlagLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFriendFlagSelected();
            }
        });

        clockLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClockSelected();
            }
        });
    }

    @Override
    protected void init() {
        list = new ArrayList<>();
        friendFlagLayout = (LinearLayout) mRootView.findViewById(R.id.flag_friend_layout);
        clockLayout = (LinearLayout) mRootView.findViewById(R.id.clock_tv_layout);
        friendFlagTv = (TextView) mRootView.findViewById(R.id.flag_friend_tv);
        clockFlagTv = (TextView) mRootView.findViewById(R.id.clock_tv);
        viewPager = (CustomViewPager) mRootView.findViewById(R.id.community_view_pager);

        list.add(new FriendFlagFragment());
        list.add(new ClockFragment());

        SampleViewPagerAdapter adapter = new SampleViewPagerAdapter(this.getActivity().getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);
        setFriendFlagSelected();
    }

    private void setFriendFlagSelected() {
        friendFlagTv.setTextColor(getResources().getColor(R.color.tab_text_color_navigation));
        clockFlagTv.setTextColor(getResources().getColor(R.color.tab_text_color_gray));
        viewPager.setCurrentItem(0);
    }

    private void setClockSelected() {
        friendFlagTv.setTextColor(getResources().getColor(R.color.tab_text_color_gray));
        clockFlagTv.setTextColor(getResources().getColor(R.color.tab_text_color_navigation));
        viewPager.setCurrentItem(1);
    }
}
