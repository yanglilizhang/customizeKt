package com.tzt.customizekt.study.page

import com.tzt.common.basedepency.base.BaseActivity
import com.tzt.common.basedepency.widget.ToobarParams
import com.tzt.customizekt.R

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
    }

    override fun bindListener() {

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
