package cn.heyanle.bannerview.imginfo;

import android.widget.ImageView;

/**
 * Created by HeYanLe on 2020/5/7 0007.
 * https://github.com/heyanLE
 */
public interface ImageInfo {

    ImageInfo prepare();

    void covert(ImageView imageView);

    void recycler();

}
