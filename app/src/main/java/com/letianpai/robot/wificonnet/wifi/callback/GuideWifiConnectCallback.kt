package com.letianpai.robot.wificonnet.wifi.callback

/**
 * Created by liujunbin
 */
class GuideWifiConnectCallback private constructor() {
    private var mNetworkChangingUpdateListener: NetworkChangingUpdateListener? = null

    private object NetworkChangingUpdateHolder {
        val instance: GuideWifiConnectCallback = GuideWifiConnectCallback()
    }

    interface NetworkChangingUpdateListener {
        fun onNetworkChargingUpdateReceived(networkType: Int, networkStatus: Boolean)
        fun onWifiInfoChanged(ssid: String?, password: String?)
    }

    fun setChangingStatusUpdateListener(listener: NetworkChangingUpdateListener?) {
        this.mNetworkChangingUpdateListener = listener
    }

    fun setNetworkStatus(networkType: Int, networkStatus: Boolean) {
        if (mNetworkChangingUpdateListener != null) {
            mNetworkChangingUpdateListener!!.onNetworkChargingUpdateReceived(
                networkType,
                networkStatus
            )
        }
    }

    fun setWifiInfo(ssid: String?, password: String?) {
        if (mNetworkChangingUpdateListener != null) {
            mNetworkChangingUpdateListener!!.onWifiInfoChanged(ssid, password)
        }
    }


    companion object {
        const val NETWORK_TYPE_DISABLED: Int = 0
        const val NETWORK_TYPE_WIFI: Int = 1

        val instance: GuideWifiConnectCallback
            get() = NetworkChangingUpdateHolder.instance
    }
}
