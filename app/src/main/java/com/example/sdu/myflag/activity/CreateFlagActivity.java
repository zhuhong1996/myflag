package com.example.sdu.myflag.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.bean.FriendBean;
import com.example.sdu.myflag.util.NetUtil;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import carbon.widget.RadioButton;
import carbon.widget.RadioGroup;
import okhttp3.Response;

/**
 * 新建FLAG界面
 */
public class CreateFlagActivity extends BaseActivity {
    EditText contentEditText, limitEditText, awardEditText;
    Button commitButton;
    String content = "", form = "", beginTime = "", endTime = "", invite = "", limit = "", award = "", id = "";
    String isTeam;
    RadioButton personalRb, teamRb;
    RadioGroup radioGroup;
    TextView beginTv, endTv, formEditText, inviteEditText;
    int beginYear, beginMonth, beginDay, endYear, endMonth, endDay;
    ArrayList<FriendBean> formList, inviteList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_flag;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        init();
        setListener();
    }

    private void init() {
        contentEditText = (EditText) findViewById(R.id.newFlagContentEditText);
        formEditText = (TextView) findViewById(R.id.newFlagFormEditText);
        beginTv = (TextView) findViewById(R.id.begin_tv);
        endTv = (TextView) findViewById(R.id.end_tv);
        inviteEditText = (TextView) findViewById(R.id.newFlagInviteEditText);
        limitEditText = (EditText) findViewById(R.id.newFlagLimitEditText);
        awardEditText = (EditText) findViewById(R.id.newFlagAwardEditText);
        commitButton = (Button) findViewById(R.id.newFlagCommitButton);

        personalRb = (RadioButton) findViewById(R.id.personal_rb);
        teamRb = (RadioButton) findViewById(R.id.team_rb);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        isTeam = "false";
    }

    private void setListener() {

        beginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View calendar_view = View.inflate(CreateFlagActivity.this, R.layout.date_picker, null);
                final DatePicker datePicker = (DatePicker) calendar_view.findViewById(R.id.date_picker);
                int y, m, d;
                if (beginTv.getText().toString().isEmpty()) {
                    Calendar calendar = Calendar.getInstance();
                    y = calendar.get(Calendar.YEAR);
                    m = calendar.get(Calendar.MONTH);
                    d = calendar.get(Calendar.DAY_OF_MONTH);
                } else {
                    y = beginYear;
                    m = beginMonth;
                    d = beginDay;
                }
                datePicker.init(y, m, d, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(CreateFlagActivity.this);
                builder.setView(calendar_view);
                builder.setTitle("选择时间");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        beginYear = datePicker.getYear();
                        beginMonth = datePicker.getMonth();
                        beginDay = datePicker.getDayOfMonth();
                        beginTv.setText(beginYear + "年" + (beginMonth + 1) + "月" + beginDay + "日");
                    }
                }).show();
            }
        });

        endTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View calendar_view = View.inflate(CreateFlagActivity.this, R.layout.date_picker, null);
                final DatePicker datePicker = (DatePicker) calendar_view.findViewById(R.id.date_picker);
                int y, m, d;
                if (beginTv.getText().toString().isEmpty()) {
                    Calendar calendar = Calendar.getInstance();
                    y = calendar.get(Calendar.YEAR);
                    m = calendar.get(Calendar.MONTH);
                    d = calendar.get(Calendar.DAY_OF_MONTH);
                } else {
                    y = beginYear;
                    m = beginMonth;
                    d = beginDay;
                }
                datePicker.init(y, m, d, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(CreateFlagActivity.this);
                builder.setView(calendar_view);
                builder.setTitle("选择时间");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        endYear = datePicker.getYear();
                        endMonth = datePicker.getMonth();
                        endDay = datePicker.getDayOfMonth();
                        endTv.setText(endYear + "年" + (endMonth + 1) + "月" + endDay + "日");
                    }
                }).show();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == teamRb.getId()) {
                    isTeam = "true";
                    formEditText.setVisibility(View.VISIBLE);
                } else {
                    isTeam = "false";
                    formEditText.setVisibility(View.GONE);
                }
            }
        });

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getText()) {
                    List<NetUtil.Param> params = new LinkedList<NetUtil.Param>();
                    params.add(new NetUtil.Param("id", id));
                    params.add(new NetUtil.Param("content", content));
                    params.add(new NetUtil.Param("award", award));
                    params.add(new NetUtil.Param("isTeam", isTeam));
                    params.add(new NetUtil.Param("startTime", beginTime));
                    params.add(new NetUtil.Param("endTime", endTime));
                    params.add(new NetUtil.Param("supervise", invite));
                    params.add(new NetUtil.Param("member", form));
                    params.add(new NetUtil.Param("maxSupervise", limit));

                    CreateFlagResult createFlagResult = new CreateFlagResult();
                    try {
                        NetUtil.getResult(NetUtil.createFlagUrl, params, createFlagResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        formEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateFlagActivity.this, FriendActivity.class);
                intent.putExtra("code", 1);
                startActivityForResult(intent, 1);
            }
        });

        inviteEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateFlagActivity.this, FriendActivity.class);
                intent.putExtra("code", 1);
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (data == null)
                    break;
                formList = (ArrayList<FriendBean>) data.getExtras().get("selected");
                String form = "";
                for (int i = 0; i < formList.size(); i++) {
                    form += "@";
                    form += formList.get(i).getName();
                }
                formEditText.setText(form);
                break;

            case 2:
                if (data == null)
                    break;
                inviteList = (ArrayList<FriendBean>) data.getExtras().get("selected");
                if (inviteList == null)
                    break;
                String invite = "";
                for (int i = 0; i < inviteList.size(); i++) {
                    invite += "@";
                    invite += inviteList.get(i).getName();
                }
                inviteEditText.setText(invite);
                break;
        }
    }

    private boolean getText() {
        content = contentEditText.getText().toString();
/*        form = formEditText.getText().toString();
        invite = inviteEditText.getText().toString();*/
        form = "";
        invite = "";
        if (formList != null) {
            for (int i = 0; i < formList.size(); i++) {
                if (i > 0)
                    form += "#";
                form += formList.get(i).getId();
            }
        }
        if (inviteList != null) {
            for (int i = 0; i < inviteList.size(); i++) {
                if (i > 0)
                    invite += "#";
                invite += inviteList.get(i).getId();
            }
        }
        beginTime = beginTv.getText().toString();
        endTime = endTv.getText().toString();
        limit = limitEditText.getText().toString();
        award = awardEditText.getText().toString();

        SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences("User", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("uid", null);

        if (id == null) {
            Toast.makeText(this, "获取用户ID失败！", Toast.LENGTH_SHORT).show();
            return false;
        } else if (content.isEmpty() || beginTime.isEmpty() || endTime.isEmpty() || limit.isEmpty() || award.isEmpty()) {
            Toast.makeText(this, "请将信息填写完整！", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!convertTime()) {
            Toast.makeText(this, "时间输入不合法！", Toast.LENGTH_SHORT).show();
            return false;
        }
/*        form.replaceAll("@", "#");
        invite.replaceAll("@", "#");*/

        return true;
    }

    private boolean convertTime() {
        String t1 = beginTime;
        String t2 = endTime;
        t1 = t1.replaceAll("年", "-");
        t1 = t1.replaceAll("月", "-");
        t1 = t1.replaceAll("日", "");

        t2 = t2.replaceAll("年", "-");
        t2 = t2.replaceAll("月", "-");
        t2 = t2.replaceAll("日", "");

        if (t1.charAt(6) == '-') {
            t1 = t1.substring(0, 5) + "0" + t1.substring(5);
        }

        if (t1.length() < 10) {
            t1 = t1.substring(0, 8) + "0" + t1.substring(8);
        }

        if (t2.charAt(6) == '-') {
            t2 = t2.substring(0, 5) + "0" + t2.substring(5);
        }

        if (t2.length() < 10) {
            t2 = t2.substring(0, 8) + "0" + t2.substring(8);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = sdf.parse(t1);
            Date date2 = sdf.parse(t2);
            long a = date1.getTime() / 1000;
            long b = date2.getTime() / 1000;
            t1 = Long.toString(a);
            t2 = Long.toString(b);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        beginTime = t1;
        endTime = t2;
/*        Log.v("jljfdls", beginTime);
        Log.v("jljfdls", endTime);*/
        return true;
    }

    public void create_backTo(View view) {
        this.finish();
    }

    private class CreateFlagResult implements NetUtil.CallBackForResult {
        @Override
        public void onFailure(final IOException e) {
            CreateFlagActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CreateFlagActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onSuccess(Response response) {
            if (response.isSuccessful()) {
                try {
                    String result = response.body().string();
                    if (result.equals("0") || result.equals("2")) {
                        CreateFlagActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CreateFlagActivity.this, "创建失败", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        CreateFlagActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CreateFlagActivity.this, "创建成功", Toast.LENGTH_LONG).show();
                                CreateFlagActivity.this.finish();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
