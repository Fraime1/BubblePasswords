package com.bubble.passwrosoft.feasd.data.utils

import android.util.Log
import com.bubble.passwrosoft.feasd.presentation.app.BubblePasswordApp
import com.google.firebase.messaging.FirebaseMessaging
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BubblePasswordPushToken {

    suspend fun bubblePasswordGetToken(): String = suspendCoroutine { continuation ->
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if (!it.isSuccessful) {
                    continuation.resume(it.result)
                    Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "Token error: ${it.exception}")
                } else {
                    continuation.resume(it.result)
                }
            }
        } catch (e: Exception) {
            Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "FirebaseMessagingPushToken = null")
            continuation.resume("")
        }
    }


}