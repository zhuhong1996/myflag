package com.example.sdu.myflag.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.base.BaseApplication;
import com.example.sdu.myflag.util.AlbumUtil;
import com.example.sdu.myflag.util.BaseTools;
import com.example.sdu.myflag.util.NetUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 我的flag打卡界面
 */
public class MyFlagClockActivity extends BaseActivity {

    TextView picHint;
    EditText inputText;
    ImageView imageView;

    private String path, fid, uid, photo;
    boolean isUpLoad = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_flag_clock;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        requestReadExternalPermission();

        picHint = (TextView) findViewById(R.id.pic_hint);
        inputText = (EditText) findViewById(R.id.clock_comment);
        imageView = (ImageView) findViewById(R.id.picture);

        path = "";
        fid = getIntent().getExtras().getString("fid");
        SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences("User", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", null);
    }

    /*
    点击更换图片按钮
     */
    public void changePic(View view) {
        final String[] item = {"相册", "相机"}; // 第二级条目列表
        new AlertDialog.Builder(MyFlagClockActivity.this)// 建立对话框
                .setTitle("请选择方式")// 标题
                .setItems(item, new DialogInterface.OnClickListener() {// 以下为监听
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (which == 0) {
                            // 相册
                            // AlbumUtil.chooseAlbum(MyFlagClockActivity.this);
                            Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                            intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                            startActivityForResult(intent, 1);
                        }
                        if (which == 1) {
                            // 相机
                            AlbumUtil.chooseCamera(MyFlagClockActivity.this);
                        }
                    }
                }).show();
    }

    /*
    点击发送按钮
     */
    public void sendClock(View view) {
        if(inputText.getText().toString().length() == 0){
            Toast.makeText(MyFlagClockActivity.this, "打卡感想未输入", Toast.LENGTH_LONG).show();
            return;
        }
        if (!path.equals("")) {
            dialog.show();
            isUpLoad = false;
            final AsyncHttpClient client = new AsyncHttpClient();
            File file = new File(path);
            if (file.exists() && file.length() > 0) {
                final RequestParams params = new RequestParams();
                try {
                    params.put("picture", file);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                client.post(NetUtil.upLoadImage, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                       // Toast.makeText(MyFlagClockActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        photo = new String(responseBody);
                        photo = photo.substring(0, photo.length() - 1);
                       // Toast.makeText(MyFlagClockActivity.this, photo, Toast.LENGTH_SHORT).show();
                        RequestParams params1 = new RequestParams();
                        try {
                            params1.put("uid", uid);
                            params1.put("fid", fid);
                            params1.put("content", inputText.getText().toString());
                            params1.put("photo", photo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        client.post(NetUtil.createClockUrl, params1, new AsyncHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                                Toast.makeText(MyFlagClockActivity.this, "打卡成功", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                MyFlagClockActivity.this.finish();
                            }

                            @Override
                            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                                Toast.makeText(MyFlagClockActivity.this, "打卡失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                        Log.v("hahahaha", statusCode+"");
                        Toast.makeText(MyFlagClockActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(MyFlagClockActivity.this, "未选择图片", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*        switch (requestCode) {
            case AlbumUtil.PHOTO_REQUEST:// 相册返回
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        bmpName = AlbumUtil.setHeaderImgAlbum();
                        relativeLayout.setBackground(new BitmapDrawable(head.getBmp()));
                        break;
                }
                break;

            case AlbumUtil.CAMERA_REQUEST:// 照相返回

                switch (resultCode) {
                    case Activity.RESULT_OK:// 照相完成点击确定
                        AlbumUtil.getHeaderImgCamera();
                        break;

                    case Activity.RESULT_CANCELED:// 取消
                        break;
                }
                break;

            case AlbumUtil.CAMERA_CUT_REQUEST:

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        AlbumUtil.setHeaderImgCamera(data);
                        relativeLayout.setBackground(new BitmapDrawable(head.getBmp()));
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }*/
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            path = BaseTools.getImageAbsolutePath(MyFlagClockActivity.this, uri);
            Log.v("hahahaha", path);
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                /* 将Bitmap设定到ImageView */
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            }
        }
    }

    @SuppressLint("NewApi")
    private void requestReadExternalPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("1221", "READ permission IS NOT granted...");

            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Log.d("1221", "11111111111111");
            } else {
                // 0 是自己定义的请求coude
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                Log.d("1221", "222222222222");
            }
        } else {
            Log.d("1221", "READ permission is granted...");
        }
    }
}
