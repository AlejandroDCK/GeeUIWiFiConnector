package com.letianpai.robot.wificonnet.util

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import kotlin.math.min

/**
 * Created by liujunbin
 */
object FunctionUtils {
    @JvmStatic
    fun removeLastByte(str: String?): String? {
        if (str == null || str.isEmpty()) {
            return str // 返回原始字符串，如果为空或长度为0
        }

        // 使用substring删除最后一个字节
        return str.substring(0, str.length - 1)
    }


    fun getTopActivityName(context: Context): String? {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = am.getRunningTasks(1)
        if (runningTasks != null && runningTasks.size > 0) {
            val taskInfo = runningTasks[0]
            val componentName = taskInfo.topActivity
            if (componentName != null && componentName.className != null) {
                return componentName.className
            }
        }
        return null
    }

    fun isLauncherOnTheTop(context: Context): Boolean {
        val activityName = getTopActivityName(context)
        return if (activityName != null && activityName.contains(context.packageName)) {
            true
        } else {
            false
        }
    }

    /**
     * 关机
     *
     * @param context
     */
    fun shutdownRobot(context: Context) {
        if (!isLauncherOnTheTop(context)) {
            return
        }

        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val clazz: Class<*> = pm.javaClass
        try {
            val shutdown = clazz.getMethod(
                "shutdown",
                Boolean::class.javaPrimitiveType,
                String::class.java,
                Boolean::class.javaPrimitiveType
            )
            shutdown.invoke(pm, false, "shutdown", false)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    @JvmStatic
    val romVersion: String
        get() {
            //判断ROM版本号
            val displayVersion = Build.DISPLAY
            var localVersion = ""
            if (displayVersion.startsWith("GeeUITest")) {
                localVersion = displayVersion.replace("GeeUITest.", "")
            } else if (displayVersion.startsWith("GeeUI")) {
                localVersion = displayVersion.replace("GeeUI.", "")
            }
            localVersion = if (localVersion.endsWith("d")) {
                localVersion.replace(".d", "")
            } else {
                localVersion.replace(".u", "")
            }
            return localVersion
        }

    @JvmStatic
    fun compareVersion(version1: String, version2: String): Boolean {
        // 切割点 "."；
        val versionArray1 =
            version1.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val versionArray2 =
            version2.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var idx = 0
        // 取最小长度值
        val minLength = min(versionArray1.size.toDouble(), versionArray2.size.toDouble())
            .toInt()
        var diff = 0
        // 先比较长度 再比较字符
        while (idx < minLength && ((versionArray1[idx].length - versionArray2[idx].length).also {
                diff = it
            }) == 0 && (versionArray1[idx].compareTo(versionArray2[idx]).also { diff = it }) == 0) {
            ++idx
        }
        // 如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = if ((diff != 0)) diff else versionArray1.size - versionArray2.size
        return diff > 0
    }

    @JvmStatic
    fun isEven(number: Int): Boolean {
        return number % 2 == 0
    }

    fun isOdd(number: Int): Boolean {
        return !isEven(number)
    }

    fun getVersionName(context: Context, packageName: String?): String? {
        try {
            val pm = context.packageManager
            val packageInfo = pm.getPackageInfo(packageName!!, 0)
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            return null
        }
    }
}
