package com.letianpai.robot.wificonnet.view

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.letianpai.robot.components.network.nets.GeeUINetworkUtil
import com.letianpai.robot.components.network.nets.GeeUiNetManager
import com.letianpai.robot.components.network.system.SystemUtil
import com.letianpai.robot.wificonnet.MainActivity
import com.letianpai.robot.wificonnet.R
import com.letianpai.robot.wificonnet.adapter.CustomAdapter
import com.letianpai.robot.wificonnet.callback.KeyPressCallback
import com.letianpai.robot.wificonnet.callback.KeyPressCallback.KeyPressCallbackListener
import com.letianpai.robot.wificonnet.manager.WifiScanner
import com.letianpai.robot.wificonnet.parser.RobotBindInfo
import com.letianpai.robot.wificonnet.util.FunctionUtils.isEven
import com.letianpai.robot.wificonnet.util.FunctionUtils.removeLastByte
import com.letianpai.robot.wificonnet.util.HardCodeUtils.getDeviceSign
import com.letianpai.robot.wificonnet.util.KeyConsts
import com.letianpai.robot.wificonnet.util.KeysMapUtils.Companion.getInstance
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference
import kotlin.collections.HashMap
import kotlin.collections.set

/**
 * @author liujunbin
 */
class PairingCodeKeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private var mContext: Context? = null
    private var kbLk1: KeyImageButton? = null
    private var kbLk2: KeyImageButton? = null
    private var kbRk1: KeyImageButton? = null
    private var kbRk2: KeyImageButton? = null
    private var kbBk1: KeyImageButton? = null
    private var kbBk2: KeyImageButton? = null
    private var glKeys: RecyclerView? = null
    private lateinit var recyclerView: RecyclerView
    private var adapter: CustomAdapter? = null
    private lateinit var wifiPassword: TextView
    private var robotName: TextView? = null
    private var ivWifiCommit: CommitButton? = null
    private var mUpdateViewHandler: UpdateViewHandler? = null
    private val alertDialog: AlertDialog? = null
    private var contentPassword: String? = ""
    private val currentKeypadPosition = 0
    private val wifiScanner: WifiScanner? = null
    private val mWifiName: String? = null
    private var bindRobotCallback: Callback? = null


    init {
        this.mContext = context
        initView()
    }

    private fun initView() {
        inflate(mContext, R.layout.keyboard_pair, this)
        initViews()
        initCallback()
        initFunctionButtonData()
        setData()
        addKeyPressedListener()
    }

    private fun initCallback() {
        bindRobotCallback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                Log.e("letianpai", "bindRobotCallback ========== 0 =========")
                if (response.body != null) {
                    var info = ""
                    if (response.body != null) {
                        info = response.body!!.string()
                    }
                    val robotBindInfo: RobotBindInfo?
                    if (info != null) {
                        try {
                            Log.e("letianpai", "bindRobotCallback$info")
                            robotBindInfo = Gson().fromJson(info, RobotBindInfo::class.java)
                            if (robotBindInfo != null && robotBindInfo.code == 0 && robotBindInfo.data != null && !TextUtils.isEmpty(
                                    robotBindInfo.data!!.country
                                )
                            ) {
                                (mContext as MainActivity?)!!.updateView(robotBindInfo.data!!.country)
                            } else {
                                cleanWifiPassword()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    private fun initFunctionButtonData() {
        kbLk1!!.setEnable(false)
        kbLk1!!.setImagePosition(KeyConsts.CENTER_VERTICAL, 30, 20, 16, 16, 36, 36)
        kbLk1!!.setUnPressedImage(R.drawable.keypad_icon)


        kbRk2!!.setImagePosition(KeyConsts.LEFT_TOP, 12, 17, 0, 0, 36, 36)
        kbRk2!!.setPressedImage(R.drawable.fun_delete_pressed)
        kbRk2!!.setUnPressedImage(R.drawable.fun_delete_unpressed)
    }

    private fun addKeyPressedListener() {
        KeyPressCallback.instance.setKeyPressCallbackListener(object : KeyPressCallbackListener {
            override fun onKeyPressed(keyType: Int, keyValue: String?) {
                if ((mContext as MainActivity?)!!.isPairCodeViewVisible) {
                    updateWifiPassword(keyType, keyValue)
                }
            }
        })
        kbLk1!!.setOnClickListener { }
        kbLk2!!.setOnClickListener { }
        kbRk1!!.setOnClickListener {
            //                responseSwitch();
        }
        kbRk2!!.setOnClickListener { responseDelete() }
        kbBk1!!.setOnClickListener { }
        kbBk2!!.setOnClickListener { }

        ivWifiCommit!!.setOnClickListener {
            Log.e("letianpai", "ivWifiCommit ======= ")
            //                if (TextUtils.isEmpty(mWifiName)){
//                    Toast.makeText(mContext,"Wi-Fi未设置",Toast.LENGTH_LONG).show();
//                }else{
//                    connectWifi();
//                }
            bindRobotWithCode()
        }
    }


    private fun responseDelete() {
        contentPassword = removeLastByte(contentPassword)
        wifiPassword!!.text = contentPassword
    }

    private fun setData() {
        adapter = CustomAdapter(getInstance(mContext!!)!!.numericPairingCodeKeyList)
        recyclerView.adapter = adapter
        try {
            robotName!!.text = "Robot-" + SystemUtil.getLtpLastSn()
        } catch (e: SecurityException) {
            Log.e("PairingCodeKeyboardView", "Failed to get device serial number: ${e.message}")
            robotName!!.text = "Robot-Unknown" // Fallback name
        }
    }


    private fun initViews() {
        robotName = findViewById(R.id.tv_robot_name)
        ivWifiCommit = findViewById(R.id.iv_wifi_commit)
        wifiPassword = findViewById(R.id.wifi_password)
        wifiPassword.setFocusableInTouchMode(false) // 设置为不可触摸模式
        wifiPassword.setFocusable(true)
        wifiPassword.setCursorVisible(true)

        glKeys = findViewById(R.id.recyclerView)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setLayoutManager(GridLayoutManager(mContext, 4))
        mUpdateViewHandler = mContext?.let { UpdateViewHandler(it) }

        kbLk1 = findViewById(R.id.lf1)
        kbLk2 = findViewById(R.id.lf2)
        kbRk1 = findViewById(R.id.rf1)
        kbRk2 = findViewById(R.id.rf2)
        kbBk1 = findViewById(R.id.bf1)
        kbBk2 = findViewById(R.id.bf2)
    }

    fun cleanPassword() {
        contentPassword = ""
        wifiPassword!!.text = contentPassword
    }

    private inner class UpdateViewHandler(context: Context) : Handler() {
        private val context = WeakReference(context)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                UPDATE_WIFI_PASSWORD -> updateWifiPassword(msg)
                CLEAN_WIFI_PASSWORD -> cleanPassword()
            }
        }
    }

    private fun updateWifiPassword(msg: Message) {
        if (msg.arg1 == KeyPressCallback.KEY_TYPE_VALUE) {
            contentPassword = contentPassword + msg.obj
        }
        wifiPassword!!.isPressed = true
        wifiPassword!!.text = contentPassword
    }

    fun updateWifiPassword(keyPressType: Int, updateContent: String?) {
        val message = Message()
        message.arg1 = keyPressType
        message.obj = updateContent
        message.what = UPDATE_WIFI_PASSWORD
        mUpdateViewHandler!!.sendMessage(message)
    }

    fun cleanWifiPassword() {
        val message = Message()
        message.what = CLEAN_WIFI_PASSWORD
        mUpdateViewHandler!!.sendMessage(message)
    }


    private fun bindRobot(code: String?, callback: Callback?) {
        Log.e("letianpai", "bindRobotWithCode ========== 04 =========contentPassword: ")
        val country = getCountry(code)
        val mac = SystemUtil.getWlanMacAddress()
        val ts = (System.currentTimeMillis() / 1000).toString() + ""
        val sn = SystemUtil.getLtpSn()
        val deviceSign = getDeviceSign(mac, ts)
        //TODO 增加获取
        val hashMap: HashMap<String?, Any?> = HashMap()

        hashMap["code"] = code
        hashMap[GeeUINetworkUtil.COUNTRY] = country
        hashMap["device_sign"] = deviceSign
        hashMap["hard_code"] = ""
        hashMap["mac"] = mac
        hashMap["sn"] = sn
        hashMap["ts"] = ts

        GeeUiNetManager.bindRobot(mContext, isChinese(code), hashMap, callback)
    }

    private fun bindRobotWithCode() {
        Log.e("letianpai", "bindRobotWithCode ========== 01 =========")
        Log.e(
            "letianpai",
            "bindRobotWithCode ========== 01 =========contentPassword: $contentPassword"
        )
        if (TextUtils.isEmpty(contentPassword)) {
            Log.e(
                "letianpai",
                "bindRobotWithCode ========== 02 =========contentPassword: $contentPassword"
            )
            Toast.makeText(mContext, R.string.there_is_pair_code, Toast.LENGTH_LONG).show()
            return
        }
        Thread {
            Log.e(
                "letianpai",
                "bindRobotWithCode ========== 03 =========contentPassword: $contentPassword"
            )
            bindRobot(contentPassword, bindRobotCallback)
        }.start()
    }

    private fun getCountry(code: String?): String {
        val startCode = code!!.substring(0, 1).toInt()
        return if (isEven(startCode)) {
            GeeUINetworkUtil.GLOBAL
        } else {
            GeeUINetworkUtil.CN
        }
    }

    private fun isChinese(code: String?): Boolean {
        return if (getCountry(code) == GeeUINetworkUtil.CN) {
            true
        } else {
            false
        }
    }

    companion object {
        const val UPDATE_WIFI_PASSWORD: Int = 102
        const val CLEAN_WIFI_PASSWORD: Int = 103
    }
}
