package com.letianpai.robot.wificonnet.parser

/**
 * @author liujunbin
 */
class VersionInfo {
    @JvmField
    var code: Int = 0
    var msg: String? = null
    @JvmField
    var data: VersionData? = null

    override fun toString(): String {
        return "{" +
                "code:" + code +
                ", msg:'" + msg + '\'' +
                ", data:" + data +
                '}'
    }
}
