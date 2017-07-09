package com.example.sdu.myflag.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AlbumUtil {

    public final static String photo_path = getSDPath() + "/FindTeam";

    public static final int PHOTO_REQUEST = 1001;
    public static final int CAMERA_REQUEST = 1002;
    public static final int CAMERA_CUT_REQUEST = 1003;

    public static final int TEAMHEADER = 11;
    public static final int PERSONHEADER = 12;

    public static String headImgName;

    // 调用系统相册，选择并调用裁切，并储存路径
    public static void chooseAlbum(Activity activity) {
        Intent innerIntent = new Intent(Intent.ACTION_PICK); // 调用相册
        //innerIntent.putExtra("crop", "true");// 剪辑功能
        innerIntent.putExtra("crop", "false");// 剪辑功能
        innerIntent.putExtra("aspectX", 1);// 放大和缩小功能
        innerIntent.putExtra("aspectY", 1);
        innerIntent.putExtra("outputX", 140);// 输出图片大小
        innerIntent.putExtra("outputY", 140);
        innerIntent.setType("image/*");// 查看类型
        innerIntent.putExtra("output", Uri.fromFile(makeDir()));// 传入目标文件
        innerIntent.putExtra("outputFormat", "JPEG"); // 输入文件格式

        activity.startActivityForResult(innerIntent, PHOTO_REQUEST); // 设返回
        // 码为PHOTO_REQUEST
        // onActivityResult
        // 中的 requestCode 对应
    }


    // 建立头像文件夹功能
    public static File makeDir() {
        // ////////////////////////////////////////////////////////////////
        // 照片的命名，目标文件夹下，以当前时间数字串为名称，即可确保每张照片名称不相同。网上流传的其他Demo这里的照片名称都写死了，
        // 则会发生无论拍照多少张，后一张总会把前一张照片覆盖。细心的同学还可以设置这个字符串，比如加上“ＩＭＧ”字样等；然后就会发现
        // sd卡中myimage这个文件夹下，会保存刚刚调用相机拍出来的照片，照片名称不会重复。

        Date date = new Date(); // 获取当前时间
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE); // 时间字符串格式
        headImgName = format.format(date);
        File tempFile = new File(photo_path + "/" + headImgName + ".jpg");
        File file = new File(photo_path);
        if (!file.exists()) {
            file.mkdir();
        }
        return tempFile;
    }

    /*
    获取sd卡路径
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        return sdDir.getPath();
    }

    // 调用系统相机
    public static void chooseCamera(Activity activity) {
        // photoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用android自带的照相机
        // cameraIntent.putExtra("crop", "true");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(makeDir()));// 传入目标文件
        activity.startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
}
