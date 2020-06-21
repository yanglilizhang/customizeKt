package com.tzt.customizekt.study

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Color.parseColor
import android.os.Build
import android.util.AttributeSet
import android.view.View

/*
 *
 */
class PintView : View {
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 36f
        textAlign = Paint.Align.CENTER
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

//    draw()：super.draw()之前，被背景盖住；super.draw()后，盖住前景；
//    onDraw()：super.onDraw()之前，背景与主体内容之前；super.onDraw()之后，主体内容和子View之间；
//    dispatchDraw()：super.dispatchDraw()之前，主体内容和子View之间；super.dispatchDraw()之后，子View和前景之间；
//    onDrawForeground()：super.onDrawForeground()之前，子View和前景之间；super.onDrawForeground()之后，盖住前景；

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        when (1) {
            1 -> {
                val shape = Path().apply {
                    lineTo(0f, 0f)
                    lineTo(-20f, -20f)
                    lineTo(-40f, 0f)
                    close()
                }
                val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    isAntiAlias = true//是否抗锯齿
                    style = Paint.Style.FILL //填充的风格 Stroke 线风格
                    strokeWidth = 10f //线条宽度
                    strokeCap = Paint.Cap.ROUND //线头形状
                    strokeJoin = Paint.Join.ROUND //拐角形状
                    strokeJoin = Paint.Join.MITER //拐角形状是尖的比BEVEL长一些
                    strokeMiter = 4f //默认情况下延长线的最大值为4f
                    //设置抖动来优化色彩深度降低时的绘制效果->加抖动比较好
                    isDither = true //图片的色彩优化--true是dither(抖动)
                    //设置双线性过滤来优化Bitmap放大的绘制效果->双线性过滤效果较好false
                    isFilterBitmap = false//false是FilterBitmap(双线性过滤绘制Bitmap)
                }
                paint.apply {
                    //-----------使用 PathEffect 来给图形的轮廓设置效果-----------
                    pathEffect = null //一般的线
                    //圆角轮廓效果:
                    pathEffect = CornerPathEffect(20f)//CornerPathEffect 所有的拐角边圆角
                    //线条偏离轮廓效果:线条进行随机的偏离 segmentLength: 分段长度, 示例20f deviation: 偏离, 示例5f
                    pathEffect = DiscretePathEffect(20f, 5f)
                    //虚线轮廓效果:虚线 intervals: 长度>=2 phase: 相位偏移到间隔数组中-
                    pathEffect = DashPathEffect(floatArrayOf(10f, 5f), 25f)
                    //路径虚线轮廓效果:使用一个 Path 来绘制「虚线
                    //advance:两个相邻shape之间的间隔
                    //phase: 虚线的偏移
                    //style:用来指定拐弯改变的时候 shape 的转换方式
                    pathEffect = PathDashPathEffect(shape, 40f, 0f, PathDashPathEffect.Style.ROTATE)
                    //组合效果:按照两种PathEffect分别对目标进行绘制
                    val dash = DashPathEffect(floatArrayOf(20f, 10f), 0f)
                    val discrete = DiscretePathEffect(20f, 5f)
                    pathEffect = SumPathEffect(dash, discrete)
                    //组合效果:先对目标 Path 使用一个 PathEffect，然后再对这个改变后的 Path 使用另一个 PathEffect
                    pathEffect = ComposePathEffect(dash, discrete)
                    //---------------------------------------------------
                }
                paint.apply {
                    //阴影!!!!!!!!!!!!!如果要清除阴影层，使用 clearShadowLayer()
                    //radius: 阴影的模糊范围
                    //dx dy: 阴影的偏移量
                    //shadowColor: 阴影的颜色
                    //禁止硬件加速 绘制文字
                    setLayerType(LAYER_TYPE_SOFTWARE, null)
                    setShadowLayer(10f, 0f, 0f, Color.RED)
                    "注意: 在硬件加速开启的情况下, setShadowLayer() 只支持文字的绘制,文字之外的绘制必须关闭硬件加速才能正常绘制阴影。\n" +
                            "\t\t· 如果 shadowColor 是半透明的，阴影的透明度就使用 shadowColor 自己的透明度；" +
                            "而如果 shadowColor 是不透明的，阴影的透明度就使用 paint 的透明度。"
                    clearShadowLayer() //如果不清除画的圆是没有阴影的
                    setShadowLayer(15f, 2f, 2f, Color.GREEN)
                }
                canvas?.drawCircle(width / 2f, height / 2f, height / 4f, textPaint)
                ///对于图片处理------在绘制层上方的附加效果,基于整个画面来进行过滤!!!
//                NORMAL: 内外都模糊绘制
//                SOLID: 内部正常绘制，外部模糊
//                INNER: 内部模糊，外部不绘制
//                OUTER: 内部不绘制，外部模糊
                //无效果？硬件加速
                paint.apply {
                    maskFilter = null
                    //内外都模糊绘制-整个图像都被模糊掉
                    maskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.NORMAL)
                    //内部模糊，外部不绘制-----在图像内部边沿产生模糊效果
                    maskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.INNER)
                    //内部不绘制，外部模糊------图像边界外产生一层阴影，并且将图像变成透明效果
                    maskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.OUTER)
                    //内部正常绘制，外部模糊-----图像边界外产生一层与图像颜色一致阴影效果
                    maskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.SOLID)
                    /**
                     * 浮雕的效果
                     *
                     * @param direction  指定光源的位置，长度为xxx的数组标量[x,y,z]
                     * @param ambient    环境光的因子 （0~1），越接近0，环境光越暗
                     * @param specular   镜面反射系数 越接近0，镜面反射越强
                     * @param blurRadius 模糊半径 值越大，模糊效果越明显
                     */
                    maskFilter = EmbossMaskFilter(floatArrayOf(1f, 1f, 1f), 0.3f, 60f, 80f)
                }
                //在线条宽度不为 0（并且模式为 STROKE 模式或 FLL_AND_STROKE ）,
                // 或者设置了 PathEffect 的时候，实际 Path 就和原 Path 不一样了
                //存储实际path
                val dstPath = Path()
                paint.apply {
                    //计算出绘制 Path 或文字时的实际 Path
//                    getFillPath(绘制path,实际path)
//                    getTextPath() 绘制文字的实际path
                }
                //paint中对颜色的处理
                //1. LinearGradient 线性渐变
//                注意：在设置了 Shader 的情况下， Paint.setColor/ARGB() 所设置的颜色就不再起作用。
                //TileMode 有三种类型
                //2. RadialGradient 辐射渐变
                //3. SweepGradient 扫描渐变
                val shader: Shader = LinearGradient(
                    100f, 100f, 500f, 500f, parseColor("#E91E63"),
                    parseColor("#2196F3"), Shader.TileMode.CLAMP
                )
                //如果设置了Shader则原先给paint设置的setColor()失效
                paint.apply {
                    color = Color.parseColor("#009688")
                    setARGB(10, 255, 33, 200)
                    setShader(shader)
                    //为绘制设置颜色过滤,颜色过滤的意思，就是为绘制的内容设置一个统一的过滤策略
//                    setColorFilter()//Canvas.drawXXX() 方法会对每个像素都进行过滤后再绘制出来
//                    LightingColorFilter PorterDuffColorFilter 和 ColorMatrixColorFilter
                }
                //4. BitmapShader--->用 Bitmap 的像素来作为图形或文字的填充
//                参数：
//                bitmap：用来做模板的 Bitmap 对象
//                tileX：横向的 TileMode
//                tileY：纵向的 TileMode。
//                BitmapShader(Bitmap bitmap, Shader.TileMode tileX, Shader.TileMode tileY)

                //5.结合两个Shader---->ComposeShader(shader1, shader2, PorterDuff.Mode.SRC_OVER);
                //mode:两个Shader的叠加模式，即shaderA和shaderB应该怎样共同绘制。它的类型是 PorterDuff.Mode
                //PorterDuff.Mode 是用来指定两个图像共同绘制时的颜色策略的。
                /**ComposeShader() 在硬件加速下是不支持两个相同类型的 Shader 的，所以这里也需要关闭硬件加速才能看到效果。*/
                paint.apply {
                    //为绘制设置颜色过滤,颜色过滤的意思，就是为绘制的内容设置一个统一的过滤策略
                    //用来基于颜色进行过滤处理
//                    setColorFilter()//Canvas.drawXXX() 方法会对每个像素都进行过滤后再绘制出来
//                    LightingColorFilter PorterDuffColorFilter 和 ColorMatrixColorFilter

                    //要想使用 setXfermode() 正常绘制，必须使用离屏缓存 (Off-screen Buffer)
                    // 把内容绘制在额外的层上，再把绘制好的内容贴回 View 中。
//                    setXfermode() 用来处理源图像和 View 已有内容的关系

                    //PorterDuff.Mode两个图层按照不同模式，可以组合成不同的结果显示出来！

//                    离屏缓冲使用！！！！！！
//                    val saved = canvas?.saveLayer(null, null, Canvas.ALL_SAVE_FLAG)
//                    canvas?.drawBitmap(bitmapDst, matrix, paint)
//                    paint.xfermode = PorterDuffXfermode(model)
//                    canvas?.drawBitmap(bitmapSrc, matrix, paint)
//                    paint.xfermode = null
//                    canvas?.restoreToCount(saved ?: 0)

//                    View.setLayerType() 是直接把整个 View 都绘制在离屏缓冲中。
//                    setLayerType(LAYER_TYPE_HARDWARE) 是使用 GPU 来缓冲，
//                    setLayerType(LAYER_TYPE_SOFTWARE) 是直接直接用一个 Bitmap 来缓冲。
                }

                //绘制文字
                paint.apply {
                    textSize = 18f
                    typeface = Typeface.DEFAULT //文字样式
                    isFakeBoldText = true //是否为伪粗体
                    isStrikeThruText = true //是否加删除线
                    isUnderlineText = true //下划线
                    textSkewX = -0.5f //文字斜度
                    textSkewX = 0.5f //文字斜度
                    textScaleX = 1.2f //文字的胖瘦
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textPaint.letterSpacing = 0.2f//字符间距
//                        canvas?.drawText(text, width / 2f, height / 4f * 2, textPaint)
                    }
                    // 用 CSS 的 font-feature-settings 的方式来设置文字
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textPaint.fontFeatureSettings = null
//                        canvas?.drawText(text, width / 2f, height / 3f, textPaint)
                        textPaint.fontFeatureSettings = "smcp"
//                        canvas?.drawText(text, width / 2f, height / 3f * 2, textPaint)
                    }
                    textAlign = Paint.Align.CENTER//文字的对齐方式
                }
//                canvas.drawText()
//                canvas.drawTextRun()
//                canvas.drawTextOnPath()
            }
        }
    }

    ///解决彩色图片阴影问题
//    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
    //        //获取图片的色彩通道
//        Bitmap bg_bimap=bitmap1.extractAlpha();
//        //设置画笔为我们想要的颜色
//        paint.setColor(Color.DKGRAY);
//        //样式采用 NORMAL 或者 SOLID
//        paint.setMaskFilter(new BlurMaskFilter(50,BlurMaskFilter.Blur.NORMAL));
//        //先画背景图片
//        canvas.drawBitmap(bg_bimap,50,50,paint);
//        //再画我们的前景图片
//        canvas.drawBitmap(bitmap1,50,50,null);
//    }

    /**
    private static final Xfermode[] sModes = {
    new PorterDuffXfermode(PorterDuff.Mode.CLEAR),      // 清空所有，要闭硬件加速，否则无效
    new PorterDuffXfermode(PorterDuff.Mode.SRC),        // 显示前都图像，不显示后者
    new PorterDuffXfermode(PorterDuff.Mode.DST),        // 显示后者图像，不显示前者
    new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),   // 后者叠于前者
    new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),   // 前者叠于后者
    new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),     // 显示相交的区域，但图像为后者
    new PorterDuffXfermode(PorterDuff.Mode.DST_IN),     // 显示相交的区域，但图像为前者
    new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),    // 显示后者不重叠的图像
    new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),    // 显示前者不重叠的图像
    new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),   // 显示前者图像，与后者重合的图像
    new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),   // 显示后者图像，与前者重合的图像
    new PorterDuffXfermode(PorterDuff.Mode.XOR),        // 显示持有不重合的图像
    new PorterDuffXfermode(PorterDuff.Mode.DARKEN),     // 后者叠于前者上，后者与前者重叠的部份透明。要闭硬件加速，否则无效
    new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),    // 前者叠于前者，前者与后者重叠部份透明。要闭硬件加速，否则无效
    new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),   // 显示重合的图像，且颜色会合拼
    new PorterDuffXfermode(PorterDuff.Mode.SCREEN)    // 显示持有图像，重合的会变白
    };
     */
    /**
    SRC类
    优先显示源图像
    SRC
    SRC_OVER
    SRC_IN
    SRC_OUT
    SRC_ATOP

    DST类
    优先显示目标图像
    DST
    DST_OVER
    DST_IN
    DST_OUT
    DST_ATOP

    其他类
    其它的叠加效果
    CLEAR
    XOR
    DARKEN
    LIGHTEN
    MULTIPLY
    SCREEN
    ADD
    OVERLAY
     */


}