package com.bubble.passwrosoft.feasd.presentation.pushhandler

import android.os.Bundle
import android.util.Log
import com.bubble.passwrosoft.feasd.presentation.app.BubblePasswordApp

class BubblePasswordPushHandler() {
    fun bubblePasswordHandlePush(extras: Bundle?) {
        Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "Extras from Push = ${extras?.keySet()}")
        if (extras != null) {
            val map = bubblePasswordBundleToMap(extras)
            Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "Map from Push = $map")
            map?.let {
                if (map.containsKey("url")) {
                    BubblePasswordApp.BUBBLE_PASSWORD_FB_LI = map["url"]
                    Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "UrlFromActivity = $map")
                }
            }
        } else {
            Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "Push data no!")
        }
    }

    private fun bubblePasswordBundleToMap(extras: Bundle): Map<String, String?>? {
        val map: MutableMap<String, String?> = HashMap()
        val ks = extras.keySet()
        val iterator: Iterator<String> = ks.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            map[key] = extras.getString(key)
        }
        return map
    }

}