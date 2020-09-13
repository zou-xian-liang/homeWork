package com.zxl.homework.base

import android.app.Activity
import java.util.*

/**
 * Activity 管理类
 */
class ActivityTask {
    /**
     * 将当天activity添加至栈中
     * @param activity
     */
    fun addActivity(activity: Activity) {
        taskActivity.add(activity)
    }

    /**
     * 将activity移出栈
     * @param activity
     */
    fun removeActivity(activity: Activity) {
        taskActivity.remove(activity)
    }

    fun clearActivity() {
        while (taskActivity.size > 0) {
            taskActivity[taskActivity.size - 1]
                .finish()
        }
    }

    /**
     * 通过类的名字来移除任务栈中的Activity
     * @param classSimpleName   类的名字
     */
    fun removeActivityByName(classSimpleName: String) {
        for (i in taskActivity.indices.reversed()) {
            if (classSimpleName == taskActivity[i].javaClass.simpleName) {
                taskActivity[i].finish()
            }
        }
    }

    companion object {
        /**
         * Load all showed that the Activity of collection
         */
        var taskActivity: MutableList<Activity> = ArrayList()

        /**
         * @return ActivityTask
         */
        var instance = ActivityTask()

    }
}