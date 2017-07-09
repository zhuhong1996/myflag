package com.example.sdu.myflag.util;

/**
 * 基本的工具类
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.example.sdu.myflag.R;
import com.example.sdu.myflag.base.BaseApplication;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.Deflater;


public class BaseTools {

    public static final long ONE_DAY_TIME = 24 * 60 * 60;

    /**
     * 获取屏幕分辨率
     *
     * @param context
     * @return
     */
    public static int getWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    public static int getHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeigt = dm.heightPixels;
        return screenHeigt;
    }

    /**
     * 获取年月日时间
     */
    public static String getNYYMMDD() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String yymmddString = sdf.format(new Date());
        return yymmddString;
    }


    /**
     * 获取年月日时间
     */
    public static String getNYYMMDD(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        long now = date.getTime();
        long next = now + day * ONE_DAY_TIME * 1000;
        String yymmddString = sdf.format(next);
        return yymmddString;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {

        if (null != context && context.getResources() != null && context.getResources().getDisplayMetrics() != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

        return (int) dpValue;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {

        if (null != context && context.getResources() != null && context.getResources().getDisplayMetrics() != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }

        return (int) pxValue;
    }

    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(final Context context) {
        boolean netStatus = false;

        ConnectivityManager connectManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            netStatus = networkInfo.isAvailable();
        }
        return netStatus;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 隐藏键盘
     */
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * 绘制导航标签
     *
     * @param context
     * @param total           总的位置信息
     * @param currentPosition 当前位置
     * @param basicImageId    基本的图形信息
     * @param currentImageId  当前位置所要显示的图形信息
     * @return
     */
    public static Bitmap drawNavigator(Context context, int total,
                                       int currentPosition, int basicImageId, int currentImageId) {
        return drawNavigator(context, total,
                currentPosition, basicImageId, currentImageId, 6);
    }

    /**
     * 绘制导航标签
     *
     * @param context
     * @param total           总的位置信息
     * @param currentPosition 当前位置
     * @param basicImageId    基本的图形信息
     * @param currentImageId  当前位置所要显示的图形信息
     * @param marginSpace     导航条圆点中间的间距
     * @return
     */
    public static Bitmap drawNavigator(Context context, int total, int currentPosition,
                                       int basicImageId, int currentImageId, int marginSpace) {
        if (total <= 0) {
            total = 1;
        }
        int current = currentPosition;
        Drawable drawable = context.getResources().getDrawable(currentImageId);
        int imageWidth = drawable.getIntrinsicWidth();
        int imageHeight = drawable.getIntrinsicHeight();
        //生成最终要绘制的bitmap，长度是总的标签数乘以图片本身的宽度
        Bitmap navigatorBitmap = Bitmap.createBitmap((imageWidth + 6) * total, imageHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(navigatorBitmap);
        //往画板上填充基本的图像内容
        Bitmap basic = BitmapFactory.decodeResource(context.getResources(), basicImageId);
        for (int i = 0; i < total; i++) {
            canvas.drawBitmap(basic, (marginSpace + imageWidth) * i, 0, null);
        }
        drawable.setBounds((imageWidth + marginSpace) * (current - 1), 0, (imageWidth + marginSpace) * current - marginSpace, imageHeight);
        drawable.draw(canvas);
        return navigatorBitmap;
    }

    /**
     * 打开图片剪裁页面
     *
     * @param context
     * @param imageUri              图片剪裁的原图
     * @param defaultSizeWithSource 剪裁区域的默认大小是否与原图保持一致  true 保持一致
     * @param requestCode
     * @param cropImageFileName     图片剪裁成功后的缓存文件名称，如果不为空，则获取剪裁后的文件方式为：
     *                              <pre>String fileName = extras.getString("file-data");
     *                                                                                        File file = this.getFileStreamPath(fileName);
     *                                                                                        Uri uri = Uri.fromFile(file);</pre>
     *                              如果为空，则获取剪裁后的图片方式为：
     *                              <pre>Bundle extras = data.getExtras();
     *                                                                                        if (extras != null) {
     *                                                                                        Bitmap photo = extras.getParcelable("data");
     *                                                                                        }</pre>
     *                              使用的时候，建议按照文件路径（也就是传入有效的cropImageFileName）值使用，原因在于：intent在传递值的时候，对于bitmap这样的值有40kb的大小限制
     */
    public static void startCropImageActivityForResult(Activity context, Uri imageUri, boolean defaultSizeWithSource,
                                                       int requestCode, String cropImageFileName) {
        startCropImageActivityForResult(context, imageUri, defaultSizeWithSource, requestCode, cropImageFileName, 100, 100);
    }

    public static void startCropImageActivityForResult(Activity context, Uri imageUri, boolean defaultSizeWithSource,
                                                       int requestCode, String cropImageFileName, int aspectX, int aspectY) {
        try {
            Intent intent = new Intent();
            //this will open all images
            intent.setDataAndType(imageUri, "image/*");
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
            intent.putExtra("crop", "true");
            // true to return a Bitmap, false to directly save the cropped iamge
            intent.putExtra("return-data", true);
            if (defaultSizeWithSource) {
                intent.putExtra("defaultCropRectangleSize", "same-with-same-image");
            }
            if (cropImageFileName != null) {
                intent.putExtra("return-data-file-name", cropImageFileName);
            }
            context.startActivityForResult(intent, requestCode);
        } catch (Throwable tr) {

        }
    }

    public static void showToast(Context context, int resId) {
        String content = context.getString(resId);
        showToast(context, content);
    }

    public static void showToast(Context context, String content) {
        if (!TextUtils.isEmpty(content)) {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        }
    }

    public static String getImsi(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();
        return imsi;
    }

    public static String getImei(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTelephonyMgr.getDeviceId();
        return imei;
    }

    /**
     * 图片翻转
     *
     * @param bitmap
     * @param orientation
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        try {
            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    return bitmap;
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    matrix.setScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.setRotate(-90);
                    break;
                default:
                    return bitmap;
            }
            try {
                Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return bmRotated;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }


/*    public static void showTips(Context context,String tipsString){
        showTips(context,tipsString,0);
    }
    public static void showTips(Context context,String tipsString,int duration) {
        final Toast t = new Toast(context);
        t.setGravity(Gravity.TOP, 0, 0);
        if (duration == 0) {
            t.setDuration(Toast.LENGTH_SHORT);
        } else {
            t.setDuration(duration);
        }
        final LayoutInflater mInflater = LayoutInflater.from(context);
        final View v = mInflater.inflate(R.layout.wukong_tips_layout, null);
        final TextView text = (TextView) v.findViewById(R.id.tips_text);
        text.setText(tipsString);
        v.setMinimumWidth(9999);
        v.setMinimumHeight(dp2px(context,48));
        t.setView(v);
        t.show();
    }*/

    public static boolean isFlagOverdue(long time) {
        Date date = new Date();
        if (time > (date.getTime() / 1000))
            return false;
        else
            return true;
    }

    /*
* 将时间戳转换为时间
*/
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = Long.valueOf(s) * 1000;
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getParamTime() {
        Date date = new Date();
        long t = date.getTime() / 1000;
        String time = Long.toString(t);
        return time.substring(time.length() - 10);
    }

    /*
    volley加载图片
     */
    public static void loadBitmap(String imgPath, final ImageView imageView) {
        ImageLoader imageLoader = new ImageLoader(BaseApplication.getQueues(), BaseApplication.bitmapCache);
        ImageLoader.ImageListener imageListener = imageLoader.getImageListener(imageView,
                R.drawable.picture_default, R.drawable.picture_default);
        imageLoader.get(imgPath, imageListener);
    }

    /*
    显示加载条
     */
    public static Dialog showLoadingDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.progress_dialog);
        dialog.setContentView(R.layout.item_loading_image);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.loading_image);
        Glide.with(context).load(R.drawable.loading_image).into(imageView);
        //Glide.with(context).load(R.drawable.loading_dialog).into(imageView);
        return dialog;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     */
    public static float daysBetween(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date smdate = null;
        Date bdate = null;
        try {
            smdate = sdf.parse(start);
            bdate = sdf.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Float.parseFloat(String.valueOf(between_days));
    }
}

