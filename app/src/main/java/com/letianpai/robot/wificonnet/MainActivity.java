package com.letianpai.robot.wificonnet;


import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.letianpai.robot.components.network.nets.GeeUiNetManager;
import com.letianpai.robot.components.storage.RobotSubConfigManager;
import com.letianpai.robot.wificonnet.callback.BleConnectStatusCallback;
import com.letianpai.robot.wificonnet.parser.BindStatusInfo;
import com.letianpai.robot.wificonnet.system.SystemUtil;
import com.letianpai.robot.wificonnet.util.FunctionUtils;
import com.letianpai.robot.wificonnet.view.AutoConnectWifiView;
import com.letianpai.robot.wificonnet.view.KeyBoardView;
import com.letianpai.robot.wificonnet.view.PairingCodeKeyboardView;
import com.letianpai.robot.wificonnet.view.PairingInfoView;
import com.letianpai.robot.wificonnet.view.TwelveGridKeyboardView;
import com.letianpai.robot.wificonnet.view.WifiConnectOtaView;
import com.letianpai.robot.wificonnet.wifi.WIFIStateReceiver;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author liujunbin
 */
public class MainActivity extends Activity {
    private static final int SHOW_CONNECTING = 1;
    private static final int WIFI_CONNECT_SUCCESS = 2;
    private static final int WIFI_CONNECT_FAILED = 3;
    private static final int GET_BIND_STATUS = 5;
    private static final int GET_ROBOT_BIND_STATUS = 13;
    private static final int UPDATE_ROBOT_LANGUAGE = 6;
    private static final int CLOSE_DEVICE = 7;
    private static final int UPDATE_ROBOT_VIEW = 8;
    private static final int SKIP_QRCODE_VIEW = 9;
    private static final int SHOW_PAIR_CODE = 10;
    private static final int GET_BIND_STATUS_TO_INFO_OR_SKIP_TO_MAIN = 11;
    private static final int CLOSE_WIFI = 12;
    //            private static final long CLOSE_ROBOT_TIME = 20 *1000;
//            private static final long CLOSE_ROBOT_TIME = 10 *1000;
    private static final long CLOSE_ROBOT_TIME = 5 * 60 * 1000;
    private KeyBoardView keyBoardView;
    private TwelveGridKeyboardView twelveGridKeyboardView;
    //    private QRCodeView qrCodeView;
    private AutoConnectWifiView autoConnectWifiView;
    private WifiConnectOtaView wifiConnectOtaView;
    private PairingCodeKeyboardView pairingCodeKeyboardView;
    private PairingInfoView pairingInfoView;
    private UpdateViewHandler updateViewHandler;
    private boolean isGetValidBindInfo = false;
    private boolean isGetRobotBindStatus = false;
    private static final String GLOBAL = "global";
    private static final String CN = "cn";
    private static final String OPEN_FROM = "from";
    private static final String OPEN_FROM_START = "from_open_robot";
    private String openAppFrom = "";
    private String wifiName;
    private boolean hadRequestOta = false;
    private boolean hadSkipToQrCodeView = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerWIFIStateReceiver();
        openAppFrom = getIntent().getStringExtra(OPEN_FROM);
        setContentView(R.layout.activity_main);
        SystemUtil.setAppLanguage(MainActivity.this);
        updateViewHandler = new UpdateViewHandler(MainActivity.this);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        initViews();
        addListeners();
        if (isFromOpenRobot()) {
            closeRobot();
        }

    }

    private void initViews() {
        //        keyBoardView = findViewById(R.id.keyboard);
        twelveGridKeyboardView = findViewById(R.id.twelveGridKeyboardView);
        if (!RobotSubConfigManager.getInstance(MainActivity.this).isNeedRegisterWifi()) {
            twelveGridKeyboardView.setVisibility(View.GONE);
        }
        autoConnectWifiView = findViewById(R.id.autoConnectWifiView);
//        qrCodeView = findViewById(R.id.qrCodeView);
        wifiConnectOtaView = findViewById(R.id.wifiConnectOtaView);
        pairingCodeKeyboardView = findViewById(R.id.pairingCodeKeyboardView);
        pairingInfoView = findViewById(R.id.pairingInfoView);

    }

    private void addListeners() {
        BleConnectStatusCallback.getInstance().registerBleConnectStatusListener(new BleConnectStatusCallback.BleConnectStatusChangedListener() {
            @Override
            public void onBleConnectStatusChanged(int connectStatus) {
                if (BleConnectStatusCallback.BLE_STATUS_CONNECTING_NET == connectStatus) {
                    showConnecting();
                } else if (BleConnectStatusCallback.BLE_STATUS_CONNECTED_ANIMATION_PLAY_END == connectStatus) {

                    connectSuccess();
                } else if (BleConnectStatusCallback.BLE_STATUS_CONNECTED_FAILED_ANIMATION_PLAY_END == connectStatus) {
                    connectFailed();
                }

            }
        });

    }

    private WIFIStateReceiver mWIFIStateReceiver;

    public void registerWIFIStateReceiver() {
        if (!RobotSubConfigManager.getInstance(MainActivity.this).isNeedRegisterWifi()) {
            return;
        }
        if (mWIFIStateReceiver == null) {
            mWIFIStateReceiver = new WIFIStateReceiver(MainActivity.this);
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(mWIFIStateReceiver, filter);
        }
    }

    public void unregisterWIFIStateReceiver() {
        if (mWIFIStateReceiver != null) {
            unregisterReceiver(mWIFIStateReceiver);
            mWIFIStateReceiver = null;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (wifiConnectOtaView.getVisibility() == View.VISIBLE) {
            skipToQRCodeView();
            if (isFromOpenRobot()) {
                closeRobot();
            }
        }
//        getCountryInfoByIP();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterWIFIStateReceiver();
    }

    public void updateDeviceBindStatus() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isDeviceBind(true);
            }
        }).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000);
//                    Log.e("RemoteCmdResponser", "commandDistribute:command ========= 2222 ======== info: ");
//                    isDeviceBind(false);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();

    }

    public void getRobotDeviceBindStatus() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isRobotDeviceBind();
            }
        }).start();

    }

    public void isDeviceBind(boolean isChinese) {
        GeeUiNetManager.isDeviceBind1(MainActivity.this, isChinese, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                skipToQRCodeView();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response != null && response.body() != null) {
                    String info = "";
                    if (response != null && response.body() != null) {
                        info = response.body().string();

                        BindStatusInfo bindStatusInfo;
                        try {
                            if (info != null) {
                                bindStatusInfo = new Gson().fromJson(info, BindStatusInfo.class);
                                if (bindStatusInfo != null && bindStatusInfo.getData() != null) {
                                    if (bindStatusInfo.getData().isIs_bind() == true && !TextUtils.isEmpty(bindStatusInfo.getData().getCountry())) {
                                        updateView(bindStatusInfo.getData().getCountry());

                                    } else {
                                        //TODO
                                        skipToQRCodeView();
                                    }
                                } else {
                                    //TODO
                                    skipToQRCodeView();
                                }

                            } else {
                                //TODO
                                skipToQRCodeView();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //TODO
                            skipToQRCodeView();
                        }
                    } else {
                        //TODO
                        skipToQRCodeView();
                    }


                } else {
                    //TODO
                    skipToQRCodeView();
                }
            }
        });
    }

    public void isRobotDeviceBind() {
        GeeUiNetManager.isDeviceBind1(MainActivity.this, SystemUtil.isInChinese(), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                //TODO 跳转到OTA
                wifiConnectOtaView.startRequestOTA();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (isGetRobotBindStatus){
                    return;
                }
                isGetRobotBindStatus = true;
                if (response == null || response.body() == null){
                    //TODO 跳转到OTA
                    wifiConnectOtaView.startRequestOTA();
                    return;
                }
                String info = "";
                info = response.body().string();
                Log.e("letianpai","isRobotDeviceBind： "+ info);
                BindStatusInfo bindStatusInfo;
                try {
                    if (info == null){
                        //TODO 跳转到OTA
                        wifiConnectOtaView.startRequestOTA();
                        return;
                    }
                    bindStatusInfo = new Gson().fromJson(info, BindStatusInfo.class);
                    if (bindStatusInfo == null || bindStatusInfo.getData() == null){
                        //TODO 跳转到OTA
                        wifiConnectOtaView.startRequestOTA();
                        return;
                    }
                    if (bindStatusInfo.getData().isIs_bind() == true && !TextUtils.isEmpty(bindStatusInfo.getData().getCountry())) {
                        updateView(bindStatusInfo.getData().getCountry());
                    }else{
                        //TODO 跳转到OTA
                        wifiConnectOtaView.startRequestOTA();
                        return;
                    }

                }catch (Exception e){
                    //TODO 跳转到OTA
                    wifiConnectOtaView.startRequestOTA();
                    return;
                }
            }
        });
    }


    private void responseLanguageChange(String localInfo) {
        Locale locale;
        if (localInfo.equals(GLOBAL)) {
            locale = new Locale("en");
            SystemUtil.set(SystemUtil.REGION_LANGUAGE, "en");
        } else {
            locale = new Locale("zh");
            SystemUtil.set(SystemUtil.REGION_LANGUAGE, "zh");
        }
        SystemUtil.setRobotActivated();
        //切换语言
        try {
            Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
            Method getDefault = activityManagerNative.getMethod("getDefault");
            Object iActivityManager = getDefault.invoke(activityManagerNative);

            Class<?> configurationClass = Class.forName("android.content.res.Configuration");
            Constructor<?> configurationConstructor = configurationClass.getConstructor();
            Object configuration = configurationConstructor.newInstance();

            Method setLocale = configurationClass.getMethod("setLocale", Locale.class);
            setLocale.invoke(configuration, locale);

            Field userSetLocale = configurationClass.getField("userSetLocale");
            userSetLocale.setBoolean(configuration, true);

            // Method updateConfiguration = iActivityManager.getClass().getMethod("updateConfiguration", configurationClass);
            // updateConfiguration.invoke(iActivityManager, configuration);

            Method updatePersistentConfiguration = iActivityManager.getClass().getMethod("updatePersistentConfiguration", configurationClass);
            updatePersistentConfiguration.invoke(iActivityManager, configuration);

            Class<?> backupManagerClass = Class.forName("android.app.backup.BackupManager");
            Method dataChanged = backupManagerClass.getMethod("dataChanged", String.class);
            dataChanged.invoke(null, "com.android.providers.settings");

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            //TODO 打开Launcher主界面
            updateViewHandler.removeMessages(CLOSE_DEVICE);
//            qrCodeView.setVisibility(View.GONE);

            openLauncherMainView();
            finish();
            closeWifiApp();
        }


    }

    private void updateRobotData(String localInfo) {

        Message message = new Message();
        message.what = UPDATE_ROBOT_LANGUAGE;
        message.obj = localInfo;
        updateViewHandler.sendMessage(message);
    }

    private void openLauncherMainView() {
        if (!isGetValidBindInfo) {
            isGetValidBindInfo = true;
            RobotSubConfigManager.getInstance(MainActivity.this).setOpenMainViewTime(System.currentTimeMillis());
            RobotSubConfigManager.getInstance(MainActivity.this).commit();
            String START_FROM = "from";
            String START_FROM_WIFI_CONNECTOR = "wifi_connector";
            String className = "com.renhejia.robot.launcher.main.activity.LeTianPaiMainActivity";
            String packageName = "com.renhejia.robot.launcher";
            Intent intent = new Intent();
            intent.putExtra(START_FROM, START_FROM_WIFI_CONNECTOR);
            intent.setComponent(new ComponentName(packageName, className));
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }


    }

    private void responseSkipToBindInfoView() {
        wifiConnectOtaView.setVisibility(View.GONE);
//        qrCodeView.setVisibility(View.VISIBLE);
        pairingInfoView.setVisibility(View.VISIBLE);

        pairingCodeKeyboardView.setVisibility(View.GONE);
        twelveGridKeyboardView.setVisibility(View.GONE);
        autoConnectWifiView.setVisibility(View.GONE);


    }

    private class UpdateViewHandler extends Handler {
        private final WeakReference<Context> context;

        public UpdateViewHandler(Context context) {
            this.context = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case SHOW_CONNECTING:
                    responseConnecting();
                    break;

                case WIFI_CONNECT_SUCCESS:
                    responseConnectSuccess();
                    break;

                case WIFI_CONNECT_FAILED:
                    responseConnectFailed();
                    break;

                case GET_BIND_STATUS:
                    if (!isGetValidBindInfo) {
                        updateDeviceBindStatus();
//                        getBindStatusDelay();
                    }
                    break;
                case GET_ROBOT_BIND_STATUS:
                    if (!isGetRobotBindStatus) {
                        getRobotDeviceBindStatus();
                    }
                    break;

                case UPDATE_ROBOT_LANGUAGE:
                    responseLanguageChange((String) (msg.obj));
                    break;

                case CLOSE_DEVICE:
                    FunctionUtils.shutdownRobot(MainActivity.this);
                    break;

                case UPDATE_ROBOT_VIEW:
                    String country = (String) msg.obj;
                    if (!TextUtils.isEmpty(country)) {
                        updateRobotView(country);
                    }

                    break;

                case SKIP_QRCODE_VIEW:
                    responseSkipToBindInfoView();
                    break;

                case GET_BIND_STATUS_TO_INFO_OR_SKIP_TO_MAIN:
                    responseGetBindStatusInfoOrSkipToMain();
                    break;

                case SHOW_PAIR_CODE:
                    responseShowPairCodeView();
                    break;

                case CLOSE_WIFI:
                    responseCloseWifi();
                    break;

            }
        }
    }

    private void responseCloseWifi() {
        System.exit(0);
    }

    private void responseGetBindStatusInfoOrSkipToMain() {
        getBindStatus();
    }

    private void responseShowPairCodeView() {
        pairingCodeKeyboardView.setVisibility(View.VISIBLE);
        pairingInfoView.setVisibility(View.GONE);
        wifiConnectOtaView.setVisibility(View.GONE);
//        qrCodeView.setVisibility(View.GONE);
        twelveGridKeyboardView.setVisibility(View.GONE);
        autoConnectWifiView.setVisibility(View.GONE);
//        getBindStatus();

    }

    private void updateRobotView(String country) {
        wifiConnectOtaView.setVisibility(View.GONE);
//        qrCodeView.setVisibility(View.GONE);
        pairingInfoView.setVisibility(View.GONE);
        twelveGridKeyboardView.setVisibility(View.GONE);
        autoConnectWifiView.setVisibility(View.GONE);
        pairingCodeKeyboardView.setVisibility(View.GONE);
        updateRobotData(country);

    }

    private void responseConnectFailed() {
        autoConnectWifiView.setVisibility(View.GONE);
        twelveGridKeyboardView.cleanWifiPassword();
        twelveGridKeyboardView.setVisibility(View.VISIBLE);
        pairingInfoView.setVisibility(View.GONE);
        pairingCodeKeyboardView.setVisibility(View.GONE);
//        qrCodeView.setVisibility(View.GONE);

    }

    private void responseConnectSuccess() {
        String wifiName = twelveGridKeyboardView.getmWifiName();
        String password = twelveGridKeyboardView.getContentPassword();
        if (!TextUtils.isEmpty(wifiName)) {
            SystemUtil.set("persist.sys.ssid", wifiName);
            SystemUtil.set("persist.sys.password", password);
        }

        autoConnectWifiView.setVisibility(View.GONE);
        twelveGridKeyboardView.setVisibility(View.GONE);
        if (isFromOpenRobot()) {
//            if (qrCodeView.getVisibility() != View.VISIBLE){
//                wifiConnectOtaView.setVisibility(View.VISIBLE);
//            }
            if (pairingInfoView.getVisibility() != View.VISIBLE && pairingCodeKeyboardView.getVisibility() != View.VISIBLE && !hadRequestOta) {
                hadRequestOta = true;
                wifiConnectOtaView.setVisibility(View.VISIBLE);
                //TODO 在此更新本地判断逻辑
//                wifiConnectOtaView.startRequestOTA();
                getRobotBindStatus();
//                wifiConnectOtaView.startRequestOTA();
            } else {
                skipToQRCodeView();
            }
        } else {
            finish();
            System.exit(0);
        }
//        if (isFromOpenRobot()) {
//            qrCodeView.setVisibility(View.VISIBLE);
//            getBindStatus();
//        } else {
//            finish();
//        }
    }

    private void responseConnecting() {
        autoConnectWifiView.setVisibility(View.VISIBLE);
        twelveGridKeyboardView.setVisibility(View.GONE);

    }

    private void connectFailed() {
        Message message = new Message();
        message.what = WIFI_CONNECT_FAILED;
        updateViewHandler.sendMessage(message);
    }

    private void connectSuccess() {
        Message message = new Message();
        message.what = WIFI_CONNECT_SUCCESS;
        updateViewHandler.sendMessage(message);
    }

    private void showConnecting() {
        Message message = new Message();
        message.what = SHOW_CONNECTING;
        updateViewHandler.sendMessage(message);
    }

    public void updateView(String name) {
        Message message = new Message();
        message.what = UPDATE_ROBOT_VIEW;
        message.obj = name;
        updateViewHandler.sendMessage(message);
    }

    private void closeRobot() {
        Message message = new Message();
        message.what = CLOSE_DEVICE;
        updateViewHandler.sendMessageDelayed(message, CLOSE_ROBOT_TIME);
//        updateViewHandler.removeMessages(CLOSE_DEVICE);
    }

    private void getBindStatus() {
        Message message = new Message();
        message.what = GET_BIND_STATUS;
        updateViewHandler.sendMessage(message);
    }

    private void getRobotBindStatus() {
        Message message = new Message();
        message.what = GET_ROBOT_BIND_STATUS;
        updateViewHandler.sendMessage(message);
    }

    private void getBindStatusDelay() {
        Message message = new Message();
        message.what = GET_BIND_STATUS;
        updateViewHandler.sendMessageDelayed(message, 4000);
    }

    public void skipToQRCodeView() {
        if (!hadSkipToQrCodeView) {
            hadSkipToQrCodeView = true;
            Message message = new Message();
            message.what = SKIP_QRCODE_VIEW;
            updateViewHandler.sendMessage(message);
        }

    }

    public void getBindStatusInfoOrSkipToMain() {
        Message message = new Message();
        message.what = GET_BIND_STATUS_TO_INFO_OR_SKIP_TO_MAIN;
        updateViewHandler.sendMessage(message);
    }

    public void showPairCodeView() {
        Message message = new Message();
        message.what = SHOW_PAIR_CODE;
        updateViewHandler.sendMessage(message);
    }

    public void closeWifiApp() {
        Message message = new Message();
        message.what = CLOSE_WIFI;
        updateViewHandler.sendMessageDelayed(message, 10 * 1000);
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getWifiName() {
        return wifiName;
    }

    public boolean isFromOpenRobot() {
        if (TextUtils.isEmpty(openAppFrom)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPairCodeViewVisible() {
        if (pairingCodeKeyboardView.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isAutoConnectViewVisible() {
        if (autoConnectWifiView.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }

    }

    public void removeCloseRobot() {
        updateViewHandler.removeMessages(CLOSE_DEVICE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterWIFIStateReceiver();
    }
}