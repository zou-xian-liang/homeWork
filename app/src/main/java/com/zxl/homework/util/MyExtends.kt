package com.zxl.homework.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.zxl.homework.BuildConfig
import com.zxl.homework.global.MyApp

/**
 * 将dp转为px
 */
fun dp2px(dpValue: Float): Int {
    val scale = MyApp.context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * 日志打印，点击控制台可以跳转到指定日志打印出
 * @receiver Any
 * @param msg Any
 */
fun Any.loge(msg: Any, tag: String = "") {
    if (BuildConfig.DEBUG.not()) return
    val stringBuffer = StringBuffer()
    //打印栈帧中的所有方法调用
    try {
        val stackTrace = Thread.currentThread().stackTrace
        var className = stackTrace[3].fileName
        var methodName = stackTrace[3].methodName
        var lineNumber = stackTrace[3].lineNumber
        if (className == "MyExtends.kt") {//匿名内部类获取位置不一样
            className = stackTrace[4].fileName
            methodName = stackTrace[4].methodName
            lineNumber = stackTrace[4].lineNumber
        }
        //方法名左右的()不要去掉，去掉之后无法点击跳转
        stringBuffer.append("(").append(className).append(":").append(lineNumber).append(")-->")
    } catch (e: Exception) {
        Log.e("Frame+MyExtend.kt", "打印日志出现异常")
    }
    Log.e("GameClub-$tag", "$stringBuffer${msg.toString()}")
}

/**
 * 动态设置状态栏高度
 * @param statusBar 占位View
 */
fun setStatusBarHeight(statusBar: View, context: Activity) {
    val layoutParams = statusBar.layoutParams
    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
    layoutParams.height = getStateHeightPixels(context)
    statusBar.layoutParams = layoutParams
}

/**
 * 获取statusBar高度
 * @param activity
 */
fun getStateHeightPixels(activity: Activity): Int {
    var statusBarHeight = -1
    val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) statusBarHeight = activity.resources.getDimensionPixelSize(resourceId)
    return statusBarHeight
}

/**
 * 沉浸式系统UI，将系统UI(状态栏和导航栏)设为透明，并且主布局沉浸到系统UI下面。
 * @param [light] true-状态栏字体和图标为黑色，false-状态栏图标为白色
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun AppCompatActivity.immersiveSystemUi(light: Boolean = true) {
    window.run {
        val mode =
            if (light) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or mode
        statusBarColor = Color.TRANSPARENT
    }
}

