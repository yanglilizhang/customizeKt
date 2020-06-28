package com.tzt.customizekt.diy.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatSeekBar
import android.view.MotionEvent


/**移动侦测*/
class AlarmView : AppCompatSeekBar {

    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs,
        android.R.attr.editTextStyle
    )

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(h, w, oldh, oldw)

    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }


    override fun onDraw(c: Canvas?) {
        c?.rotate(-90F)
        c?.translate((-height).toFloat(), 0F)
//        c?.drawText("33", 50f, 0F, Paint().apply {
//            color = Color.RED
//            textSize = 80f
//        })
        super.onDraw(c)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isEnabled) {
            return false
        }

        when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                progress = max - (max * event.y / height).toInt()
                Log.e("ss", "------------->progress=$progress")
                onSizeChanged(width, height, 0, 0)
                Log.e("ss", "------------->width=$width,height=$height")
            }

            MotionEvent.ACTION_CANCEL -> {
            }
        }
        return true
    }

}