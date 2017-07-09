package com.example.sdu.myflag.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.bean.ClockBean;
import com.example.sdu.myflag.bean.SuperViseBriefBean;
import com.example.sdu.myflag.util.BaseTools;
import com.example.sdu.myflag.util.NetUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * 打卡适配器
 */
public class ClockListAdapter extends BaseAdapter {

    private ArrayList<ClockBean> clockList;
    private Context context;
    Bitmap b;

    public ClockListAdapter(Context context, ArrayList<ClockBean> clockList) {
        this.context = context;
        this.clockList = clockList;
    }

    @Override
    public int getCount() {
        return clockList.size();
    }

    @Override
    public Object getItem(int position) {
        return clockList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;
        if(convertView == null){
            myViewHolder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_clock_list, null);
            myViewHolder.nicknameTv = (TextView) convertView.findViewById(R.id.clock_nickname);
            myViewHolder.contentTv = (TextView) convertView.findViewById(R.id.clock_content);
            myViewHolder.timeTv = (TextView) convertView.findViewById(R.id.clock_time);
            myViewHolder.icon = (ImageView) convertView.findViewById(R.id.clock_icon);
            myViewHolder.picture = (ImageView) convertView.findViewById(R.id.clock_picture);

            convertView.setTag(myViewHolder);
        }
        else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        myViewHolder.icon.setImageDrawable(context.getResources().getDrawable(BaseApplication.HeadIcon[clockList.get(position).iconId]));
        BaseTools.loadBitmap(NetUtil.getImageUrl + clockList.get(position).photo, myViewHolder.picture);
        myViewHolder.nicknameTv.setText(clockList.get(position).nickname);
        myViewHolder.contentTv.setText(clockList.get(position).content);
        myViewHolder.timeTv.setText(clockList.get(position).time);
        return convertView;
    }

    class MyViewHolder {
        public TextView nicknameTv;
        public TextView contentTv;
        public TextView timeTv;
        public ImageView icon;
        public ImageView picture;
    }
}
