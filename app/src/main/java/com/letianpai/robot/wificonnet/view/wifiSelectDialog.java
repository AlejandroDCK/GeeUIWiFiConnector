package com.letianpai.robot.wificonnet.view;

import android.app.Dialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.View;
import android.widget.ListView;

import com.letianpai.robot.wificonnet.R;

import java.util.List;

public class wifiSelectDialog extends Dialog {

    private ListView wifiListView;
    private WifiAdapter adapter;
    List<ScanResult> robotWifiList;
//    public wifiSelectDialog(Context context) {
//        super(context);
//        initsView(context);
//    }
    public wifiSelectDialog(Context context, List<ScanResult> wifiList) {
        super(context);
        this.robotWifiList = wifiList;
        initsView(context);
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

    private void initsView(Context context) {
        View view = View.inflate(context, R.layout.wifi_dialog, null);


    }

}
