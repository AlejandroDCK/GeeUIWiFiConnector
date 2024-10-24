package com.letianpai.robot.wificonnet.view

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.letianpai.robot.wificonnet.MainActivity
import com.letianpai.robot.wificonnet.R
import com.letianpai.robot.wificonnet.callback.BleConnectStatusCallback
import com.letianpai.robot.wificonnet.callback.BleConnectStatusCallback.BleConnectStatusChangedListener
import com.letianpai.robot.wificonnet.system.SystemUtil.ltpLastSn
import com.letianpai.robot.wificonnet.util.ViewUtils.getViewWidthSize
import com.letianpai.robot.wificonnet.util.ViewUtils.resizeImageViewSize
import com.letianpai.robot.wificonnet.wifi.WIFIAutoConnectionService
import com.letianpai.robot.wificonnet.wifi.WIFIStateReceiver
import com.letianpai.robot.wificonnet.wifi.callback.GuideWifiConnectCallback
import com.letianpai.robot.wificonnet.wifi.callback.GuideWifiConnectCallback.NetworkChangingUpdateListener
import java.lang.ref.WeakReference

/**
 * 自动联网页面
 *
 * @author liujunbin
 */
class AutoConnectWifiView : RelativeLayout {
    private var mContext: Context? = null

    //    private AnimationDrawable animationDrawable;
    private var root: RelativeLayout? = null
    private var ivWifi: ImageView? = null
    private var tvWifiName: TextView? = null
    private var tvWifiStatus: TextView? = null
    private var wifiName = ""
    private val wifiPassword = ""
    private val mWIFIStateReceiver: WIFIStateReceiver? = null
    private var isConnected = false
    private var mLottieAnimationView: LottieAnimationView? = null
    private var currentLottie: String? = null
    private var mHandler: Handler? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //        unregisterWIFIStateReceiver();
    }

    private fun init(context: Context) {
        this.mContext = context
        inflate(mContext, R.layout.robot_guide_connect_wifi_view, this)
        initView()
        resizeView()
        showWifiConnectAnimation()
        addListeners()
        registerCallback()
        registerBleUpdateCallback()
        //        registerWIFIStateReceiver();
    }

    private fun registerBleUpdateCallback() {
        BleConnectStatusCallback.instance.registerBleConnectStatusListener(object :
            BleConnectStatusChangedListener {
            override fun onBleConnectStatusChanged(connectStatus: Int) {
                if (connectStatus == BleConnectStatusCallback.BLE_STATUS_CONNECTED_NET_FAILED) {
                    updateAnimation(
                        CONNECT_FAILED,
                        false,
                        BleConnectStatusCallback.BLE_STATUS_CONNECTED_NET_FAILED
                    )
                } else if (connectStatus == BleConnectStatusCallback.BLE_STATUS_CONNECTING_NET) {
                    updateAnimation(
                        CONNECTING,
                        true,
                        BleConnectStatusCallback.BLE_STATUS_CONNECTING_NET
                    )
                } else if (connectStatus == BleConnectStatusCallback.BLE_STATUS_CONNECT_TO_CLIENT) {
                    updateAnimation(
                        CONNECTING,
                        true,
                        BleConnectStatusCallback.BLE_STATUS_CONNECT_TO_CLIENT
                    )
                }
            }
        })
    }

    //    public void registerWIFIStateReceiver() {
    //        if (mWIFIStateReceiver == null) {
    //            mWIFIStateReceiver = new WIFIStateReceiver(mContext);
    //            IntentFilter filter = new IntentFilter();
    //            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
    //            filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
    //            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    //            mContext.registerReceiver(mWIFIStateReceiver, filter);
    //        }
    //    }
    //
    //    public void unregisterWIFIStateReceiver() {
    //        if (mWIFIStateReceiver != null) {
    //            mContext.unregisterReceiver(mWIFIStateReceiver);
    //        }
    //
    //    }
    private fun resizeView() {
        val width = getViewWidthSize(
            mContext!!,
            (mContext as Activity?)!!.window
        )
        val iconWidth = (150 * width) / 480
        resizeImageViewSize(ivWifi!!, iconWidth, iconWidth)
    }

    //注册网络回调信息
    private fun registerCallback() {
        GuideWifiConnectCallback.instance
            .setChangingStatusUpdateListener(object : NetworkChangingUpdateListener {
                override fun onNetworkChargingUpdateReceived(
                    networkType: Int,
                    networkStatus: Boolean
                ) {
                    Log.e("auto_connect", "networkType: $networkStatus")
                    Log.e("auto_connect", "networkStatus: $networkStatus")
                    if ((networkType == GuideWifiConnectCallback.NETWORK_TYPE_WIFI) && (networkStatus == true)) {
                        tvWifiName!!.text = wifiName
                        tvWifiStatus!!.setText(R.string.connect_success)

                        updateAnimation(
                            CONNECT_SUCCESS,
                            false,
                            BleConnectStatusCallback.BLE_STATUS_CONNECTED_NET_SUCCESS
                        )
                        isConnected = true
                    }
                }

                override fun onWifiInfoChanged(ssid: String?, password: String?) {
                    wifiName = ssid!!
                }
            })
    }

    private fun playLottieAnimation(animation: String, loop: Boolean) {
        mLottieAnimationView!!.setAnimation(animation)
        mLottieAnimationView!!.loop(loop)
        currentLottie = animation
        mLottieAnimationView!!.playAnimation()
    }

    private fun addListeners() {
        root!!.setOnClickListener { }

        mLottieAnimationView!!.addAnimatorUpdateListener { }
        mLottieAnimationView!!.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                if (currentLottie == CONNECT_SUCCESS) {
                    //关闭Activity
                    BleConnectStatusCallback.instance.setBleConnectStatus(BleConnectStatusCallback.BLE_STATUS_CONNECTED_ANIMATION_PLAY_END)

                    //                    BleConnectStatusCallback.getInstance().setBleConnectStatus(BleConnectStatusCallback.BLE_STATUS_CONNECTED_NET_SUCCESS);
                } else if (currentLottie == CONNECT_FAILED) {
                    BleConnectStatusCallback.instance.setBleConnectStatus(BleConnectStatusCallback.BLE_STATUS_CONNECTED_FAILED_ANIMATION_PLAY_END)

                    //                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            BleConnectStatusCallback.getInstance().setBleConnectStatus(BleConnectStatusCallback.BLE_STATUS_CONNECTED_FAILED_ANIMATION_PLAY_END);
//                        }
//                    }, 1000);
                }
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
    }

    private fun startConnectWifi() {
        tvWifiName!!.text = wifiName
        tvWifiStatus!!.setText(R.string.connecting_wifi)
        Log.e("auto_connect", "onClick_1")
        mContext?.let { WIFIAutoConnectionService.start(it, wifiName, wifiPassword) }
    }

    private fun initView() {
        mHandler = AnimationHandler(mContext)
        //        animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.wifi_animlist);
        root = findViewById(R.id.guide_root)
        ivWifi = findViewById(R.id.iv_wifi)
        tvWifiName = findViewById(R.id.tv_wifi_name)
        tvWifiStatus = findViewById(R.id.tv_wifi_status)

        mLottieAnimationView = findViewById(R.id.lav_view)
        //        playLottieAnimation(CONNECTING,true);
        updateAnimation(CONNECTING, true, BleConnectStatusCallback.BLE_STATUS_CONNECTING_NET)
        //showWifiConnectAnimation();
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
        context: Context?, attrs: AttributeSet?, defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun showWifiConnectAnimation() {
        //        if (animationDrawable != null) {
//            animationDrawable.setOneShot(false);
//            ivWifi.setBackground(animationDrawable);
//            animationDrawable.start();
//        }
    }

    private fun updateAnimation(animation: String, loop: Boolean, status: Int) {
        mHandler!!.removeMessages(CHANGE_LOTTIE) //TODO 111
        val message = Message()
        message.what = CHANGE_LOTTIE
        message.obj = animation
        if (loop) {
            message.arg1 = 2
        } else {
            message.arg1 = 0
        }
        message.arg2 = status
        mHandler!!.sendMessage(message)
    }

    private inner class AnimationHandler(context: Context?) : Handler() {
        private val context: WeakReference<Context?>? = WeakReference(context)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == CHANGE_LOTTIE) {
                if (context?.get() != null) {
                    if (msg.arg1 == 2) {
                        playLottieAnimation(msg.obj as String, true)
                    } else {
                        playLottieAnimation(msg.obj as String, false)
                    }
                    if (msg.arg2 == BleConnectStatusCallback.BLE_STATUS_CONNECT_TO_CLIENT) {
                        tvWifiStatus!!.text = mContext!!.getText(R.string.wait_connect_wifi)
                            .toString() + "\n" + mContext!!.getText(R.string.robot_info) + ltpLastSn
                    } else if (msg.arg2 == BleConnectStatusCallback.BLE_STATUS_CONNECTING_NET) {
                        // tvWifiStatus.setText(mContext.getText(R.string.connecting_wifi) + "\n" + wifiName);
//                        tvWifiStatus.setText(mContext.getText(R.string.connecting_wifi) + "\n" + mContext.getText(R.string.robot_info) + SystemUtil.getLtpLastSn());
                        val wifiName = (mContext as MainActivity?)!!.wifiName
                        tvWifiStatus!!.text =
                            mContext!!.getText(R.string.connecting_wifi)
                                .toString() + "\n" + wifiName
                    } else if (msg.arg2 == BleConnectStatusCallback.BLE_STATUS_CONNECTING_NET) {
                        // tvWifiStatus.setText(mContext.getText(R.string.connecting_wifi) + "\n" + wifiName);
                        tvWifiStatus!!.text = """
                            
                            $wifiName
                            """.trimIndent()
                    } else if (msg.arg2 == BleConnectStatusCallback.BLE_STATUS_CONNECTED_NET_FAILED) {
                        //为了统一海外国内的文案显示，所以去掉原来的中文，海外国内改成一致的。
                        // tvWifiStatus.setText(R.string.connect_failed);
                        tvWifiStatus!!.text = ""
                    } else if (msg.arg2 == BleConnectStatusCallback.BLE_STATUS_CONNECTED_NET_SUCCESS) {
                        tvWifiStatus!!.setText(R.string.connect_success)
                    }
                }
            }
        }
    }

    companion object {
        private const val CONNECTING = "connectwifi/wifi_connecting.json"
        private const val CONNECT_SUCCESS = "connectwifi/connect_wifi_success.json"
        private const val CONNECT_FAILED = "connectwifi/sonconnect_wifi_failed.json"
        private const val CHANGE_LOTTIE = 1
    }
}
