package com.example.sdu.myflag.util;

/**
 * 网络请求工具类
 */

import android.media.audiofx.AcousticEchoCanceler;
import android.os.Handler;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络请求类
 */
public class NetUtil {

    private static NetUtil mInstance;
    private OkHttpClient mOkHttpClient;
    public static final String TAG = "OkHttpClientManager";
    public static final String loginUrl = "http://119.29.236.181/myflag/user/Login";
    public static final String registerUrl = "http://119.29.236.181/myflag/user/Register";
    public static final String createFlagUrl = "http://119.29.236.181/myflag/flag/CreateFlag";
    public static final String getMyFlagUrl = "http://119.29.236.181/myflag/flag/MyFlag";
    public static final String findUserUrl = "http://119.29.236.181/myflag/user/FindUser";
    public static final String addFriendUrl = "http://119.29.236.181/myflag/friend/AddFriend";
    public static final String editInfoUrl = "http://119.29.236.181/myflag/user/UpdateInformation";
    public static final String getFriendMsgUrl = "http://119.29.236.181/myflag/friend/GetFriendRequest";
    public static final String getSuperViseMsgUrl = "http://119.29.236.181/myflag/flag/GetMySuperviseRequest";
    public static final String getFriendListUrl = "http://119.29.236.181/myflag/friend/MyFriends";
    public static final String confirmFriendUrl = "http://119.29.236.181/myflag/friend/ConfirmFriend";
    public static final String confirmSuperViseUrl = "http://119.29.236.181/myflag/flag/ConfirmSuperviseRequest";
    public static final String getOneFlagUrl = "http://119.29.236.181/myflag/flag/Flag";
    public static final String getFriendFlagUrl = "http://119.29.236.181/myflag/flag/FriendsFlag";
    public static final String mySuperViseUrl = "http://119.29.236.181/myflag/flag/MySuperviseFlag";
    public static final String judgeSuperViseUrl = "http://119.29.236.181/myflag/flag/JudgeFlag";
    public static final String modifyPassWordUrl = "http://119.29.236.181/myflag/user/UpdatePassword";
    public static final String ApplySuperviseUrl = "http://119.29.236.181/myflag/flag/ApplySupervise";
    public static final String getApplySuperviseUrl = "http://119.29.236.181/myflag/flag/GetApplyMyFlag";
    public static final String confirmApplySuperviseUrl = "http://119.29.236.181/myflag/flag/ConfirmApplySupervise";
    public static final String deleteFlagUrl = "http://119.29.236.181/myflag/flag/DeleteFlag";

    private NetUtil() {
        mOkHttpClient = new OkHttpClient();
    }

    public static NetUtil getInstance() {
        if (mInstance == null) {
            synchronized (NetUtil.class) {
                if (mInstance == null) {
                    mInstance = new NetUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 利用Okhttp的同步GET方法，
     * 用于获取json等大小较小的数据
     * 不能再主线程中使用
     *
     * @param url
     * @return string
     */

    public String OkHttpGet(String url) {
        String result = null;

        //创建一个Request
        final Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            result = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    public void post(String url, List<Param> params, final CallBackForResult callBackForResult) throws IOException {
        int size = params.size();
        FormBody.Builder mBuilder = new FormBody.Builder();
        RequestBody body;
        for (int i = 0; i < size; i++) {
            mBuilder.add(params.get(i).key, params.get(i).value);
        }
        body = mBuilder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBackForResult.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBackForResult.onSuccess(response);
            }
        });
    }

    public void post(String url, List<Param> params, String key, String value, final CallBackForResult callBackForResult) throws IOException {
        int size = params.size();
        FormBody.Builder mBuilder = new FormBody.Builder();
        RequestBody body;
        for (int i = 0; i < size; i++) {
            mBuilder.add(params.get(i).key, params.get(i).value);
        }
        body = mBuilder.build();
        Request request = new Request.Builder().url(url).addHeader(key, value).post(body).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBackForResult.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBackForResult.onSuccess(response);
            }
        });
    }

    /**
     * 对外公布的方法
     */
    public static void getResultWithHeader(String url, List<Param> params, String key, String value, CallBackForResult callBackForResult) throws IOException {
        getInstance().post(url, params, key, value, callBackForResult);
    }

    public static void getResult(String url, List<Param> params, CallBackForResult callBackForResult) throws IOException {
        getInstance().post(url, params, callBackForResult);
    }

    public static String getResultGet(String url) {
        return getInstance().OkHttpGet(url);
    }

    public static class Param {
        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }

    public interface CallBackForResult {
        void onFailure(IOException e);

        void onSuccess(Response response);
    }

}