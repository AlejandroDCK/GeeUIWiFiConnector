package com.letianpai.robot.wificonnet.wifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import com.letianpai.robot.wificonnet.callback.BleConnectStatusCallback
import com.letianpai.robot.wificonnet.wifi.callback.GuideWifiConnectCallback

/**
 * Wi-Fi 自动连接广播
 * @author liujunbin
 */
class WIFIStateReceiver(private val mContext: Context) : BroadcastReceiver() {
    private val mWifiManager = mContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private var isConnected = false
    private val handler: Handler? = null
    private val isTimeout = false

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("WIFIStateReceiver", "onReceive---: " + intent.action)
        if (isConnected) {
            return
        }
        if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            // 网络状态变化
            val networkInfo = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
            if (networkInfo != null) {
                val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInfo = wifiManager.connectionInfo
                var ssid = wifiInfo.ssid // 获取当前连接的 WiFi 网络的 SSID
                ssid = ssid.replace("\"", "")
                if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                    // 已连接到 WiFi 网络
//                    Log.e("letianpai_net","net_Connect:"+ssid);
                    val currentSsid = WIFIConnectionManager.getInstance(mContext)?.currentSsid
                    Log.e("letianpai_net", "conneced-ssid:$ssid--currentSsid:$currentSsid")
                    if (!TextUtils.isEmpty(currentSsid) && ssid == currentSsid) {
                        updateNetworkStatus(context, intent)
                    } else {
                        if (!TextUtils.isEmpty(currentSsid)) {
                            //切换网络失败
                            BleConnectStatusCallback.instance
                                .setBleConnectStatus(BleConnectStatusCallback.BLE_STATUS_CONNECTED_NET_FAILED)
                        }
                    }
                } else if (networkInfo.state == NetworkInfo.State.DISCONNECTED) {
                    Log.e("letianpai_net", "net_Disconnect:$ssid")
                    // WiFi 网络已断开连接
                    // BleConnectStatusCallback.getInstance().setBleConnectStatus(BleConnectStatusCallback.BLE_STATUS_CONNECTED_NET_FAILED);
                }
            }

            // if (WIFIConnectionManager.getInstance(mContext).isConnected()) {
            //     LogUtils.logd("WIFIStateReceiver", "onReceive: isConnected true");

            // } else {
            //     LogUtils.logd("WIFIStateReceiver", "onReceive: isConnected flase");
            // }
        } else if (intent.action == WifiManager.SUPPLICANT_STATE_CHANGED_ACTION) {
            val linkWifiResult = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 123)
            Log.e("WIFIStateReceiver", "onReceive:linkWifiResult= $linkWifiResult")
            if (linkWifiResult == WifiManager.ERROR_AUTHENTICATING) {
//                    密码错误时  清空networkId的相关信息
//                WIFIConnectionManager.getInstance(mContext).removeNetwork();
                if ((BleConnectStatusCallback.instance
                        .status != BleConnectStatusCallback.BLE_STATUS_DEFAULT) && (!WIFIConnectionManager.getInstance(
                        mContext
                    )?.isSetIncorrectPassword!!)
                ) {
                    BleConnectStatusCallback.instance
                        .setBleConnectStatus(BleConnectStatusCallback.BLE_STATUS_CONNECTED_NET_FAILED)
                    WIFIConnectionManager.getInstance(mContext)?.isSetIncorrectPassword = true
                }
            }
        }
    }

    private fun updateNetworkStatus(context: Context, intent: Intent) {
        if (mWifiManager.isWifiEnabled && isWifiConnected(context)) {
            if (!isConnected) {
                isConnected = true
                GuideWifiConnectCallback.instance
                    .setNetworkStatus(GuideWifiConnectCallback.NETWORK_TYPE_WIFI, true)
                BleConnectStatusCallback.instance
                    .setBleConnectStatus(BleConnectStatusCallback.BLE_STATUS_CONNECTED_NET_SUCCESS)
            }
        }
    }

    private fun isWifiConnected(context: Context?): Boolean {
        if (context != null) {
            val mConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mWiFiNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mWiFiNetworkInfo != null) {
                Log.e(
                    "WIFIStateReceiver",
                    "isWifiConnected: " + (mWiFiNetworkInfo != null) + "  " + mWiFiNetworkInfo.isAvailable + "  " + mWiFiNetworkInfo.isConnected
                )
                return mWiFiNetworkInfo.isAvailable && mWiFiNetworkInfo.isConnected
            }
        }
        Log.e("WIFIStateReceiver", "isWifiConnected: mWiFiNetworkInfo null")
        return false
    }

    companion object {
        private val TAG: String = WIFIStateReceiver::class.java.name
    }
}
