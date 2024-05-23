package com.letianpai.robot.wificonnet.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.letianpai.robot.wificonnet.R;
import com.letianpai.robot.wificonnet.manager.WifiScanner;
import com.letianpai.robot.wificonnet.util.ViewUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liujunbin
 */
public class KeyBoardView extends LinearLayout {
    private Button k11, k12, k13, k14, k15, k16, k17, k18, k19, k110;
    private Button k21, k22, k23, k24, k25, k26, k27, k28, k29;
    private Button k31, k32, k33, k34, k35, k36, k37;
    private Button kDelete, kSpace1, kSpace2;
    private View kUp, kDown;
    private ImageView ivSelectWifi;
    private ArrayList<Button> buttonList = new ArrayList<>();
    private String[] keysMap1 = new String[]{"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K", "L", "Z", "X", "C", "V", "B", "N", "M"};
    private String[] keysMap2 = new String[]{"q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v", "b", "n", "m"};
    private String[] keysMap3 = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "~", "!", "@", "#", "$", "%", "/", "^", "&", "*", "(", ")", "_", "+", "[", "]"};
    private int currentMapNum = 0;
    private String[] currentKeysMap = keysMap1;
    private Context mContext;
    private TextView wifiPassword;
    private TextView wifiName;
    private String contentPassword = "";
    private WifiScanner wifiScanner;
    private Spinner wifiSpinner;
    private boolean isExpanded = false;
    private RelativeLayout rlWifi;
    private UpdateViewHandler mUpdateViewHandler;
    private AlertDialog alertDialog;

    public KeyBoardView(Context context) {
        super(context);
        iniKeyBoardView(context);
    }

    public KeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniKeyBoardView(context);
    }

    public KeyBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iniKeyBoardView(context);
    }


    public KeyBoardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        iniKeyBoardView(context);

    }

    private void iniKeyBoardView(Context context) {
        this.mContext = context;
        inflate(context, R.layout.keyboard_view_new, this);
        initManager();
        initView(context);
        initKeyBoardData();
        initKeyData(context);
        setOnClickListeners();
    }

    private void initManager() {
        wifiScanner = new WifiScanner(mContext);
        mUpdateViewHandler = new UpdateViewHandler(mContext);
    }

    private void setOnClickListeners() {
        for (int i = 0; i < buttonList.size(); i++) {
            int finalI = i;
            buttonList.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String buttonText = ((Button) v).getText() + "";
                    Log.e("letianpai_onClick", finalI + "_" + buttonText);
//                    Toast.makeText(mContext,buttonText,Toast.LENGTH_SHORT).show();
                    Log.e("letianpai_onClick", "contentPassword.length: " + contentPassword.length());
                    if (contentPassword.length() < 20) {
                        contentPassword = contentPassword + buttonText;
                        wifiPassword.setText(contentPassword);
                    } else {
//                        Toast.makeText(mContext,"密码不能超过20位",Toast.LENGTH_SHORT).show();
                        updatePassword(contentPassword);
                    }

                }
            });
        }

        kDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                contentPassword = removeLastByte(contentPassword);
                wifiPassword.setText(contentPassword);
            }
        });
//        kSpace2.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                contentPassword = removeLastByte(contentPassword);
//                wifiPassword.setText(contentPassword);
//            }
//        });
        kDown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMapNum = currentMapNum + 1;
                Log.e("letianpai_onClick", "currentMapNum: " + currentMapNum);
                if (currentMapNum > 2) {
                    currentMapNum = 0;
                }
                Log.e("letianpai_onClick", "currentMapNum1: " + currentMapNum);
                if (currentMapNum == 0) {
                    Log.e("letianpai_onClick", "currentMapNum2: " + currentMapNum);
                    currentKeysMap = keysMap1;

                } else if (currentMapNum == 1) {
                    Log.e("letianpai_onClick", "currentMapNum3: " + currentMapNum);
                    currentKeysMap = keysMap2;

                } else if (currentMapNum == 2) {
                    Log.e("letianpai_onClick", "currentMapNum4: " + currentMapNum);
                    currentKeysMap = keysMap3;
                }
                initKeyData(mContext);

            }
        });
        kUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMapNum = currentMapNum - 1;
                if (currentMapNum < 0) {
                    currentMapNum = 2;
                }
                if (currentMapNum == 0) {
                    currentKeysMap = keysMap1;

                } else if (currentMapNum == 1) {
                    currentKeysMap = keysMap2;

                } else if (currentMapNum == 2) {
                    currentKeysMap = keysMap3;
                }
                initKeyData(mContext);

            }
        });
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

        wifiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isExpanded = position == 0;
                if (position == 0 && isExpanded) {
                    ViewUtils.resizeRelativeLayoutViewHeightSize(rlWifi, 200);
//                    view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250)); // 设置展开高度为 400dp
                } else {
                    ViewUtils.resizeRelativeLayoutViewHeightSize(rlWifi, 40);
//                    view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40)); // 设置默认高度为 40dp
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isExpanded = false;
            }
        });
    }

    private void showWifiSelectDialog() {
        List<ScanResult> wifiList = wifiScanner.getAvailableWifiList();
        List<ScanResult> finalWifiList = new ArrayList<>();
        for (int i = 0; i < wifiList.size(); i++) {
            Log.e("wifi_scan", i + "_wifiList.get(i).SSID: " + wifiList.get(i).SSID);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Log.e("wifi_scan", i + "_wifiList.get(i).getWifiStandard(): " + wifiList.get(i).getWifiStandard());
            }
            if (!TextUtils.isEmpty(wifiList.get(i).SSID)) {
                finalWifiList.add(wifiList.get(i));
            }
        }
        createListDialog2(mContext, finalWifiList);
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


    private void showWifiList(List<ScanResult> wifiList) {
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
        WifiAdapter adapter = new WifiAdapter(mContext, wifiList);
        wifiSpinner.setAdapter(adapter);

    }

    private void updatePassword(String password) {
        if (TextUtils.isEmpty(password)) {

        } else {
            if (contentPassword.length() > 20) {
                Toast.makeText(mContext, "密码不能超过20位", Toast.LENGTH_SHORT).show();
            } else {
                wifiPassword.setText(contentPassword);
            }

        }


    }

    private static String removeLastByte(String str) {
        if (str == null || str.isEmpty()) {
            return str; // 返回原始字符串，如果为空或长度为0
        }

        // 使用substring删除最后一个字节
        return str.substring(0, str.length() - 1);
    }

    private void initKeyBoardData() {
        buttonList.clear();
        buttonList.add(k11);
        buttonList.add(k12);
        buttonList.add(k13);
        buttonList.add(k14);
        buttonList.add(k15);
        buttonList.add(k16);
        buttonList.add(k17);
        buttonList.add(k18);
        buttonList.add(k19);
        buttonList.add(k110);

        buttonList.add(k21);
        buttonList.add(k22);
        buttonList.add(k23);
        buttonList.add(k24);
        buttonList.add(k25);
        buttonList.add(k26);
        buttonList.add(k27);
        buttonList.add(k28);
        buttonList.add(k29);

        buttonList.add(k31);
        buttonList.add(k32);
        buttonList.add(k33);
        buttonList.add(k34);
        buttonList.add(k35);
        buttonList.add(k36);
        buttonList.add(k37);
    }

    private void initKeyData(Context context) {
        for (int i = 0; i < buttonList.size(); i++) {
            if (currentMapNum == 1) {
                currentKeysMap[i] = currentKeysMap[i].toLowerCase();
            }

            buttonList.get(i).setText(currentKeysMap[i]);
        }

    }

    private void initView(Context context) {
        k11 = findViewById(R.id.k11);
        k12 = findViewById(R.id.k12);
        k13 = findViewById(R.id.k13);
        k14 = findViewById(R.id.k14);
        k15 = findViewById(R.id.k15);
        k16 = findViewById(R.id.k16);
        k17 = findViewById(R.id.k17);
        k18 = findViewById(R.id.k18);
        k19 = findViewById(R.id.k19);
        k110 = findViewById(R.id.k110);

        k21 = findViewById(R.id.k21);
        k22 = findViewById(R.id.k22);
        k23 = findViewById(R.id.k23);
        k24 = findViewById(R.id.k24);
        k25 = findViewById(R.id.k25);
        k26 = findViewById(R.id.k26);
        k27 = findViewById(R.id.k27);
        k28 = findViewById(R.id.k28);
        k29 = findViewById(R.id.k29);

        k31 = findViewById(R.id.k31);
        k32 = findViewById(R.id.k32);
        k33 = findViewById(R.id.k33);
        k34 = findViewById(R.id.k34);
        k35 = findViewById(R.id.k35);
        k36 = findViewById(R.id.k36);
        k37 = findViewById(R.id.k37);

        kUp = findViewById(R.id.kUp);
        kDown = findViewById(R.id.kDown);
        kDelete = findViewById(R.id.kDelete);

        kSpace1 = findViewById(R.id.space_1);
        kSpace2 = findViewById(R.id.space_2);

        wifiName = findViewById(R.id.tv_selected_wifi);
        wifiPassword = findViewById(R.id.wifi_password);
        ivSelectWifi = findViewById(R.id.ivSelectWifi);
        wifiSpinner = findViewById(R.id.sp_wifi_list);

        rlWifi = findViewById(R.id.rl_wifi_list);
    }

    private void createListDialog(Context context, List<ScanResult> wifiList) {

        // 通过LayoutInflater加载自定义布局
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_list, null);

        // 初始化ListView
        ListView listView = view.findViewById(R.id.listView);

        // 准备数据
//        String[] items = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};

        // 创建适配器
        ArrayAdapter<ScanResult> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, wifiList);

        // 设置适配器
        listView.setAdapter(adapter);

        // 设置ListView的点击事件
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // 处理点击事件
            ScanResult selectedItem = adapter.getItem(position);

            // TODO: 处理点击事件逻辑
        });

        // 创建AlertDialog.Builder并设置布局
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
//
//        // 设置取消按钮
//        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());

        // 创建并显示AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

    private static final int UPDATE_WIFI_NAME = 1;
    private static final int UPDATE_BUTTON_DISPLAY = 2;

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
//                case UPDATE_TIME:
//                case LOAD_SKIN:
//                    loadCustomSkin();
//                    break;
//                case HIDE_VIEWS:
//                    break;
//
//                case SHOW_VERSION:
//                    showSystemVersion();
//                    break;
//
//                case UPLOAD_DATA:
//                    uploadData();
//                    break;
//
//                case UPDATE_BACKGROUND:
//                    updateTimeBackGround();
//                    break;
//
//                case UPDATE_ALL_VIEWS:
//                    updateAllViews(msg);
//                    break;
//
//                case UPDATE_CUSTOM_BACKGROUND:
//                    updateCustomBackground();

            }
        }
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
        wifiName.setText((String) msg.obj);
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }

    }

}
