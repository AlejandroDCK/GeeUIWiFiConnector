package com.letianpai.robot.wificonnet.system

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.net.NetworkInterface
import java.net.SocketException
import java.util.Locale

/**
 * @author liujunbin
 */
object SystemUtil {
    private var sysPropGet: Method? = null
    private var sysPropGetInt: Method? = null
    private var sysPropSet: Method? = null
    private const val SN = "ro.serialno"
    private const val MCU_VERSION = "persist.sys.mcu.version"
    const val HARD_CODE: String = "persist.sys.hardcode"
    const val DEVICE_SIGN: String = "persist.sys.device.sign"
    private const val ROBOT_STATUS = "persist.sys.robot.status"
    const val REGION_LANGUAGE: String = "persist.sys.region.language"
    var wifiPermissionsGranted = false

    private const val ROBOT_STATUS_TRUE = "true"
    const val REGION_LANGUAGE_ZH: String = "zh"
    const val REGION_LANGUAGE_EN: String = "en"


    init {
        try {
            val S = Class.forName("android.os.SystemProperties")
            val M = S.methods
            for (m in M) {
                val n = m.name
                if (n == "get") {
                    sysPropGet = m
                } else if (n == "getInt") {
                    sysPropGetInt = m
                } else if (n == "set") {
                    sysPropSet = m
                }
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    fun get(name: String?, default_value: String?): String? {
        try {
            return sysPropGet!!.invoke(null, name, default_value) as String
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return default_value
    }

    @JvmStatic
    fun set(name: String?, value: String?) {
        try {
            sysPropSet!!.invoke(null, name, value)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }

    fun setRobotActivated() {
        set(ROBOT_STATUS, ROBOT_STATUS_TRUE)
    }

    val robotActivateStatus: Boolean
        get() {
            val status = get(ROBOT_STATUS, null)
            if (status == ROBOT_STATUS_TRUE) {
                return true
            }
            return false
        }

    @JvmStatic
    val ltpSn: String
        get() = Build.getSerial()

    @JvmStatic
    val ltpLastSn: String?
        get() {
            val sn = ltpSn
            return if (TextUtils.isEmpty(sn)) {
                null
            } else {
                sn.substring(sn.length - 4)
            }
        }

    var hardCode: String?
        get() = get(HARD_CODE, null)
        set(hardCode) {
            set(HARD_CODE, hardCode)
        }

    fun hasHardCode(): Boolean {
        if (TextUtils.isEmpty(hardCode)) {
            return false
        }
        return true
    }

    var deviceSign: String?
        get() = get(DEVICE_SIGN, null)
        set(deviceSign) {
            set(DEVICE_SIGN, deviceSign)
        }

    val mcu: String?
        get() = get(MCU_VERSION, null)

    fun hadDeviceSign(): Boolean {
        if (TextUtils.isEmpty(deviceSign)) {
            return false
        }
        return true
    }


    @JvmStatic
    val wlanMacAddress: String?
        get() {
            try {
                val networkInterfaces = NetworkInterface.getNetworkInterfaces()
                while (networkInterfaces.hasMoreElements()) {
                    val networkInterface = networkInterfaces.nextElement()
                    if (networkInterface.name == "wlan0") {
                        val mac = StringBuilder()
                        val hardwareAddress = networkInterface.hardwareAddress
                        var hex = Integer.toHexString(hardwareAddress[0].toInt() and 0xff)
                        if (hex.length == 1) {
                            mac.append('0')
                        }
                        mac.append(hex)
                        for (i in 1 until hardwareAddress.size) {
                            mac.append(':')
                            hex = Integer.toHexString(hardwareAddress[i].toInt() and 0xff)
                            if (hex.length == 1) {
                                mac.append('0')
                            }
                            mac.append(hex)
                        }
                        return mac.toString()
                    }
                }
            } catch (ex: SocketException) {
                Log.i("", ex.message!!)
            }
            return null
        }

    fun getIp(context: Context): String? {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wifiManager != null) {
            val wifiInfo = wifiManager.connectionInfo
            val ip = wifiInfo.ipAddress

            val ipAddress = String.format(
                "%d.%d.%d.%d",
                (ip and 0xff),
                (ip shr 8 and 0xff),
                (ip shr 16 and 0xff),
                (ip shr 24 and 0xff)
            )
            return ipAddress
        }
        return null
    }

    val robotStatus: Boolean
        get() {
            val status = get(ROBOT_STATUS, null)
            if (status == ROBOT_STATUS_TRUE) {
                return true
            }
            return false
        }


    val robotInChineseStatus: String?
        get() {
            val pro = get(REGION_LANGUAGE, "zh")
            return pro
        }

    val isInChinese: Boolean
        get() {
            val pro = get(REGION_LANGUAGE, "zh")
            return if ("zh" == pro) {
                true
            } else {
                false
            }
            //        return false;
        }

    val language: String?
        get() = get(REGION_LANGUAGE, REGION_LANGUAGE_ZH)

    val isChineseLanguage: Boolean
        get() = if (language == REGION_LANGUAGE_ZH) {
            true
        } else {
            false
        }


    /**
     * @param context
     * @param language
     */
    fun setDefaultLanguage(context: Context, language: String) {
        if (TextUtils.isEmpty(language)) {
            return
        }

        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        val metrics = context.resources.displayMetrics

        var loc = Locale.CHINA
        if (language == REGION_LANGUAGE_EN) {
            loc = Locale.ENGLISH
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(loc)
        } else {
            configuration.locale = loc
        }

        context.resources.updateConfiguration(configuration, metrics)
    }

    /**
     * @param context
     */
    fun setAppLanguage(context: Context) {
        if (isInChinese) {
            setDefaultLanguage(context, "zh")
        } else {
            setDefaultLanguage(context, REGION_LANGUAGE_EN)
        }
    }

    fun checkWifiPermissions(activity: Activity) {
        val requiredPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
        )

        val missingPermissions = requiredPermissions.filter { permission ->
            ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, missingPermissions.toTypedArray(), 1)
        }
        wifiPermissionsGranted = true
    }
}
