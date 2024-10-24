package com.letianpai.robot.wificonnet.parser

class CountryInfo {
    @JvmField
    var code: Int = 0
    var msg: String? = null
    @JvmField
    var data: CountryData? = null

    override fun toString(): String {
        return "{" +
                "code:" + code +
                ", msg:'" + msg + '\'' +
                ", data:" + data +
                '}'
    }
}
