package com.tzt.customizekt.study.page

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.TypeEvaluator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Point
import android.util.Property
import android.widget.TextView
import com.tzt.common.basedepency.base.BaseActivity
import com.tzt.common.basedepency.widget.ToobarParams
import com.tzt.customizekt.R
import com.tzt.customizekt.study.utils.ProvinceUtil
import kotlinx.android.synthetic.main.activity_page.*


class PageActivity : BaseActivity() {

    override fun layoutResID(): Int {
        return R.layout.activity_page
    }

    override fun getToobarParams(): ToobarParams? {
        return ToobarParams(
            createFinisIcon(),
            title = "我的自定义"
        )
    }

    override fun initData() {
        super.initData()
        startActivity(Intent(this, MiSportsActivity::class.java))
    }

    internal class PointEvaluator1 :
        TypeEvaluator<Float> {
        override fun evaluate(fraction: Float, startValue: Float?, endValue: Float?): Float {
            // startValue:1   endValue:3    fraction: 0.2  return:(1+(3-1))*0.2 注意要加初始值1
            //1+(3-1)*0.2
            return 0f
        }

    }


    internal class PointEvaluator :
        TypeEvaluator<Point> {
        override fun evaluate(fraction: Float, startValue: Point, endValue: Point): Point {
            // (1, 1)   (5, 5)     fraction: 0.2   x: 1 + (5 - 1) * 0.2 y: 1 + (5 - 1) * 0.2
            val x = startValue.x + (endValue.x - startValue.x) * fraction
            val y = startValue.y + (endValue.y - startValue.y) * fraction
            return Point(x.toInt(), y.toInt())
        }
    }


    internal class ProvinceEvaluator :
        TypeEvaluator<String> {
        override fun evaluate(fraction: Float, startValue: String, endValue: String): String {
            // 北京市      上海市       fraction 0.5f
            val startIndex = ProvinceUtil.provinces.indexOf(startValue)
            val endIndex = ProvinceUtil.provinces.indexOf(endValue)
            val index = (startIndex + (endIndex - startIndex) * fraction).toInt()
            return ProvinceUtil.provinces[index]
        }
    }


    /////////////////自定义Property：////////////////
//    public static class MyProperty extends Property<TextView,String>{
//        public MyProperty(String name) {
//            super(String.class, name);
//        }
//
//        @Override
//        public String get(TextView object) {
//            return object.getText().toString();
//        }
//
//        @Override
//        public void set(TextView object, String value) {
//            object.setText(value);
//        }
//    }

    override fun bindListener() {

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
