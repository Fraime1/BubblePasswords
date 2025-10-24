package com.bubble.passwrosoft.feasd.presentation.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Message
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.PermissionRequest
import android.webkit.URLUtil
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import com.bubble.passwrosoft.feasd.presentation.app.BubblePasswordApp

class BubblePasswordVi(
    private val bubblePasswordContext: Context,
    private val bubblePasswordCallback: BubblePasswordCallBack,
    private val bubblePasswordWindow: Window
) : WebView(bubblePasswordContext) {
    init {
        val webSettings = settings
        webSettings.apply {
            setSupportMultipleWindows(true)
            allowFileAccess = true
            allowContentAccess = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            userAgentString = WebSettings.getDefaultUserAgent(bubblePasswordContext).replace("; wv)", "").replace("Version/4.0 ", "")
            @SuppressLint("SetJavaScriptEnabled")
            javaScriptEnabled = true
            cacheMode = WebSettings.LOAD_NO_CACHE
        }
        isNestedScrollingEnabled = true



        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        super.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?,
            ): Boolean {
                val link = request?.url?.toString() ?: ""

                return if (request?.isRedirect == true) {
                    view?.loadUrl(request?.url.toString())
                    true
                }
                else if (URLUtil.isNetworkUrl(link)) {
                    false
                } else if(link.startsWith("intent")){
                    bubblePasswordIntentStart(link)
                    true
                } else {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    try {
                        bubblePasswordContext.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(bubblePasswordContext, "This application not found", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
            }


            override fun onPageFinished(view: WebView?, url: String?) {
                CookieManager.getInstance().flush()
                bubblePasswordCallback.bubblePasswordOnFirstPageFinished()
                if (url?.contains("ninecasino") == true) {
                    BubblePasswordApp.bubblePasswordInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
                    Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "onPageFinished : ${BubblePasswordApp.bubblePasswordInputMode}")
                    bubblePasswordWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                } else {
                    BubblePasswordApp.bubblePasswordInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                    Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "onPageFinished : ${BubblePasswordApp.bubblePasswordInputMode}")
                    bubblePasswordWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
            }


        })

        super.setWebChromeClient(object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest?) {
                bubblePasswordCallback.bubblePasswordOnPermissionRequest(request)
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: WebChromeClient.FileChooserParams?,
            ): Boolean {
                bubblePasswordCallback.bubblePasswordOnShowFileChooser(filePathCallback)
                return true
            }
            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
            ): Boolean {
                bubblePasswordHandleCreateWebWindowRequest(resultMsg)
                return true
            }
        })
    }


    fun bubblePasswordFLoad(link: String) {
        super.loadUrl(link)
    }

    private fun bubblePasswordHandleCreateWebWindowRequest(resultMsg: Message?) {
        if (resultMsg == null) return
        if (resultMsg.obj != null && resultMsg.obj is WebView.WebViewTransport) {
            val transport = resultMsg.obj as WebView.WebViewTransport
            val windowWebView = BubblePasswordVi(bubblePasswordContext, bubblePasswordCallback, bubblePasswordWindow)
            transport.webView = windowWebView
            resultMsg.sendToTarget()
            bubblePasswordCallback.bubblePasswordHandleCreateWebWindowRequest(windowWebView)
        }
    }

    private fun bubblePasswordIntentStart(link: String) {
        var scheme = ""
        var token = ""
        val part1 = link.split("#").first()
        val part2 = link.split("#").last()
        token = part1.split("?").last()
        part2.split(";").forEach {
            if (it.startsWith("scheme")) {
                scheme = it.split("=").last()
            }
        }
        val finalUriString = "$scheme://receiveetransfer?$token"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(finalUriString))
        try {
            bubblePasswordContext.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(bubblePasswordContext, "This application not found", Toast.LENGTH_SHORT).show()
        }
    }

}