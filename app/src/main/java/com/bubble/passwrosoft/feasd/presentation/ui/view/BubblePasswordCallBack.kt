package com.bubble.passwrosoft.feasd.presentation.ui.view


import android.net.Uri
import android.webkit.PermissionRequest
import android.webkit.ValueCallback

interface BubblePasswordCallBack {
    fun bubblePasswordHandleCreateWebWindowRequest(bubblePasswordVi: BubblePasswordVi)

    fun bubblePasswordOnPermissionRequest(bubblePasswordRequest: PermissionRequest?)

    fun bubblePasswordOnShowFileChooser(bubblePasswordFilePathCallback: ValueCallback<Array<Uri>>?)

    fun bubblePasswordOnFirstPageFinished()
}