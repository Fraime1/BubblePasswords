package com.bubble.passwrosoft

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout
import com.bubble.passwrosoft.feasd.presentation.app.BubblePasswordApp

class GlobalLayoutUtil {

    private var mChildOfContent: View? = null
    private var usableHeightPrevious = 0

    fun assistActivity(activity: Activity) {
        val content = activity.findViewById<FrameLayout>(android.R.id.content)
        mChildOfContent = content.getChildAt(0)

        mChildOfContent?.viewTreeObserver?.addOnGlobalLayoutListener {
            possiblyResizeChildOfContent(activity)
        }
    }

    private fun possiblyResizeChildOfContent(activity: Activity) {
        val usableHeightNow = computeUsableHeight()
        if (usableHeightNow != usableHeightPrevious) {
            val usableHeightSansKeyboard = mChildOfContent?.rootView?.height ?: 0
            val heightDifference = usableHeightSansKeyboard - usableHeightNow

            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                activity.window.setSoftInputMode(BubblePasswordApp.bubblePasswordInputMode)
            } else {
                activity.window.setSoftInputMode(BubblePasswordApp.bubblePasswordInputMode)
            }
//            mChildOfContent?.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }

    private fun computeUsableHeight(): Int {
        val r = Rect()
        mChildOfContent?.getWindowVisibleDisplayFrame(r)
        return r.bottom - r.top  // Visible height без status bar
    }
}