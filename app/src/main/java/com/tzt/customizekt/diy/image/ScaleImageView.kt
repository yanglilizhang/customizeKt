package com.tzt.customizekt.diy.image

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import com.tzt.customizekt.R

/**
 *
 *@author abc
 *@time 2019/9/12 9:41
 */
class ScaleImageView : AppCompatImageView {

    var scaleRatio: Float = 1.2f

    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        attrs?.let {
            val array = context.obtainStyledAttributes(it, R.styleable.ScaleImageView)
            if (array.hasValue(R.styleable.ScaleImageView_scaleRatio)) {
                scaleRatio = array.getFloat(R.styleable.ScaleImageView_scaleRatio, scaleRatio)
            }
            array.recycle()
        }
    }


    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)
        Log.e("**", "---->pressed=$pressed")
        if (pressed) {
            scaleX = scaleRatio
            scaleY = scaleRatio
        } else {
            scaleX = 1.0f
            scaleY = 1.0f
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
//        when (event.actionMasked) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> isPressed = true
            MotionEvent.ACTION_MOVE -> isPressed = false
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> isPressed = false
        }
        return true
    }


}