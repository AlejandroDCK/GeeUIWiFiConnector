package com.letianpai.robot.wificonnet.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout

/**
 * Created by liujunbin
 */
object ViewUtils {
    /**
     * @param context
     * @param view
     */
    fun resizeLinearViewSize(context: Activity, view: View) {
        val screenWidth = context.windowManager.defaultDisplay.width
        val bannerlayout = view.layoutParams as LinearLayout.LayoutParams
        bannerlayout.width = screenWidth * 2 / 3

        //        bannerlayout.height = screenWidth * height / width;
        view.layoutParams = bannerlayout
    }

    /**
     * @param context
     * @param view
     * @param width
     * @param height
     */
    fun resizeLinearLayoutViewSize0(context: Activity?, view: View, width: Int, height: Int) {
        val bannerlayout = view.layoutParams as LinearLayout.LayoutParams
        bannerlayout.width = width
        bannerlayout.height = height
        view.layoutParams = bannerlayout
    }

    //    /**
    //     * @param view
    //     * @param width
    //     * @param height
    //     */
    //    public static void resizeLinearLayoutViewSize(View view, int width, int height) {
    //        LinearLayout.LayoutParams bannerlayout = (LinearLayout.LayoutParams) (view.getLayoutParams());
    //        bannerlayout.width = width;
    //        bannerlayout.height = height;
    //        view.setLayoutParams(bannerlayout);
    //    }
    /**
     * @param view
     * @param width
     * @param height
     */
    fun resizeLinearLayoutViewSize(view: LinearLayout, width: Int, height: Int) {
        val bannerlayout = view.layoutParams as LinearLayout.LayoutParams
        bannerlayout.width = width
        bannerlayout.height = height
        view.layoutParams = bannerlayout
    }

    /**
     * @param view
     * @param width
     * @param height
     */
    fun resizeViewGroupViewSize(view: ViewGroup, width: Int, height: Int) {
        val bannerlayout = view.layoutParams as ViewGroup.LayoutParams
        //        bannerlayout.width = width;
        bannerlayout.height = height
        view.layoutParams = bannerlayout
    }

    /**
     * @param view
     * @param width
     * @param height
     */
    fun resizeGridLayoutViewSize(view: LinearLayout, width: Int, height: Int) {
//        GridLayout.LayoutParams bannerlayout = (GridLayout.LayoutParams) (view.getLayoutParams());
        val bannerlayout = GridLayout.LayoutParams()
        bannerlayout.width = width
        bannerlayout.height = height
        view.layoutParams = bannerlayout
    }

    /**
     * @param view
     * @param height
     */
    fun resizeLinearLayoutViewSize(view: View, height: Int) {
        val bannerlayout = view.layoutParams as LinearLayout.LayoutParams
        bannerlayout.height = height
        view.layoutParams = bannerlayout
    }

    /**
     * @param view
     * @param width
     * @param height
     */
    fun resizeRelativeLayoutViewSize(view: View, width: Int, height: Int) {
        val bannerlayout = view.layoutParams as RelativeLayout.LayoutParams
        bannerlayout.width = width
        bannerlayout.height = height
        view.layoutParams = bannerlayout
    }

    /**
     * @param view
     * @param width
     * @param height
     */
    fun resizeRelativeLayoutViewSize0(view: View, width: Int, height: Int) {
        val bannerlayout = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        bannerlayout.width = width
        bannerlayout.height = height
        view.layoutParams = bannerlayout
    }

    /**
     * @param view
     * @param width
     * @param height
     */
    fun resizeRelativeLayoutViewSize0(view: View, width: Int, height: Int, left: Int, top: Int) {
        val bannerlayout = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        bannerlayout.width = width
        bannerlayout.height = height
        bannerlayout.setMargins(left, top, 0, 0)
        view.layoutParams = bannerlayout
    }

    /**
     * @param view
     * @param width
     * @param height
     */
    fun resizeLinearLayoutViewSize(view: View, width: Int, height: Int) {
        val bannerlayout = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        bannerlayout.width = width
        bannerlayout.height = height
        view.layoutParams = bannerlayout
    }

    /**
     * @param view
     * @param width
     * @param height
     */
    fun resizeLinearLayoutViewSize0(
        view: View,
        width: Int,
        height: Int,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        val bannerlayout = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        bannerlayout.width = width
        bannerlayout.height = height
        bannerlayout.setMargins(left, top, right, bottom)
        view.layoutParams = bannerlayout
    }

    /**
     * @param view
     * @param width
     * @param height
     */
    fun resizeViewSize(view: View, width: Int, height: Int) {
        val params = view.layoutParams
        params.height = height
        params.width = width
        view.layoutParams = params
    }

    /**
     * @param view
     */
    fun resizeRelativeLayoutViewSize(view: RelativeLayout, size: Int) {
        val params = view.layoutParams
        params.height = size
        params.width = size
        view.layoutParams = params
    }

    /**
     * @param view
     */
    @JvmStatic
    fun resizeRelativeLayoutViewHeightSize(view: RelativeLayout, height: Int) {
        val params = view.layoutParams
        params.height = height
        view.layoutParams = params
    }

    /**
     * @param view
     */
    fun resizeLinearLayoutViewHeightSize(view: LinearLayout, height: Int) {
        val params = view.layoutParams
        params.height = height
        view.layoutParams = params
    }

    /**
     * @param view
     */
    fun resizeLinearLayoutViewHeightSize1(view: LinearLayout, height: Int) {
        Log.e("Mars", "resizeLinearLayoutViewHeightSize_height: $height")
        val params = LinearLayout.LayoutParams(view.layoutParams)
        params.height = height
        view.layoutParams = params
    }

    /**
     * @param view
     * @param width
     * @param height
     */
    @JvmStatic
    fun resizeImageViewSize(view: ImageView, width: Int, height: Int) {
        val params = view.layoutParams
        params.height = height
        params.width = width
        view.layoutParams = params
    }

    /**
     * @param view
     * @param width
     * @param height
     */
    fun resizeImageViewSize(view: ImageView, width: Int, height: Int, left: Int) {
        val params = view.layoutParams
        params.height = height
        params.width = width
        view.layoutParams = params
    }

    /**
     * @param view
     * @param width
     * @param height
     */
    fun resizeButtonViewSize(view: Button, width: Int, height: Int) {
        val params = view.layoutParams
        params.height = height
        params.width = width
        view.layoutParams = params
    }


    /**
     * 重置dialogView 大小
     *
     * @param width
     * @param height
     */
    fun resizeViewSize(context: Context, window: Window, width: Int, height: Int) {
        val manager = (context as Activity).windowManager
        val params = window.attributes
        window.setGravity(Gravity.CENTER)
        val display = manager.defaultDisplay
        params.width = width
        params.height = height
        window.attributes = params
    }

    /**
     * 重置dialogView 所在View短边大小
     *
     * @param width
     * @param height
     */
    fun getAnswerDialogDisplaySize(
        context: Context,
        window: Window,
        width: Float,
        height: Float
    ): Int {
        var shortSize = 0
        val manager = (context as Activity).windowManager
        val params = window.attributes
        window.setGravity(Gravity.CENTER)
        val display = manager.defaultDisplay
        val displayWidth = (display.width * width).toInt()
        val displayHeight = (display.height * height).toInt()
        shortSize = if (displayWidth > displayHeight) {
            displayHeight
        } else {
            displayWidth
        }
        return shortSize
    }

    /**
     * 重置dialogView 所在View短边大小
     *
     */
    @JvmStatic
    fun getViewWidthSize(context: Context, window: Window): Int {
        val shortSize = 0
        val manager = (context as Activity).windowManager
        val params = window.attributes
        window.setGravity(Gravity.CENTER)
        val display = manager.defaultDisplay
        val displayHeight = display.height

        //        LogUtils.k("displayWidth: "+ displayWidth);
//        LogUtils.k("displayHeight: "+ displayHeight);
        return display.width
    }

    /**
     * 获取View高度
     *
     */
    fun getViewHeightSize(context: Context, window: Window): Int {
        val shortSize = 0
        val manager = (context as Activity).windowManager
        val params = window.attributes
        window.setGravity(Gravity.CENTER)
        val display = manager.defaultDisplay
        val displayWidth = display.width

        //        LogUtils.k("displayWidth: "+ displayWidth);
//        LogUtils.k("displayHeight: "+ displayHeight);
        return display.height
    }


    //    /**
    //     *
    //     * @param context
    //     * @param view
    //     */
    //    public static void resizeViewSize(Activity context, View view) {
    //        int screenWidth = context.getWindowManager().getDefaultDisplay().getWidth();
    //        RelativeLayout.LayoutParams bannerlayout = (RelativeLayout.LayoutParams) (view.getLayoutParams());
    //        bannerlayout.width = screenWidth * 2 / 3;
    //
    //        view.setLayoutParams(bannerlayout);
    //
    //    }
    /**
     * 填充titlebar
     * @param window
     */
    fun fillTitleBar(window: Window) {
        window.requestFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            )
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        }
    }

    /**
     * 获取statusbar高度
     */
    fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight2 = -1
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val `object` = clazz.newInstance()
            val height = clazz.getField("status_bar_height")[`object`].toString().toInt()
            statusBarHeight2 = context.resources.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return statusBarHeight2
    }

    /**
     * 填充titlebar
     */
    fun resizeStatusAndTitlebar(context: Context, relativeLayout: RelativeLayout, viewHeight: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val statusBarHeight = getStatusBarHeight(context)

            resizeRelativeLayoutViewHeightSize(relativeLayout, viewHeight + statusBarHeight)
        } else {
            resizeRelativeLayoutViewHeightSize(relativeLayout, viewHeight)
        }
    }


    fun moveRelateLayout(left: Int, top: Int, relativeLayout: ViewGroup) {
        val lp = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        lp.setMargins(0, top, 0, 0)
        relativeLayout.layoutParams = lp
    }

    fun moveLinearLayout(left: Int, top: Int, linearLayout: LinearLayout) {
        val lp = LinearLayout.LayoutParams(linearLayout.layoutParams)
        lp.setMargins(0, top, 0, 0)
        linearLayout.layoutParams = lp
    }

    fun moveLinearLayout0(left: Int, top: Int, linearLayout: LinearLayout) {
        val lp = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        lp.setMargins(0, top, 0, 0)
        linearLayout.layoutParams = lp
    }

    fun moveLinearImageView(left: Int, top: Int, imageView: ImageView) {
        val lp = LinearLayout.LayoutParams(imageView.layoutParams)
        lp.setMargins(0, top, 0, 0)
        imageView.layoutParams = lp
    }

    fun moveRelativeImageView(left: Int, top: Int, imageView: ImageView) {
        val lp = RelativeLayout.LayoutParams(imageView.layoutParams)
        lp.setMargins(left, top, 0, 0)
        imageView.layoutParams = lp
    }

    // 两次点击按钮之间的点击间隔不能少于3秒
    private const val MIN_CLICK_DELAY_TIME = 3000
    private var lastClickTime: Long = 0
    val isFastClick: Boolean
        get() {
            var flag = false
            val curClickTime = System.currentTimeMillis()
            if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                flag = true
            }
            lastClickTime = curClickTime
            return flag
        }
}
