package com.letianpai.robot.wificonnet.view

import android.app.Dialog
import android.content.Context
import android.net.wifi.ScanResult
import android.view.View
import android.widget.ListView
import com.letianpai.robot.wificonnet.R

class wifiSelectDialog(context: Context, var robotWifiList: List<ScanResult>) : Dialog(context) {
    private val wifiListView: ListView? = null
    private val adapter: WifiAdapter? = null

    //    public wifiSelectDialog(Context context) {
    //        super(context);
    //        initsView(context);
    //    }
    init {
        initsView(context)
    }

    //    public wifiSelectDialog(Context context, int themeResId) {
    //        super(context, themeResId);
    //        initsView(context);
    //    }
    //
    //    protected wifiSelectDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
    //        super(context, cancelable, cancelListener);
    //        initsView(context);
    //    }
    private fun initsView(context: Context) {
        val view = View.inflate(context, R.layout.wifi_dialog, null)
    }
}
