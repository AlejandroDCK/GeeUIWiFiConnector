package com.letianpai.robot.wificonnet.parser

class RobotBindData {
    var device_id: String? = null
    var model: String? = null
    var mac: String? = null
    var device_name: String? = null
    var is_online: Int = 0
    @JvmField
    var country: String? = null

    override fun toString(): String {
        return "RobotBindData{" +
                "device_id='" + device_id + '\'' +
                ", model='" + model + '\'' +
                ", mac='" + mac + '\'' +
                ", device_name='" + device_name + '\'' +
                ", is_online=" + is_online +
                ", country='" + country + '\'' +
                '}'
    }
}
