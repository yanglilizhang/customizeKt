package com.tzt.customizekt.study.path

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import com.tzt.customizekt.study.base.BaseView

class QuadToView: BaseView {
    val path = Path()
    // 控制点： 手指的前一个点，用来当控制点
    var prevX = 0f
    var prevY = 0f
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {

    }

    override fun init(context: Context?) {

    }

    override fun onDraw(canvas: Canvas) {
        drawCoordinate(canvas)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            style = Paint.Style.STROKE
            color = Color.RED
        }
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(event.x, event.y)
                prevX = event.x
                prevY = event.y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
//                path.lineTo(event.x, event.y)
                // 结束点 为线段的中间位置
                val endX = (event.x + prevX) / 2
                val endY = (event.y + prevY) / 2
                path.quadTo(prevX, prevY, endX, endY)
                prevX = endX   // 下一个控制点
                prevY = endY
                postInvalidate()
            }
        }
        return super.onTouchEvent(event)
    }
}