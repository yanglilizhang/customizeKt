package com.tzt.customizekt.study.path.path_measure

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.tzt.customizekt.study.base.BaseView2

/**
 * <pre>
 *     author  : devyk on 2019-12-03 22:00
 *     blog    : https://juejin.im/user/578259398ac2470061f3a3fb/posts
 *     github  : https://github.com/yangkun19921001
 *     mailbox : yang1001yk@gmail.com
 *     desc    : This is PathMeasureView
 * </pre>
 */
class PathMeasureView : BaseView2 {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    lateinit var mPathPaint: Paint

    lateinit var mPath: Path
    lateinit var mTempPath2: Path

    lateinit var mPathMeasure: PathMeasure
    lateinit var mPathMeasure2: PathMeasure

    var stop = 0f
    var stopValues = 0f


    override fun init(context: Context?, attrs: AttributeSet?) {
        super.init(context, attrs)
        mPathPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPathPaint.style = Paint.Style.STROKE
        mPathPaint.strokeWidth = 5f
        mPathPaint.setColor(Color.RED)

        mPath = Path()
        mTempPath = Path()
        mTempPath2 = Path()

        mViewHeight


        mPathMeasure = PathMeasure()
        mPathMeasure2 = PathMeasure()

        //在使用 getSegment 是需要 在构造函数中调用setLayerType(LAYER_TYPE_SOFTWARE,null) 函数来禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.addUpdateListener { animation ->
            stopValues = animation.animatedValue as Float
            invalidate()
        }
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.setDuration(1500)
        valueAnimator.start()


    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        /**
         * 1. getLength
         */
/*        //将起点移动到 100，100 的位置
        mPath.moveTo(100f,100f)
        //绘制连接线
        mPath.lineTo(100f,450f)
        mPath.lineTo(450f,500f)
        mPath.lineTo(500f,100f)
        mPathMeasure.setPath(mPath,false)//不被闭合
        mPathMeasure2.setPath(mPath,true)//闭合
        println("forceClosed false pathLength =${mPathMeasure.length}")
        println("forceClosed true pathLength =${mPathMeasure2.length}")*/


        /**
         * 2. nextContour
         */

/*        mPath.addCircle(500f,500f,10f,Path.Direction.CW)
        mPath.addCircle(500f,500f,80f,Path.Direction.CW)
        mPath.addCircle(500f,500f,150f,Path.Direction.CW)
        mPath.addCircle(500f,500f,200f,Path.Direction.CW)
        mPathMeasure.setPath(mPath,false)//不被闭合
        canvas.drawPath(mPath,mPathPaint)
        do {
            println("forceClosed  pathLength =${mPathMeasure.length}")
        }while (mPathMeasure.nextContour())
        */

//startD：开始截取位置距离 Path 起始点的长度
//stopD: 结束截取位置距离 Path 起始点的长度
//dst: 截取的 Path 将会被添加到 dst 中，注意是添加，而不是替换
//startWithMoveTo: 起始点是否使用 moveTo
        /**
         * 3. getSegment
         */
        mPath.addCircle(500f, 500f, 200f, Path.Direction.CCW)
        mPathMeasure.setPath(mPath, false)//不被闭合
        mTempPath.rewind()
        stop = mPathMeasure.length * stopValues
        val start = (stop - (0.5 - Math.abs(stopValues - 0.5)) * mPathMeasure.length).toFloat()
        val segment = mPathMeasure.getSegment(start, stop, mTempPath, true)
        println("总长度：${mPathMeasure.length} 是否截取成功:$segment + start:$start  stop:$stop")
        canvas.drawPath(mTempPath, mPathPaint)


    }
}