package com.bubble.passwrosoft.feasd.domain.model

import com.google.gson.annotations.SerializedName


data class BubblePasswordEntity (
    @SerializedName("ok")
    val ok: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("expires")
    val expires: Int,
)