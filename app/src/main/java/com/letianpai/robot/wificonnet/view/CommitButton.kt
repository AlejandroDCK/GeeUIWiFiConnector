package com.letianpai.robot.wificonnet.view

import android.content.Context
import android.util.AttributeSet
import com.letianpai.robot.wificonnet.R

/**
 * 提交按钮
 * @author liujunbin
 */
class CommitButton : AbstractKeyButton {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun initData() {
        keyButton!!.text = mContext!!.getText(R.string.commit_value)
    }

    override fun setButtonUnPressed() {
        keyButton!!.setTextColor(mContext!!.getColor(R.color.commit_background_color))
    }

    override fun setButtonPressed() {
        keyButton!!.setTextColor(mContext!!.getColor(R.color.white))
    }
}
