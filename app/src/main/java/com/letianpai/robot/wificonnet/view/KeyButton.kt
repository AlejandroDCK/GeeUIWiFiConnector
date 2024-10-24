package com.letianpai.robot.wificonnet.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.Button
import com.letianpai.robot.wificonnet.R

/**
 * @author liujunbin
 */
class KeyButton : androidx.appcompat.widget.AppCompatButton {
    private var keyButton: KeyButton? = null
    private var mContext: Context? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
        this.mContext = context
        keyButton = this@KeyButton
        setButtonUnPressed()
        addKeyButtonListeners()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun addKeyButtonListeners() {
        keyButton!!.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                setButtonUnPressed()
            } else if (event.action == MotionEvent.ACTION_CANCEL) {
                setButtonUnPressed()
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                setButtonPressed()
            }
            false
        }
    }

    private fun setButtonUnPressed() {
        keyButton!!.setBackgroundColor(mContext!!.resources.getColor(R.color.background))
        keyButton!!.setTextColor(mContext!!.getColor(R.color.keyboard_text_color))
    }

    private fun setButtonPressed() {
        keyButton!!.setBackgroundColor(mContext!!.resources.getColor(R.color.keyboard_highlight))
        keyButton!!.setTextColor(mContext!!.getColor(R.color.white))
    }
}
