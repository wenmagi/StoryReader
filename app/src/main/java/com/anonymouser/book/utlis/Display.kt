package com.anonymouser.book.widget

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.anonymouser.book.BookApp
import android.opengl.ETC1.getHeight



/**
 * Created by YandZD on 2017/7/7.
 */
object Display {
    private var tempWidth = 0
    private var tempHeight = 0
    private var tempDensity = 0f

    var mWidth = 0
        get() {
            getCalWidthAndHeight()
            return tempWidth
        }

    var mHeight = 0
        get() {
            getCalWidthAndHeight()
            return tempHeight
        }
    var mDensity = 0f
        get() {
            getCalWidthAndHeight()
            return tempDensity
        }

    //获取屏幕的宽高
    private fun getCalWidthAndHeight() {
        if (tempWidth == 0) {
            val windowManager = BookApp.mContext?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)


            tempWidth = metrics.widthPixels
            tempHeight = metrics.heightPixels
            tempDensity = metrics.density
        }
    }

}