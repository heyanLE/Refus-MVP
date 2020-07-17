package cn.heyanle.bannerview.imginfo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by HeYanLe on 2020/5/7 0007.
 * https://github.com/heyanLE
 */
public class DrawableInfo implements ImageInfo {

    private Bitmap bitmap;
    private int resId;
    private Context context;

    @Override
    public DrawableInfo prepare() {

        Resources r = context.getResources();
        InputStream is = r.openRawResource(resId);
        BitmapDrawable  bmpDraw = new BitmapDrawable(r,is);
        bitmap = bmpDraw.getBitmap();
        return this;
    }

    @Override
    public void covert(ImageView imageView) {
        try {
            imageView.setImageBitmap(bitmap);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void recycler() {
        try{
            if(bitmap != null && !bitmap.isRecycled()){
                bitmap.recycle();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static DrawableInfo of(int resId , Context context){
        DrawableInfo info = new DrawableInfo();
        info.resId = resId;
        info.context = context;
        return info;
    }
}
