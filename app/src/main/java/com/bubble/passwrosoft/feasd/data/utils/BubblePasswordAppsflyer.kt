package com.bubble.passwrosoft.feasd.data.utils

import android.content.Context
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.bubble.passwrosoft.feasd.presentation.app.BubblePasswordApp
import com.bubble.passwrosoft.feasd.presentation.app.BubblePasswordAppsFlyerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


private const val BUBBLE_PASSWORD_APP_DEV = "VrbyKhJnxRgK5GxARAx6eQ"

class BubblePasswordAppsflyer(private val context: Context) {


    suspend fun init(): BubblePasswordAppsFlyerState = withContext(Dispatchers.IO) {
        suspendCoroutine { cont ->
            val appsflyer = AppsFlyerLib.getInstance()
            bubblePasswordSetDebufLogger(appsflyer)
            bubblePasswordMinTimeBetween(appsflyer)

            var isResumed = false
            fun safeResume(state: BubblePasswordAppsFlyerState) {
                if (!isResumed) {
                    isResumed = true
                    cont.resume(state)
                }
            }

            appsflyer.init(
                BUBBLE_PASSWORD_APP_DEV,
                object : AppsFlyerConversionListener {
                    override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                        Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "onConversionDataSuccess: $p0")

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
                                    Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "After 5s: $resp")
                                    if (resp?.get("af_status") == "Organic") {
                                        safeResume(BubblePasswordAppsFlyerState.BubblePasswordError)
                                    } else {
                                        safeResume(
                                            BubblePasswordAppsFlyerState.BubblePasswordSuccess(resp)
                                        )
                                    }
                                } catch (d: Exception) {
                                    Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "Error: ${d.message}")
                                    safeResume(BubblePasswordAppsFlyerState.BubblePasswordError)
                                }
                            }
                        } else {
                            safeResume(BubblePasswordAppsFlyerState.BubblePasswordSuccess(p0))
                        }
                    }

                    override fun onConversionDataFail(p0: String?) {
                        Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "onConversionDataFail: $p0")
                        safeResume(BubblePasswordAppsFlyerState.BubblePasswordError)
                    }

                    override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                        Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "onAppOpenAttribution")
//                        safeResume(BubblePasswordAppsFlyerState.BubblePasswordError)
                    }

                    override fun onAttributionFailure(p0: String?) {
                        Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "onAttributionFailure: $p0")
//                        safeResume(BubblePasswordAppsFlyerState.BubblePasswordError)
                    }
                },
                context.applicationContext
            )

            appsflyer.start(context, BUBBLE_PASSWORD_APP_DEV, object : AppsFlyerRequestListener {
                override fun onSuccess() {
                    Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "AppsFlyer started")
                }

                override fun onError(p0: Int, p1: String) {
                    Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "AppsFlyer start error: $p0 - $p1")
                    safeResume(BubblePasswordAppsFlyerState.BubblePasswordError)
                }
            })
        }
    }


    private fun bubblePasswordGetAppsflyerId(): String {
        val appsflyrid = AppsFlyerLib.getInstance().getAppsFlyerUID(context) ?: ""
        Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "AppsFlyer: AppsFlyer Id = $appsflyrid")
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

}


interface BubblePasswordAppsApi {
    @Headers("Content-Type: application/json")
    @GET("com.bubble.passwrosoft")
    fun bubblePasswordGetClient(
        @Query("devkey") devkey: String,
        @Query("device_id") deviceId: String,
    ): Call<MutableMap<String, Any>?>
}