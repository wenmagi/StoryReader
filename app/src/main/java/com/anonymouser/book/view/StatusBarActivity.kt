package com.anonymouser.book.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.anonymouser.book.utlis.StatusBarUtil


/**
 * @author zhangzhaowen @ Zhihu Inc.
 * @since 06-20-2018
 */
open class StatusBarActivity : AppCompatActivity() {

    // StatusBar 背景
    private var mStatusBarDrawable: StatusBarUtil.StatusBarDrawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtil.translucentStatusBar(this)
        super.onCreate(savedInstanceState)
        invalidateStatusBar()
    }


    fun updateSystemUiColor() {
        StatusBarUtil.setStatusBarLightMode(this, false)
    }

    /**
     * 刷新 FakeStatusBar 的属性（改变颜色后需要调用）
     */
    protected fun invalidateStatusBar() {
        val rootView = window.decorView

        if (rootView == null) {
            return
        }

        if (!isSystemUiFullscreen()) {
            if (mStatusBarDrawable == null) {
                mStatusBarDrawable = StatusBarUtil.addStatusBarDrawableToView(rootView, provideStatusBarColor())
            }

            // 设置 FakeStatusBar 的颜色
            mStatusBarDrawable!!.setColor(provideStatusBarColor())
            mStatusBarDrawable!!.invalidateSelf()

        } else {
            if (mStatusBarDrawable != null) {
                StatusBarUtil.removeStatusBarDrawableFromView(rootView)
                mStatusBarDrawable = null
            }
        }

        StatusBarUtil.setStatusBarLightMode(this, false)

    }

    open fun isSystemUiFullscreen(): Boolean {
        return false
    }

    open fun provideStatusBarColor(): Int {
        return 0
    }

}