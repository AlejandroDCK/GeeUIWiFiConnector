package com.letianpai.robot.wificonnet.callback

/**
 * @author liujunbin
 */
class BleConnectStatusCallback private constructor() {
    var status: Int = BLE_STATUS_DEFAULT

    private object WatchViewUpdateCallbackHolder {
        val instance: BleConnectStatusCallback = BleConnectStatusCallback()
    }

    private val bleConnectListeners: MutableList<BleConnectStatusChangedListener?> = ArrayList()

    interface BleConnectStatusChangedListener {
        fun onBleConnectStatusChanged(connectStatus: Int)
    }

    fun registerBleConnectStatusListener(listener: BleConnectStatusChangedListener?) {
        if (listener != null) {
            bleConnectListeners.add(listener)
        }
    }

    fun unregisterBleConnectStatusListener(listener: BleConnectStatusChangedListener?) {
        if (listener != null) {
            bleConnectListeners.remove(listener)
        }
    }

    fun setBleConnectStatus(connectStatus: Int) {
        this.status = connectStatus
        for (i in bleConnectListeners.indices) {
            if (bleConnectListeners[i] != null) {
                bleConnectListeners[i]!!.onBleConnectStatusChanged(connectStatus)
            }
        }
    }

    companion object {
        const val BLE_STATUS_DEFAULT: Int = 0
        const val BLE_STATUS_CONNECT_TO_CLIENT: Int = 1
        const val BLE_STATUS_DISCONNECT_FROM_CLIENT: Int = 2
        const val BLE_STATUS_CONNECTING_NET: Int = 6
        const val BLE_STATUS_CONNECTED_NET_SUCCESS: Int = 3
        const val BLE_STATUS_CONNECTED_NET_FAILED: Int = 4
        const val BLE_STATUS_CONNECTED_ANIMATION_PLAY_END: Int = 5
        const val BLE_STATUS_CONNECTED_FAILED_ANIMATION_PLAY_END: Int = 7
        @JvmStatic
        val instance: BleConnectStatusCallback
            get() = WatchViewUpdateCallbackHolder.instance
    }
}
