package cn.heyanle.refus.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import androidx.core.content.FileProvider;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class BitmapUtil {


    //public static final String APP_AUTHORITY = "com.ifreedomer.timenote.provider";

    public static Bitmap covertView2Bitmap(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);/** 如果不设置canvas画布为白色，则生成透明 */v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }



    public static boolean saveBitmap(Bitmap bitmap, String path) {
        try {
            FileOutputStream out = new FileOutputStream(path);
            if (bitmap == null) {
                return false;
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
