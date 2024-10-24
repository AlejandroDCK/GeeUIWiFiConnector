package com.letianpai.robot.wificonnet.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.letianpai.robot.wificonnet.MainActivity
import com.letianpai.robot.wificonnet.R

/**
 * @author liujunbin
 */
class PairingInfoView : LinearLayout {
    private var mContext: Context? = null
    private var commitButton: CommitButton? = null


    constructor(context: Context) : super(context) {
        init(context)
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

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    private fun init(context: Context) {
        this.mContext = context
        initView()
    }

    private fun initView() {
        inflate(mContext, R.layout.keyboard_pair_info, this)
        commitButton = findViewById(R.id.iv_ok)
        addKeyPressedListener()
    }

    private fun addKeyPressedListener() {
        commitButton!!.setOnClickListener { showPairCodeView() }
    }

    private fun showPairCodeView() {
        (mContext as MainActivity?)!!.showPairCodeView()
    }
}
