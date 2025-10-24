package com.bubble.passwrosoft.feasd.presentation.app

import android.app.Application
import android.view.WindowManager
import com.bubble.passwrosoft.feasd.data.utils.BubblePasswordAppsflyer
import com.bubble.passwrosoft.feasd.data.utils.BubblePasswordSystemService
import com.bubble.passwrosoft.feasd.presentation.di.bubblePasswordModule
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


sealed interface BubblePasswordAppsFlyerState {
    data object BubblePasswordDefault : BubblePasswordAppsFlyerState
    data class BubblePasswordSuccess(val bubblePasswordData: MutableMap<String, Any>?) : BubblePasswordAppsFlyerState
    data object BubblePasswordError : BubblePasswordAppsFlyerState
}

class BubblePasswordApp : Application() {


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BubblePasswordApp)
            modules(
                listOf(
                    bubblePasswordModule
                )
            )
        }
        val bubblePasswordAppsflyer = BubblePasswordAppsflyer(this)
        val bubblePasswordSystemService = BubblePasswordSystemService(this)
        if (bubblePasswordSystemService.bubblePasswordIsOnline()) {
            bubblePasswordAppsflyer.init { data ->
                bubblePasswordConversionFlow.value = data
            }
        }
    }

    companion object {
        var bubblePasswordInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        val bubblePasswordConversionFlow: MutableStateFlow<BubblePasswordAppsFlyerState> = MutableStateFlow(BubblePasswordAppsFlyerState.BubblePasswordDefault)
        var BUBBLE_PASSWORD_FB_LI: String? = null
        const val BUBBLE_PASSWORD_MAIN_TAG = "BubblePasswordMainTag"
    }
}