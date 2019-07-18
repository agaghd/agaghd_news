package io.agaghd.agaghdnews.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

/**
 * @author wjy
 * time :   2019/7/17
 * desc :   屏幕上输出Log的工具 Kotlin Version
 */
object ScreenUtils {

    private val SCREEN_LOGGER_TAG = "ScreenLoggerTag"

    public fun print(context: Context, msg: CharSequence) {
        print(context, msg, false, false)
    }

    public fun print(context: Context, msg: CharSequence, clearMsg: Boolean) {
        print(context, msg, clearMsg, false)
    }

    public fun println(context: Context, msg: CharSequence) {
        print(context, msg, false, true)
    }

    public fun println(context: Context, msg: CharSequence, clearMsg: Boolean) {
        print(context, msg, clearMsg, true)
    }

    /**
     * 直接向屏幕输出内容，核心方法
     */
    public fun print(context: Context, msg: CharSequence, clearMsg: Boolean, lineFeed: Boolean) {
        if (context is Activity) {
            val activity = context
            val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
            contentView.post {
                val screeLogger = getScreenLogger(contentView)
                if (clearMsg) {
                    screeLogger.text = msg
                } else {
                    screeLogger.append(msg)
                    if (lineFeed) {
                        screeLogger.append("\n")
                    }
                }
            }
        } else {
            ToastUtil.showToast(context, msg)
        }
    }

    private fun getScreenLogger(contentView: ViewGroup): TextView {
        val screenLogger = TextView(contentView.context)
        //看logger是否已经创建
        for (i in 0..contentView.childCount) {
            val child = contentView.getChildAt(i)
            if (child is TextView && SCREEN_LOGGER_TAG == child.tag) {
                return child
            }
        }
        //logger未创建，进行创建
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT
            , FrameLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.topMargin = 60
        layoutParams.bottomMargin = 60
        screenLogger.setPadding(32, 0, 32, 0)
        screenLogger.textSize = 14.toFloat()
        screenLogger.setTextColor(Color.WHITE)
        screenLogger.setBackgroundColor(0X80000000.toInt())
        screenLogger.layoutParams = layoutParams
        screenLogger.tag = SCREEN_LOGGER_TAG
        return screenLogger
    }
}