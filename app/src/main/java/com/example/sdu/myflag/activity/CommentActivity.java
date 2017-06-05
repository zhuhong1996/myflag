package com.example.sdu.myflag.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.adapter.CommentListAdapter;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.bean.SuperViseBriefBean;

import java.util.ArrayList;

/**
 * 评论界面
 */
public class CommentActivity extends BaseActivity {

    private ListView comment_lv;
    private ArrayList<SuperViseBriefBean> SuperViseBriefList;
    private CommentListAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_comment;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        comment_lv = (ListView) findViewById(R.id.comment_lv);

        Intent intent = getIntent();
        SuperViseBriefList = (ArrayList<SuperViseBriefBean>) intent.getExtras().get("briefBean");
        adapter = new CommentListAdapter(this, SuperViseBriefList);
        comment_lv.setAdapter(adapter);
    }

    public void CommentBack(View view) {
        CommentActivity.this.finish();
    }
}
