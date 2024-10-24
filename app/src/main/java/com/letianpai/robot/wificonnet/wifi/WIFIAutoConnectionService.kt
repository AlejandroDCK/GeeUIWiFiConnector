package com.letianpai.robot.wificonnet.wifi

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.util.Log
import com.letianpai.robot.wificonnet.callback.BleConnectStatusCallback
import com.letianpai.robot.wificonnet.callback.BleConnectStatusCallback.Companion.instance
import com.letianpai.robot.wificonnet.system.SystemUtil
import com.letianpai.robot.wificonnet.system.SystemUtil.wifiPermissionsGranted
import com.letianpai.robot.wificonnet.wifi.callback.GuideWifiConnectCallback

/**
 * @author liujunbin
 */
class WIFIAutoConnectionService : Service() {
    /**
     * wifi名
     */
    private var ssid: String? = ""

    /**
     * 密码
     */
    private var password: String? = ""

    private var isRetry = false

    private lateinit var mHandler: Handler

    /**
     * 负责不断尝试连接指定wifi
     */
    override fun onCreate() {
        super.onCreate()

        mHandler = Handler(Looper.getMainLooper(), object : Handler.Callback {
            @SuppressLint("MissingPermission")
            override fun handleMessage(msg: Message): Boolean {
                when (msg.what) {
                    SCAN_RESULT -> {
                        var isSSIDAvailable = false
                        if (mWifiManager != null) {
                            if (wifiPermissionsGranted) {
                                val scanResults = mWifiManager!!.scanResults
                                for (scanResult in scanResults) {
//                        Log.e("WIFIConnectionManager", "发现的 ssid 有: " + scanResult.SSID);
                                    if (scanResult.SSID == ssid) {
                                        // 找到了指定的 SSID
                                        isSSIDAvailable = true
                                        break
                                    }
                                }
                            }

                        }

                        // WIFIConnectionManager.getInstance(WIFIAutoConnectionService.this).connect(mSsid, mPwd);
                        if (isSSIDAvailable) {
                            //没有扫描到指定的wifi ssid 连接错误
//                        Log.e("WIFIConnectionManager", "found ssid: " + mSsid);
                            WIFIConnectionManager.getInstance(this@WIFIAutoConnectionService)?.connect(
                                ssid!!, password!!
                            )
                        } else {
//                        Log.e("WIFIConnectionManager", "not found ssid: " + mSsid);
                            //如果没有扫描到WiFi，都一直扫描
                            // mHandler.sendEmptyMessageAtTime(SCAN_RESULT, 3000);
                            //如果这里执行下面的代码，在没有扫描到的时候，就会提示配网失败。
                            instance.setBleConnectStatus(BleConnectStatusCallback.BLE_STATUS_CONNECTED_NET_FAILED)
                        }
                    }

                    START_CONNECT -> {
                        //                    Log.e("auto_connect", "onClick_4_mSsid: " + mSsid);
//                    Log.e("auto_connect", "onClick_4_mPwd: " + mPwd);
                        if (TextUtils.isEmpty(ssid)) {
                            return false
                        }
                        WIFIConnectionManager.getInstance(this@WIFIAutoConnectionService)?.connect(
                            ssid!!, password!!
                        )

                        val connected = (WIFIConnectionManager.getInstance(
                            this@WIFIAutoConnectionService
                        )!!.isConnected(
                            ssid
                        )) && (WIFIConnectionManager.isNetworkAvailable(
                            this@WIFIAutoConnectionService
                        ))
                        if (connected) {
                            Log.e("auto_connect", "connected: ===== 1 ======")
                            GuideWifiConnectCallback.instance.setNetworkStatus(GuideWifiConnectCallback.NETWORK_TYPE_WIFI, true)
                            BleConnectStatusCallback.instance.setBleConnectStatus(BleConnectStatusCallback.BLE_STATUS_CONNECTED_NET_SUCCESS)
                            mHandler.removeCallbacksAndMessages(null)
                            stopSelf()
                        } else {
                            if (isRetry) {
                                GuideWifiConnectCallback.instance
                                    .setNetworkStatus(GuideWifiConnectCallback.NETWORK_TYPE_WIFI, true)
                                instance.setBleConnectStatus(BleConnectStatusCallback.BLE_STATUS_CONNECTED_NET_FAILED)
                                stopSelf()
                            } else {
                                mHandler.sendEmptyMessageDelayed(START_CONNECT, 5000) //5s循环
                                isRetry = true
                            }
                        }
                    }
                }
                return true
            }
        })

    }



    private var mWifiManager: WifiManager? = null



    /**
     * @return always null
     */
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e("auto_connect", "onClick_3")
        ssid = intent.getStringExtra(KEY_SSID)
        password = intent.getStringExtra(KEY_PWD)
        handleConnect()
        return START_NOT_STICKY
    }

    private fun handleConnect() {
        if (TextUtils.isEmpty(ssid)) {
            Log.e("WIFIAutoConnectionService", "handleConnect: wifi 名为空，不连接")
            instance.setBleConnectStatus(BleConnectStatusCallback.BLE_STATUS_CONNECTED_NET_FAILED)
            return
        }
        mWifiManager = this.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        if (!mWifiManager!!.isWifiEnabled) {
            mWifiManager!!.setWifiEnabled(true)
        }
        //        mWifiManager.startScan();
        mHandler.sendEmptyMessageAtTime(START_CONNECT, 4000)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    companion object {
        private const val TAG = "www_wifi"
        private const val KEY_SSID = "KEY_SSID"
        private const val KEY_PWD = "KEY_PWD"
        const val SCAN_RESULT: Int = 0x03
        const val START_CONNECT: Int = 0x04

        /**
         * 连接指定wifi热点, 失败后5s循环
         *
         * @param context 用于启动服务的上下文
         * @param ssid    默认HUD-WIFI
         * @param pwd     (WPA加密)默认12345678
         */
        fun start(context: Context, ssid: String, pwd: String) {
            Log.e("auto_connect", "onClick_2")
            Log.e("auto_connect", "ssid: $ssid")
            Log.e("auto_connect", "pwd: $pwd")
            val starter = Intent(context, WIFIAutoConnectionService::class.java)
            starter.putExtra(KEY_SSID, ssid).putExtra(KEY_PWD, pwd)
            context.startService(starter)
        }

        fun stop(context: Context) {
            val starter = Intent(context, WIFIAutoConnectionService::class.java)
            context.stopService(starter)
            Log.d(TAG, "stop: ")
        }
    }
}
