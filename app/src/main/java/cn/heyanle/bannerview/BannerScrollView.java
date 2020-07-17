package cn.heyanle.bannerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

/**
 * Created by HeYanLe on 2020/5/8 0008.
 * https://github.com/heyanLE
 */
public class BannerScrollView extends NestedScrollView {

    private boolean isScroll = true;
    private float xDistance, yDistance, xLast, yLast;

    public BannerScrollView(@NonNull Context context) {
        super(context);
    }

    public BannerScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BannerScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isScroll = false;
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isScroll){
                    return false;
                }
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                if (xDistance > yDistance) {
                    if(xDistance > 50) {
                        isScroll = true;
                        return false;
                    }
                }else if(yDistance >50) {
                    return true;
                }
            case MotionEvent.ACTION_UP:
                isScroll = false;

        }
        return super.onInterceptTouchEvent(ev);
    }
}
