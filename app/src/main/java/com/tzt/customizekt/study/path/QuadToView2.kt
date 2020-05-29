package com.tzt.customizekt.study.path

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import com.tzt.customizekt.study.base.BaseView2
import com.tzt.customizekt.study.utils.BaseView


class QuadToView2 : BaseView2 {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 2f
        style = Paint.Style.STROKE
        color = Color.BLUE
    }

    //波浪宽
    private val mItemWidth = 400

    private var mBezierPath: Path? = null

    private var mAnimator: ValueAnimator? = null
    private var mOffsetX = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    override fun init(context: Context?, attrs: AttributeSet?) {
        super.init(context, attrs)

        mBezierPath = Path()
        mAnimator = ValueAnimator.ofInt(0, mItemWidth)
        mAnimator?.addUpdateListener(AnimatorUpdateListener { animation ->
            mOffsetX = animation.animatedValue as Int
            invalidate()
        })

        mAnimator?.interpolator = LinearInterpolator()

        mAnimator?.duration = 2000
        mAnimator?.repeatCount = -1
        mAnimator?.start()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        drawCoordinate(canvas)
//        canvas.translate(mWidth / 2, mHeight / 2)

        //水波纹
//        mBezierPath?.moveTo(-300f, 0f)
////        mBezierPath?.quadTo(-200f, -200f, 0f, 0f)
//////        mBezierPath?.rQuadTo(200f,200f,300f,0f)
////        mBezierPath?.quadTo(200f,200f,300f,0f)
//https://www.jianshu.com/p/12fcc3fedbbc
        /**
        //水波纹
        mBezierPath?.moveTo(100f, 300f)
        mBezierPath?.quadTo(300f, 100f, 500f, 500f)
        //rQuadTo传的控制点(200,300)并非坐标，而是相对于第一段曲线的终点(500,500)来计算
        //即(500+200, 500+300)才是第二段曲线控制点的真正坐标
        //同理第二段曲线终点的坐标是(500+400, 500-200)
        mBezierPath?.rQuadTo(200f, 300f, 400f, -200f)
         */

        mBezierPath?.reset()
        val halfItem = mItemWidth / 2 //200
//        mBezierPath?.moveTo(0f, 200f)

        //水波纹
        //表示右移半个波浪， 并且上移100，即一个浪的最高点
//        mBezierPath?.rQuadTo((halfItem / 2).toFloat(), -100f, halfItem.toFloat(), 0f)
//        //再右移半个波浪，并且下移100，即一个浪的最低点，
//        mBezierPath?.rQuadTo((halfItem / 2).toFloat(), 100f, halfItem.toFloat(), 0f)

        //全波浪
//        mBezierPath?.moveTo(0f, 400f)
//        canvas?.drawLine(0f,400f,600f,400f,paint)
//        var i = 0
        //        for(int i=0; i<mItemWidth + getWidth(); i+=mItemWidth){
//        while (i < mItemWidth + width) {
//            mBezierPath?.rQuadTo((halfItem / 2).toFloat(), -100f, halfItem.toFloat(), 0f)
//            mBezierPath?.rQuadTo((halfItem / 2).toFloat(), 100f, halfItem.toFloat(), 0f)
//            i += mItemWidth
//        }


        var j = 0
        val x0 = -mItemWidth + mOffsetX
        val y0 = halfItem
        //必须先减去一个浪的宽度，以便第一遍动画能够刚好位移出一个波浪，形成无限波浪的效果
        mBezierPath?.moveTo(x0.toFloat(), y0.toFloat())
        while (j < mItemWidth + width) {
            mBezierPath?.rQuadTo((halfItem / 2).toFloat(), -100f, halfItem.toFloat(), 0f)
            mBezierPath?.rQuadTo((halfItem / 2).toFloat(), 100f, halfItem.toFloat(), 0f)
            j += mItemWidth
        }


        mBezierPath?.let {
            paint.color = Color.BLUE

            //闭合路径波浪以下区域
            mBezierPath?.lineTo(width.toFloat(), height.toFloat());
            mBezierPath?.lineTo(0f, height.toFloat());
            mBezierPath?.close()

            canvas?.drawPath(it, paint)
        }

    }


}