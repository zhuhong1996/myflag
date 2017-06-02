package com.example.sdu.myflag.adapter;

import android.content.Context;
import android.graphics.LightingColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.bean.SuperViseBriefBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/23.
 */
public class CommentListAdapter extends BaseAdapter {

    private ArrayList<SuperViseBriefBean> SuperViseBriefList;
    private Context context;

    public CommentListAdapter(Context context, ArrayList<SuperViseBriefBean> SuperViseBriefList){
        this.context = context;
        this.SuperViseBriefList = SuperViseBriefList;
    }

    @Override
    public int getCount() {
        return SuperViseBriefList.size();
    }

    @Override
    public Object getItem(int position) {
        return SuperViseBriefList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_comment, null);
        TextView nicknameTv = (TextView) convertView.findViewById(R.id.comment_nickname);
        TextView commentText = (TextView) convertView.findViewById(R.id.comment_text);
        ImageView comment_icon = (ImageView) convertView.findViewById(R.id.comment_icon);

        comment_icon.setImageDrawable(context.getResources().getDrawable(BaseApplication.HeadIcon[SuperViseBriefList.get(position).iconId]));
        nicknameTv.setText(SuperViseBriefList.get(position).nickname);
        commentText.setText(SuperViseBriefList.get(position).evaluate);
        return convertView;
    }
}
