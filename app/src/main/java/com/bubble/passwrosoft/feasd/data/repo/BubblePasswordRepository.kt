package com.bubble.passwrosoft.feasd.data.repo

import android.util.Log
import com.bubble.passwrosoft.feasd.domain.model.BubblePasswordEntity
import com.bubble.passwrosoft.feasd.domain.model.BubblePasswordParam
import com.bubble.passwrosoft.feasd.presentation.app.BubblePasswordApp.Companion.BUBBLE_PASSWORD_MAIN_TAG
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface BubblePasswordApi {
    @Headers("Content-Type: application/json")
    @POST("config.php")
    fun getClient(
        @Body jsonString: JsonObject,
    ): Call<BubblePasswordEntity>
}


private const val BUBBLE_PASSWORD_MAIN = "https://bubblepasswords.com/"
class BubblePasswordRepository {

    suspend fun bubblePasswordGetClient(
        bubblePasswordParam: BubblePasswordParam,
        bubblePasswordConversion: MutableMap<String, Any>?
    ): BubblePasswordEntity? {
        val gson = Gson()
        val api = bubblePassowrdGetApi(BUBBLE_PASSWORD_MAIN, null)

        val bubblePasswordJsonObject = gson.toJsonTree(bubblePasswordParam).asJsonObject
        bubblePasswordConversion?.forEach { (key, value) ->
            val element: JsonElement = gson.toJsonTree(value)
            bubblePasswordJsonObject.add(key, element)
        }
        return try {
            val request: Call<BubblePasswordEntity> = api.getClient(
                jsonString = bubblePasswordJsonObject,
            )
//            Log.d(
//                EGGSAFE_MAIN_TAG,
//                "Retrofit: Request URL = ${(request.request() as Request).url}"
//            )
//            Log.d(
//                EGGSAFE_MAIN_TAG,
//                "Retrofit: Request Body = ${(request.request() as Request).body}"
//            )
            val bubblePasswordResult = request.awaitResponse()
            Log.d(BUBBLE_PASSWORD_MAIN_TAG, "Retrofit: Result code: ${bubblePasswordResult.code()}")
            if (bubblePasswordResult.code() == 200) {
                Log.d(BUBBLE_PASSWORD_MAIN_TAG, "Retrofit: Get request success")
                Log.d(BUBBLE_PASSWORD_MAIN_TAG, "Retrofit: Code = ${bubblePasswordResult.code()}")
                Log.d(BUBBLE_PASSWORD_MAIN_TAG, "Retrofit: ${bubblePasswordResult.body()}")
                bubblePasswordResult.body()
            } else {
                null
            }
        } catch (e: java.lang.Exception) {
            Log.d(BUBBLE_PASSWORD_MAIN_TAG, "Retrofit: Get request failed")
            Log.d(BUBBLE_PASSWORD_MAIN_TAG, "Retrofit: ${e.message}")
            null
        }
    }


    private fun bubblePassowrdGetApi(url: String, client: OkHttpClient?) : BubblePasswordApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(client ?: OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create()
    }


}
