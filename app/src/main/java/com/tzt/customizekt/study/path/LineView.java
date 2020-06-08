package com.tzt.customizekt.study.path;

import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 简单虚线path
 */
public class LineView extends View {
    private static final String TAG = "circle";
    private Paint greenPaint;
    private int offset = 6;
    private Path path;

    public LineView(Context context) {
        this(context,null);
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        path = new Path();
        path.moveTo(0,0);
        path.quadTo(0,0,700,50);
        PathEffect effect = new DashPathEffect(new float[]{20,20},0);

        greenPaint = new Paint();
        greenPaint.setAntiAlias(true);
        greenPaint.setStrokeWidth(offset);
        greenPaint.setStyle(Paint.Style.STROKE);
        greenPaint.setStrokeCap(Paint.Cap.ROUND);
        greenPaint.setColor(Color.parseColor("#00da4e"));
        greenPaint.setPathEffect(effect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMySize(100,widthMeasureSpec);
        int height = getMySize(100,heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    private int getMySize(int defaultSize,int measureSpec){
        int mySize = defaultSize;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode){
            case MeasureSpec.UNSPECIFIED:
                mySize = defaultSize;
                break;
            case MeasureSpec.AT_MOST:
                mySize = defaultSize;
                break;
            case MeasureSpec.EXACTLY:
                mySize = size;
                break;
            default:
                break;
        }
        return mySize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#ffffff"));

        canvas.drawPath(path,greenPaint);

    }
}