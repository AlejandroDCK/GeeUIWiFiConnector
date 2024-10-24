package com.letianpai.robot.wificonnet.parser

import android.graphics.Rect

/**
 * @author liujunbin
 */
class RobotKeyboardLabel {
    var text: String? = null
    var bgColor: Int = 0
    var bgPressedColor: Int = 0
    var textColor: Int = 0
    var align: Int = 0
    var origTextSize: Int = 0
    var origRect: Rect? = null
    var dispRect: Rect? = null
    var origTouchRect: Rect? = null
    var dispTouchRect: Rect? = null
}
