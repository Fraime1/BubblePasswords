package com.bubble.passwrosoft.feasd.data.utils

import android.content.Context
import android.os.Looper
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
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


private const val BUBBLE_PASSWORD_APP_DEV = "VrbyKhJnxRgK5GxARAx6eQ"

class BubblePasswordAppsflyer(private val context: Context) {


    fun init(
        bubblePasswordCallback: (BubblePasswordAppsFlyerState) -> Unit
    ) {
        val appsflyer = AppsFlyerLib.getInstance()
        bubblePasswordSetDebufLogger(appsflyer)
        bubblePasswordMinTimeBetween(appsflyer)
        appsflyer.init(
            BUBBLE_PASSWORD_APP_DEV,
            object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                    Looper.prepare()
                    Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "AppsFlyer: onConversionDataSuccess")
                    Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "AppsFlyer: $p0")
                    Log.d(
                        BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG,
                        "AppsFlyer: af_status: ${p0?.get("af_status")}"
                    )
//                    bubblePasswordCallback(BubblePasswordAppsFlyerState.BubblePasswordSuccess(p0))
                    if (p0?.get("af_status") == "Organic") {
                        val corouteScope = CoroutineScope(Dispatchers.IO)
                        corouteScope.launch {
                            try {
                                delay(5000)
                                val api = bubblePasswordGetApi("https://gcdsdk.appsflyer.com/install_data/v4.0/", null)
                                val request = api.bubblePasswordGetClient(
                                    devkey = BUBBLE_PASSWORD_APP_DEV,
                                    deviceId = bubblePasswordGetAppsflyerId()
                                )
                                val response = request.awaitResponse()
                                Log.d(
                                    BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG,
                                    "AppsFlyer: Conversion after 5 seconds: ${response.body()}"
                                )
                                if (response.body()?.get("af_status") == "Organic") {
                                    bubblePasswordCallback(BubblePasswordAppsFlyerState.BubblePasswordError)
                                } else {
                                    bubblePasswordCallback(BubblePasswordAppsFlyerState.BubblePasswordSuccess(response.body()))
                                }
                            } catch (e: Exception) {
                                Log.d(
                                    BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG,
                                    "AppsFlyer: ${e.message}"
                                )
                                bubblePasswordCallback(BubblePasswordAppsFlyerState.BubblePasswordError)
                            }
                        }
                    } else {
                        bubblePasswordCallback(BubblePasswordAppsFlyerState.BubblePasswordSuccess(p0))
                    }
                }

                override fun onConversionDataFail(p0: String?) {
                    Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "AppsFlyer: onConversionDataFail: $p0")
                    bubblePasswordCallback(BubblePasswordAppsFlyerState.BubblePasswordError)
                }

                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                    Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "AppsFlyer: onAppOpenAttribution")
                    bubblePasswordCallback(BubblePasswordAppsFlyerState.BubblePasswordError)
                }

                override fun onAttributionFailure(p0: String?) {
                    Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "AppsFlyer: onAttributionFailure: $p0")
                    bubblePasswordCallback(BubblePasswordAppsFlyerState.BubblePasswordError)
                }
            },
            context.applicationContext
        )
        appsflyer.start(context, BUBBLE_PASSWORD_APP_DEV, object : AppsFlyerRequestListener {
            override fun onSuccess() {
                Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "AppsFlyer: Start is Success")
            }

            override fun onError(p0: Int, p1: String) {
                Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "AppsFlyer: Start is Error")
                Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "AppsFlyer: Error code: $p0, error message: $p1")
                bubblePasswordCallback(BubblePasswordAppsFlyerState.BubblePasswordError)
            }

        })
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