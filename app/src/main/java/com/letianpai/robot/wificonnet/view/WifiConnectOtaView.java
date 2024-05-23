package com.letianpai.robot.wificonnet.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.letianpai.robot.components.network.lexnet.callback.SessionTokenUpdateCallback;
import com.letianpai.robot.components.network.lexnet.parser.LexSessionToken;
import com.letianpai.robot.components.network.nets.GeeUiNetManager;
import com.letianpai.robot.components.network.system.SystemUtil;
import com.letianpai.robot.components.parser.tips.Tips;
import com.letianpai.robot.wificonnet.MainActivity;
import com.letianpai.robot.wificonnet.R;
import com.letianpai.robot.wificonnet.parser.CountryInfo;
import com.letianpai.robot.wificonnet.parser.VersionInfo;
import com.letianpai.robot.wificonnet.util.FunctionUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author liujunbin
 */
public class WifiConnectOtaView extends RelativeLayout {

    private Context mContext;
    private WifiConnectorHandler wifiConnectorHandler;
    private final static int REQUEST_OTA = 1;
    private final static int SKIP_OTA = 2;
    private final static int START_OTA = 3;
    private final static int REQUEST_OTA_FAIL = 4;
    private final static int ANIMATION_START = 5;
    private final static int ANIMATION_STOP = 6;
    private final static int REQUEST_COUNTRY_INFO = 7;
    private final static int RESPONSE_COUNTRY_INFO = 8;
    private RotateAnimation rotateAnimation;
    private boolean isStartOTA = false;
    private String countryInfo;
    private static final String GLOBAL = "global";
    private int requestTime = 0;

    private ImageView otaLoading;
    private boolean hasRequestOTA ;

    public WifiConnectOtaView(Context context) {
        super(context);
        init(context);
    }

    public WifiConnectOtaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WifiConnectOtaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public WifiConnectOtaView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        inflate(context, R.layout.wifi_connect_ota_view, this);
        initView();
        addListeners();

    }

    public void startRequestOTA() {
        Log.e("letianpai_ota","startRequestOTA  ============= 1 =========== ");
        if (isStartOTA){
            Log.e("letianpai_ota","startRequestOTA ============= 2 ===========");
            return;
        }
        Log.e("letianpai_ota","startRequestOTA ============= 3 ===========");

        isStartOTA = true;
        startAnimation();
        requestCountryInfo();
        //requestOTA();
    }

    private void skipOTA() {
        Log.e("letianpai_ota","skipOTA ============= 4 =========== ");
        Message message = new Message();
        message.what = SKIP_OTA;
        wifiConnectorHandler.sendMessage(message);
    }

    private void startAnimation() {
        Log.e("letianpai_ota","startAnimation ============= 5 ===========");
        Message message = new Message();
        message.what = ANIMATION_START;
        wifiConnectorHandler.sendMessage(message);
    }

    private void requestOTA() {
        Log.e("letianpai_ota","requestOTA ============= 6 ===========");
        Message message = new Message();
        message.what = REQUEST_OTA;
//        wifiConnectorHandler.sendMessageDelayed(message, 8000);
        wifiConnectorHandler.sendMessageDelayed(message, 2000);
    }

    private void requestCountryInfo() {
        Log.e("letianpai_ota","requestCountryInfo ============= 7 ===========");
        requestTime += 1;
        Message message = new Message();
        message.what = REQUEST_COUNTRY_INFO;
        wifiConnectorHandler.sendMessageDelayed(message, 8000);
    }

    private void requestCountryInfo2000() {
        Log.e("letianpai_ota","requestCountryInfo2000 ============= 8 ===========");
        if (requestTime >5){
            Log.e("letianpai_ota","info----------_2.12");
            skipOTA();
            return;
        }
        Log.e("letianpai_ota","requestCountryInfo2000 ============= 9 ===========");
        requestTime += 1;
        Message message = new Message();
        message.what = REQUEST_COUNTRY_INFO;
        wifiConnectorHandler.sendMessageDelayed(message, 2000);
    }

    private void startOTA() {
        Log.e("letianpai_ota","requestCountryInfo2000 ============= 10 ===========");
        Message message = new Message();
        message.what = START_OTA;
        wifiConnectorHandler.sendMessage(message);

    }
    private void setLocalCountryInfo(String countryInfo) {
        Log.e("letianpai_ota","setLocalCountryInfo ============= 11 ===========");
        Message message = new Message();
        message.what = RESPONSE_COUNTRY_INFO;
        message.obj = countryInfo;
        wifiConnectorHandler.sendMessage(message);
    }

    private void responseSkipOTA() {
        //TODO 在这增加请求绑定状态的逻辑
//        ((MainActivity)(mContext)).skipToQRCodeView();
        Log.e("letianpai_skip","===================== 1 ======================");
        ((MainActivity)(mContext)).getBindStatusInfoOrSkipToMain();
    }

    private void responseRequestOTA() {
        String localRomVersion = FunctionUtils.getRomVersion();
        GeeUiNetManager.getOTAInfo(mContext, SystemUtil.isInChinese(), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("letianpai_ota","info----------_3:_____ onFailure ");
                e.printStackTrace();
                skipOTA();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response != null && response.body() != null) {
                    String info = "";

                    info = response.body().string();
                    if (!TextUtils.isEmpty(info)){
                        Log.e("letianpai_ota","info----------_2: "+ info);
                    }

                    VersionInfo versionInfo;;

                    try {
                        Log.e("letianpai_ota","info----------_2.1-exception: ");
                        if (info != null) {
                            Log.e("letianpai_ota","info----------_2.2-exception: ");
                            versionInfo = new Gson().fromJson(info, VersionInfo.class);
                            Log.e("letianpai_ota","info----------_2.3-exception: versionInfo:"+ versionInfo.toString());
                            if (versionInfo != null && versionInfo.getData() != null && versionInfo.getCode() ==0) {
                                Log.e("letianpai_ota","info----------_2.4-exception: versionInfo.getData(): "+versionInfo.getData().toString());
                                String onlineVersion = versionInfo.getData().getRom_version();
                                Log.e("letianpai_ota","info----------_2.5-exception: onlineVersion: "+ onlineVersion);
                                boolean isStartOTA = FunctionUtils.compareVersion(onlineVersion,localRomVersion);
                                Log.e("letianpai_ota","info----------_2.6-exception: isStartOTA: "+ isStartOTA);
                                if (isStartOTA){
                                    Log.e("letianpai_ota","info----------_2.7-exception: ");
                                    startOTA();
                                }else{
                                    Log.e("letianpai_ota","info----------_2.8-exception: ");
                                    skipOTA();
                                }
                            }else{
                                Log.e("letianpai_ota","info----------_2.9");
                                skipOTA();
                            }

                        }else{
                            Log.e("letianpai_ota","info----------_2.10");
                            skipOTA();
                        }


                    } catch (Exception e) {
                        Log.e("letianpai_ota","info----------_2.11");
                        e.printStackTrace();
                        skipOTA();
                    }
                }else{
                    Log.e("letianpai_ota","info----------_2.12");
                    skipOTA();
                }

            }
        });

    }

    private void responseStartOTA() {
//        Log.e("letianpai_ota","responseStartOTA(): ================= responseStartOTA() ====================");
        ((MainActivity)(mContext)).removeCloseRobot();
        Intent intent = new Intent();
        ComponentName cn = new ComponentName("com.letianpai.otaservice",
                "com.letianpai.otaservice.ota.GeeUpdateService");
        intent.setComponent(cn);
        mContext.startService(intent);
    }

    private void addListeners() {

    }

    private void initView() {
        wifiConnectorHandler = new WifiConnectorHandler(mContext);
        otaLoading = findViewById(R.id.iv_ota_loading);
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(3000);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        otaLoading.startAnimation(rotateAnimation);
    }


    private class WifiConnectorHandler extends Handler {
        private final WeakReference<Context> context;

        public WifiConnectorHandler(Context context) {
            this.context = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case REQUEST_OTA:
                    if (!hasRequestOTA){
                        hasRequestOTA = true;
                        responseRequestOTA();
                    }

                    break;

                case SKIP_OTA:
                    responseSkipOTA();
                    break;

                case START_OTA:
                    responseStartOTA();
                    break;

                case REQUEST_OTA_FAIL:
                    requestOtaFailed();
                    break;

                case ANIMATION_START:
                    startOtaAnimation();
                    break;

                case ANIMATION_STOP:
                    stopOtaAnimation();
                    break;
                    
                case REQUEST_COUNTRY_INFO:
                    getCountryInfoByIP();
                    break;

                case RESPONSE_COUNTRY_INFO:
                    responseSetLocalCountryInfo((String) msg.obj);
                    break;


            }
        }
    }


    public void getCountryInfoByIP() {
//        Log.e("letianpai_ota", "commandDistribute:command ========= 3333 ======== info: ");
        GeeUiNetManager.getCountryByIp(mContext, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requestCountryInfo2000();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response != null && response.body() != null) {
                    String info = "";
                    if (response != null && response.body() != null) {
                        info = response.body().string();
//                        Log.e("letianpai_ota", "getCountryInfoByIP_info: "+ info);
                        CountryInfo countryInfo;
                        try {
                            if (info != null) {
//                                Log.e("letianpai_ota", "getCountryInfoByIP_info: ========= 1 ====== ");
                                countryInfo = new Gson().fromJson(info, CountryInfo.class);
                                if (countryInfo != null  && countryInfo.getCode() == 0 && countryInfo.getData() != null) {
//                                    Log.e("letianpai_ota", "getCountryInfoByIP_info: ========= 2 ====== ");
                                    if (!TextUtils.isEmpty(countryInfo.getData().getCountry())) {
//                                        Log.e("letianpai_ota", "getCountryInfoByIP_info: ========= 3 ====== ");
                                        setLocalCountryInfo(countryInfo.getData().getCountry());
//                                        requestOTA();
                                    }
                                }else{
//                                    requestCountryInfo2000();
                                    skipOTA();
                                }

                            }else {
                                skipOTA();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            skipOTA();
                        }
                    }


                }
            }
        });
    }


    private void responseSetLocalCountryInfo(String countryInfo) {
        if (countryInfo.equals(GLOBAL)) {
            com.letianpai.robot.wificonnet.system.SystemUtil.set(com.letianpai.robot.wificonnet.system.SystemUtil.REGION_LANGUAGE, "en");
        } else {
            com.letianpai.robot.wificonnet.system.SystemUtil.set(com.letianpai.robot.wificonnet.system.SystemUtil.REGION_LANGUAGE, "zh");
        }
        requestOTA();
    }

    private void stopOtaAnimation() {
        rotateAnimation.cancel();

    }

    private void startOtaAnimation() {
        rotateAnimation.start();

    }

    private void requestOtaFailed() {

    }

}
