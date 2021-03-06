package com.tzt.customizekt.diy.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.tzt.customizekt.R
import kotlin.math.abs

/**
 *
 *@author abc
 *@time 2020/4/9 17:58
 */
class VerProgressBar : View, View.OnTouchListener {

    private val DEFAULT_WIDTH = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        80F,
        resources.displayMetrics
    )
    private val DEFAULT_HEIGHT = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        200F,
        resources.displayMetrics
    )

    private var mProgressWidth = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        2F,
        resources.displayMetrics
    )


    private var mSlideRadius = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        10F,
        resources.displayMetrics
    )

    private var mSlideColor = Color.WHITE
    private var mSlideShadowColor = Color.parseColor("#905A9428")
    private var mProgressBgCplor = Color.parseColor("#B6B6B6")
    private var mProgressColor = ContextCompat.getColor(context, R.color.colorAccent)
    private var mMaxProgress = 100

    private lateinit var mSlidePoint: PointF
    private lateinit var mSlidePaint: Paint
    private lateinit var mProgressPaint: Paint

    private var mStartY = 0f

    private var mOnProgressChangeListener: OnProgressChangeListener? = null

    private var mProgress: Int = 0

    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {
        initAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initAttrs(context, attrs)
    }

    init {
        setOnTouchListener(this)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.VerProgressBar).apply {
            mProgressBgCplor =
                getColor(R.styleable.VerProgressBar_vp_pregress_bg_color, mProgressBgCplor)
            mProgressColor = getColor(R.styleable.VerProgressBar_vp_progress_color, mProgressColor)
            mSlideColor = getColor(R.styleable.VerProgressBar_vp_slide_color, mSlideColor)
            mSlideShadowColor =
                getColor(R.styleable.VerProgressBar_vp_slide_shadow_color, mSlideShadowColor)

            mMaxProgress = getInt(R.styleable.VerProgressBar_vp_max_progress, mMaxProgress)

            mProgressWidth =
                getDimensionPixelSize(
                    R.styleable.VerProgressBar_vp_progress_width,
                    mProgressWidth.toInt()
                ).toFloat()

            mSlideRadius =
                getDimensionPixelSize(
                    R.styleable.VerProgressBar_vp_slide_radius,
                    mSlideRadius.toInt()
                ).toFloat()

            mProgress = getInt(R.styleable.VerProgressBar_vp_progress, mProgress)
            recycle()
        }



        mSlidePaint = Paint().apply {
            style = Paint.Style.FILL
            color = mSlideColor
            isAntiAlias = true
            setShadowLayer(mSlideRadius + 1, 0f, 0f, mSlideShadowColor)
        }

        mProgressPaint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        mSlidePoint = PointF()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY) {
            MeasureSpec.makeMeasureSpec(
                DEFAULT_WIDTH.toInt(), MeasureSpec.EXACTLY
            )
        } else widthMeasureSpec
        val height = if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY) {
            MeasureSpec.makeMeasureSpec(
                DEFAULT_HEIGHT.toInt(), MeasureSpec.EXACTLY
            )
        } else heightMeasureSpec
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mSlidePoint.set(width / 2f, calSlidPointF())
        mStartY = mSlidePoint.y
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            drawProgress(it)
            drawSlide(it)
        }

    }

    private fun drawProgress(canvas: Canvas) {
        mProgressPaint.color = mProgressBgCplor
        val space = mProgressWidth / 2f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(
                mSlidePoint.x - space,
                mSlideRadius * 2,
                mSlidePoint.x + space,
                height - mSlideRadius * 2,
                space,
                space,
                mProgressPaint
            )
        } else {
            canvas.drawRect(
                mSlidePoint.x - space,
                mSlideRadius * 2,
                mSlidePoint.x + space,
                height - mSlideRadius * 2,
                mProgressPaint
            )
        }
        mProgressPaint.color = mProgressColor
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(
                mSlidePoint.x - space,
                height - mSlideRadius * 2,
                mSlidePoint.x + space,
                mSlidePoint.y,
                space,
                space,
                mProgressPaint
            )
        } else {
            canvas.drawRect(
                mSlidePoint.x - space,
                height - mSlideRadius * 2,
                mSlidePoint.x + space,
                mSlidePoint.y,
                mProgressPaint
            )
        }
        mProgress = calProgress()
        mOnProgressChangeListener?.onProgressChange(mProgress)
    }


    private fun drawSlide(canvas: Canvas) {
        canvas.drawCircle(mSlidePoint.x, mSlidePoint.y, mSlideRadius, mSlidePaint)
    }

    private fun calProgress(): Int {
        val totalHeight = height - mSlideRadius * 4
        val curHeight = height - mSlideRadius * 2 - mSlidePoint.y
        return (curHeight * mMaxProgress / totalHeight).toInt()
    }

    private fun calSlidPointF(): Float {
        val totalHeight = height - mSlideRadius * 4
        val cHeight = mProgress * totalHeight / mMaxProgress
        return height - mSlideRadius * 2 - cHeight
    }

    fun setProgress(progress: Int) {
        if (progress > mMaxProgress) return
        mProgress = progress

        mSlidePoint.y = calSlidPointF()
        invalidate()
    }

    fun setOnProgressChangeListsner(listener: OnProgressChangeListener) {
        this.mOnProgressChangeListener = listener
    }

    private fun moveSlide(y: Float) {
        val distance = y - mStartY
        if (abs(distance) > 3) {
            val pointY = when {
                y < mSlideRadius * 2 -> mSlideRadius * 2
                y > height - mSlideRadius * 2 -> height - mSlideRadius * 2
                else -> y
            }
            mSlidePoint.y = pointY
            mStartY = pointY
            invalidate()
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                moveSlide(event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                moveSlide(event.y)
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {
                mOnProgressChangeListener?.onTouchUpProgressChange(mProgress)
            }
        }
        return true
    }

    override fun onDetachedFromWindow() {
        mOnProgressChangeListener = null
        super.onDetachedFromWindow()
    }

    interface OnProgressChangeListener {
        fun onProgressChange(progress: Int)
        fun onTouchUpProgressChange(progress: Int)
    }
}