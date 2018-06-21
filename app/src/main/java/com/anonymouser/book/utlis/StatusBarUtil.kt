package com.anonymouser.book.utlis

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.support.annotation.IntRange
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager


/**
 * @author zhangzhaowen @ Zhihu Inc.
 * @since 06-20-2018
 */
class StatusBarUtil {
    companion object {


        /**
         * 显示 StatusBar
         */
        fun showStatusBar(activity: Activity) {
            val window = activity.window
            val attrs = window.attributes
            attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
            window.attributes = attrs
        }

        /**
         * 隐藏 StatusBar
         */
        fun hideStatusBar(activity: Activity) {
            val window = activity.window
            val attrs = window.attributes
            attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
            window.attributes = attrs
        }

        /**
         * 获取状态栏高度
         */
        fun getStatusBarHeight(context: Context): Int {
            val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            return context.resources.getDimensionPixelSize(resourceId)
        }

        /**
         * 添加 FakeStatusBar Drawable 到 View 的背景中
         */
        fun addStatusBarDrawableToView(rootView: View, color: Int): StatusBarDrawable {
            val backgroud = rootView.background

            val context = rootView.context
            val statusBarDrawable = StatusBarDrawable(color, getStatusBarHeight(context))

            rootView.background = CombinedDrawable(backgroud, statusBarDrawable)
            rootView.setPadding(
                    rootView.paddingLeft,
                    rootView.paddingTop + getStatusBarHeight(context),
                    rootView.paddingRight,
                    rootView.paddingBottom)

            return statusBarDrawable

        }

        /**
         * 从 View 的背景中清除 FakeStatusBar Drawable
         */
        fun removeStatusBarDrawableFromView(rootView: View) {
            val background = rootView.background

            if (background is CombinedDrawable) {
                rootView.background = (background as CombinedDrawable).origin

                rootView.setPadding(
                        rootView.paddingLeft,
                        rootView.paddingTop - getStatusBarHeight(rootView.context),
                        rootView.paddingRight,
                        rootView.paddingBottom)
            }
        }


        fun translucentStatusBar(activity: Activity) {
            var window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 5.x
                // StatusBar 去除颜色
                cleanStatusBarBackgroundColor(activity)
            } else {
                // Content 填充 & StatusBar 颜色叠加
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }

            // 设置 FitsSystemWindow 为 False，不预留 StatusBar 位置
            val contentView: ViewGroup = activity.window.findViewById(Window.ID_ANDROID_CONTENT) as ViewGroup
            val contentChild = contentView.getChildAt(0)
            contentChild.fitsSystemWindows = false
        }


        /**
         * 去除 StatusBar 的颜色
         */
        fun cleanStatusBarBackgroundColor(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 5.x
                val window = activity.window
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                activity.window.statusBarColor = Color.TRANSPARENT
            }
        }

        /**
         * 设置 StatusBar 的文本颜色为黑色
         * @link [](http://www.jianshu.com/p/932568ed31af)
         */
        fun setStatusBarLightMode(activity: Activity, lightmode: Boolean) {
            // 尝试将小米或魅族手机的状态栏文字改为黑色
            setMIUIStatusBarLightMode(activity, lightmode)
            setFlymeStatusBarLightMode(activity, lightmode)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
                val view = activity.window.decorView
                var flag = view.systemUiVisibility
                if (lightmode) {
                    flag = flag or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    flag = flag and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
                view.systemUiVisibility = flag
            }
        }

        private fun setMIUIStatusBarLightMode(activity: Activity, lightmode: Boolean): Boolean {
            var result = false
            val clazz = activity.window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                extraFlagField.invoke(activity.window, if (lightmode) darkModeFlag else 0, darkModeFlag)
                result = true
            } catch (ignored: Exception) {

            }

            return result
        }

        /**
         * Flyme 系统中设置 StatusBar 的文本颜色为黑色
         */
        private fun setFlymeStatusBarLightMode(activity: Activity, lightmode: Boolean): Boolean {
            var result = false
            try {
                val lp = activity.window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                        .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                if (lightmode) {
                    value = value or bit
                } else {
                    value = value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                activity.window.attributes = lp
                result = true
            } catch (ignored: Exception) {

            }

            return result
        }

    }

    /**
     * FakeStatusBar Drawable
     */
    class StatusBarDrawable(color: Int, private val height1: Int) : Drawable() {
        private val paint: Paint
        private var height: Int

        init {
            this.paint = Paint()
            this.paint.color = color
            this.height = height1
        }

        fun setColor(color: Int) {
            paint.color = color
        }

        override fun draw(canvas: Canvas) {
            canvas.drawRect(0f, 0f, canvas.width.toFloat(), height.toFloat(), paint)
        }

        override fun setAlpha(@IntRange(from = 0, to = 255) i: Int) {
            paint.alpha = i
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
            paint.colorFilter = colorFilter
        }

        override fun getOpacity(): Int {
            return PixelFormat.TRANSLUCENT
        }
    }

    /**
     * 用于组合原背景和 FakeStatusBar Drawable
     */
    class CombinedDrawable internal constructor(public val origin: Drawable?, statusBarDrawable: StatusBarDrawable) : LayerDrawable(if (origin == null) arrayOf<Drawable>(statusBarDrawable) else arrayOf(origin, statusBarDrawable))

}