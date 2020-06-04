package com.tzt.customizekt.study.path

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.tzt.customizekt.R
import com.tzt.customizekt.study.base.BaseView2

/**
 * <pre>
 *     author  : devyk on 2019-12-04 10:50
 *     blog    : https://juejin.im/user/578259398ac2470061f3a3fb/posts
 *     github  : https://github.com/yangkun19921001
 *     mailbox : yang1001yk@gmail.com
 *     desc    : This is AirplaneRotate 小飞机旋转
 * </pre>
 */
class CarRotate : BaseView2 {


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /**
     * 定义一个 Bitmap
     */
    var mBitmap: Bitmap? = null

    /**
     * 定义一个 path 测量
     */
    var mPathMeasure: PathMeasure? = null

    /**
     * 定义一个矩阵，目的是给 bitmap 修改角度
     */
    var mMatrix: Matrix? = null

    /**
     * 截取的变量值
     */
    var mCurValues = 0f

    private var pos: FloatArray? = null                // 当前点的实际位置
    private var tan: FloatArray? = null                // 当前点的tangent值,用于计算图片所需旋转的角度

    override fun init(context: Context?, attrs: AttributeSet?) {
        super.init(context, attrs)

        //初始化 bitmap
        val options = BitmapFactory.Options()
        options.inSampleSize = 8
        mBitmap = BitmapFactory.decodeResource(context!!.resources, R.drawable.car, options)
        mPathMeasure = PathMeasure()
        mMatrix = Matrix()

        pos = FloatArray(2)
        tan = FloatArray(2)

        mTempPaint.style = Paint.Style.STROKE

    }


    /**
     * 实现 1
     */
/* override fun onDraw(canvas: Canvas?) {
     super.onDraw(canvas)
     //清楚 path 数据
     mTempPath.rewind()
     //绘制一个模拟公路
     addLineToPath()
     //测量 path,不闭合
     mPathMeasure!!.setPath(mTempPath, true)
     //动态变化的值
     mCurValues += 0.002f
     if (mCurValues >= 1) mCurValues = 0f
     //拿到当前点上的 正弦值
     mPathMeasure!!.getPosTan(mPathMeasure!!.length * mCurValues, pos, tan)
     //通过正弦值拿到当前弧度
     val y = tan!![1].toDouble()
     val x = tan!![0].toDouble()
     //拿到 bitmap 需要旋转的角度，之后将矩阵旋转
     val degrees = (Math.atan2(y, x) * 180f / Math.PI).toFloat()
     println("角度：$degrees")
     mMatrix!!.reset()
     mMatrix!!.postRotate(degrees, mBitmap!!.width / 2.toFloat(), mBitmap!!.height / 2.toFloat())
      //这里要将设置到小车的中心点
     mMatrix!!.postTranslate(pos!![0] - mBitmap!!.getWidth() / 2, pos!![1] - mBitmap!!.getHeight() / 2)
     //绘制Bitmap和path
     canvas!!.drawPath(mTempPath, mTempPaint)
     canvas!!.drawBitmap(mBitmap!!, mMatrix!!, mTempPaint)
     //重绘
     postInvalidate()
 }*/

    /**
     * 实现 2,直接用getMatrix()
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //清楚 path 数据
        mTempPath.rewind()
        //绘制一个模拟公路
        addLineToPath()
        //测量 path,不闭合
        mPathMeasure!!.setPath(mTempPath, true)
        //动态变化的值
        mCurValues += 0.002f
        if (mCurValues >= 1) mCurValues = 0f


        // 获取当前位置的坐标以及趋势的矩阵
        //这个方法是用于得到路径上某一长度的位置以及该位置的正切值的矩阵
        //flags	规定哪些内容会存入到matrix中:可选择 POSITION_MATRIX_FLAG(位置) ANGENT_MATRIX_FLAG(正切)
        //measure.getMatrix(distance, matrix, PathMeasure.TANGENT_MATRIX_FLAG | PathMeasure.POSITION_MATRIX_FLAG);
        mPathMeasure!!.getMatrix(
            mPathMeasure!!.length * mCurValues, mMatrix!!,
            (PathMeasure.TANGENT_MATRIX_FLAG or PathMeasure.POSITION_MATRIX_FLAG)
        )
        // 将图片绘制中心调整到与当前点重合(偏移加旋转)//这里要将设置到小车的中心点
        mMatrix!!.preTranslate(-mBitmap!!.width / 2f, -mBitmap!!.height / 2f);


        //绘制Bitmap和path
        canvas!!.drawPath(mTempPath, mTempPaint)
        canvas!!.drawBitmap(mBitmap!!, mMatrix!!, mTempPaint)

        //重绘
        postInvalidate()

    }


    /*
    这里我们讲一下postTranslate和preTranslate的差别，
    Postconcats the matrix with the specified translation. M' = T(dx, dy) * M  代表指定平移之后再进行矩阵拼接
    Preconcats the matrix with the specified translation. M' = M * T(dx, dy) 代表指定平移之前就要进行矩阵拼接

    我们这里，使用getPosTan方法时，调用的是postTranslate：
    这是因为，在调用postRotate接口的时候，已经指定了旋转中心，然后再调用postTranslate进行平移，若调用
    preTranslate的话，则会导致旋转的时候由于平移使旋转中心发生改变，导致小车旋转角度不正确。

    而在调用getMatrix方法时，调用的是preTranslate，这是由于，
    getMatrix将坐标和角度信息拼接到矩阵carMatrix的时候并没有指定旋转中心，因此这里需要先使用preTranslate进行移动和矩阵拼接，
    然后getMatrix内部对carMatrix矩阵赋值进行角度旋转的时候会以移动后的位置为中心
    */


    private fun addLineToPath() {
        mTempPath.moveTo(100f, 100f)
        mTempPath.lineTo(100f, 200f)
        mTempPath.lineTo(200f, 300f)
        mTempPath.lineTo(300f, 400f)
        mTempPath.lineTo(400f, 500f)
        mTempPath.lineTo(500f, 600f)
        mTempPath.lineTo(600f, 300f)
        mTempPath.lineTo(600f, 900f)
        mTempPath.lineTo(900f, 1200f)
        mTempPath.lineTo(1200f, 800f)
        mTempPath.lineTo(800f, 900f)
        mTempPath.lineTo(900f, 100f)
        mTempPath.close()
    }
}