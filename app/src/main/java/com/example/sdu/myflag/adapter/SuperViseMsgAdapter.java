package com.example.sdu.myflag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.activity.FriendMsgDetailActivity;
import com.example.sdu.myflag.activity.SuperViseMsgDetailActivity;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.bean.SuperviseBean;

import java.util.ArrayList;

/**
 *  监督消息列表
 */
public class SuperViseMsgAdapter extends BaseAdapter {

    private ArrayList<SuperviseBean> list;
    private Context context;

    public SuperViseMsgAdapter(Context context, ArrayList<SuperviseBean> list){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_mymessage, null);
        final ImageView unread = (ImageView) convertView.findViewById(R.id.unread_red_img);
        ImageView head_icon_msg = (ImageView) convertView.findViewById(R.id.head_icon_msg);
        TextView msg = (TextView) convertView.findViewById(R.id.message_tv);
        final TextView readTv = (TextView) convertView.findViewById(R.id.read_tv);

        head_icon_msg.setImageDrawable(context.getResources().getDrawable(BaseApplication.HeadIcon[list.get(position).iconId]));
        if (list.get(position).agree.equals("0")) {
            unread.setVisibility(View.VISIBLE);
            readTv.setText("[未读]");
        } else {
            unread.setVisibility(View.GONE);
            readTv.setText("[已读]");
        }

        msg.setText(list.get(position).nickname + " 邀请你作为TA的FLAG的监督人");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readTv.setText("[已读]");
                unread.setVisibility(View.GONE);
                Intent intent = new Intent(context, SuperViseMsgDetailActivity.class);
                intent.putExtra("bean", list.get(position));
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
