package com.letianpai.robot.wificonnet.callback

/**
 * 按键功能回调
 * @author liujunbin
 */
class KeyPressCallback private constructor() {
    private val keyPressCallbackListenerList = ArrayList<KeyPressCallbackListener?>()

    private object KeyPressCallbackHolder {
        val instance: KeyPressCallback = KeyPressCallback()
    }

    fun setKeyPressCallbackListener(keyPressCallbackListener: KeyPressCallbackListener?) {
        keyPressCallbackListenerList.add(keyPressCallbackListener)
    }

    interface KeyPressCallbackListener {
        fun onKeyPressed(keyType: Int, keyValue: String?)
    }

    fun pressKey(keyType: Int, keyValue: String?) {
        for (i in keyPressCallbackListenerList.indices) {
            if (keyPressCallbackListenerList[i] != null) {
                keyPressCallbackListenerList[i]!!.onKeyPressed(keyType, keyValue)
            }
        }
    }


    companion object {
        const val KEY_TYPE_FUNCTION: Int = 10010
        const val KEY_TYPE_VALUE: Int = 10011

        @JvmStatic
        val instance: KeyPressCallback
            get() = KeyPressCallbackHolder.instance
    }
}
