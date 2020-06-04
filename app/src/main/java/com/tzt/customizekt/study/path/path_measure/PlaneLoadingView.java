package com.tzt.customizekt.study.path.path_measure;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.tzt.customizekt.R;
import com.tzt.customizekt.study.base.BaseView;
/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/5
 * @description
 */
public class PlaneLoadingView extends BaseView {

    private static final float DELAY = 0.005f;

    // PathMeasure 测量过程中的坐标
    private float mPos[];
    // PathMeasure 测量过程中的正切
    private float mTan[];

    // 圈的画笔
    private Paint mCirclePaint;

    // 箭头图片
    private Bitmap mArrowBitmap;

    // 圆路径
    private Path mCirclePath;

    // 路径测量
    private PathMeasure mPathMeasure;

    // 当前移动值
    private float mCurrentValue = 0;

    private Matrix mMatrix;

    private ValueAnimator valueAnimator;

    public PlaneLoadingView(Context context) {
        super(context);
    }

    public PlaneLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaneLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void init(Context context) {
        // 初始化 画笔 [抗锯齿、不填充、红色、线条2px]
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(Color.RED);
        mCirclePaint.setStrokeWidth(2);

        // 获取图片
        mArrowBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow, null);

        // 初始化 圆路径 [圆心(0,0)、半径200px、顺时针画]
        mCirclePath = new Path();
        mCirclePath.addCircle(0, 0, 200, Path.Direction.CW);

        // 初始化 装载 坐标 和 正余弦 的数组
        mPos = new float[2];
        mTan = new float[2];

        // 初始化 PathMeasure 并且关联 圆路径
        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mCirclePath, false);

        // 初始化矩阵
        mMatrix = new Matrix();

        // 初始化 估值器 [区间0-1、时长5秒、线性增长、无限次循环]
        valueAnimator = ValueAnimator.ofFloat(0, 1f);
        valueAnimator.setDuration(5000);
        // 匀速增长
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 第一种做法：通过自己控制，是箭头在原来的位置继续运行
                mCurrentValue += DELAY;
                if (mCurrentValue >= 1) {
                    mCurrentValue -= 1;
                }

                // 第二种做法：直接获取可以通过估值器，改变其变动规律
//                mCurrentValue = (float) animation.getAnimatedValue();

                invalidate();
            }
        });
        startLoading();
    }

    //用于获取关联的Path上距离起始点长度（ 即传入的distance，范围0<=distance<=getLength() ）的点的坐标和正切值（两者可选，由flags决定）
    //pathMeasure.getMatrix(distance, matrix, PathMeasure.TANGENT_MATRIX_FLAG | PathMeasure.POSITION_MATRIX_FLAG);
    //public boolean getMatrix(float distance, Matrix matrix, int flags)

    @Override
    protected void onDraw(Canvas canvas) {
        // 画网格和坐标轴
        drawCoordinate(canvas);

        // 移至canvas中间
        canvas.translate(mWidth / 2, mHeight / 2);

        // 画圆路径
        canvas.drawPath(mCirclePath, mCirclePaint);

        // 测量 运动过程中的pos(坐标) 和 tan(正切)：
        // 获取关联的Path距离起始点长度(distance)的点的 坐标(pos) 和 余弦(tan[0]，即cos)与正弦(tan[1]，即sin)。
        // distance： 即需要的测量点与当前path起始位置的距离
        //测量点的正余弦值，tan[0]为cos，即余弦值或称为单位圆的x坐标
        //tan[1]为sin，即正弦值或称为单位圆的y坐标；
        mPathMeasure.getPosTan(mPathMeasure.getLength() * mCurrentValue, mPos, mTan);

        // 计算角度
        float degree = (float) (Math.atan2(mTan[1], mTan[0]) * 180 / Math.PI);

        Log.i("PlaneLoadingView_1", "------------pos[0] = " + mPos[0] + "; pos[1] = " + mPos[1]);
        Log.i("PlaneLoadingView_2", "------------tan[0](cos) = " + mTan[0] + "; tan[1](sin) = " + mTan[1]);
        Log.i("PlaneLoadingView_3", "path length = " + mPathMeasure.getLength() * mCurrentValue);
        Log.i("PlaneLoadingView_4", "degree = " + degree);

        // 重置矩阵
        mMatrix.reset();
        // 设置旋转角度
        mMatrix.postRotate(degree, mArrowBitmap.getWidth() / 2, mArrowBitmap.getHeight() / 2);
        // 设置偏移量
        mMatrix.postTranslate(mPos[0] - mArrowBitmap.getWidth() / 2,
                mPos[1] - mArrowBitmap.getHeight() / 2);

        // 画原点
        canvas.drawCircle(0, 0, 3, mCirclePaint);

        // 画箭头，使用矩阵旋转
        canvas.drawBitmap(mArrowBitmap, mMatrix, mCirclePaint);

        // 画在 箭头 图标的中心点
        canvas.drawCircle(mPos[0], mPos[1], 3, mCirclePaint);

    }

    public void startLoading() {
        valueAnimator.start();
    }

    public void stopLoading() {
        valueAnimator.cancel();
    }

}