package com.tzt.customizekt.study.draw;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.tzt.customizekt.study.base.BaseView;

/**
 * 仪表盘
 */
public class InstrumentBoardView extends BaseView {

    private Paint paint;
    private Path path;
    private static float RADIUS = px2dp(100);
    private static float ANGLE = 45f;
    private static int SCALE_COUNT = 12;
    private static int POINTER_LENGTH = (int) px2dp(60);
    private PathDashPathEffect dashPathEffect;
    private RectF rectF;
    private RectF rectF2;
    private float centerX;
    private float centerY;

    public InstrumentBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(px2dp(2));
        //仪表盘弧长
        path = new Path();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        centerX = (float) getWidth() / 2;
        centerY = (float) getHeight() / 2;
        rectF = new RectF();
        rectF.left = (float) getWidth() / 2 - RADIUS;
        rectF.right = (float) getWidth() / 2 + RADIUS;
        rectF.top = (float) getHeight() / 2 - RADIUS;
        rectF.bottom = (float) getHeight() / 2 + RADIUS;
        path.addArc(rectF, ANGLE + 90, 360 - 2 * ANGLE);
        //刻度矩形大小
        Path rectPath = new Path();
        rectF2 = new RectF();
        rectF2.left = 0;
        rectF2.right = (int) px2dp(2);
        rectF2.top = 0;
        rectF2.bottom = (int) px2dp(10);
        rectPath.addRect(rectF2, Path.Direction.CW);
        //刻度间距
        PathMeasure pathMeasure = new PathMeasure();
        pathMeasure.setPath(path, false);
        int advance = ((int) pathMeasure.getLength() - (int) px2dp(2)) / SCALE_COUNT;
        dashPathEffect = new PathDashPathEffect(rectPath, advance, 0, PathDashPathEffect.Style.ROTATE);


    }

    @Override
    protected void init(Context context) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCoordinate(canvas);
//        canvas.translate(getWidth()/2,getHeight()/2);
//        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        canvas.drawPoint(0, 0, paint);//屏幕的左上角

        canvas.drawRect(rectF, paint);
        canvas.drawPath(path, paint);
        //可设置画笔效果，画虚线，圆角等，但是会吃掉本次绘制
        paint.setPathEffect(dashPathEffect);
        canvas.drawPath(path, paint);

        paint.setPathEffect(null);

        //画指针
        float stopX = centerX - (float) (Math.cos(Math.toRadians(ANGLE)) * POINTER_LENGTH);
        float stopY = centerY - (float) (Math.sin(Math.toRadians(ANGLE)) * POINTER_LENGTH);
        canvas.drawLine(centerX, centerY, stopX, stopY, paint);

        //画指针2
        float stopX2 = centerX - (float) (Math.cos(Math.toRadians(ANGLE + 90)) * (POINTER_LENGTH + 10));
        float stopY2 = centerY - (float) (Math.sin(Math.toRadians(ANGLE + 90)) * (POINTER_LENGTH + 10));
        canvas.drawLine(centerX, centerY, stopX2, stopY2, paint);
    }


    private static float px2dp(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                Resources.getSystem().getDisplayMetrics());
    }


}