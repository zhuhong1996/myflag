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
import com.example.sdu.myflag.util.BaseTools;
import com.john.waveview.WaveView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        convertView = LayoutInflater.from(context).inflate(R.layout.item_flag_mine, null);

        TextView flag = (TextView) convertView.findViewById(R.id.flag_tv);
        TextView reward = (TextView) convertView.findViewById(R.id.reward_tv);
        TextView time = (TextView) convertView.findViewById(R.id.time_tv);
        TextView watch = (TextView) convertView.findViewById(R.id.watch_tv);
        TextView teamOrNot = (TextView) convertView.findViewById(R.id.team_ornot_tv);
        WaveView waveView = (WaveView) convertView.findViewById(R.id.wave_view);

       // float betweenStartToEnd = BaseTools.daysBetween(mList.get(position).getTime_begin(), mList.get(position).getTime_end());
       // float betweenStartToCur = BaseTools.daysBetween(mList.get(position).getTime_begin(), new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
       // int value = (int) (100 * (betweenStartToCur / betweenStartToEnd));
        //waveView.setProgress(value > 100 ? 100 : value); //这里的参数放置一个1-100的参数   参数=100*（currentTime-startTime）/(endTime-startTime)   修改波浪的波动程度去xml文件里面修改

        //waveView.

        flag.setText(mList.get(position).getFlag());
        ArrayList<String> watchList = mList.get(position).getWatch_name();
        String watchStr = "";
        for (int i = 0; i < watchList.size(); i++) {
            watchStr += watchList.get(i) + " ";
        }
        watch.setText(watchStr);
        time.setText(mList.get(position).getTime_begin() + "  -  " + mList.get(position).getTime_end());
        reward.setText(mList.get(position).getReward());
        teamOrNot.setText(mList.get(position).getTeamOrNot());
        if (mList.get(position).getIsFinish().equals("true")) {
            if (mList.get(position).getAchieve().equals("2")) {
                waveView.setBackgroundColor(context.getResources().getColor(R.color.carbon_green_100));
            } else {
                waveView.setBackgroundColor(context.getResources().getColor(R.color.carbon_red_100));
            }
        }
        waveView.setProgress(100);

        return convertView;
    }
}
