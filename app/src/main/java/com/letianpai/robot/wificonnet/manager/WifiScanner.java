package com.letianpai.robot.wificonnet.manager;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * @author liujunbin
 */
public class WifiScanner {

    private WifiManager wifiManager;

    public WifiScanner(Context context) {
        // 获取WifiManager实例
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public List<ScanResult> getAvailableWifiList() {
        // 开始WiFi扫描
        wifiManager.startScan();

        // 获取扫描结果列表
        List<ScanResult> scanResults = wifiManager.getScanResults();

        // 返回可用的WiFi列表
        return scanResults;
    }
}