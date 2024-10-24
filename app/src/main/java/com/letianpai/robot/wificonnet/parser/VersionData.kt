package com.letianpai.robot.wificonnet.parser

class VersionData {
    var version: String? = null
    @JvmField
    var rom_version: String? = null
    var mcu_version: String? = null

    override fun toString(): String {
        return "{" +
                "version:'" + version + '\'' +
                ", rom_version:'" + rom_version + '\'' +
                ", mcu_version:'" + mcu_version + '\'' +
                '}'
    }
}
