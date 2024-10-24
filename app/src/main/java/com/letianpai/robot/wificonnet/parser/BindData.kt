package com.letianpai.robot.wificonnet.parser

class BindData {
    var isIs_bind: Boolean = false
        private set
    var country: String? = null

    fun setIs_bind(is_bind: Boolean) {
        this.isIs_bind = is_bind
    }

    override fun toString(): String {
        return "{" +
                "is_bind:" + isIs_bind +
                ", country:'" + country + '\'' +
                '}'
    }
}
