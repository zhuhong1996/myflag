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
import com.example.sdu.myflag.activity.MySuperViseActivity;
import com.example.sdu.myflag.activity.SuperViseDetailActivity;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.bean.FlagBean;

import java.util.ArrayList;


public class FlagFriendListAdapter extends BaseAdapter {

    private ArrayList<FlagBean> mList;
    private Context context;

    public FlagFriendListAdapter(Context context, ArrayList<FlagBean> list) {
        this.mList = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (mList == null)
            return null;
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_flag_friend, null);
            viewHolder.nickname = (TextView) convertView.findViewById(R.id.friend_flag_nick_name);
            viewHolder.flag = (TextView) convertView.findViewById(R.id.flag_tv);
            viewHolder.reward = (TextView) convertView.findViewById(R.id.reward_tv);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time_tv);
            viewHolder.watch = (TextView) convertView.findViewById(R.id.watch_tv);
            viewHolder.teamOrNot = (TextView) convertView.findViewById(R.id.team_ornot_tv);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.head_icon);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.flag.setText(mList.get(position).getFlag());
        ArrayList<String> watchList = mList.get(position).getWatch_name();
        String watchStr = "";
        if (watchList != null) {
            for (int i = 0; i < watchList.size(); i++) {
                watchStr += watchList.get(i) + " ";
            }
        }
        viewHolder.watch.setText(watchStr);
        viewHolder.time.setText(mList.get(position).getTime_begin() + "  -  " + mList.get(position).getTime_end());
        viewHolder.reward.setText(mList.get(position).getReward());
        viewHolder.teamOrNot.setText(mList.get(position).getTeamOrNot());
        viewHolder.nickname.setText(mList.get(position).getUser_name());
        viewHolder.icon.setImageDrawable(context.getResources().getDrawable(BaseApplication.HeadIcon[mList.get(position).getIconId()]));

/*        if (mList.get(position).getIsFinish().equals("true")) {
            viewHolder.flag_finish_img.setVisibility(View.VISIBLE);
            if (mList.get(position).getAchieve().equals("2"))
                viewHolder.flag_finish_img.setImageResource(R.drawable.flag_finish_img);
            else
                viewHolder.flag_finish_img.setImageResource(R.drawable.flag_end_img);
        } else {
            viewHolder.flag_finish_img.setVisibility(View.GONE);
        }*/

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SuperViseDetailActivity.class);
                intent.putExtra("bean", mList.get(position));
                if (context instanceof MySuperViseActivity)
                    intent.putExtra("code", 1);
                else
                    intent.putExtra("code", 2);

                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        public TextView nickname;
        public TextView flag;
        public TextView time;
        public TextView watch;
        public TextView reward;
        public TextView teamOrNot;
        public ImageView icon;
    }
}
