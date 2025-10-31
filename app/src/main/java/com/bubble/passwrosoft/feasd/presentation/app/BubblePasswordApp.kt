package com.bubble.passwrosoft.feasd.presentation.app

import android.app.Application
import android.util.Log
import android.view.WindowManager
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.bubble.passwrosoft.feasd.presentation.di.bubblePasswordModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


sealed interface BubblePasswordAppsFlyerState {
    data object BubblePasswordDefault : BubblePasswordAppsFlyerState
    data class BubblePasswordSuccess(val bubblePasswordData: MutableMap<String, Any>?) : BubblePasswordAppsFlyerState
    data object BubblePasswordError : BubblePasswordAppsFlyerState
}
private const val BUBBLE_PASSWORD_APP_DEV = "VrbyKhJnxRgK5GxARAx6eQ"
interface BubblePasswordAppsApi {
    @Headers("Content-Type: application/json")
    @GET("com.bubble.passwrosoft")
    fun bubblePasswordGetClient(
        @Query("devkey") devkey: String,
        @Query("device_id") deviceId: String,
    ): Call<MutableMap<String, Any>?>
}
class BubblePasswordApp : Application() {
    private var bubblePasswordIsResumed = false

    override fun onCreate() {
        super.onCreate()

        val appsflyer = AppsFlyerLib.getInstance()
        bubblePasswordSetDebufLogger(appsflyer)
        bubblePasswordMinTimeBetween(appsflyer)


        appsflyer.init(
            BUBBLE_PASSWORD_APP_DEV,
            object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                    Log.d(BUBBLE_PASSWORD_MAIN_TAG, "onConversionDataSuccess: $p0")

                    val afStatus = p0?.get("af_status")?.toString() ?: "null"
                    if (afStatus == "Organic") {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                delay(5000)
                                val api = bubblePasswordGetApi(
                                    "https://gcdsdk.appsflyer.com/install_data/v4.0/",
                                    null
                                )
                                val response = api.bubblePasswordGetClient(
                                    devkey = BUBBLE_PASSWORD_APP_DEV,
                                    deviceId = bubblePasswordGetAppsflyerId()
                                ).awaitResponse()

                                val resp = response.body()
                                Log.d(BUBBLE_PASSWORD_MAIN_TAG, "After 5s: $resp")
                                if (resp?.get("af_status") == "Organic") {
                                    safeResume(BubblePasswordAppsFlyerState.BubblePasswordError)
                                } else {
                                    safeResume(
                                        BubblePasswordAppsFlyerState.BubblePasswordSuccess(resp)
                                    )
                                }
                            } catch (d: Exception) {
                                Log.d(BUBBLE_PASSWORD_MAIN_TAG, "Error: ${d.message}")
                                safeResume(BubblePasswordAppsFlyerState.BubblePasswordError)
                            }
                        }
                    } else {
                        safeResume(BubblePasswordAppsFlyerState.BubblePasswordSuccess(p0))
                    }
                }

                override fun onConversionDataFail(p0: String?) {
                    Log.d(BUBBLE_PASSWORD_MAIN_TAG, "onConversionDataFail: $p0")
                    safeResume(BubblePasswordAppsFlyerState.BubblePasswordError)
                }

                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                    Log.d(BUBBLE_PASSWORD_MAIN_TAG, "onAppOpenAttribution")
//                        safeResume(BubblePasswordAppsFlyerState.BubblePasswordError)
                }

                override fun onAttributionFailure(p0: String?) {
                    Log.d(BUBBLE_PASSWORD_MAIN_TAG, "onAttributionFailure: $p0")
//                        safeResume(BubblePasswordAppsFlyerState.BubblePasswordError)
                }
            },
            this
        )

        appsflyer.start(this, BUBBLE_PASSWORD_APP_DEV, object :
            AppsFlyerRequestListener {
            override fun onSuccess() {
                Log.d(BUBBLE_PASSWORD_MAIN_TAG, "AppsFlyer started")
            }

            override fun onError(p0: Int, p1: String) {
                Log.d(BUBBLE_PASSWORD_MAIN_TAG, "AppsFlyer start error: $p0 - $p1")
                safeResume(BubblePasswordAppsFlyerState.BubblePasswordError)
            }
        })
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BubblePasswordApp)
            modules(
                listOf(
                    bubblePasswordModule
                )
            )
        }
    }

    private fun safeResume(state: BubblePasswordAppsFlyerState) {
        if (!bubblePasswordIsResumed) {
            bubblePasswordIsResumed = true
            bubblePasswordConversionFlow.value = state
        }
    }

    private fun bubblePasswordGetAppsflyerId(): String {
        val appsflyrid = AppsFlyerLib.getInstance().getAppsFlyerUID(this) ?: ""
        Log.d(BUBBLE_PASSWORD_MAIN_TAG, "AppsFlyer: AppsFlyer Id = $appsflyrid")
        return appsflyrid
    }

    private fun bubblePasswordSetDebufLogger(appsflyer: AppsFlyerLib) {
        appsflyer.setDebugLog(true)
    }

    private fun bubblePasswordMinTimeBetween(appsflyer: AppsFlyerLib) {
        appsflyer.setMinTimeBetweenSessions(0)
    }

    private fun bubblePasswordGetApi(url: String, client: OkHttpClient?) : BubblePasswordAppsApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(client ?: OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create()
    }

    companion object {
        var bubblePasswordInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        val bubblePasswordConversionFlow: MutableStateFlow<BubblePasswordAppsFlyerState> = MutableStateFlow(BubblePasswordAppsFlyerState.BubblePasswordDefault)
        var BUBBLE_PASSWORD_FB_LI: String? = null
        const val BUBBLE_PASSWORD_MAIN_TAG = "BubblePasswordMainTag"
    }
}