package com.example.sdu.myflag.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.adapter.SampleViewPagerAdapter;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.fragment.*;
import com.example.sdu.myflag.menupath.SatelliteMenu;
import com.example.sdu.myflag.menupath.SatelliteMenuItem;
import com.example.sdu.myflag.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static MainActivity mainActivity;
    CustomViewPager viewPager;
    List<Fragment> fragmentList;
    SampleViewPagerAdapter sampleViewPagerAdapter;
    ImageView main_img, community_img, friend_img, myself_img;
    TextView main_tv, community_tv, friend_tv, myself_tv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mainActivity = this;
        initView();
        init();
    }

    public static MainActivity getInstance() {
        return mainActivity;
    }

    public void initView() {
        main_img = (ImageView) findViewById(R.id.tab_main_img);
        community_img = (ImageView) findViewById(R.id.tab_community_img);
        myself_img = (ImageView) findViewById(R.id.tab_myself_img);
        friend_img = (ImageView) findViewById(R.id.tab_friend_img);
        viewPager = (CustomViewPager) findViewById(R.id.main_view_pager);

        main_tv = (TextView) findViewById(R.id.tab_main_tv);
        community_tv = (TextView) findViewById(R.id.tab_community_tv);
        myself_tv = (TextView) findViewById(R.id.tab_myself_tv);
        friend_tv = (TextView) findViewById(R.id.tab_friend_tv);

        SatelliteMenu menu = (SatelliteMenu) findViewById(R.id.menu);
        List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();


        items.add(new SatelliteMenuItem(1, R.drawable.create_flag));
        items.add(new SatelliteMenuItem(2, R.drawable.my_supervise));
        //items.add(new SatelliteMenuItem(4, R.drawable.ic_4));
        //items.add(new SatelliteMenuItem(3, R.drawable.ic_5));
        //items.add(new SatelliteMenuItem(2, R.drawable.ic_6));
        items.add(new SatelliteMenuItem(3, R.drawable.search_friend));

        menu.addItems(items);

        menu.setOnItemClickedListener(new SatelliteMenu.SateliteClickedListener() {
            public void eventOccured(int id) {
                if (id == 1) {
                    startNewActivity(CreateFlagActivity.class);
                } else if (id == 2) {
                    startNewActivity(MySuperViseActivity.class);
                } else {
                    startNewActivity(SearchFriendActivity.class);
                }
            }
        });


    }

    private void init() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new MainFragment());
        fragmentList.add(new CommunityFragment());
        fragmentList.add(new FriendFragment());
        fragmentList.add(new MyFragment());
        sampleViewPagerAdapter = new SampleViewPagerAdapter(this.getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(sampleViewPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(0);
        setSelected(viewPager.getCurrentItem());
    }

    private void setSelected(int cur) {
        switch (cur) {
            case 0:
                setMainTabSelected();
                break;
            case 1:
                setCommunityTabSelected();
                break;
            case 2:
                setFriendTabSelected();
                break;
            case 3:
                setMySelfTabSelected();
                break;
        }
    }

    private void setMainTabSelected() {
        main_img.setImageDrawable(getResources().getDrawable(R.drawable.main_page_selected));
        main_tv.setTextColor(getResources().getColor(R.color.tab_text_color_navigation));

        community_img.setImageDrawable(getResources().getDrawable(R.drawable.community_default));
        community_tv.setTextColor(getResources().getColor(R.color.tab_text_color_gray));

        friend_img.setImageDrawable(getResources().getDrawable(R.drawable.friend_default));
        friend_tv.setTextColor(getResources().getColor(R.color.tab_text_color_gray));

        myself_img.setImageDrawable(getResources().getDrawable(R.drawable.myself_default));
        myself_tv.setTextColor(getResources().getColor(R.color.tab_text_color_gray));
    }

    private void setCommunityTabSelected() {
        main_img.setImageDrawable(getResources().getDrawable(R.drawable.main_page_default));
        main_tv.setTextColor(getResources().getColor(R.color.tab_text_color_gray));

        community_img.setImageDrawable(getResources().getDrawable(R.drawable.community_selected));
        community_tv.setTextColor(getResources().getColor(R.color.tab_text_color_navigation));

        friend_img.setImageDrawable(getResources().getDrawable(R.drawable.friend_default));
        friend_tv.setTextColor(getResources().getColor(R.color.tab_text_color_gray));

        myself_img.setImageDrawable(getResources().getDrawable(R.drawable.myself_default));
        myself_tv.setTextColor(getResources().getColor(R.color.tab_text_color_gray));
    }

    private void setFriendTabSelected() {
        main_img.setImageDrawable(getResources().getDrawable(R.drawable.main_page_default));
        main_tv.setTextColor(getResources().getColor(R.color.tab_text_color_gray));

        community_img.setImageDrawable(getResources().getDrawable(R.drawable.community_default));
        community_tv.setTextColor(getResources().getColor(R.color.tab_text_color_gray));

        friend_img.setImageDrawable(getResources().getDrawable(R.drawable.friend_selected));
        friend_tv.setTextColor(getResources().getColor(R.color.tab_text_color_navigation));

        myself_img.setImageDrawable(getResources().getDrawable(R.drawable.myself_default));
        myself_tv.setTextColor(getResources().getColor(R.color.tab_text_color_gray));
    }

    private void setMySelfTabSelected() {
        main_img.setImageDrawable(getResources().getDrawable(R.drawable.main_page_default));
        main_tv.setTextColor(getResources().getColor(R.color.tab_text_color_gray));

        community_img.setImageDrawable(getResources().getDrawable(R.drawable.community_default));
        community_tv.setTextColor(getResources().getColor(R.color.tab_text_color_gray));

        friend_img.setImageDrawable(getResources().getDrawable(R.drawable.friend_default));
        friend_tv.setTextColor(getResources().getColor(R.color.tab_text_color_gray));

        myself_img.setImageDrawable(getResources().getDrawable(R.drawable.myself_selected));
        myself_tv.setTextColor(getResources().getColor(R.color.tab_text_color_navigation));
    }

    public void onMainTabClick(View view) {
        setMainTabSelected();
        viewPager.setCurrentItem(0);
    }

    public void onCommunityTabClick(View view) {
        setCommunityTabSelected();
        viewPager.setCurrentItem(1);
    }

    public void onFriendTabClick(View view) {
        setFriendTabSelected();
        viewPager.setCurrentItem(2);
    }

    public void onMySelfTabClick(View view) {
        setMySelfTabSelected();
        viewPager.setCurrentItem(3);
    }

    public void createFlag(View view) {
        startNewActivity(CreateFlagActivity.class);
    }

    private long firstTime;

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Snackbar sb = Snackbar.make(viewPager, "再按一次退出", Snackbar.LENGTH_SHORT);
            sb.show();
            firstTime = secondTime;
        } else {
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                String nickname = data.getStringExtra("nickname");
                String information = data.getStringExtra("info");
                String sex = data.getStringExtra("sex");
                int select = data.getIntExtra("photo", 0);
                ((MyFragment) fragmentList.get(2)).setHeadIcon(BaseApplication.HeadIcon[select]);
                ((MyFragment) fragmentList.get(2)).setNickName(nickname);
                ((MyFragment) fragmentList.get(2)).setIntro(information);
                ((MainFragment) fragmentList.get(0)).setIcon(select);
                break;
        }
    }
}
