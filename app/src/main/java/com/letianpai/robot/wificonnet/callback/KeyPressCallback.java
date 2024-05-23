package com.letianpai.robot.wificonnet.callback;

import java.util.ArrayList;

/**
 * 按键功能回调
 * @author liujunbin
 */
public class KeyPressCallback {
    public static final int KEY_TYPE_FUNCTION = 10010;
    public static final int KEY_TYPE_VALUE = 10011;

    private ArrayList<KeyPressCallbackListener> keyPressCallbackListenerList = new ArrayList<>();

    private static class KeyPressCallbackHolder {
        private static KeyPressCallback instance = new KeyPressCallback();
    }

    public static KeyPressCallback getInstance() {
        return KeyPressCallbackHolder.instance;
    }

    private KeyPressCallback() {

    }

    public void setKeyPressCallbackListener(KeyPressCallbackListener keyPressCallbackListener) {
        this.keyPressCallbackListenerList.add(keyPressCallbackListener);
    }

    public interface KeyPressCallbackListener {
        void onKeyPressed(int keyType,String keyValue);
    }

    public void pressKey(int keyType,String keyValue) {
        for (int i = 0; i < keyPressCallbackListenerList.size();i++){
            if (keyPressCallbackListenerList.get(i) != null) {
                keyPressCallbackListenerList.get(i).onKeyPressed(keyType,keyValue);
            }
        }

    }


}
