package com.anonymouser.book

import android.graphics.Typeface
import android.util.Log


/**
 * @author zhangzhaowen @ Zhihu Inc.
 * @since 06-19-2018
 */
enum class CustomFont(s: String) {
    方正楷体("fonts/fangzhengkaiti.ttf"),
    方正行楷("fonts/fangzhengxingkai.ttf"),
    经典宋体("fonts/songti.ttf"),
    迷你隶书("fonts/mini_lishu.ttf"),
    方正黄草("fonts/fangzhenghuangcao.ttf"),
    书体安景臣钢笔行书("fonts/shuti_anjingchen_gangbixingshu.ttf");


    var path: String = "fonts/fangzhengkaiti.ttf"

    private fun Font(path: String) {
        this.path = path
    }

    operator fun get(var0: Int): CustomFont {
        return values()[var0]
    }

    fun fromString(string: String): CustomFont {
        return CustomFont.valueOf(string)
    }

    fun getTypeFace(): Typeface {
        return Typeface.createFromAsset(BookApp.mContext.assets, path)
    }
}