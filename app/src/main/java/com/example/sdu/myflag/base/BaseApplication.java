package com.example.sdu.myflag.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.sdu.myflag.R;
import com.example.sdu.myflag.util.BitmapCache;


public class BaseApplication extends Application {

    private static RequestQueue queues;

    public static BitmapCache bitmapCache;
    public static ImageLoader imageLoader;

    private static BaseApplication instance;
    public static int HeadIcon[] = {
                    R.drawable.head_icon_default,
                    R.drawable.one,
                    R.drawable.two,
                    R.drawable.three,
                    R.drawable.four,
                    R.drawable.five,
                    R.drawable.six,
                    R.drawable.seven,
                    R.drawable.eight,
                    R.drawable.nine
            };

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        queues = Volley.newRequestQueue(getApplicationContext());
        bitmapCache = new BitmapCache();
        imageLoader = new ImageLoader(getQueues(), bitmapCache);
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    public static RequestQueue getQueues() {
        return queues;
    }
}
