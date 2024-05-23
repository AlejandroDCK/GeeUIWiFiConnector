package com.letianpai.robot.wificonnet.util;

import static android.content.Context.POWER_SERVICE;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by liujunbin
 */

public class FunctionUtils {

    public static String removeLastByte(String str) {
        if (str == null || str.isEmpty()) {
            return str; // 返回原始字符串，如果为空或长度为0
        }

        // 使用substring删除最后一个字节
        return str.substring(0, str.length() - 1);
    }


    public static String getTopActivityName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
        if (runningTasks != null && runningTasks.size() > 0) {
            ActivityManager.RunningTaskInfo taskInfo = runningTasks.get(0);
            ComponentName componentName = taskInfo.topActivity;
            if (componentName != null && componentName.getClassName() != null) {
                return componentName.getClassName();
            }
        }
        return null;
    }

    public static boolean isLauncherOnTheTop(Context context) {
        String activityName = getTopActivityName(context);
        if (activityName != null && activityName.contains(context.getPackageName())) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 关机
     *
     * @param context
     */
    public static void shutdownRobot(Context context) {
        if (!isLauncherOnTheTop(context)) {
            return;
        }

        PowerManager pm = (PowerManager) context.getSystemService(POWER_SERVICE);
        Class clazz = pm.getClass();
        try {
            Method shutdown = clazz.getMethod("shutdown", boolean.class, String.class, boolean.class);
            shutdown.invoke(pm, false, "shutdown", false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static String getRomVersion() {
        //判断ROM版本号
        String displayVersion = Build.DISPLAY;
        String localVersion = "";
        if (displayVersion.startsWith("GeeUITest")) {
            localVersion = displayVersion.replace("GeeUITest.", "");
        } else if (displayVersion.startsWith("GeeUI")) {
            localVersion = displayVersion.replace("GeeUI.", "");
        }
        if (localVersion.endsWith("d")) {
            localVersion = localVersion.replace(".d", "");
        } else {
            localVersion = localVersion.replace(".u", "");
        }
        return localVersion;
    }

    public static boolean compareVersion(String version1, String version2) {
        // 切割点 "."；
        String[] versionArray1 = version1.split("\\.");
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        // 取最小长度值
        int minLength = Math.min(versionArray1.length, versionArray2.length);
        int diff = 0;
        // 先比较长度 再比较字符
        while (idx < minLength && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {
            ++idx;
        }
        // 如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff > 0;
    }

    public static boolean isEven(int number) {
        return number % 2 == 0;
    }

    public static boolean isOdd(int number) {
        return !isEven(number);
    }

    public static String getVersionName(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }


}
