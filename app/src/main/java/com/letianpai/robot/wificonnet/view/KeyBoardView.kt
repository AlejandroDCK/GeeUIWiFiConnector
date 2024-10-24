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
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.letianpai.robot.wificonnet.R
import com.letianpai.robot.wificonnet.manager.WifiScanner
import com.letianpai.robot.wificonnet.util.ViewUtils.resizeRelativeLayoutViewHeightSize
import java.lang.ref.WeakReference
import java.util.Locale

/**
 * @author liujunbin
 */
class KeyBoardView : LinearLayout {
    private var k11: Button? = null
    private var k12: Button? = null
    private var k13: Button? = null
    private var k14: Button? = null
    private var k15: Button? = null
    private var k16: Button? = null
    private var k17: Button? = null
    private var k18: Button? = null
    private var k19: Button? = null
    private var k110: Button? = null
    private var k21: Button? = null
    private var k22: Button? = null
    private var k23: Button? = null
    private var k24: Button? = null
    private var k25: Button? = null
    private var k26: Button? = null
    private var k27: Button? = null
    private var k28: Button? = null
    private var k29: Button? = null
    private var k31: Button? = null
    private var k32: Button? = null
    private var k33: Button? = null
    private var k34: Button? = null
    private var k35: Button? = null
    private var k36: Button? = null
    private var k37: Button? = null
    private var kDelete: Button? = null
    private var kSpace1: Button? = null
    private var kSpace2: Button? = null
    private var kUp: View? = null
    private var kDown: View? = null
    private var ivSelectWifi: ImageView? = null
    private val buttonList = ArrayList<Button?>()
    private val keysMap1 = arrayOf(
        "Q",
        "W",
        "E",
        "R",
        "T",
        "Y",
        "U",
        "I",
        "O",
        "P",
        "A",
        "S",
        "D",
        "F",
        "G",
        "H",
        "J",
        "K",
        "L",
        "Z",
        "X",
        "C",
        "V",
        "B",
        "N",
        "M"
    )
    private val keysMap2 = arrayOf(
        "q",
        "w",
        "e",
        "r",
        "t",
        "y",
        "u",
        "i",
        "o",
        "p",
        "a",
        "s",
        "d",
        "f",
        "g",
        "h",
        "j",
        "k",
        "l",
        "z",
        "x",
        "c",
        "v",
        "b",
        "n",
        "m"
    )
    private val keysMap3 = arrayOf(
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "0",
        "~",
        "!",
        "@",
        "#",
        "$",
        "%",
        "/",
        "^",
        "&",
        "*",
        "(",
        ")",
        "_",
        "+",
        "[",
        "]"
    )
    private var currentMapNum = 0
    private var currentKeysMap = keysMap1
    private var mContext: Context? = null
    private var wifiPassword: TextView? = null
    private var wifiName: TextView? = null
    private var contentPassword: String? = ""
    private var wifiScanner: WifiScanner? = null
    private var wifiSpinner: Spinner? = null
    private var isExpanded = false
    private var rlWifi: RelativeLayout? = null
    private var mUpdateViewHandler: UpdateViewHandler? = null
    private lateinit var alertDialog: AlertDialog

    constructor(context: Context) : super(context) {
        iniKeyBoardView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        iniKeyBoardView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        iniKeyBoardView(context)
    }


    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        iniKeyBoardView(context)
    }

    private fun iniKeyBoardView(context: Context) {
        this.mContext = context
        inflate(context, R.layout.keyboard_view_new, this)
        initManager()
        initView(context)
        initKeyBoardData()
        initKeyData(context)
        setOnClickListeners()
    }

    private fun initManager() {
        wifiScanner = WifiScanner(mContext!!)
        mUpdateViewHandler = UpdateViewHandler(mContext!!)
    }

    private fun setOnClickListeners() {
        for (i in buttonList.indices) {
            val finalI = i
            buttonList[i]!!.setOnClickListener { v ->
                val buttonText = (v as Button).text.toString() + ""
                Log.e("letianpai_onClick", finalI.toString() + "_" + buttonText)
                //                    Toast.makeText(mContext,buttonText,Toast.LENGTH_SHORT).show();
                Log.e("letianpai_onClick", "contentPassword.length: " + contentPassword!!.length)
                if (contentPassword!!.length < 20) {
                    contentPassword = contentPassword + buttonText
                    wifiPassword!!.text = contentPassword
                } else {
//                        Toast.makeText(mContext,"密码不能超过20位",Toast.LENGTH_SHORT).show();
                    updatePassword(contentPassword)
                }
            }
        }

        kDelete!!.setOnClickListener {
            contentPassword = removeLastByte(contentPassword)
            wifiPassword!!.text = contentPassword
        }
        //        kSpace2.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                contentPassword = removeLastByte(contentPassword);
//                wifiPassword.setText(contentPassword);
//            }
//        });
        kDown!!.setOnClickListener {
            currentMapNum = currentMapNum + 1
            Log.e("letianpai_onClick", "currentMapNum: $currentMapNum")
            if (currentMapNum > 2) {
                currentMapNum = 0
            }
            Log.e("letianpai_onClick", "currentMapNum1: $currentMapNum")
            if (currentMapNum == 0) {
                Log.e("letianpai_onClick", "currentMapNum2: $currentMapNum")
                currentKeysMap = keysMap1
            } else if (currentMapNum == 1) {
                Log.e("letianpai_onClick", "currentMapNum3: $currentMapNum")
                currentKeysMap = keysMap2
            } else if (currentMapNum == 2) {
                Log.e("letianpai_onClick", "currentMapNum4: $currentMapNum")
                currentKeysMap = keysMap3
            }
            initKeyData(mContext)
        }
        kUp!!.setOnClickListener {
            currentMapNum = currentMapNum - 1
            if (currentMapNum < 0) {
                currentMapNum = 2
            }
            if (currentMapNum == 0) {
                currentKeysMap = keysMap1
            } else if (currentMapNum == 1) {
                currentKeysMap = keysMap2
            } else if (currentMapNum == 2) {
                currentKeysMap = keysMap3
            }
            initKeyData(mContext)
        }

        //        kDelete.setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                contentPassword = "";
//                updatePassword(contentPassword);
//
//
//                return true;
//            }
//        });
//        k15.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("letianpai_onClick", "==========================");
//                String buttonText = ((Button) v).getText() + "";
//                Log.e("letianpai_onClick", "_" + buttonText);
//            }
//        });
        ivSelectWifi!!.setOnClickListener { showWifiSelectDialog() }
        wifiName!!.setOnClickListener { showWifiSelectDialog() }

        //        wifiSpinner.setOnItemSelectedListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position ==0){
//                    wifiSpinner.setDropDownWidth(250);
//                }else{
//                    wifiSpinner.setDropDownWidth(45);
//                }
//            }
//        });
        wifiSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                isExpanded = position == 0
                if (position == 0 && isExpanded) {
                    resizeRelativeLayoutViewHeightSize(
                        rlWifi!!, 200
                    )
                    //                    view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250)); // 设置展开高度为 400dp
                } else {
                    resizeRelativeLayoutViewHeightSize(
                        rlWifi!!, 40
                    )
                    //                    view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40)); // 设置默认高度为 40dp
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                isExpanded = false
            }
        }
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
            if (!TextUtils.isEmpty(wifiList[i].SSID)) {
                finalWifiList.add(wifiList[i])
            }
        }
        createListDialog2(mContext, finalWifiList)
    }


    //
    //    // 创建自定义的 ArrayAdapter
    //    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_dropdown_item, data) {
    //        @Override
    //        public View getDropDownView(int position, View convertView, ViewGroup parent) {
    //            View view = super.getDropDownView(position, convertView, parent);
    //
    //            // 根据选中项动态设置下拉列表项的高度
    //            if (position == 0 && isExpanded) {
    //                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400)); // 设置展开高度为 400dp
    //            } else {
    //                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40)); // 设置默认高度为 40dp
    //            }
    //
    //            return view;
    //        }
    //    };
    //
    //    final ArrayAdapter<String> adapter = new WifiAdapter(mContext, wifiList) {
    //        @Override
    //        public View getDropDownView(int position, View convertView, ViewGroup parent) {
    //            View view = super.getDropDownView(position, convertView, parent);
    //
    //            // 根据选中项动态设置下拉列表项的高度
    //            if (position == 0 && isExpanded) {
    //                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400)); // 设置展开高度为 400dp
    //            } else {
    //                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40)); // 设置默认高度为 40dp
    //            }
    //
    //            return view;
    //        }
    //    };
    private fun showWifiList(wifiList: List<ScanResult>) {
//        wifiSpinner.setDropDownWidth(300);
//        wifiSpinner.setDropDownHorizontalOffset(100);
//        wifiSpinner.setDropDownVerticalOffset(100);
//        final WifiAdapter adapter = new WifiAdapter(mContext,wifiList);
//        wifiSpinner.setAdapter(adapter);
//        wifiSpinner.notify();
//        wifiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                Log.e("letianpai",((ScanResult)adapter.getItem(pos)).toString());
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
        val adapter = WifiAdapter(mContext, wifiList)
        wifiSpinner!!.adapter = adapter
    }

    private fun updatePassword(password: String?) {
        if (TextUtils.isEmpty(password)) {
        } else {
            if (contentPassword!!.length > 20) {
                Toast.makeText(mContext, "密码不能超过20位", Toast.LENGTH_SHORT).show()
            } else {
                wifiPassword!!.text = contentPassword
            }
        }
    }

    private fun initKeyBoardData() {
        buttonList.clear()
        buttonList.add(k11)
        buttonList.add(k12)
        buttonList.add(k13)
        buttonList.add(k14)
        buttonList.add(k15)
        buttonList.add(k16)
        buttonList.add(k17)
        buttonList.add(k18)
        buttonList.add(k19)
        buttonList.add(k110)

        buttonList.add(k21)
        buttonList.add(k22)
        buttonList.add(k23)
        buttonList.add(k24)
        buttonList.add(k25)
        buttonList.add(k26)
        buttonList.add(k27)
        buttonList.add(k28)
        buttonList.add(k29)

        buttonList.add(k31)
        buttonList.add(k32)
        buttonList.add(k33)
        buttonList.add(k34)
        buttonList.add(k35)
        buttonList.add(k36)
        buttonList.add(k37)
    }

    private fun initKeyData(context: Context?) {
        for (i in buttonList.indices) {
            if (currentMapNum == 1) {
                currentKeysMap[i] = currentKeysMap[i].lowercase(Locale.getDefault())
            }

            buttonList[i]!!.text = currentKeysMap[i]
        }
    }

    private fun initView(context: Context) {
        k11 = findViewById(R.id.k11)
        k12 = findViewById(R.id.k12)
        k13 = findViewById(R.id.k13)
        k14 = findViewById(R.id.k14)
        k15 = findViewById(R.id.k15)
        k16 = findViewById(R.id.k16)
        k17 = findViewById(R.id.k17)
        k18 = findViewById(R.id.k18)
        k19 = findViewById(R.id.k19)
        k110 = findViewById(R.id.k110)

        k21 = findViewById(R.id.k21)
        k22 = findViewById(R.id.k22)
        k23 = findViewById(R.id.k23)
        k24 = findViewById(R.id.k24)
        k25 = findViewById(R.id.k25)
        k26 = findViewById(R.id.k26)
        k27 = findViewById(R.id.k27)
        k28 = findViewById(R.id.k28)
        k29 = findViewById(R.id.k29)

        k31 = findViewById(R.id.k31)
        k32 = findViewById(R.id.k32)
        k33 = findViewById(R.id.k33)
        k34 = findViewById(R.id.k34)
        k35 = findViewById(R.id.k35)
        k36 = findViewById(R.id.k36)
        k37 = findViewById(R.id.k37)

        kUp = findViewById(R.id.kUp)
        kDown = findViewById(R.id.kDown)
        kDelete = findViewById(R.id.kDelete)

        kSpace1 = findViewById(R.id.space_1)
        kSpace2 = findViewById(R.id.space_2)

        wifiName = findViewById(R.id.tv_selected_wifi)
        wifiPassword = findViewById(R.id.wifi_password)
        ivSelectWifi = findViewById(R.id.ivSelectWifi)
        wifiSpinner = findViewById(R.id.sp_wifi_list)

        rlWifi = findViewById(R.id.rl_wifi_list)
    }

    private fun createListDialog(context: Context, wifiList: List<ScanResult>) {
        // 通过LayoutInflater加载自定义布局

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_list, null)

        // 初始化ListView
        val listView = view.findViewById<ListView>(R.id.listView)

        // 准备数据
//        String[] items = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};

        // 创建适配器
        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, wifiList)

        // 设置适配器
        listView.adapter = adapter

        // 设置ListView的点击事件
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent: AdapterView<*>?, view1: View?, position: Int, id: Long ->
                // 处理点击事件
                val selectedItem = adapter.getItem(position)
            }

        // 创建AlertDialog.Builder并设置布局
        val builder = AlertDialog.Builder(context)
        builder.setView(view)

        //
//        // 设置取消按钮
//        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());

        // 创建并显示AlertDialog
        val alertDialog = builder.create()
        alertDialog.show()
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

    private inner class UpdateViewHandler(context: Context) : Handler() {
        private val context = WeakReference(context)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                UPDATE_WIFI_NAME -> updateWifiDisplayName(msg)
            }
        }
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
        wifiName!!.text = msg.obj as String
        if (alertDialog != null && alertDialog!!.isShowing) {
            alertDialog!!.dismiss()
        }
    }

    companion object {
        private fun removeLastByte(str: String?): String? {
            if (str == null || str.isEmpty()) {
                return str // 返回原始字符串，如果为空或长度为0
            }

            // 使用substring删除最后一个字节
            return str.substring(0, str.length - 1)
        }

        private const val UPDATE_WIFI_NAME = 1
        private const val UPDATE_BUTTON_DISPLAY = 2
    }
}
