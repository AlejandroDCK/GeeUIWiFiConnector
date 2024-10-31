package com.letianpai.robot.wificonnet

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.letianpai.robot.components.network.nets.GeeUiNetManager
import com.letianpai.robot.components.storage.RobotSubConfigManager
import com.letianpai.robot.wificonnet.callback.BleConnectStatusCallback
import com.letianpai.robot.wificonnet.parser.BindStatusInfo
import com.letianpai.robot.wificonnet.system.SystemUtil
import com.letianpai.robot.wificonnet.util.FunctionUtils
import com.letianpai.robot.wificonnet.view.AutoConnectWifiView
import com.letianpai.robot.wificonnet.view.KeyBoardView
import com.letianpai.robot.wificonnet.view.PairingCodeKeyboardView
import com.letianpai.robot.wificonnet.view.PairingInfoView
import com.letianpai.robot.wificonnet.view.TwelveGridKeyboardView
import com.letianpai.robot.wificonnet.view.WifiConnectOtaView
import com.letianpai.robot.wificonnet.wifi.WIFIStateReceiver
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.Locale


/**
 * @author liujunbin
 */
class MainActivity : Activity() {
    private val keyBoardView: KeyBoardView? = null
    private var twelveGridKeyboardView: TwelveGridKeyboardView? = null

    //    private QRCodeView qrCodeView;
    private var autoConnectWifiView: AutoConnectWifiView? = null
    private var wifiConnectOtaView: WifiConnectOtaView? = null
    private var pairingCodeKeyboardView: PairingCodeKeyboardView? = null
    private var pairingInfoView: PairingInfoView? = null
    private var updateViewHandler: UpdateViewHandler? = null
    private var isGetValidBindInfo = false
    private var isGetRobotBindStatus = false
    private var openAppFrom: String? = ""
    @JvmField
    var wifiName: String? = null
    private var hadRequestOta = false
    private var hadSkipToQrCodeView = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerWIFIStateReceiver()
        openAppFrom = intent.getStringExtra(OPEN_FROM)
        setContentView(R.layout.activity_main)
        SystemUtil.setAppLanguage(this@MainActivity)
        updateViewHandler = UpdateViewHandler(this@MainActivity)
        val decorView = window.decorView
        val uiOptions =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        initViews()
        addListeners()
        SystemUtil.checkWifiPermissions(this)
        if (isFromOpenRobot) {
            closeRobot()
        }
    }

    private fun initViews() {
        //        keyBoardView = findViewById(R.id.keyboard);
        twelveGridKeyboardView = findViewById(R.id.twelveGridKeyboardView)
        if (!RobotSubConfigManager.getInstance(this@MainActivity)!!.isNeedRegisterWifi) {
            twelveGridKeyboardView?.visibility = View.GONE
        }
        autoConnectWifiView = findViewById(R.id.autoConnectWifiView)
        //        qrCodeView = findViewById(R.id.qrCodeView);
        wifiConnectOtaView = findViewById(R.id.wifiConnectOtaView)
        pairingCodeKeyboardView = findViewById(R.id.pairingCodeKeyboardView)
        pairingInfoView = findViewById(R.id.pairingInfoView)
    }


    private fun addListeners() {
        val bleStatusListener = object : BleConnectStatusCallback.BleConnectStatusChangedListener {
            override fun onBleConnectStatusChanged(connectStatus: Int) {
                when (connectStatus) {
                    BleConnectStatusCallback.BLE_STATUS_CONNECTING_NET -> {
                        showConnecting()
                    }
                    BleConnectStatusCallback.BLE_STATUS_CONNECTED_ANIMATION_PLAY_END -> {
                        connectSuccess()
                    }
                    BleConnectStatusCallback.BLE_STATUS_CONNECTED_FAILED_ANIMATION_PLAY_END -> {
                        connectFailed()
                    }
                }
            }
        }
        BleConnectStatusCallback.instance.registerBleConnectStatusListener(bleStatusListener)
    }


    private var mWIFIStateReceiver: WIFIStateReceiver? = null

    fun registerWIFIStateReceiver() {
        if (!RobotSubConfigManager.getInstance(this@MainActivity)!!.isNeedRegisterWifi) {
            return
        }
        if (mWIFIStateReceiver == null) {
            mWIFIStateReceiver = WIFIStateReceiver(this@MainActivity)
            val filter = IntentFilter()
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
            filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            registerReceiver(mWIFIStateReceiver, filter)
        }
    }

    fun unregisterWIFIStateReceiver() {
        if (mWIFIStateReceiver != null) {
            unregisterReceiver(mWIFIStateReceiver)
            mWIFIStateReceiver = null
        }
    }

    override fun onResume() {
        super.onResume()

        if (wifiConnectOtaView!!.visibility == View.VISIBLE) {
            skipToQRCodeView()
            if (isFromOpenRobot) {
                closeRobot()
            }
        }
        //        getCountryInfoByIP();
    }

    override fun onPause() {
        super.onPause()
        //        unregisterWIFIStateReceiver();
    }

    fun updateDeviceBindStatus() {
        Thread { isDeviceBind(true) }.start()

        //        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000);
//                    Log.e("RemoteCmdResponser", "commandDistribute:command ========= 2222 ======== info: ");
//                    isDeviceBind(false);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
    }

    val robotDeviceBindStatus: Unit
        get() {
            Thread { isRobotDeviceBind }.start()
        }

    fun isDeviceBind(isChinese: Boolean) {
        GeeUiNetManager.isDeviceBind1(this@MainActivity, isChinese, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                skipToQRCodeView()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.body != null) {
                    var info = ""
                    if (response.body != null) {
                        info = response.body!!.string()

                        val bindStatusInfo: BindStatusInfo?
                        try {
                            if (info != null) {
                                bindStatusInfo = Gson().fromJson(info, BindStatusInfo::class.java)
                                if (bindStatusInfo?.data != null) {
                                    if (bindStatusInfo.data!!.isIs_bind && !TextUtils.isEmpty(
                                            bindStatusInfo.data!!.country
                                        )
                                    ) {
                                        updateView(bindStatusInfo.data!!.country)
                                    } else {
                                        //TODO
                                        skipToQRCodeView()
                                    }
                                } else {
                                    //TODO
                                    skipToQRCodeView()
                                }
                            } else {
                                //TODO
                                skipToQRCodeView()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            //TODO
                            skipToQRCodeView()
                        }
                    } else {
                        //TODO
                        skipToQRCodeView()
                    }
                } else {
                    //TODO
                    skipToQRCodeView()
                }
            }
        })
    }

    val isRobotDeviceBind: Unit
        get() {
            GeeUiNetManager.isDeviceBind1(
                this@MainActivity,
                SystemUtil.isInChinese,
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                        //TODO 跳转到OTA
                        wifiConnectOtaView!!.startRequestOTA()
                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        if (isGetRobotBindStatus) {
                            return
                        }
                        isGetRobotBindStatus = true
                        if (response.body == null) {
                            //TODO 跳转到OTA
                            wifiConnectOtaView!!.startRequestOTA()
                            return
                        }
                        var info = ""
                        info = response.body!!.string()
                        Log.e("letianpai", "isRobotDeviceBind： $info")
                        val bindStatusInfo: BindStatusInfo?
                        try {
                            if (info == null) {
                                //TODO 跳转到OTA
                                wifiConnectOtaView!!.startRequestOTA()
                                return
                            }
                            bindStatusInfo = Gson().fromJson(info, BindStatusInfo::class.java)
                            if (bindStatusInfo == null || bindStatusInfo.data == null) {
                                //TODO 跳转到OTA
                                wifiConnectOtaView!!.startRequestOTA()
                                return
                            }
                            if (bindStatusInfo.data!!.isIs_bind == true && !TextUtils.isEmpty(
                                    bindStatusInfo.data!!.country
                                )
                            ) {
                                updateView(bindStatusInfo.data!!.country)
                            } else {
                                //TODO 跳转到OTA
                                wifiConnectOtaView!!.startRequestOTA()
                                return
                            }
                        } catch (e: Exception) {
                            //TODO 跳转到OTA
                            wifiConnectOtaView!!.startRequestOTA()
                            return
                        }
                    }
                })
        }


    private fun responseLanguageChange(localInfo: String) {
        val locale: Locale
        if (localInfo == GLOBAL) {
            locale = Locale("en")
            SystemUtil.set(SystemUtil.REGION_LANGUAGE, "en")
        } else {
            locale = Locale("zh")
            SystemUtil.set(SystemUtil.REGION_LANGUAGE, "zh")
        }
        SystemUtil.setRobotActivated()
        //切换语言
        try {
            val activityManagerNative = Class.forName("android.app.ActivityManagerNative")
            val getDefault = activityManagerNative.getMethod("getDefault")
            val iActivityManager = getDefault.invoke(activityManagerNative)

            val configurationClass = Class.forName("android.content.res.Configuration")
            val configurationConstructor = configurationClass.getConstructor()
            val configuration = configurationConstructor.newInstance()

            val setLocale = configurationClass.getMethod("setLocale", Locale::class.java)
            setLocale.invoke(configuration, locale)

            val userSetLocale = configurationClass.getField("userSetLocale")
            userSetLocale.setBoolean(configuration, true)

            // Method updateConfiguration = iActivityManager.getClass().getMethod("updateConfiguration", configurationClass);
            // updateConfiguration.invoke(iActivityManager, configuration);
            val updatePersistentConfiguration = iActivityManager.javaClass.getMethod(
                "updatePersistentConfiguration",
                configurationClass
            )
            updatePersistentConfiguration.invoke(iActivityManager, configuration)

            val backupManagerClass = Class.forName("android.app.backup.BackupManager")
            val dataChanged = backupManagerClass.getMethod("dataChanged", String::class.java)
            dataChanged.invoke(null, "com.android.providers.settings")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            //TODO 打开Launcher主界面
            updateViewHandler!!.removeMessages(CLOSE_DEVICE)

            //            qrCodeView.setVisibility(View.GONE);
            openLauncherMainView()
            finish()
            closeWifiApp()
        }
    }

    private fun updateRobotData(localInfo: String) {
        val message = Message()
        message.what = UPDATE_ROBOT_LANGUAGE
        message.obj = localInfo
        updateViewHandler!!.sendMessage(message)
    }

    private fun openLauncherMainView() {
        if (!isGetValidBindInfo) {
            isGetValidBindInfo = true
            RobotSubConfigManager.getInstance(this@MainActivity)!!.openMainViewTime =
                System.currentTimeMillis()
            RobotSubConfigManager.getInstance(this@MainActivity)!!.commit()
            val START_FROM = "from"
            val START_FROM_WIFI_CONNECTOR = "wifi_connector"
            val className = "com.renhejia.robot.launcher.main.activity.LeTianPaiMainActivity"
            val packageName = "com.renhejia.robot.launcher"
            val intent = Intent()
            intent.putExtra(START_FROM, START_FROM_WIFI_CONNECTOR)
            intent.setComponent(ComponentName(packageName, className))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }

    private fun responseSkipToBindInfoView() {
        wifiConnectOtaView!!.visibility = View.GONE
        //        qrCodeView.setVisibility(View.VISIBLE);
        pairingInfoView!!.visibility = View.VISIBLE

        pairingCodeKeyboardView!!.visibility = View.GONE
        twelveGridKeyboardView!!.visibility = View.GONE
        autoConnectWifiView!!.visibility = View.GONE
    }

    private inner class UpdateViewHandler(context: Context) : Handler() {
        private val context = WeakReference(context)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                SHOW_CONNECTING -> responseConnecting()
                WIFI_CONNECT_SUCCESS -> responseConnectSuccess()
                WIFI_CONNECT_FAILED -> responseConnectFailed()
                GET_BIND_STATUS -> if (!isGetValidBindInfo) {
                    updateDeviceBindStatus()
                    //                        getBindStatusDelay();
                }

                GET_ROBOT_BIND_STATUS -> if (!isGetRobotBindStatus) {
                    robotBindStatus
                }

                UPDATE_ROBOT_LANGUAGE -> responseLanguageChange(
                    msg.obj as String
                )

                CLOSE_DEVICE -> FunctionUtils.shutdownRobot(this@MainActivity)
                UPDATE_ROBOT_VIEW -> {
                    val country = msg.obj as String
                    if (!TextUtils.isEmpty(country)) {
                        updateRobotView(country)
                    }
                }

                SKIP_QRCODE_VIEW -> responseSkipToBindInfoView()
                GET_BIND_STATUS_TO_INFO_OR_SKIP_TO_MAIN -> responseGetBindStatusInfoOrSkipToMain()
                SHOW_PAIR_CODE -> responseShowPairCodeView()
                CLOSE_WIFI -> responseCloseWifi()
            }
        }
    }

    private fun responseCloseWifi() {
        System.exit(0)
    }

    private fun responseGetBindStatusInfoOrSkipToMain() {
        bindStatus
    }

    private fun responseShowPairCodeView() {
        pairingCodeKeyboardView!!.visibility = View.VISIBLE
        pairingInfoView!!.visibility = View.GONE
        wifiConnectOtaView!!.visibility = View.GONE
        //        qrCodeView.setVisibility(View.GONE);
        twelveGridKeyboardView!!.visibility = View.GONE
        autoConnectWifiView!!.visibility = View.GONE

        //        getBindStatus();
    }

    private fun updateRobotView(country: String) {
        wifiConnectOtaView!!.visibility = View.GONE
        //        qrCodeView.setVisibility(View.GONE);
        pairingInfoView!!.visibility = View.GONE
        twelveGridKeyboardView!!.visibility = View.GONE
        autoConnectWifiView!!.visibility = View.GONE
        pairingCodeKeyboardView!!.visibility = View.GONE
        updateRobotData(country)
    }

    private fun responseConnectFailed() {
        autoConnectWifiView!!.visibility = View.GONE
        twelveGridKeyboardView!!.cleanWifiPassword()
        twelveGridKeyboardView!!.visibility = View.VISIBLE
        pairingInfoView!!.visibility = View.GONE
        pairingCodeKeyboardView!!.visibility = View.GONE

        //        qrCodeView.setVisibility(View.GONE);
    }

    private fun responseConnectSuccess() {
        val wifiName = twelveGridKeyboardView!!.getmWifiName()
        val password = twelveGridKeyboardView!!.contentPassword
        if (!TextUtils.isEmpty(wifiName)) {
            SystemUtil.set("persist.sys.ssid", wifiName)
            SystemUtil.set("persist.sys.password", password)
        }

        autoConnectWifiView!!.visibility = View.GONE
        twelveGridKeyboardView!!.visibility = View.GONE
        if (isFromOpenRobot) {
//            if (qrCodeView.getVisibility() != View.VISIBLE){
//                wifiConnectOtaView.setVisibility(View.VISIBLE);
//            }
            if (pairingInfoView!!.visibility != View.VISIBLE && pairingCodeKeyboardView!!.visibility != View.VISIBLE && !hadRequestOta) {
                hadRequestOta = true
                wifiConnectOtaView!!.visibility = View.VISIBLE
                //TODO 在此更新本地判断逻辑
//                wifiConnectOtaView.startRequestOTA();
                robotBindStatus
                //                wifiConnectOtaView.startRequestOTA();
            } else {
                skipToQRCodeView()
            }
        } else {
            finish()
            System.exit(0)
        }
        //        if (isFromOpenRobot()) {
//            qrCodeView.setVisibility(View.VISIBLE);
//            getBindStatus();
//        } else {
//            finish();
//        }
    }

    private fun responseConnecting() {
        autoConnectWifiView!!.visibility = View.VISIBLE
        twelveGridKeyboardView!!.visibility = View.GONE
    }

    private fun connectFailed() {
        val message = Message()
        message.what = WIFI_CONNECT_FAILED
        updateViewHandler!!.sendMessage(message)
    }

    private fun connectSuccess() {
        val message = Message()
        message.what = WIFI_CONNECT_SUCCESS
        updateViewHandler!!.sendMessage(message)
    }

    private fun showConnecting() {
        val message = Message()
        message.what = SHOW_CONNECTING
        updateViewHandler!!.sendMessage(message)
    }

    fun updateView(name: String?) {
        val message = Message()
        message.what = UPDATE_ROBOT_VIEW
        message.obj = name
        updateViewHandler!!.sendMessage(message)
    }

    private fun closeRobot() {
        val message = Message()
        message.what = CLOSE_DEVICE
        updateViewHandler!!.sendMessageDelayed(message, CLOSE_ROBOT_TIME)
        //        updateViewHandler.removeMessages(CLOSE_DEVICE);
    }

    private val bindStatus: Unit
        get() {
            val message = Message()
            message.what = GET_BIND_STATUS
            updateViewHandler!!.sendMessage(message)
        }

    private val robotBindStatus: Unit
        get() {
            val message = Message()
            message.what = GET_ROBOT_BIND_STATUS
            updateViewHandler!!.sendMessage(message)
        }

    private val bindStatusDelay: Unit
        get() {
            val message = Message()
            message.what = GET_BIND_STATUS
            updateViewHandler!!.sendMessageDelayed(message, 4000)
        }

    fun skipToQRCodeView() {
        if (!hadSkipToQrCodeView) {
            hadSkipToQrCodeView = true
            val message = Message()
            message.what = SKIP_QRCODE_VIEW
            updateViewHandler!!.sendMessage(message)
        }
    }

    val bindStatusInfoOrSkipToMain: Unit
        get() {
            val message = Message()
            message.what = GET_BIND_STATUS_TO_INFO_OR_SKIP_TO_MAIN
            updateViewHandler!!.sendMessage(message)
        }

    fun showPairCodeView() {
        val message = Message()
        message.what = SHOW_PAIR_CODE
        updateViewHandler!!.sendMessage(message)
    }

    fun closeWifiApp() {
        val message = Message()
        message.what = CLOSE_WIFI
        updateViewHandler!!.sendMessageDelayed(message, (10 * 1000).toLong())
    }

    val isFromOpenRobot: Boolean
        get() = if (TextUtils.isEmpty(openAppFrom)) {
            true
        } else {
            false
        }

    val isPairCodeViewVisible: Boolean
        get() = if (pairingCodeKeyboardView!!.visibility == View.VISIBLE) {
            true
        } else {
            false
        }

    val isAutoConnectViewVisible: Boolean
        get() = if (autoConnectWifiView!!.visibility == View.VISIBLE) {
            true
        } else {
            false
        }

    fun removeCloseRobot() {
        updateViewHandler!!.removeMessages(CLOSE_DEVICE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterWIFIStateReceiver()
    }

    companion object {
        private const val SHOW_CONNECTING = 1
        private const val WIFI_CONNECT_SUCCESS = 2
        private const val WIFI_CONNECT_FAILED = 3
        private const val GET_BIND_STATUS = 5
        private const val GET_ROBOT_BIND_STATUS = 13
        private const val UPDATE_ROBOT_LANGUAGE = 6
        private const val CLOSE_DEVICE = 7
        private const val UPDATE_ROBOT_VIEW = 8
        private const val SKIP_QRCODE_VIEW = 9
        private const val SHOW_PAIR_CODE = 10
        private const val GET_BIND_STATUS_TO_INFO_OR_SKIP_TO_MAIN = 11
        private const val CLOSE_WIFI = 12

        //            private static final long CLOSE_ROBOT_TIME = 20 *1000;
        //            private static final long CLOSE_ROBOT_TIME = 10 *1000;
        private const val CLOSE_ROBOT_TIME = (5 * 60 * 1000).toLong()
        private const val GLOBAL = "global"
        private const val CN = "cn"
        private const val OPEN_FROM = "from"
        private const val OPEN_FROM_START = "from_open_robot"
    }
}