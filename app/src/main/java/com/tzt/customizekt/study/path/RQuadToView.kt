package com.tzt.customizekt.study.path

import android.animation.ValueAnimator
import android.animation.ValueAnimator.RESTART
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.LinearInterpolator
import com.tzt.customizekt.study.utils.BaseView

class RQuadToView : BaseView {
    val path = Path()
    // 波浪高
    val waveHeight = 100f
    // 波浪宽
    val waveWidth = 800f
    // 波浪起始位置
    var waveY = 300f
    // 动画==波浪水平偏移
    var waveWidthDx = 0f
    // 动画==波浪垂直偏移
    var waveHeightDx = 0f

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 2f
        style = Paint.Style.FILL
        color = Color.GREEN
    }


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {

    }

    override fun init(context: Context?) {
        /**参数都为相对于上一个位置的位移偏量，可为负数...传进去的是相对距离,相对于起始点的位移*/
//        public void rQuadTo(float dx1, float dy1, float dx2, float dy2)
    }

    override fun onDraw(canvas: Canvas) {
        drawCoordinate(canvas)
        path.apply {
            reset()  // 复位

            // === 1.画波浪 ===
            // path的起始位置向左移一个波长
            moveTo(-waveWidth + waveWidthDx, waveY + waveHeightDx)
            val halfWaveWidth = waveWidth / 2
            var i = -halfWaveWidth
            // 画出屏幕内所有的波浪
            while (i <= width + halfWaveWidth) {
                rQuadTo(halfWaveWidth / 2.0f, -waveHeight, halfWaveWidth, 0f)
                rQuadTo(halfWaveWidth / 2.0f, waveHeight, halfWaveWidth, 0f)
                i += halfWaveWidth
            }

            // === 2.闭合path，实现fill效果  ===
            path.lineTo(width.toFloat(), height.toFloat())
            path.lineTo(0f, height.toFloat())
            path.close()

        }
//        path.reset()
//        path.apply {
//            moveTo(-500f,0f)
//            quadTo(200f,100f,-300f,-100f)
//        }

        canvas.drawPath(path, paint)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this.setOnClickListener {
            // === 3.第三步动画，实现波浪动画
            val valueAnimator = ValueAnimator.ofFloat(0f, waveWidth).apply {
                duration = 2000
                repeatMode = RESTART
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                addUpdateListener { it ->
                    waveWidthDx = it.animatedValue as Float
                    postInvalidate()
                }
            }
            valueAnimator.start()


            // === 4. 不断缩小范围动画
            ValueAnimator.ofFloat(0f, height.toFloat()).apply {
                duration = 8000
                repeatMode = RESTART
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                addUpdateListener { it ->
                    waveHeightDx = it.animatedValue as Float
                    postInvalidate()
                }
            }.start()

            // === 可以尝试使用联合动画
        }
    }

}