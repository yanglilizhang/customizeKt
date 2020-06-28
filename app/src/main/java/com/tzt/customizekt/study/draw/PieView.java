package com.tzt.customizekt.study.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.tzt.customizekt.diy.data.PieData;

import java.util.ArrayList;

public class PieView extends View {

    private Paint mPaint = new Paint();
    private int[] mColors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.DKGRAY};
    private ArrayList<PieData> mData;
    private float mStartAngle = 0;
    private int mWidth;
    private int mHeight;

    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(null == mData)
            return;
        float currentAngle = mStartAngle;
        canvas.translate(mWidth/2 ,mHeight / 2);
        float radius = (float)(Math.min(mWidth,mHeight) / 2 * 0.8);
        RectF rectF = new RectF(-radius,-radius,radius,radius);

        for (PieData item: mData){
            mPaint.setColor(item.getColor());
            canvas.drawArc(rectF,currentAngle,item.getAngle(),true,mPaint);
            currentAngle += item.getAngle();
        }
    }

    //设置起始角度
    public void setStartAngle(int startAngle) {
        this.mStartAngle = startAngle;
        invalidate();
    }

    public void setData(ArrayList<PieData> data){
        this.mData = data;
        initData(data);
        invalidate();
    }

    private void initData(ArrayList<PieData> data) {
        if (null == data || data.isEmpty())
            return;
        float sumValue = 0;
        for (PieData item : data){
            sumValue += item.getValue();
            int j = data.indexOf(item) % mColors.length;
            item.setColor(mColors[j]);
        }

        float sumAngle = 0;
        for(PieData item : data){
            float percentage = item.getValue() / sumValue;
            float angle = percentage * 360;
            item.setPercentage(percentage);
            item.setAngle(angle);
            sumAngle += angle;
        }
    }
}