package com.zxl.homework.global

import android.app.Application
import android.content.Context
import android.os.Handler

/**
 * Created by zxl on 2020/9/11 9:59
 * 版本：v2.1.0 [2020年8月17日]
 * 说明：
 */
class MyApp : Application() {

    companion object {
        lateinit var context: Context
        lateinit var handler: Handler
        lateinit var instance: MyApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = this
        handler = Handler()
    }
}