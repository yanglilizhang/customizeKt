package com.tzt.customizekt.diy.text

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.tzt.customizekt.R

class ColorTrackTextView3
@JvmOverloads constructor(context: Context, attr: AttributeSet? = null, defStyle: Int = 0) :
    AppCompatTextView(context, attr, defStyle) {

    private var mOriginColor: Int = Color.BLACK
    private var mChangeColor: Int = Color.RED
    private var mOriginPaint: Paint
    private var mChangePaint: Paint

    //1.当前的进度 0-1
    private var mCurrentProgress: Float = 0f

    //2.实现不同的朝向
    enum class Direction {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }

    private var mDirection: Direction = Direction.LEFT_TO_RIGHT

    init {
        val typedArray = context.obtainStyledAttributes(attr, R.styleable.ColorTrackTextView3)
        mOriginColor =
            typedArray.getColor(R.styleable.ColorTrackTextView3_originColor, mOriginColor)
        mChangeColor =
            typedArray.getColor(R.styleable.ColorTrackTextView3_changeColor, mChangeColor)
        typedArray.recycle()
        mChangePaint = getPaintByColor(mChangeColor)
        mOriginPaint = getPaintByColor(mOriginColor)
    }

    /**
     * 1.
     */
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        //super.onDraw(canvas)
        canvas?.save()
        val middle = (mCurrentProgress * width).toInt()
        if (mDirection == Direction.LEFT_TO_RIGHT) {
            drawText(canvas, mChangePaint, 0, middle)
            drawText(canvas, mOriginPaint, middle, width)
        } else {
            drawText(canvas, mChangePaint, width, width - middle)
            drawText(canvas, mOriginPaint, width - middle, 0)
        }
    }

    private fun drawText(canvas: Canvas?, paint: Paint, start: Int, end: Int) {
        canvas?.save()
        val rect = Rect(start, 0, end, height)
        canvas?.clipRect(rect) //裁剪区域
        val text = text.toString()
        //获取字体的宽度
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val x = (width - bounds.width()) / 2
        val fontMetrics = paint.fontMetrics
        val dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
        val baseLine = height / 2 + dy
        canvas?.drawText(text, x.toFloat(), baseLine, paint)
        canvas?.restore()
    }


    fun setDirection(direction: Direction) {
        this.mDirection = direction
    }

    fun setProgress(progress: Float) {
        this.mCurrentProgress = progress
        invalidate()
    }

    fun setChangeColor(color: Int) {
        this.mChangePaint.color = color
    }

    fun setOriginColor(color: Int) {
        this.mOriginPaint.color = color
    }


    private fun getPaintByColor(color: Int): Paint {
        val paint = Paint()
        paint.color = color
        paint.isAntiAlias = true
        paint.isDither = true
        paint.textSize = this.textSize
        return paint
    }

    //4.结合viewpager
//    private void leftToRight(){
//        mView.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
//        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0,1);
//        valueAnimator.setDuration(2000);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float value = (float)animation.getAnimatedValue();
//                mView.setProgress(value);
//            }
//        });
//        valueAnimator.start();
//    }
//
//    private void rightToLeft(){
//        mView.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
//        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0,1);
//        valueAnimator.setDuration(2000);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float value = (float)animation.getAnimatedValue();
//                mView.setProgress(value);
//            }
//        });
//        valueAnimator.start();
//    }
}