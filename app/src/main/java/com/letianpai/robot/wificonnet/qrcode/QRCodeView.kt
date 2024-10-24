package com.letianpai.robot.wificonnet.qrcode

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.letianpai.robot.wificonnet.R
import com.letianpai.robot.wificonnet.system.SystemUtil
import com.letianpai.robot.wificonnet.util.HardCodeUtils
import com.letianpai.robot.wificonnet.util.ViewUtils

/**
 * 引导页二维码展示页面
 *
 * @author liujunbin
 */
class QRCodeView : RelativeLayout {
    private var mContext: Context? = null
    private lateinit var qrcode: ImageView
    private var tipsDesc: TextView? = null
    private var addDeviceTv: TextView? = null
    private lateinit var tvScan: TextView
    private lateinit var robotVersion: TextView

    //    private ImageView qrcode;
    constructor(context: Context?) : super(context) {
        inits(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        inits(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        inits(context)
    }

    protected fun inits(context: Context?) {
        this.mContext = context
        inflate(mContext, R.layout.robot_qr_code_view, this)
        initView()
        resizeView()
    }

    private fun resizeView() {
        if (mContext != null){
            var width = ViewUtils.getViewWidthSize(
                mContext!!,
                (mContext as Activity?)!!.window
            )
            var height = ViewUtils.getViewHeightSize(
                mContext!!,
                (mContext as Activity?)!!.window
            )
            if (width < height) {
                width = width * 5 / 12
                ViewUtils.resizeImageViewSize(qrcode, width, width)
            } else {
                height = height * 5 / 12
                ViewUtils.resizeImageViewSize(qrcode, height, height)
            }
        }
    }

    protected fun initView() {
        qrcode = findViewById(R.id.iv_qrcode)
        addDeviceTv = findViewById(R.id.add_device_tv)
        tipsDesc = findViewById(R.id.tips_desc)
        robotVersion = findViewById(R.id.robot_version)

        tvScan = findViewById(R.id.tv_weixin_scan)
        val ltpSN = SystemUtil.ltpLastSn
        tvScan.setText(mContext!!.getText(R.string.download_app).toString() + ltpSN)
        robotVersion.setText(Build.DISPLAY)

        // qrcode.setImageResource(R.drawable.qrcode_app);
        val netInfo = HardCodeUtils.bindConfig

        try {
            val bitmap = QRCodeUtil.createQRCodWithLogo(
                mContext,
                "https://cn.letianpai.com/?page_id=762&Robot-$ltpSN&touch=$netInfo",
                400
            )

            // Bitmap bitmap = QRCodeUtil.createQRCodWithLogo(mContext,"#小程序://乐天派/9FmxXfvaVKWtVTJ", 400);
            qrcode.setBackgroundColor(mContext!!.resources.getColor(R.color.white))
            qrcode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setAddDeviceTvVisible(visible: Int) {
        if (addDeviceTv != null) {
            addDeviceTv!!.visibility = visible
        }
    }

    fun setQrcodeVisible(visible: Int) {
        if (qrcode != null) {
            qrcode!!.visibility = visible
        }
    }

    fun setTipsDescVisible(visible: Int) {
        if (tipsDesc != null) {
            tipsDesc!!.visibility = visible
        }
    }

    fun setQrcode(drawable: Int) {
        if (qrcode != null) {
            qrcode!!.setImageResource(drawable)
        }
    }

    fun setTvScan(scan: String?) {
        if (tvScan != null) {
            tvScan!!.text = scan
        }
    }
}
