package com.letianpai.robot.wificonnet.util

import android.util.Log
import com.letianpai.robot.wificonnet.system.SystemUtil.ltpSn
import com.letianpai.robot.wificonnet.system.SystemUtil.wlanMacAddress
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object HardCodeUtils {
    private fun bytesToHex(hash: ByteArray): String {
        val hexString = StringBuilder(2 * hash.size)
        for (b in hash) {
            val hex = Integer.toHexString(0xff and b.toInt())
            if (hex.length == 1) {
                hexString.append('0')
            }
            hexString.append(hex)
        }
        return hexString.toString()
    }

    private fun sha256(input: String): String? {
        try {
            val digest = MessageDigest.getInstance("SHA-256")
            val encodedhash = digest.digest(input.toByteArray(StandardCharsets.UTF_8))
            return bytesToHex(encodedhash)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        }
    }

    private const val partSecretKey = "your partSecretKey"

    @JvmStatic
    fun getDeviceSign(inputValue: String?, timeStr: String): String? {
        val deviceSecretKey = md5(inputValue + timeStr + partSecretKey)
        // deviceSecretKey: c6478a35ec15a395ac65ea295390846a
        val mac_sign = sha256(inputValue + timeStr + deviceSecretKey)
        return mac_sign
        // deviceSign: cc5dc034069905a983e7f08be16e082d82dc23fa76b5aa1090af4e9e806ff9b6
    }

    private fun md5(string: String): String {
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(string.toByteArray())
            var tem: Int
            val buffer = StringBuffer()
            for (it in md.digest()) {
                tem = it.toInt()
                if (tem < 0) {
                    tem += 256
                }
                if (tem < 16) {
                    buffer.append("0")
                }
                buffer.append(Integer.toHexString(tem))
            }
            return buffer.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return ""
    }

    val bindConfig: String
        get() {
            val mac = wlanMacAddress
            val ts = (System.currentTimeMillis() / 1000).toString() + ""
            val sn = ltpSn
            var deviceSign = getDeviceSign(mac, ts)

            deviceSign = getEncodeDeviceSign(deviceSign)
            val json =
                ("{" + "\"cmd\":\"requestMacAddress\"," + "\"mac\":\"" + mac + "\"," + "\"ts\":" + ts + ","
                        + "\"device_sign\":\"" + deviceSign + "\"," + "\"sn\":\"" + sn + "\"" + "}")
            Log.e("letianpai_bindCode", "json: $json")
            return json
        }

    private fun getEncodeDeviceSign(deviceSign: String?): String {
        var deviceSign = deviceSign
        var deviceSign1 = deviceSign!!.substring(0, 8)
        val deviceSign2 = deviceSign.substring(8, deviceSign.length)
        val reversedString = StringBuilder(deviceSign1).reverse()
        deviceSign1 = reversedString.toString()
        deviceSign = deviceSign1 + deviceSign2
        return deviceSign
    }
}
