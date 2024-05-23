package com.letianpai.robot.wificonnet.view;

import android.app.AlertDialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
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

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.letianpai.robot.wificonnet.MainActivity;
import com.letianpai.robot.wificonnet.R;
import com.letianpai.robot.wificonnet.adapter.CustomAdapter;
import com.letianpai.robot.wificonnet.callback.BleConnectStatusCallback;
import com.letianpai.robot.wificonnet.callback.KeyPressCallback;
import com.letianpai.robot.wificonnet.manager.WifiScanner;
import com.letianpai.robot.wificonnet.util.FunctionUtils;
import com.letianpai.robot.wificonnet.util.KeyConsts;
import com.letianpai.robot.wificonnet.util.KeysMapUtils;
import com.letianpai.robot.wificonnet.wifi.WIFIAutoConnectionService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liujunbin
 */
public class TwelveGridKeyboardView extends LinearLayout {
    private Context mContext;
    private KeyImageButton kbLk1, kbLk2, kbRk1, kbRk2,kbBk1,kbBk2;
    private RecyclerView glKeys;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private TextView wifiPassword;
    private TextView wifiName;
    private ImageView ivSelectWifi;
    private CommitButton ivWifiCommit;
    public static final int UPDATE_WIFI_NAME = 101;
    public static final int UPDATE_WIFI_PASSWORD = 102;
    public static final int CLEAN_WIFI_PASSWORD = 103;
    private UpdateViewHandler mUpdateViewHandler;
    private AlertDialog alertDialog;
    private String contentPassword = "";
    private int currentKeypadPosition = 0;
    private WifiScanner wifiScanner;
    private String mWifiName;


    public TwelveGridKeyboardView(Context context) {
        super(context);
        init(context);
    }

    public TwelveGridKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TwelveGridKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TwelveGridKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(Context context) {
        this.mContext = context;
        initView();
    }

    private void initView() {
        inflate(mContext, R.layout.keyboard_scroll_view, this);
        initManager();
        initViews();
        initFunctionButtonData();
        setData();
        addKeyPressedListener();

    }

    private void initFunctionButtonData() {
        kbLk1.setEnable(false);
        kbLk1.setImagePosition(KeyConsts.CENTER_VERTICAL,30,20,16,16,36,36);
        kbLk1.setUnPressedImage(R.drawable.keypad_icon);

        kbLk2.setImagePosition(KeyConsts.RIGHT_TOP,0,17,12,0,36,36);
        kbLk2.setPressedImage(R.drawable.fun_return_pressed);
        kbLk2.setUnPressedImage(R.drawable.fun_return_unpressed);

        kbRk1.setImagePosition(KeyConsts.CENTER_VERTICAL,12,0,0,0,36,36);
        kbRk1.setPressedImage(R.drawable.fun_switch_pressed);
        kbRk1.setUnPressedImage(R.drawable.fun_switch_unpressed);

        kbRk2.setImagePosition(KeyConsts.LEFT_TOP,12,17,0,0,36,36);
        kbRk2.setPressedImage(R.drawable.fun_delete_pressed);
        kbRk2.setUnPressedImage(R.drawable.fun_delete_unpressed);

    }

    private void addKeyPressedListener() {
        KeyPressCallback.getInstance().setKeyPressCallbackListener(new KeyPressCallback.KeyPressCallbackListener() {
            @Override
            public void onKeyPressed(int keyType, String keyValue) {
                updateWifiPassword(keyType, keyValue);
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
                backToRobotView();

            }
        });
        kbRk1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                responseSwitch();

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
        ivSelectWifi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showWifiSelectDialog();
            }
        });
        wifiName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showWifiSelectDialog();
            }
        });
        ivWifiCommit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (TextUtils.isEmpty(mWifiName)){
//                    Toast.makeText(mContext,"Wi-Fi未设置",Toast.LENGTH_LONG).show();
//                }else{
//                    connectWifi();
//                }
                connectWifi();

            }
        });

    }

    private void backToRobotView() {
        if (!((MainActivity)mContext).isFromOpenRobot()){
            ((MainActivity)mContext).finish();
        }

    }

    private void connectWifi() {
        Log.e("letianpai_wifi","connect_wifi");
        if (!TextUtils.isEmpty(mWifiName)){
            BleConnectStatusCallback.getInstance().setBleConnectStatus(BleConnectStatusCallback.BLE_STATUS_CONNECTING_NET);
            WIFIAutoConnectionService.start(mContext, mWifiName, contentPassword);

        }

    }

    private void responseSwitch() {
        currentKeypadPosition += 1;
        if (currentKeypadPosition > 3){
            currentKeypadPosition = 0;
        }
        if (currentKeypadPosition == 0){
            adapter = new CustomAdapter(KeysMapUtils.getInstance(mContext).getKeysList());
            recyclerView.setAdapter(adapter);

        }else if (currentKeypadPosition == 1){
            adapter = new CustomAdapter(KeysMapUtils.getInstance(mContext).getSmallCharacterList());
            recyclerView.setAdapter(adapter);

        }else if (currentKeypadPosition == 2){
            adapter = new CustomAdapter(KeysMapUtils.getInstance(mContext).getNumList());
            recyclerView.setAdapter(adapter);

        }else if (currentKeypadPosition == 3){
            adapter = new CustomAdapter(KeysMapUtils.getInstance(mContext).getSpecialKeyList());
            recyclerView.setAdapter(adapter);
        }

    }

    private void responseDelete() {
        contentPassword = FunctionUtils.removeLastByte(contentPassword);
        wifiPassword.setText(contentPassword);
    }

    private void setData() {
        adapter = new CustomAdapter(KeysMapUtils.getInstance(mContext).getKeysList());
        recyclerView.setAdapter(adapter);
    }

    private void initViews() {
        ivWifiCommit = findViewById(R.id.iv_wifi_commit);
        ivSelectWifi = findViewById(R.id.ivSelectWifi);
        wifiName = findViewById(R.id.tv_selected_wifi);
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

                case UPDATE_WIFI_NAME:
                    updateWifiDisplayName(msg);
                    break;

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

    private void createListDialog2(Context context, List<ScanResult> wifiList) {

        // 通过LayoutInflater加载自定义布局
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_list, null);

        // 初始化ListView
        ListView listView = view.findViewById(R.id.listView);

        // 准备数据
//        String[] items = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};

        WifiAdapter adapter = new WifiAdapter(mContext, wifiList);
        listView.setAdapter(adapter);

        // 设置ListView的点击事件
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // 处理点击事件
            ScanResult selectedItem = adapter.getItem(position);
            updateWifiDisplayName(selectedItem.SSID);
            Log.e("letianpai_wifi", "selectedItem.SSID: " + selectedItem.SSID);

            // TODO: 处理点击事件逻辑
        });

        // 创建AlertDialog.Builder并设置布局
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
//
//        // 设置取消按钮
//        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());

        // 创建并显示AlertDialog
        alertDialog = builder.create();
        alertDialog.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = 320;
        layoutParams.height = 300;

        alertDialog.getWindow().setAttributes(layoutParams);
        alertDialog.getWindow().setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.rounded_corner_background));
    }

    private void updateWifiDisplayName(String name) {
        Message message = new Message();
        message.obj = name;
        message.what = UPDATE_WIFI_NAME;
        mUpdateViewHandler.sendMessage(message);
    }

    private void updateWifiDisplayName(Message msg) {
        if (msg == null || msg.obj == null) {
            return;
        }
        mWifiName = (String)( msg.obj);
        wifiName.setText((String) msg.obj);
        ((MainActivity)mContext).setWifiName((String) msg.obj);
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
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

    private void showWifiSelectDialog() {
        List<ScanResult> wifiList = wifiScanner.getAvailableWifiList();
        List<ScanResult> finalWifiList = new ArrayList<>();
        for (int i = 0; i < wifiList.size(); i++) {
            Log.e("wifi_scan", i + "_wifiList.get(i).SSID: " + wifiList.get(i).SSID);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Log.e("wifi_scan", i + "_wifiList.get(i).getWifiStandard(): " + wifiList.get(i).getWifiStandard());
            }
            if (!TextUtils.isEmpty(wifiList.get(i).SSID) && !isInWifiList(wifiList.get(i).SSID,finalWifiList)) {
                finalWifiList.add(wifiList.get(i));
            }
        }
        createListDialog2(mContext, finalWifiList);
    }

    private boolean isInWifiList(String ssid,List<ScanResult> finalWifiList) {
        for (int i = 0; i < finalWifiList.size(); i++) {
            if (finalWifiList.get(i).SSID.equals(ssid)) {
                return true;
            }
        }
        return false;
    }

    private void initManager() {
        wifiScanner = new WifiScanner(mContext);
    }

    public String getmWifiName() {
        return mWifiName;
    }

    public String getContentPassword() {
        return contentPassword;
    }
}
