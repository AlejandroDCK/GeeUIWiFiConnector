package com.letianpai.robot.wificonnet.qrcode

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Log
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.letianpai.robot.wificonnet.R
import java.util.Hashtable

/**
 * @author liujunbin
 */
object QRCodeUtil {
    private val TAG: String = QRCodeUtil::class.java.simpleName
    private const val BLACK = -0x1000000
    @Throws(Exception::class)
    fun createBitmap(size: Int): Bitmap {
        //TODO 需要增加拼配网络请求的逻辑，这部分需要确认网络请求格后增加这部分逻辑，再再验证
        val url = "www.baidu.com_www.baidu.com"

        //        if (OverseasUtils.isInternationalized(RHJApp.getRhjApp())){
        //            url = ActivationConsts.LTP_ACTIVATE_OVERSEAS_URL;
        //        }
        //TODO 需要增加网络请求逻辑
        Log.i(TAG, "url===$url")
        val bitmap = createQRCode(url, size)
        return bitmap
    }

    @Throws(WriterException::class)
    fun createQRCode(str: String?, widthAndHeight: Int): Bitmap {
        val hints = Hashtable<EncodeHintType, Any?>()
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.L //容错率
        hints[EncodeHintType.CHARACTER_SET] = "utf-8"
        hints[EncodeHintType.MARGIN] = 1
        val matrix = QRCodeWriter().encode(
            str,
            BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight, hints
        )
        val width = matrix.width
        val height = matrix.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (matrix[x, y]) {
                    pixels[y * width + x] = BLACK
                }
            }
        }
        val bitmap = Bitmap.createBitmap(
            width, height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

    /**
     *
     * @param context
     * @param content 二维码内容
     * @param size 二维码边长
     * @return
     * @throws WriterException
     */
    @Throws(WriterException::class)
    fun createQRCodWithLogo(context: Context?, content: String?, size: Int): Bitmap? {
        val qrBitmap = createQRCode(content, size)
        val logoBitmap = getLogoBitmap(context, R.drawable.logo)
        val mergedBitmap = addLogo(qrBitmap, logoBitmap)
        return mergedBitmap
    }

    /**
     *
     * @param context
     * @param content 二维码内容
     * @param size 二维码边长
     * @return
     * @throws WriterException
     */
    @Throws(WriterException::class)
    fun createQRCodWithLogo(
        context: Context,
        content: String?,
        imageView: ImageView,
        size: Int
    ): Bitmap? {
        val qrBitmap = createQRCode(content, size)
        val logoBitmap = getLogoBitmap(context, R.drawable.logo)
        val mergedBitmap = addLogo(qrBitmap, logoBitmap)
        imageView.setBackgroundColor(context.resources.getColor(R.color.white))
        return mergedBitmap
    }

    fun decToHex(dec: Int): String {
        var dec = dec
        var hex = ""
        while (dec != 0 && dec != -1) {
            var h = (dec and 0xff).toString(16)
            if ((h.length and 0x01) == 1) {
                h = "0$h"
            }
            hex = hex + h
            dec = dec shr 8
        }
        return hex
    }

    /**
     * 加密 RC4>Base64
     * @param
     * @return
     */
    fun encodeWithOnlyHardCode(data: IntArray?): String? {
        // TODO 需要做加密逻辑
        return null
    }

    /**
     * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     * @param src byte[] data
     * @return hex string
     */
    fun bytesToHexString(src: IntArray?): String? {
        val stringBuilder = StringBuilder("")
        if (src == null || src.size <= 0) {
            return null
        }
        for (i in src.indices) {
            val v = src[i] and 0xFF
            val hv = Integer.toHexString(v)
            if (hv.length < 2) {
                stringBuilder.append(0)
            }
            stringBuilder.append(hv)
        }
        return stringBuilder.toString()
    }

    /**
     * 在二维码中间添加Logo图案
     */
    fun addLogo(src: Bitmap?, logo: Bitmap?): Bitmap? {
        if (src == null) {
            return null
        }

        if (logo == null) {
            return src
        }

        //获取图片的宽高
        val srcWidth = src.width
        val srcHeight = src.height
        val logoWidth = logo.width
        val logoHeight = logo.height

        if (srcWidth == 0 || srcHeight == 0) {
            return null
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src
        }

        //logo大小为二维码整体大小的1/5
        val scaleFactor = srcWidth * 1.0f / 5 / logoWidth
        //        float scaleFactor = srcWidth * 1.0f / 4 / logoWidth;
        var bitmap: Bitmap? = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888)
        try {
            val canvas = Canvas(bitmap!!)
            canvas.drawBitmap(src, 0f, 0f, null)
            canvas.scale(
                scaleFactor,
                scaleFactor,
                (srcWidth / 2).toFloat(),
                (srcHeight / 2).toFloat()
            )
            canvas.drawBitmap(
                logo,
                ((srcWidth - logoWidth) / 2).toFloat(),
                ((srcHeight - logoHeight) / 2).toFloat(),
                null
            )

            //            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.save()
            canvas.restore()
        } catch (e: Exception) {
            bitmap = null
            e.stackTrace
        }

        return bitmap
    }

    fun getLogoBitmap(context: Context?, res: Int): Bitmap {
        val logoBitmap = BitmapFactory.decodeResource(
            context!!.resources, res
        )
        return logoBitmap
    }


    /**
     *
     * 用于创建一个二维码
     *
     * @param content
     *
     * @param width
     *
     * @param height
     */
    fun createQRCode(content: String?, width: Int, height: Int): Bitmap? {
        try {
            //1,创建实例化对象

            val writer = QRCodeWriter()

            //2,设置字符集
            val map: MutableMap<EncodeHintType, String?> = HashMap()
            map[EncodeHintType.CHARACTER_SET] = "UTF-8"

            //3，通过encode方法将内容写入矩阵对象
            val matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, map)

            //4，定义一个二维码像素点的数组，向每个像素点中填充颜色
            val pixels = IntArray(width * height)

            //5,往每一像素点中填充颜色（像素没数据则用黑色填充，没有则用彩色填充，不过一般用白色）
            for (i in 0 until height) {
                for (j in 0 until width) {
                    if (matrix[j, i]) {
                        pixels[i * width + j] = -0x1000000
                    } else {
                        pixels[i * width + j] = -0x1
                    }
                }
            }

            //6,创建一个指定高度和宽度的空白bitmap对象
            val bm_QR = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            //7，将每个像素的颜色填充到bitmap对象
            bm_QR.setPixels(pixels, 0, width, 0, 0, width, height)

            //8，创建一个bitmap对象用于作为其图标

//            Bitmap bm_login = BitmapFactory.decodeResource(getResources(), R.drawable.img_kf_qq);
//
//            //9，创建一个方法在二维码上添加一张图片
//
//            if (addLogin(bm_QR,bm_login) != null) {
//
//                imageView_main.setImageBitmap(addLogin(bm_QR,bm_login));
//
//            }
            return bm_QR
        } catch (e: WriterException) {
            // TODO Auto-generated catch block

            e.printStackTrace()
        }
        return null
    }
}
