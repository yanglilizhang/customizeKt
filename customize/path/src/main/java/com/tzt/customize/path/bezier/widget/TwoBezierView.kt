package com.tzt.customize.path.bezier.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet


/**
 * Description:
 *
 * @author tangzhentao
 * @since 2020/4/26
 */
class TwoBezierView : CoordinateView {
    private lateinit var pointPaint: Paint
    private lateinit var textPaint: Paint
    private lateinit var linePaint: Paint
    private var halfH: Float = 0.0f
    private var halfW: Float = 0.0f

    private lateinit var valueAnimator: ValueAnimator
    private lateinit var pointA: PointF
    private lateinit var pointB: PointF
    private lateinit var pointC: PointF

    private var proportion = 0.0f

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        initAll()
    }

    fun initAll() {
        pointA = PointF(-300f, 0f)
        pointB = PointF(0f, -400f)
        pointC = PointF(300f, 0f)
        //点
        pointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 20f
            style = Paint.Style.STROKE
            color = Color.BLUE
        }
        //文字
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 36f
            color = Color.GRAY
            textAlign = Paint.Align.LEFT
        }
        //线
        linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 8f
            color = Color.BLUE
        }

        valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            repeatCount = -1
            duration = 10000
            addUpdateListener {
                proportion = animatedValue as Float
                postInvalidate()
            }
        }

        startAnimator()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //width:屏幕的宽 height:屏幕的高
        halfH = height / 2f
        halfW = width / 2f
        // 画布原点平移
        canvas?.translate(halfW, halfH)

        textPaint.color = Color.RED
        textPaint.textAlign = Paint.Align.CENTER //居中绘制
        val bounds = Rect()
        textPaint.getTextBounds("中心点", 0, "中心点".length, bounds)
        val fontMetrics: Paint.FontMetrics = textPaint.fontMetrics
        // 计算文字高度
        val fontHeight: Float = fontMetrics.bottom - fontMetrics.top
        // 计算文字baseline
        val textBaseY: Float =
            bounds.height() - (bounds.height() - fontHeight) / 2 - fontMetrics.bottom
        val baseLineY = Math.abs(textPaint.ascent() + textPaint.descent()) / 2
        canvas?.drawText("中心点", 0f, baseLineY, textPaint)

        pointPaint.color = Color.BLUE
        canvas?.drawPoint(pointA.x, pointA.y, pointPaint)
        canvas?.drawPoint(pointB.x, pointB.y, pointPaint)
        canvas?.drawPoint(pointC.x, pointC.y, pointPaint)
        linePaint.color = Color.BLUE
        canvas?.drawLine(pointA.x, pointA.y, pointB.x, pointB.y, linePaint)
        canvas?.drawLine(pointB.x, pointB.y, pointC.x, pointC.y, linePaint)
        linePaint.color = Color.RED
        val path = Path()
        path.moveTo(pointA.x, pointA.y)
        // (x1,y1)是控制点坐标，(x2,y2)是终点坐标
        path.quadTo(pointB.x, pointB.y, pointC.x, pointC.y)
        /**参数都为相对于上一个位置的位移偏量，可为负数*/
//        path.rQuadTo(pointB.x, pointB.y, pointC.x, pointC.y)
        canvas?.drawPath(path, linePaint)

        // 画进度和点文字
        textPaint.color = Color.GRAY
        canvas?.drawText("u = $proportion", -halfW / 5 * 4, halfH / 5 * 4, textPaint)
        textPaint.color = Color.RED
        canvas?.drawText("A", pointA.x, pointA.y + 50f, textPaint)
        canvas?.drawText("B", pointB.x - 50f, pointB.y, textPaint)
        canvas?.drawText("C", pointC.x, pointC.y + 50f, textPaint)

        if (proportion < 1f) {
            val pointD = PointF().apply {
                x = pointA.x + (pointB.x - pointA.x) * proportion
                y = pointA.y + (pointB.y - pointA.y) * proportion
            }
            val pointE = PointF().apply {
                x = pointB.x + (pointC.x - pointB.x) * proportion
                y = pointB.y + (pointC.y - pointB.y) * proportion
            }
            val pointF = PointF().apply {
                x = pointD.x + (pointE.x - pointD.x) * proportion
                y = pointD.y + (pointE.y - pointD.y) * proportion
            }

            // 画点名称
            canvas?.drawText("D", pointD.x, pointD.y - 50f, textPaint)
            canvas?.drawText("E", pointE.x, pointE.y - 50f, textPaint)
            canvas?.drawText("F", pointF.x, pointF.y + 50f, textPaint)

            linePaint.color = Color.YELLOW
            canvas?.drawLine(pointD.x, pointD.y, pointE.x, pointE.y, linePaint)
            pointPaint.color = Color.YELLOW
            canvas?.drawPoint(pointD.x, pointD.y, pointPaint)
            canvas?.drawPoint(pointE.x, pointE.y, pointPaint)
            pointPaint.color = Color.RED
            canvas?.drawPoint(pointF.x, pointF.y, pointPaint)
        }
    }

    /**
     * 启动动画
     */
    fun startAnimator() {
        valueAnimator.start()
    }
}