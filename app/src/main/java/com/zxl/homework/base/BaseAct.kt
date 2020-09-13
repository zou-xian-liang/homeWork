package com.zxl.homework.base

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zxl.homework.global.ActivityTask
import com.zxl.homework.global.MyApp
import com.zxl.homework.util.immersiveSystemUi

abstract class BaseAct<VM : BaseViewModel> : AppCompatActivity() {
    private lateinit var activity: Activity

    private val activityTask: ActivityTask = ActivityTask.instance

    protected open lateinit var mViewModel: VM

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        setCustomDensityDensity(this, MyApp.instance)
        super.onCreate(savedInstanceState)
        activity = this
        setContentView(setLayout())
        immersiveSystemUi(setStatusBar())
        activityTask.addActivity(activity)
        initViewModel()
        observe()
        initView()
        if (savedInstanceState == null) {
            initData()
        }
    }

    open fun setStatusBar(): Boolean = true
    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(viewModelClass())
    }

    protected abstract fun viewModelClass(): Class<VM>

    /**
     * 观察数据变化
     */
    open fun observe() {}

    protected abstract fun initView()

    /**
     * 设置布局
     * @return
     */
    protected abstract fun setLayout(): Int

    /**
     * 初始化相关的数据
     * 获取上个页面的数据需要在super调用之前获取
     */
    protected open fun initData() {}

    /**
     * Removes the current Activity from task stack
     */
    override fun finish() {
        super.finish()
        activityTask.removeActivity(activity)
    }

    /**
     * 监听返回键的点击事件
     * @param keyCode
     * @param event
     * @return
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                finish()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        private var sNoncompatDensity = 0f
        private var sNoncompatScaleDensity = 0f
        private fun setCustomDensityDensity(activity: Activity, application: Application) {
            val appDisplayMetrics = application.resources.displayMetrics
            if (sNoncompatDensity == 0f) {
                sNoncompatDensity = appDisplayMetrics.density
                sNoncompatScaleDensity = appDisplayMetrics.scaledDensity
                application.registerComponentCallbacks(object : ComponentCallbacks {
                    override fun onConfigurationChanged(newConfig: Configuration) {
                        if (newConfig != null && newConfig.fontScale > 0) {
                            sNoncompatScaleDensity =
                                application.resources.displayMetrics.scaledDensity
                        }
                    }

                    override fun onLowMemory() {}
                })
            }
            val targetDensity = appDisplayMetrics.widthPixels / 360.toFloat()
            //跟随系统字体大小变化
            val targetScaleDensity = targetDensity * (sNoncompatScaleDensity / sNoncompatDensity)
            val targetDensityDpi = (160 * targetDensity).toInt()
            appDisplayMetrics.density = targetDensity
            appDisplayMetrics.scaledDensity = targetDensity
            appDisplayMetrics.densityDpi = targetDensityDpi
            val activityDisplayMetrics = activity.resources.displayMetrics
            activityDisplayMetrics.density = targetDensity
            activityDisplayMetrics.scaledDensity = targetDensity
            activityDisplayMetrics.densityDpi = targetDensityDpi
        }
    }

}