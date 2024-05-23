package com.letianpai.robot.wificonnet.view;

import android.app.AlertDialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.letianpai.robot.components.charging.ChargingUpdateCallback;
import com.letianpai.robot.components.network.nets.GeeUINetworkUtil;
import com.letianpai.robot.components.network.nets.GeeUiNetManager;
import com.letianpai.robot.components.network.system.SystemUtil;
import com.letianpai.robot.components.parser.base.BaseMessageInfo;
import com.letianpai.robot.components.storage.RobotSubConfigManager;
import com.letianpai.robot.components.utils.GeeUILogUtils;
import com.letianpai.robot.components.utils.SDCardUtil;
import com.letianpai.robot.components.utils.VolumeManager;
import com.letianpai.robot.wificonnet.MainActivity;
import com.letianpai.robot.wificonnet.R;
import com.letianpai.robot.wificonnet.adapter.CustomAdapter;
import com.letianpai.robot.wificonnet.callback.BleConnectStatusCallback;
import com.letianpai.robot.wificonnet.callback.KeyPressCallback;
import com.letianpai.robot.wificonnet.manager.WifiScanner;
import com.letianpai.robot.wificonnet.parser.RobotBindInfo;
import com.letianpai.robot.wificonnet.util.FunctionUtils;
import com.letianpai.robot.wificonnet.util.HardCodeUtils;
import com.letianpai.robot.wificonnet.util.KeyConsts;
import com.letianpai.robot.wificonnet.util.KeysMapUtils;
import com.letianpai.robot.wificonnet.wifi.WIFIAutoConnectionService;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author liujunbin
 */
public class PairingCodeKeyboardView extends LinearLayout {
    private Context mContext;
    private KeyImageButton kbLk1, kbLk2, kbRk1, kbRk2,kbBk1,kbBk2;
    private RecyclerView glKeys;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private TextView wifiPassword;
    private TextView robotName;
    private CommitButton ivWifiCommit;
    public static final int UPDATE_WIFI_PASSWORD = 102;
    public static final int CLEAN_WIFI_PASSWORD = 103;
    private UpdateViewHandler mUpdateViewHandler;
    private AlertDialog alertDialog;
    private String contentPassword = "";
    private int currentKeypadPosition = 0;
    private WifiScanner wifiScanner;
    private String mWifiName;
    private Callback bindRobotCallback;


    public PairingCodeKeyboardView(Context context) {
        super(context);
        init(context);
    }

    public PairingCodeKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PairingCodeKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PairingCodeKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(Context context) {
        this.mContext = context;
        initView();
    }

    private void initView() {
        inflate(mContext, R.layout.keyboard_pair, this);
        initViews();
        initCallback();
        initFunctionButtonData();
        setData();
        addKeyPressedListener();
    }

    private void initCallback() {
        bindRobotCallback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.e("letianpai", "bindRobotCallback ========== 0 =========" );
                if (response != null && response.body() != null) {
                    String info = "";
                    if (response != null && response.body() != null) {
                        info = response.body().string();
                    }
                    RobotBindInfo robotBindInfo;
                    if (info != null) {
                        try{
                            Log.e("letianpai", "bindRobotCallback" + info);
                            robotBindInfo = new Gson().fromJson(info, RobotBindInfo.class);
                            if (robotBindInfo != null && robotBindInfo.getCode() == 0 && robotBindInfo.getData() != null && !TextUtils.isEmpty(robotBindInfo.getData().getCountry())) {
                                    ((MainActivity)mContext).updateView(robotBindInfo.getData().getCountry());
                            }else{

                                cleanWifiPassword();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
    }

    private void initFunctionButtonData() {
        kbLk1.setEnable(false);
        kbLk1.setImagePosition(KeyConsts.CENTER_VERTICAL,30,20,16,16,36,36);
        kbLk1.setUnPressedImage(R.drawable.keypad_icon);


        kbRk2.setImagePosition(KeyConsts.LEFT_TOP,12,17,0,0,36,36);
        kbRk2.setPressedImage(R.drawable.fun_delete_pressed);
        kbRk2.setUnPressedImage(R.drawable.fun_delete_unpressed);

    }

    private void addKeyPressedListener() {
        KeyPressCallback.getInstance().setKeyPressCallbackListener(new KeyPressCallback.KeyPressCallbackListener() {
            @Override
            public void onKeyPressed(int keyType, String keyValue) {
                if (((MainActivity)mContext).isPairCodeViewVisible()){
                    updateWifiPassword(keyType, keyValue);
                }
            }
        });
        kbLk1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        kbLk2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        kbRk1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                responseSwitch();

            }
        });
        kbRk2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                responseDelete();

            }
        });
        kbBk1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        kbBk2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ivWifiCommit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("letianpai", "ivWifiCommit ======= " );
//                if (TextUtils.isEmpty(mWifiName)){
//                    Toast.makeText(mContext,"Wi-Fi未设置",Toast.LENGTH_LONG).show();
//                }else{
//                    connectWifi();
//                }
                bindRobotWithCode();

            }
        });

    }


    private void responseDelete() {
        contentPassword = FunctionUtils.removeLastByte(contentPassword);
        wifiPassword.setText(contentPassword);
    }

    private void setData() {
        adapter = new CustomAdapter(KeysMapUtils.getInstance(mContext).getNumericPairingCodeKeyList());
        recyclerView.setAdapter(adapter);
        robotName.setText("Robot-"+ SystemUtil.getLtpLastSn());
    }

    private void initViews() {
        robotName = findViewById(R.id.tv_robot_name);
        ivWifiCommit = findViewById(R.id.iv_wifi_commit);
        wifiPassword = findViewById(R.id.wifi_password);
        wifiPassword.setFocusableInTouchMode(false); // 设置为不可触摸模式
        wifiPassword.setFocusable(true);
        wifiPassword.setCursorVisible(true);

        glKeys = findViewById(R.id.recyclerView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mUpdateViewHandler = new UpdateViewHandler(mContext);

        kbLk1 = findViewById(R.id.lf1);
        kbLk2 = findViewById(R.id.lf2);
        kbRk1 = findViewById(R.id.rf1);
        kbRk2 = findViewById(R.id.rf2);
        kbBk1 = findViewById(R.id.bf1);
        kbBk2 = findViewById(R.id.bf2);



    }

    public void cleanPassword() {
        contentPassword ="";
        wifiPassword.setText(contentPassword);
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

                case UPDATE_WIFI_PASSWORD:
                    updateWifiPassword(msg);
                    break;

                case CLEAN_WIFI_PASSWORD:
                    cleanPassword();
                    break;


            }
        }
    }

    private void updateWifiPassword(Message msg) {

        if (msg.arg1 == KeyPressCallback.KEY_TYPE_VALUE) {
            contentPassword = contentPassword + msg.obj;
        }
        wifiPassword.setPressed(true);
        wifiPassword.setText(contentPassword);

    }

    public void updateWifiPassword(int keyPressType, String updateContent) {
        Message message = new Message();
        message.arg1 = keyPressType;
        message.obj = updateContent;
        message.what = UPDATE_WIFI_PASSWORD;
        mUpdateViewHandler.sendMessage(message);
    }
    public void cleanWifiPassword() {
        Message message = new Message();
        message.what = CLEAN_WIFI_PASSWORD;
        mUpdateViewHandler.sendMessage(message);

    }


    private void bindRobot(String code,Callback callback) {
        Log.e("letianpai", "bindRobotWithCode ========== 04 =========contentPassword: " );
        String country = getCountry(code);
        String mac = SystemUtil.getWlanMacAddress();
        String ts = (System.currentTimeMillis() / 1000) + "";
        String sn = SystemUtil.getLtpSn();
        String deviceSign = HardCodeUtils.getDeviceSign(mac, ts);
        //TODO 增加获取
        HashMap<String ,Object> hashMap = new HashMap();

        hashMap.put("code", code);
        hashMap.put(GeeUINetworkUtil.COUNTRY, country);
        hashMap.put("device_sign", deviceSign);
        hashMap.put("hard_code", "");
        hashMap.put("mac", mac);
        hashMap.put("sn", sn);
        hashMap.put("ts", ts);

        GeeUiNetManager.bindRobot(mContext, isChinese(code), hashMap, callback);
    }

    private void bindRobotWithCode() {
        Log.e("letianpai", "bindRobotWithCode ========== 01 =========" );
        Log.e("letianpai", "bindRobotWithCode ========== 01 =========contentPassword: "+ contentPassword );
        if (TextUtils.isEmpty(contentPassword)){
            Log.e("letianpai", "bindRobotWithCode ========== 02 =========contentPassword: "+ contentPassword );
            Toast.makeText(mContext,R.string.there_is_pair_code,Toast.LENGTH_LONG).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("letianpai", "bindRobotWithCode ========== 03 =========contentPassword: "+ contentPassword );
                bindRobot(contentPassword,bindRobotCallback);
            }
        }).start();
    }

    private String getCountry(String code) {

        int startCode = Integer.valueOf(code.substring(0,1));
        if (FunctionUtils.isEven(startCode)){
            return GeeUINetworkUtil.GLOBAL;
        }else{
            return GeeUINetworkUtil.CN;
        }

    }
    private boolean isChinese(String code) {
        if (getCountry(code).equals(GeeUINetworkUtil.CN)){
            return true;
        }else{
            return false;
        }

    }

}
