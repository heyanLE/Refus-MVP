package cn.heyanle.bannerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class DotLinear extends View {

    private Paint backPaint;
    private Paint dotPaint;
    private Paint curDotPaint;

    private int dotCount = 3;
    private int dotSize = 40;
    private int padding = 20;

    private int curDot = 0;

    public void initPoint(){
        backPaint = new Paint();
        backPaint.setColor(Color.BLACK);
        backPaint.setAlpha((int)(255*0.3));
        backPaint.setStyle(Paint.Style.FILL);

        dotPaint = new Paint();
        dotPaint.setColor(Color.WHITE);
        dotPaint.setAlpha((int)(255*0.3));
        dotPaint.setStyle(Paint.Style.FILL);

        curDotPaint = new Paint();
        curDotPaint.setColor(Color.WHITE);
        curDotPaint.setStyle(Paint.Style.FILL);
    }

    public void setCurDot(int curDot) {
        this.curDot = curDot;
        invalidate();
    }

    public void setDotCount(int dotCount) {
        this.dotCount = dotCount;
        invalidate();
    }

    public DotLinear(Context context) {
        super(context);
        initPoint();
    }

    public DotLinear(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPoint();
    }

    public DotLinear(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPoint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(dotSize*dotCount+(dotCount+1)*padding ,dotSize+2*padding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRoundRect(0,0,getMeasuredWidth() ,getMeasuredHeight() ,getMeasuredHeight()/3f,getMeasuredHeight()/3f ,backPaint);

        float x = padding+dotSize/2f;
        for(int i = 0 ; i < dotCount ; i ++){

            if (i == curDot){
                canvas.drawCircle(x ,getMeasuredHeight()/2f ,dotSize/2f ,curDotPaint);
            }else{
                canvas.drawCircle(x ,getMeasuredHeight()/2f ,dotSize/2f ,dotPaint);
            }
            x += dotSize + padding;

        }

    }
}