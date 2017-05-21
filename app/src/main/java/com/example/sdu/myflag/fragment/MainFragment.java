package com.example.sdu.myflag.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.activity.MainActivity;
import com.example.sdu.myflag.activity.SuperViseDetailActivity;
import com.example.sdu.myflag.adapter.FlagListAdapter;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.base.BaseFragment;
import com.example.sdu.myflag.bean.FlagBean;
import com.example.sdu.myflag.bean.SuperViseBriefBean;
import com.example.sdu.myflag.util.BaseTools;
import com.example.sdu.myflag.util.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Response;

/**
 * 主界面
 */
public class MainFragment extends BaseFragment {

    ListView listView;
    FlagListAdapter listAdapter;
    LinearLayout emptyLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    String myId;
    String nickname;
    TextView fragment_main_nickname_tv;
    ImageView main_head_icon;
    int headIndex;
    private PopupWindow popupWindow;
    ArrayList<FlagBean> list;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        if (BaseTools.isNetworkAvailable(this.getActivity())) {
            getFlag();
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void init() {
        listView = (ListView) mRootView.findViewById(R.id.fragment_main_flag_lv);
        emptyLayout = (LinearLayout) mRootView.findViewById(R.id.empty_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.flag_main_swipe);
        fragment_main_nickname_tv = (TextView) mRootView.findViewById(R.id.fragment_main_nickname_tv);
        main_head_icon = (ImageView) mRootView.findViewById(R.id.main_head_icon);

        nickname = BaseApplication.getInstance().getSharedPreferences("User", Context.MODE_PRIVATE).getString("nickname", "用户名");
        fragment_main_nickname_tv.setText(nickname);
        headIndex = BaseApplication.getInstance().getSharedPreferences("User", Context.MODE_PRIVATE).getInt("photo", 0);
        main_head_icon.setImageDrawable(getResources().getDrawable(BaseApplication.HeadIcon[headIndex]));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getFlag();
                Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainFragment.this.getActivity(), SuperViseDetailActivity.class);
                    intent.putExtra("bean", list.get(position));
                    intent.putExtra("code", 3);
                    MainFragment.this.getActivity().startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showPopupWindow(position);
                return false;
            }
        });
    }

    private void getFlag() {
        SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences("User", Context.MODE_PRIVATE);
        myId = sharedPreferences.getString("uid", null);

        if (myId == null) {
            Toast.makeText(this.getActivity(), "获取用户ID失败！", Toast.LENGTH_SHORT).show();
            return;
        }

        Date date = new Date();
        long t = date.getTime() / 1000;
        String time = Long.toString(t);
        time = time.substring(time.length() - 10);

        GetFlagResult getFlagResult = new GetFlagResult();

        List<NetUtil.Param> params = new LinkedList<>();
        params.add(new NetUtil.Param("id", myId));
        params.add(new NetUtil.Param("time", time));

        try {
            NetUtil.getResult(NetUtil.getMyFlagUrl, params, getFlagResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class GetFlagResult implements NetUtil.CallBackForResult {
        @Override
        public void onFailure(final IOException e) {
            MainFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainFragment.this.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onSuccess(Response response) {
            if (response.isSuccessful()) {
                try {
                    String res = response.body().string();
                    JSONObject jo = new JSONObject(res);
                    JSONArray jsonArray = jo.getJSONArray("flags");
                    int size = jsonArray.length();
                    list = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        JSONObject js = jsonArray.getJSONObject(i).getJSONObject("flag");
                        JSONArray SuperviseBrief = jsonArray.getJSONObject(i).getJSONArray("friendsJudge");
                        ArrayList<String> SuperviseBriefList = new ArrayList<>();
                        ArrayList<SuperViseBriefBean> BriefList = new ArrayList<>();
                        JSONArray userBrief = jsonArray.getJSONObject(i).getJSONArray("member");
                        ArrayList<String> userBriefList = new ArrayList<>();
                        for (int j = 0; j < SuperviseBrief.length(); j++) {
                            JSONObject jb = SuperviseBrief.getJSONObject(j);
                            String uid = jb.optString("uid");
                            String nickname = jb.optString("nickname");
                            String evaluate = jb.optString("evaluate");
                            String achieve = jb.optString("achieve");
                            int iconId = jb.optInt("photo");
                            SuperviseBriefList.add(nickname);
                            BriefList.add(new SuperViseBriefBean(uid, nickname, achieve, evaluate, iconId));
                        }
                        for (int j = 0; j < userBrief.length(); j++) {
                            JSONObject jb = userBrief.getJSONObject(j);
                            String nickname = jb.optString("nickname");
                            userBriefList.add(nickname);
                        }
                        String content = js.optString("content");
                        String award = js.optString("award");
                        String achieve = js.optString("achieve");
                        String fid = js.optString("fid");
                        String id = js.optString("id");
                        String startTime = stampToDate(js.optString("startTime"));
                        long end = js.optLong("endTime");
                        String endTime = stampToDate(end + "");
                        String createTime = js.optString("createTime");
                        String teamOrNot = js.optString("isTeam").equals("true") ? "[团队]" : "[个人]";
                        String isFinish = "false";
                        if (BaseTools.isFlagOverdue(end))
                            isFinish = "true";
                        FlagBean flagBean = new FlagBean(fid, nickname, content, startTime, endTime, SuperviseBriefList, userBriefList, award, teamOrNot, isFinish, BriefList, achieve);
                        flagBean.iconId = headIndex;
                        list.add(flagBean);
                    }
                    MainFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listAdapter = new FlagListAdapter(MainFragment.this.getContext(), list);
                            listView.setAdapter(listAdapter);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /*
        * 将时间戳转换为时间
        */
        public String stampToDate(String s) {
            String res;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            long lt = Long.valueOf(s) * 1000;
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
            return res;
        }
    }

    public void setIcon(int id){
        main_head_icon.setImageDrawable(getResources().getDrawable(BaseApplication.HeadIcon[id]));
    }

    private void showPopupWindow(final int position) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(MainFragment.this.getActivity()).inflate(
                R.layout.delete_pop_window, null);
        // 设置按钮的点击事件
        Button delete = (Button) contentView.findViewById(R.id.pop_delete_btn);

        popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setWidth(400);
        popupWindow.setHeight(150);
        popupWindow.setAnimationStyle(R.style.popwindow_anim);
        popupWindow.setTouchable(true);
        backgroundAlpha(0.5f);
        popupWindow.setOnDismissListener(new popOnDismissListener());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                ArrayList<NetUtil.Param> params = new ArrayList<>();
                params.add(new NetUtil.Param("id", list.get(position).getFid()));
                try {
                    NetUtil.getResult(NetUtil.deleteFlagUrl, params, new deleteFlagCallBack(position));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.color.textcolor_gray));

        // 设置好参数之后再show
        popupWindow.showAtLocation(MainFragment.this.getView(), Gravity.CENTER, 0, 0);
    }

    class popOnDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    /*
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = MainActivity.getInstance().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        MainFragment.this.getActivity().getWindow().setAttributes(lp);
    }

    class deleteFlagCallBack implements NetUtil.CallBackForResult {

        int position;

        public deleteFlagCallBack(int position){
            this.position = position;
        }

        @Override
        public void onFailure(final IOException e) {
            MainFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainFragment.this.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess(Response response) {
            if (response.isSuccessful()) {
                try {
                    final String res = response.body().string();
                    MainFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (res.equals("1")) {
                                Toast.makeText(MainFragment.this.getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
                                list.remove(position);
                                listAdapter.notifyDataSetChanged();
                            }
                            else if(res.equals("0")){
                                Toast.makeText(MainFragment.this.getActivity(), "删除失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


