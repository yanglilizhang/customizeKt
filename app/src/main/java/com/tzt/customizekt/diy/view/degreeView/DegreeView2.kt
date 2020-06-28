package com.tzt.customizekt.diy.view.degreeView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.tzt.customizekt.R

class DegreeView2 : View {

    private lateinit var paint: Paint
    private lateinit var paintMiddle: Paint
    private lateinit var paintStartEnds: Paint
    private var yy = 0f
    private var yyStart = 0f
    private var yyMiddle = 0f
    private var xStep = 0f

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.black)
        paint.strokeWidth = context.dpToPx(2).toFloat()

        paintStartEnds = Paint()
        paintStartEnds.style = Paint.Style.FILL
        paintStartEnds.color = ContextCompat.getColor(context, R.color.blue)
        paintStartEnds.strokeWidth = context.dpToPx(2).toFloat()

        paintMiddle = Paint()
        paintMiddle.style = Paint.Style.FILL
        paintMiddle.color = ContextCompat.getColor(context, R.color.colorAccent)
        paintMiddle.strokeWidth = context.dpToPx(2.2).toFloat()

        yy = context.dpToPx(10).toFloat()
        yyStart = context.dpToPx(15).toFloat()
        yyMiddle = context.dpToPx(20).toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        xStep = measuredWidth.toFloat() / 10
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawLines(it)
        }
    }

    private fun drawLines(canvas: Canvas) {
        for (i in 0..10) {
            val x = (i * xStep) //- paint.strokeWidth / 2
            if (i == 5) {
                canvas.drawLine(x, 0f, x, yyMiddle, paintMiddle)
            } else if (i == 0 || i == 10) {
                canvas.drawLine(x, 0f, x, yyStart, paint)
            } else {
                canvas.drawLine(x, 0f, x, yy, paint)
            }
        }
    }

    private fun Context.dpToPx(value: Number): Int {
        val dip = value.toFloat()
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.displayMetrics)
            .toInt()
    }
}