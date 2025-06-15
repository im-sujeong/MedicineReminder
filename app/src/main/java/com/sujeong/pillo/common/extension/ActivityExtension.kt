package com.sujeong.pillo.common.extension

import android.app.Activity
import android.app.ActivityManager
import android.content.Context

fun Activity.isActivityOnTop(className: String): Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val runningTask = activityManager.getRunningTasks(1)
    return runningTask.firstOrNull()?.topActivity?.className == className
}