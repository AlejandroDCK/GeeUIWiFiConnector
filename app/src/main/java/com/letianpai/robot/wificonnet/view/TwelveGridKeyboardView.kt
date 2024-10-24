package com.letianpai.robot.wificonnet.view

import android.app.AlertDialog
import android.content.Context
import android.net.wifi.ScanResult
import android.os.Build
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.letianpai.robot.wificonnet.MainActivity
import com.letianpai.robot.wificonnet.R
import com.letianpai.robot.wificonnet.adapter.CustomAdapter
import com.letianpai.robot.wificonnet.callback.BleConnectStatusCallback
import com.letianpai.robot.wificonnet.callback.KeyPressCallback
import com.letianpai.robot.wificonnet.callback.KeyPressCallback.KeyPressCallbackListener
import com.letianpai.robot.wificonnet.manager.WifiScanner
import com.letianpai.robot.wificonnet.util.FunctionUtils.removeLastByte
import com.letianpai.robot.wificonnet.util.KeyConsts
import com.letianpai.robot.wificonnet.util.KeysMapUtils.Companion.getInstance
import com.letianpai.robot.wificonnet.wifi.WIFIAutoConnectionService
import java.lang.ref.WeakReference

/**
 * @author liujunbin
 */
class TwelveGridKeyboardView : LinearLayout {
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
    private var wifiName: TextView? = null
    private var ivSelectWifi: ImageView? = null
    private var ivWifiCommit: CommitButton? = null
    private var mUpdateViewHandler: UpdateViewHandler? = null
    private lateinit var alertDialog: AlertDialog
    var contentPassword: String? = ""
        private set
    private var currentKeypadPosition = 0
    private var wifiScanner: WifiScanner? = null
    private var mWifiName = ""


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
        initView()
    }

    private fun initView() {
        inflate(mContext, R.layout.keyboard_scroll_view, this)
        initManager()
        initViews()
        initFunctionButtonData()
        setData()
        addKeyPressedListener()
    }

    private fun initFunctionButtonData() {
        kbLk1!!.setEnable(false)
        kbLk1!!.setImagePosition(KeyConsts.CENTER_VERTICAL, 30, 20, 16, 16, 36, 36)
        kbLk1!!.setUnPressedImage(R.drawable.keypad_icon)

        kbLk2!!.setImagePosition(KeyConsts.RIGHT_TOP, 0, 17, 12, 0, 36, 36)
        kbLk2!!.setPressedImage(R.drawable.fun_return_pressed)
        kbLk2!!.setUnPressedImage(R.drawable.fun_return_unpressed)

        kbRk1!!.setImagePosition(KeyConsts.CENTER_VERTICAL, 12, 0, 0, 0, 36, 36)
        kbRk1!!.setPressedImage(R.drawable.fun_switch_pressed)
        kbRk1!!.setUnPressedImage(R.drawable.fun_switch_unpressed)

        kbRk2!!.setImagePosition(KeyConsts.LEFT_TOP, 12, 17, 0, 0, 36, 36)
        kbRk2!!.setPressedImage(R.drawable.fun_delete_pressed)
        kbRk2!!.setUnPressedImage(R.drawable.fun_delete_unpressed)
    }

    private fun addKeyPressedListener() {
        KeyPressCallback.instance.setKeyPressCallbackListener(object : KeyPressCallbackListener {
            override fun onKeyPressed(keyType: Int, keyValue: String?) {
                updateWifiPassword(keyType, keyValue)
            }
        })
        kbLk1!!.setOnClickListener { }
        kbLk2!!.setOnClickListener { backToRobotView() }
        kbRk1!!.setOnClickListener { responseSwitch() }
        kbRk2!!.setOnClickListener { responseDelete() }
        kbBk1!!.setOnClickListener { }
        kbBk2!!.setOnClickListener { }
        ivSelectWifi!!.setOnClickListener { showWifiSelectDialog() }
        wifiName!!.setOnClickListener { showWifiSelectDialog() }
        ivWifiCommit!!.setOnClickListener { //                if (TextUtils.isEmpty(mWifiName)){
//                    Toast.makeText(mContext,"Wi-Fi未设置",Toast.LENGTH_LONG).show();
//                }else{
//                    connectWifi();
//                }
            connectWifi()
        }
    }

    private fun backToRobotView() {
        if (!(mContext as MainActivity?)!!.isFromOpenRobot) {
            (mContext as MainActivity?)!!.finish()
        }
    }

    private fun connectWifi() {
        Log.e("letianpai_wifi", "connect_wifi")
        if (!TextUtils.isEmpty(mWifiName)) {
            BleConnectStatusCallback.instance.setBleConnectStatus(BleConnectStatusCallback.BLE_STATUS_CONNECTING_NET)
            contentPassword?.let { mContext?.let { it1 -> WIFIAutoConnectionService.start(it1, mWifiName, it) } }
        }
    }

    private fun responseSwitch() {
        currentKeypadPosition += 1
        if (currentKeypadPosition > 3) {
            currentKeypadPosition = 0
        }
        if (currentKeypadPosition == 0) {
            adapter = CustomAdapter(getInstance(mContext!!)!!.keysList)
            recyclerView!!.adapter = adapter
        } else if (currentKeypadPosition == 1) {
            adapter = CustomAdapter(getInstance(mContext!!)!!.smallCharacterList)
            recyclerView!!.adapter = adapter
        } else if (currentKeypadPosition == 2) {
            adapter = CustomAdapter(getInstance(mContext!!)!!.numList)
            recyclerView!!.adapter = adapter
        } else if (currentKeypadPosition == 3) {
            adapter = CustomAdapter(getInstance(mContext!!)!!.specialKeyList)
            recyclerView!!.adapter = adapter
        }
    }

    private fun responseDelete() {
        contentPassword = removeLastByte(contentPassword)
        wifiPassword!!.text = contentPassword
    }

    private fun setData() {
        adapter = CustomAdapter(getInstance(mContext!!)!!.keysList)
        recyclerView!!.adapter = adapter
    }

    private fun initViews() {
        ivWifiCommit = findViewById(R.id.iv_wifi_commit)
        ivSelectWifi = findViewById(R.id.ivSelectWifi)
        wifiName = findViewById(R.id.tv_selected_wifi)
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
                UPDATE_WIFI_NAME -> updateWifiDisplayName(msg)
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

    private fun createListDialog2(context: Context?, wifiList: List<ScanResult>) {
        // 通过LayoutInflater加载自定义布局

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_list, null)

        // 初始化ListView
        val listView = view.findViewById<ListView>(R.id.listView)

        // 准备数据
//        String[] items = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};
        val adapter = WifiAdapter(mContext, wifiList)
        listView.adapter = adapter

        // 设置ListView的点击事件
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent: AdapterView<*>?, view1: View?, position: Int, id: Long ->
                // 处理点击事件
                val selectedItem = adapter.getItem(position)
                updateWifiDisplayName(selectedItem!!.SSID)
                Log.e("letianpai_wifi", "selectedItem.SSID: " + selectedItem.SSID)
            }

        // 创建AlertDialog.Builder并设置布局
        val builder = AlertDialog.Builder(context)
        builder.setView(view)

        //
//        // 设置取消按钮
//        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());

        // 创建并显示AlertDialog
        alertDialog = builder.create()
        alertDialog.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.getWindow()!!.attributes)
        layoutParams.width = 320
        layoutParams.height = 300

        alertDialog.getWindow()!!.attributes = layoutParams
        alertDialog.getWindow()!!
            .setBackgroundDrawable(mContext!!.resources.getDrawable(R.drawable.rounded_corner_background))
    }

    private fun updateWifiDisplayName(name: String) {
        val message = Message()
        message.obj = name
        message.what = UPDATE_WIFI_NAME
        mUpdateViewHandler!!.sendMessage(message)
    }

    private fun updateWifiDisplayName(msg: Message?) {
        if (msg?.obj == null) {
            return
        }
        mWifiName = msg.obj as String
        wifiName!!.text = msg.obj as String
        (mContext as MainActivity?)!!.wifiName = msg.obj as String
        if (alertDialog != null && alertDialog!!.isShowing) {
            alertDialog!!.dismiss()
        }
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

    private fun showWifiSelectDialog() {
        val wifiList = wifiScanner!!.availableWifiList
        val finalWifiList: MutableList<ScanResult> = ArrayList()
        for (i in wifiList.indices) {
            Log.e("wifi_scan", i.toString() + "_wifiList.get(i).SSID: " + wifiList[i].SSID)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Log.e(
                    "wifi_scan",
                    i.toString() + "_wifiList.get(i).getWifiStandard(): " + wifiList[i].wifiStandard
                )
            }
            if (!TextUtils.isEmpty(wifiList[i].SSID) && !isInWifiList(
                    wifiList[i].SSID,
                    finalWifiList
                )
            ) {
                finalWifiList.add(wifiList[i])
            }
        }
        createListDialog2(mContext, finalWifiList)
    }

    private fun isInWifiList(ssid: String, finalWifiList: List<ScanResult>): Boolean {
        for (i in finalWifiList.indices) {
            if (finalWifiList[i].SSID == ssid) {
                return true
            }
        }
        return false
    }

    private fun initManager() {
        wifiScanner = WifiScanner(mContext!!)
    }

    fun getmWifiName(): String? {
        return mWifiName
    }

    companion object {
        const val UPDATE_WIFI_NAME: Int = 101
        const val UPDATE_WIFI_PASSWORD: Int = 102
        const val CLEAN_WIFI_PASSWORD: Int = 103
    }
}
