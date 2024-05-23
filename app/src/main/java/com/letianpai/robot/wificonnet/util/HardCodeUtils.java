package com.letianpai.robot.wificonnet.util;

import android.util.Log;

import com.letianpai.robot.wificonnet.system.SystemUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HardCodeUtils {

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final String partSecretKey = "your partSecretKey";

    public static String getDeviceSign(String inputValue, String timeStr) {

        String deviceSecretKey = md5(inputValue + timeStr + partSecretKey);
        // deviceSecretKey: c6478a35ec15a395ac65ea295390846a
        String mac_sign = sha256(inputValue + timeStr + deviceSecretKey);
        return mac_sign;
        // deviceSign: cc5dc034069905a983e7f08be16e082d82dc23fa76b5aa1090af4e9e806ff9b6
    }

    private static String md5(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes());
            int tem;
            StringBuffer buffer = new StringBuffer();
            for (byte it : md.digest()) {
                tem = it;
                if (tem < 0) {
                    tem += 256;
                }
                if (tem < 16) {
                    buffer.append("0");
                }
                buffer.append(Integer.toHexString(tem));
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getBindConfig() {
        String mac = SystemUtil.getWlanMacAddress();
        String ts = (System.currentTimeMillis() / 1000) + "";
        String sn = SystemUtil.getLtpSn();
        String deviceSign = getDeviceSign(mac, ts);

        deviceSign = getEncodeDeviceSign(deviceSign);
        String json = "{" + "\"cmd\":\"requestMacAddress\"," + "\"mac\":\"" + mac + "\"," + "\"ts\":" + ts + ","
                + "\"device_sign\":\"" + deviceSign + "\"," + "\"sn\":\"" + sn + "\"" + "}";
        Log.e("letianpai_bindCode", "json: " + json);
        return json;
    }

    private static String getEncodeDeviceSign(String deviceSign) {
        String deviceSign1 = deviceSign.substring(0, 8);
        String deviceSign2 = deviceSign.substring(8, deviceSign.length());
        StringBuilder reversedString = new StringBuilder(deviceSign1).reverse();
        deviceSign1 = reversedString.toString();
        deviceSign = deviceSign1 + deviceSign2;
        return deviceSign;
    }

}
