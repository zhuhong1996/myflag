package com.example.sdu.myflag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.activity.MainActivity;
import com.example.sdu.myflag.activity.MySuperViseActivity;
import com.example.sdu.myflag.activity.SuperViseDetailActivity;
import com.example.sdu.myflag.bean.FlagBean;
import com.example.sdu.myflag.fragment.MainFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的flag的item
 */
public class FlagListAdapter extends BaseAdapter {

    private List<FlagBean> mList;
    private Context context;

    public FlagListAdapter(Context context, List<FlagBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_flag_mine, null);
            viewHolder.flag = (TextView) convertView.findViewById(R.id.flag_tv);
            viewHolder.reward = (TextView) convertView.findViewById(R.id.reward_tv);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time_tv);
            viewHolder.watch = (TextView) convertView.findViewById(R.id.watch_tv);
            viewHolder.teamOrNot = (TextView) convertView.findViewById(R.id.team_ornot_tv);
            viewHolder.flag_finish_img = (ImageView) convertView.findViewById(R.id.flag_finish_img);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.flag.setText(mList.get(position).getFlag());
        ArrayList<String> watchList = mList.get(position).getWatch_name();
        String watchStr = "";
        for (int i = 0; i < watchList.size(); i++) {
            watchStr += watchList.get(i) + " ";
        }
        viewHolder.watch.setText(watchStr);
        viewHolder.time.setText(mList.get(position).getTime_begin() + "  -  " + mList.get(position).getTime_end());
        viewHolder.reward.setText(mList.get(position).getReward());
        viewHolder.teamOrNot.setText(mList.get(position).getTeamOrNot());
        if (mList.get(position).getIsFinish().equals("true")) {
            viewHolder.flag_finish_img.setVisibility(View.VISIBLE);
            if (mList.get(position).getAchieve().equals("2"))
                viewHolder.flag_finish_img.setImageResource(R.drawable.flag_finish_img);
            else
                viewHolder.flag_finish_img.setImageResource(R.drawable.flag_end_img);
        } else {
            viewHolder.flag_finish_img.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        public TextView flag;
        public TextView time;
        public TextView watch;
        public TextView reward;
        public TextView teamOrNot;
        public ImageView flag_finish_img;
    }
}
