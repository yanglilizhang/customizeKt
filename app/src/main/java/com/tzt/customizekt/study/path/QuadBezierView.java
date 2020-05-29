package com.tzt.customizekt.study.path;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;

import com.tzt.customizekt.study.base.BaseView2;

/**
 * 点在线上走
 */
public class QuadBezierView extends BaseView2 {

    /**
     * 起点坐标
     */
    private float mStartPointX;
    private float mStartPointY;

    /**
     * 重点坐标
     */
    private float mEndPointX;
    private float mEndPointY;

    /**
     * 控制点
     */
    private float mCtrlPointX;
    private float mCtrlPointY;

    private Path mPath;

    /**
     * 移动坐标
     */
    private float mMovePointX;
    private float mMovePointY;

    /**
     * 画曲线所用的画笔
     */
    private Paint mPaintBezier;
    /**
     * 话辅助线所用的画笔
     */
    private Paint mPaintCtrl;
    /**
     * 绘制文字的画笔
     */
    private Paint mPaintText;
    /**
     * 绘制运动圆圈的画笔
     */
    private Paint mPaintCircle;
    public QuadBezierView(Context context) {
        super(context);
    }

    public QuadBezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public QuadBezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //初始化画笔
        mPaintBezier = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBezier.setStrokeWidth(3);//划线的宽度
        mPaintBezier.setStyle(Paint.Style.STROKE);//画笔的类型，这里是实线

        mPaintCtrl = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCtrl.setStrokeWidth(1);
        mPaintCtrl.setStyle(Paint.Style.STROKE);

        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setStyle(Paint.Style.STROKE);
        mPaintText.setTextSize(20);

        mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     *在每次View的Size变化时，设定曲线的起点、终点以及控制点，并绘制曲线
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mStartPointX = w / 10;
        mStartPointY = h / 2 - 200;

        mEndPointX = w * 9 / 10;
        mEndPointY = h / 2 - 200;

        mCtrlPointX = w / 2;
        mCtrlPointY = h / 2 - 500;

        mMovePointX = mStartPointX;
        mMovePointY = mStartPointY;

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制二阶贝塞尔曲线
        mPath.reset();
        mPath.moveTo(mStartPointX,mStartPointY);
        mPath.quadTo(mCtrlPointX, mCtrlPointY,mEndPointX,mEndPointY);
        canvas.drawPath(mPath,mPaintBezier);

        ///绘制起点、终点和控制点
        canvas.drawPoint(mStartPointX, mStartPointY, mPaintCtrl);
        canvas.drawPoint(mEndPointX, mEndPointY, mPaintCtrl);
        canvas.drawPoint(mCtrlPointX, mCtrlPointY, mPaintCtrl);

        //加上文字注解
        canvas.drawText("起点", mStartPointX, mStartPointY, mPaintText);
        canvas.drawText("终点", mEndPointX, mEndPointY, mPaintText);
        canvas.drawText("控制点", mCtrlPointX, mCtrlPointY, mPaintText);
        //绘制辅助线
        canvas.drawLine(mStartPointX, mStartPointY, mCtrlPointX, mCtrlPointY, mPaintCtrl);
        canvas.drawLine(mEndPointX, mEndPointY, mCtrlPointX, mCtrlPointY, mPaintCtrl);

        canvas.drawCircle(mMovePointX, mMovePointY, 20, mPaintCircle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:{
                mCtrlPointX = event.getX();
                mCtrlPointY = event.getY();
                //表示当前View失效，如View是可见的，则onDraw方法会被调用
                //注意：该方法需要在UI线程中执行
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:{
                mCtrlPointX = event.getX();
                mCtrlPointY = event.getY();
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1);
                valueAnimator.setDuration(2000).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        //获得当前比例值
                        float t = (float) animation.getAnimatedValue();
                        //计算当前点坐标
                        PointF p = BezierUtil.CalculateBezierPointForQuadratic(t,
                                new PointF(mStartPointX, mStartPointY),
                                new PointF(mCtrlPointX, mCtrlPointY),
                                new PointF(mEndPointX, mEndPointY) );
                        mMovePointX = (int) p.x;
                        mMovePointY = (int) p.y;
                        //重新绘制View
                        invalidate();
                    }
                });
                valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                valueAnimator.start();
                break;
            }

        }
        //表示该事件已经被消耗
        return true;
    }
}