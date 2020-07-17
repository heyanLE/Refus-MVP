package cn.heyanle.bannerview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.heyanle.bannerview.imginfo.ImageInfo;
import cn.heyanle.bannerview.imginfo.ImageRing;

/**
 * banner 轮播 View
 * 循环
 * Created by HeYanLe on 2020/5/7 0007.
 * https://github.com/heyanLE
 */
public class BannerView extends ViewGroup {

    private boolean isAnimator = false;

    //private Handler handler;

    private ImageRing imageRing;
    private ImageView ivF;
    private ImageView ivS;
    private ImageView ivT;

    private DotLinear dotLinear;

    public BannerView(Context context) {
        super(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    public void setImageRing(ImageRing imageRing){
        this.imageRing = imageRing;
        setVisibility(INVISIBLE);
        drawImage();
        scrollCur();
        setVisibility(VISIBLE);
        if (dotLinear != null)
            dotLinear.setDotCount(imageRing.getImagesCount());
    }

    public void bindDotLinear(DotLinear dotLinear) {
        this.dotLinear = dotLinear;
        if (imageRing != null){
            dotLinear.setDotCount(imageRing.getImagesCount());
            dotLinear.setCurDot(imageRing.getCurImageIndex());
        }
    }

    public void init(){

        //handler = new Handler();

        ivF = new ImageView(getContext());
        ivS = new ImageView(getContext());
        ivT = new ImageView(getContext());

        addView(ivF);
        addView(ivS);
        addView(ivT);

        drawImage();
    }

    public void drawImage(){
        if (imageRing == null){
            return;
        }
        if (getChildCount() < 3){
            init();
        }
        imageRing.getPreImage().covert(ivF);
        imageRing.getCurImage().covert(ivS);
        imageRing.getNextImage().covert(ivT);
        scrollTo(ivF.getMeasuredWidth() ,0);
        if (dotLinear != null)
            dotLinear.setCurDot(imageRing.getCurImageIndex());
        //scrollCur();
    }

    public void scrollNext(){

        if (isAnimator){
            return;
        }

        ValueAnimator animator = ValueAnimator.ofInt(getScrollX() ,ivF.getMeasuredWidth() + ivS.getMeasuredWidth());
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                scrollTo(value ,0);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimator = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageRing.scrollNext();
                drawImage();
                isAnimator = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();

    }

    public  void scrollPre(){
        if (isAnimator){
            return;
        }

        ValueAnimator animator = ValueAnimator.ofInt(getScrollX() ,0);
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                scrollTo(value ,0);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimator = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageRing.scrollPre();
                drawImage();
                isAnimator = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    public  void scrollCur(){
        if (isAnimator){
            return;
        }

        ValueAnimator animator = ValueAnimator.ofInt(getScrollX() ,ivF.getMeasuredWidth());
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                scrollTo(value ,0);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimator = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                drawImage();
                isAnimator = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }




    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cWidth = 0;
        int cHeight = 0;

        cWidth = getMeasuredWidth();
        cHeight = getMeasuredHeight();

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).layout(-cWidth, 0, 0, 0);
        }

        if (imageRing != null) {
            if (ivF != null) {
                int cl1 = 0, ct1 = 0, cr1 = 0, cb1 = 0;
                cr1 = cWidth;
                cb1 = cHeight;
                ivF.layout(cl1, ct1, cr1, cb1);
            }
            if(ivS != null){
                ivS.layout(cWidth, 0, 2*cWidth, cHeight);
            }
            if(ivT != null){
                ivT.layout(2*cWidth, 0, 3*cWidth, cHeight);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        // 计算出所有的childView的宽和高
        measureChildren(MeasureSpec.makeMeasureSpec(sizeWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(sizeHeight, MeasureSpec.AT_MOST));
        /*
         * 直接设置为父容器计算的值
         */
        setMeasuredDimension(sizeWidth, sizeHeight);
    }


    private float downX;
    private float offset;
    private boolean isMove = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();

        if (isAnimator) {
            return true;
        }

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                isMove = true;
                offset = downX - event.getX();
                scrollTo((int) (ivF.getMeasuredWidth() + offset),0);
                break;

            case MotionEvent.ACTION_UP:
                if (isMove){
                    if (offset >= 400){
                        scrollNext();
                    }else if(offset <= -400){
                        scrollPre();
                    }else{
                        scrollCur();
                    }
                }
                isMove = false;

                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 将事件拦截掉
        return true;
    }

}




