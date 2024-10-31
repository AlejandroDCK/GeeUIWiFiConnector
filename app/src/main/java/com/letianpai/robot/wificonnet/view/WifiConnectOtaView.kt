package com.letianpai.robot.wificonnet.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import com.google.gson.Gson
import com.letianpai.robot.components.network.nets.GeeUiNetManager
import com.letianpai.robot.components.network.system.SystemUtil
import com.letianpai.robot.wificonnet.MainActivity
import com.letianpai.robot.wificonnet.R
import com.letianpai.robot.wificonnet.parser.CountryInfo
import com.letianpai.robot.wificonnet.parser.VersionInfo
import com.letianpai.robot.wificonnet.system.SystemUtil.set
import com.letianpai.robot.wificonnet.util.FunctionUtils.compareVersion
import com.letianpai.robot.wificonnet.util.FunctionUtils.romVersion
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference

/**
 * @author liujunbin
 */
class WifiConnectOtaView : RelativeLayout {
    private var mContext: Context? = null
    private var wifiConnectorHandler: WifiConnectorHandler? = null
    private var rotateAnimation: RotateAnimation? = null
    private var isStartOTA = false
    private val countryInfo: String? = null
    private var requestTime = 0

    private lateinit var otaLoading: ImageView
    private var hasRequestOTA = false

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    private fun init(context: Context) {
        this.mContext = context
        inflate(context, R.layout.wifi_connect_ota_view, this)
        initView()
        addListeners()
    }

    fun startRequestOTA() {
        Log.e("letianpai_ota", "startRequestOTA  ============= 1 =========== ")
        if (isStartOTA) {
            Log.e("letianpai_ota", "startRequestOTA ============= 2 ===========")
            return
        }
        Log.e("letianpai_ota", "startRequestOTA ============= 3 ===========")

        isStartOTA = true
        startAnimation()
        requestCountryInfo()
        //requestOTA();
    }

    private fun skipOTA() {
        Log.e("letianpai_ota", "skipOTA ============= 4 =========== ")
        val message = Message()
        message.what = SKIP_OTA
        wifiConnectorHandler!!.sendMessage(message)
    }

    private fun startAnimation() {
        Log.e("letianpai_ota", "startAnimation ============= 5 ===========")
        val message = Message()
        message.what = ANIMATION_START
        wifiConnectorHandler!!.sendMessage(message)
    }

    private fun requestOTA() {
        Log.e("letianpai_ota", "requestOTA ============= 6 ===========")
        val message = Message()
        message.what = REQUEST_OTA
        //        wifiConnectorHandler.sendMessageDelayed(message, 8000);
        wifiConnectorHandler!!.sendMessageDelayed(message, 2000)
    }

    private fun requestCountryInfo() {
        Log.e("letianpai_ota", "requestCountryInfo ============= 7 ===========")
        requestTime += 1
        val message = Message()
        message.what = REQUEST_COUNTRY_INFO
        wifiConnectorHandler!!.sendMessageDelayed(message, 8000)
    }

    private fun requestCountryInfo2000() {
        Log.e("letianpai_ota", "requestCountryInfo2000 ============= 8 ===========")
        if (requestTime > 5) {
            Log.e("letianpai_ota", "info----------_2.12")
            skipOTA()
            return
        }
        Log.e("letianpai_ota", "requestCountryInfo2000 ============= 9 ===========")
        requestTime += 1
        val message = Message()
        message.what = REQUEST_COUNTRY_INFO
        wifiConnectorHandler!!.sendMessageDelayed(message, 2000)
    }

    private fun startOTA() {
        Log.e("letianpai_ota", "requestCountryInfo2000 ============= 10 ===========")
        val message = Message()
        message.what = START_OTA
        wifiConnectorHandler!!.sendMessage(message)
    }

    private fun setLocalCountryInfo(countryInfo: String?) {
        Log.e("letianpai_ota", "setLocalCountryInfo ============= 11 ===========")
        val message = Message()
        message.what = RESPONSE_COUNTRY_INFO
        message.obj = countryInfo
        wifiConnectorHandler!!.sendMessage(message)
    }

    private fun responseSkipOTA() {
        //TODO 在这增加请求绑定状态的逻辑
//        ((MainActivity)(mContext)).skipToQRCodeView();
        Log.e("letianpai_skip", "===================== 1 ======================")
        (mContext as MainActivity?)!!.bindStatusInfoOrSkipToMain
    }

    private fun responseRequestOTA() {
        val localRomVersion = romVersion
        GeeUiNetManager.getOTAInfo(mContext, SystemUtil.isInChinese, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("letianpai_ota", "info----------_3:_____ onFailure ")
                e.printStackTrace()
                skipOTA()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.body != null) {
                    var info = ""

                    info = response.body!!.string()
                    if (!TextUtils.isEmpty(info)) {
                        Log.e("letianpai_ota", "info----------_2: $info")
                    }

                    val versionInfo: VersionInfo?



                    try {
                        Log.e("letianpai_ota", "info----------_2.1-exception: ")
                        if (info != null) {
                            Log.e("letianpai_ota", "info----------_2.2-exception: ")
                            versionInfo = Gson().fromJson(info, VersionInfo::class.java)
                            Log.e(
                                "letianpai_ota",
                                "info----------_2.3-exception: versionInfo:$versionInfo"
                            )
                            if (versionInfo?.data != null && versionInfo.code == 0) {
                                Log.e(
                                    "letianpai_ota",
                                    "info----------_2.4-exception: versionInfo.getData(): " + versionInfo.data.toString()
                                )
                                val onlineVersion = versionInfo.data!!.rom_version
                                Log.e(
                                    "letianpai_ota",
                                    "info----------_2.5-exception: onlineVersion: $onlineVersion"
                                )
                                val isStartOTA = compareVersion(onlineVersion!!, localRomVersion)
                                Log.e(
                                    "letianpai_ota",
                                    "info----------_2.6-exception: isStartOTA: $isStartOTA"
                                )
                                if (isStartOTA) {
                                    Log.e("letianpai_ota", "info----------_2.7-exception: ")
                                    startOTA()
                                } else {
                                    Log.e("letianpai_ota", "info----------_2.8-exception: ")
                                    skipOTA()
                                }
                            } else {
                                Log.e("letianpai_ota", "info----------_2.9")
                                skipOTA()
                            }
                        } else {
                            Log.e("letianpai_ota", "info----------_2.10")
                            skipOTA()
                        }
                    } catch (e: Exception) {
                        Log.e("letianpai_ota", "info----------_2.11")
                        e.printStackTrace()
                        skipOTA()
                    }
                } else {
                    Log.e("letianpai_ota", "info----------_2.12")
                    skipOTA()
                }
            }
        })
    }

    private fun responseStartOTA() {
//        Log.e("letianpai_ota","responseStartOTA(): ================= responseStartOTA() ====================");
        (mContext as MainActivity?)!!.removeCloseRobot()
        val intent = Intent()
        val cn = ComponentName(
            "com.letianpai.otaservice",
            "com.letianpai.otaservice.ota.GeeUpdateService"
        )
        intent.setComponent(cn)
        mContext!!.startService(intent)
    }

    private fun addListeners() {
    }

    private fun initView() {
        wifiConnectorHandler = WifiConnectorHandler(mContext)
        otaLoading = findViewById(R.id.iv_ota_loading)
        rotateAnimation = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotateAnimation!!.duration = 3000
        rotateAnimation!!.interpolator = LinearInterpolator()
        rotateAnimation!!.repeatCount = Animation.INFINITE
        otaLoading.startAnimation(rotateAnimation)
    }


    private inner class WifiConnectorHandler(context: Context?) : Handler() {
        private val context = WeakReference(context)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                REQUEST_OTA -> if (!hasRequestOTA) {
                    hasRequestOTA = true
                    responseRequestOTA()
                }

                SKIP_OTA -> responseSkipOTA()
                START_OTA -> responseStartOTA()
                REQUEST_OTA_FAIL -> requestOtaFailed()
                ANIMATION_START -> startOtaAnimation()
                ANIMATION_STOP -> stopOtaAnimation()
                REQUEST_COUNTRY_INFO -> countryInfoByIP
                RESPONSE_COUNTRY_INFO -> responseSetLocalCountryInfo(msg.obj as String)
            }
        }
    }


    val countryInfoByIP: Unit
        get() {
//        Log.e("letianpai_ota", "commandDistribute:command ========= 3333 ======== info: ");
            GeeUiNetManager.getCountryByIp(mContext, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    requestCountryInfo2000()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    if (response.body != null) {
                        var info = ""
                        if (response.body != null) {
                            info = response.body!!.string()
                            //                        Log.e("letianpai_ota", "getCountryInfoByIP_info: "+ info);
                            val countryInfo: CountryInfo?
                            try {
                                if (info != null) {
//                                Log.e("letianpai_ota", "getCountryInfoByIP_info: ========= 1 ====== ");
                                    countryInfo = Gson().fromJson(info, CountryInfo::class.java)
                                    if (countryInfo != null && countryInfo.code == 0 && countryInfo.data != null) {
//                                    Log.e("letianpai_ota", "getCountryInfoByIP_info: ========= 2 ====== ");
                                        if (!TextUtils.isEmpty(countryInfo.data!!.country)) {
//                                        Log.e("letianpai_ota", "getCountryInfoByIP_info: ========= 3 ====== ");
                                            setLocalCountryInfo(countryInfo.data!!.country)
                                            //                                        requestOTA();
                                        }
                                    } else {
//                                    requestCountryInfo2000();
                                        skipOTA()
                                    }
                                } else {
                                    skipOTA()
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                skipOTA()
                            }
                        }
                    }
                }
            })
        }


    private fun responseSetLocalCountryInfo(countryInfo: String) {
        if (countryInfo == GLOBAL) {
            set(com.letianpai.robot.wificonnet.system.SystemUtil.REGION_LANGUAGE, "en")
        } else {
            set(com.letianpai.robot.wificonnet.system.SystemUtil.REGION_LANGUAGE, "zh")
        }
        requestOTA()
    }

    private fun stopOtaAnimation() {
        rotateAnimation!!.cancel()
    }

    private fun startOtaAnimation() {
        rotateAnimation!!.start()
    }

    private fun requestOtaFailed() {
    }

    companion object {
        private const val REQUEST_OTA = 1
        private const val SKIP_OTA = 2
        private const val START_OTA = 3
        private const val REQUEST_OTA_FAIL = 4
        private const val ANIMATION_START = 5
        private const val ANIMATION_STOP = 6
        private const val REQUEST_COUNTRY_INFO = 7
        private const val RESPONSE_COUNTRY_INFO = 8
        private const val GLOBAL = "global"
    }
}
