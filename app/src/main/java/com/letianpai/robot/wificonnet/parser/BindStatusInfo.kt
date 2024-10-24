package com.letianpai.robot.wificonnet.parser

class BindStatusInfo {
    var code: Int = 0
    var msg: String? = null
    var data: BindData? = null

    override fun toString(): String {
        return "{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}'
    }
}
