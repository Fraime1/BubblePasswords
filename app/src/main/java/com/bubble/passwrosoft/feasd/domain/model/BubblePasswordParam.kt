package com.bubble.passwrosoft.feasd.domain.model

import com.google.gson.annotations.SerializedName

data class BubblePasswordParam (
    @SerializedName("af_id")
    val afId: String,
    @SerializedName("bundle_id")
    val bundleId: String = "com.bubble.passwrosoft",
    @SerializedName("os")
    val os: String = "Android",
    @SerializedName("store_id")
    val storeId: String = "com.bubble.passwrosoft",
    @SerializedName("locale")
    val locale: String,
    @SerializedName("push_token")
    val pushToken: String,
    @SerializedName("firebase_project_id")
    val firebaseProjectId: String = "bubblepassword",

)