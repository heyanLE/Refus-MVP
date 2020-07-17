package cn.heyanle.refus.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.*;
import java.util.Objects;

public class BitmapUriUtil
{
    public static Uri bitmap2uri(Context c, Bitmap b)
    {
        File path = new File(c.getExternalCacheDir() + "0.png");
        try
        {
            OutputStream os = new FileOutputStream(path);
            b.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
            return Uri.fromFile(path);
        }
        catch (Exception ignored)
        {
        }
        return null;
    }
    public static Bitmap getBitmap(Context ctx, Uri url) throws IOException {
        InputStream stream =ctx.getContentResolver().openInputStream(url);
        //BitmapFactory.Options options = null;
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        Objects.requireNonNull(stream).close();
        return bitmap;
    }
}

