package com.bubble.passwrosoft.feasd.data.shar

import android.content.Context
import androidx.core.content.edit

class BubblePasswordSharedPreference(context: Context) {
    private val bubblePasswordPrefs = context.getSharedPreferences("bubble_password_app_prefs", Context.MODE_PRIVATE)

    var bubblePasswordSavedUrl: String
        get() = bubblePasswordPrefs.getString(BUBBLE_PASSWORD_SAVED_URL, "") ?: ""
        set(value) = bubblePasswordPrefs.edit { putString(BUBBLE_PASSWORD_SAVED_URL, value) }

    var bubblePasswordExpired : Int
        get() = bubblePasswordPrefs.getInt(BUBBLE_PASSWORD_EXPIRED, 0)
        set(value) = bubblePasswordPrefs.edit { putInt(BUBBLE_PASSWORD_EXPIRED, value) }

    var bubblePasswordAppState: Int
        get() = bubblePasswordPrefs.getInt(BUBBLE_PASSWORD_APPLICATION_STATE, 0)
        set(value) = bubblePasswordPrefs.edit { putInt(BUBBLE_PASSWORD_APPLICATION_STATE, value) }

    var bubblePasswordNotificationRequest: Long
        get() = bubblePasswordPrefs.getLong(BUBBLE_PASSWORD_NOTIFICAITON_REQUEST, 0L)
        set(value) = bubblePasswordPrefs.edit { putLong(BUBBLE_PASSWORD_NOTIFICAITON_REQUEST, value) }

    var bubblePasswordNotificationRequestedBefore: Boolean
        get() = bubblePasswordPrefs.getBoolean(BUBBLE_PASSWORD_NOTIFICATION_REQUEST_BEFORE, false)
        set(value) = bubblePasswordPrefs.edit { putBoolean(BUBBLE_PASSWORD_NOTIFICATION_REQUEST_BEFORE, value) }

    companion object {
        private const val BUBBLE_PASSWORD_SAVED_URL = "bubblePasswordSavedUrl"
        private const val BUBBLE_PASSWORD_EXPIRED = "bubblePasswordExpired"
        private const val BUBBLE_PASSWORD_APPLICATION_STATE = "bubblePasswordApplicationState"
        private const val BUBBLE_PASSWORD_NOTIFICAITON_REQUEST = "bubblePasswordNotificationRequest"
        private const val BUBBLE_PASSWORD_NOTIFICATION_REQUEST_BEFORE = "bubblePasswordNotificationRequestedBefore"
    }
}