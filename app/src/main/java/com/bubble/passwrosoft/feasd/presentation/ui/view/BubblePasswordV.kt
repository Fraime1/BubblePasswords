package com.bubble.passwrosoft.feasd.presentation.ui.view

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bubble.passwrosoft.BubblePasswordActivity
import com.bubble.passwrosoft.R
import com.bubble.passwrosoft.feasd.presentation.app.BubblePasswordApp
import com.bubble.passwrosoft.feasd.presentation.ui.load.BubblePasswordLoadFragment
import org.koin.android.ext.android.inject

class BubblePasswordV : Fragment(){

    private lateinit var bubblePasswordPhoto: Uri
    private var bubblePasswordFilePathFromChrome: ValueCallback<Array<Uri>>? = null

    private val bubblePasswordTakeFile: ActivityResultLauncher<PickVisualMediaRequest> = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        bubblePasswordFilePathFromChrome?.onReceiveValue(arrayOf(it ?: Uri.EMPTY))
        bubblePasswordFilePathFromChrome = null
    }

    private val bubblePasswordTakePhoto: ActivityResultLauncher<Uri> = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            bubblePasswordFilePathFromChrome?.onReceiveValue(arrayOf(bubblePasswordPhoto))
            bubblePasswordFilePathFromChrome = null
        } else {
            bubblePasswordFilePathFromChrome?.onReceiveValue(null)
            bubblePasswordFilePathFromChrome = null
        }
    }

    private val bubblePasswordDataStore by activityViewModels<BubblePasswordDataStore>()


    private val bubblePasswordViFun by inject<BubblePasswordViFun>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "Fragment onCreate")
        CookieManager.getInstance().setAcceptCookie(true)
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (bubblePasswordDataStore.bubblePasswordView.canGoBack()) {
                        bubblePasswordDataStore.bubblePasswordView.goBack()
                        Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "WebView can go back")
                    } else if (bubblePasswordDataStore.bubblePasswordViList.size > 1) {
                        Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "WebView can`t go back")
                        bubblePasswordDataStore.bubblePasswordViList.removeAt(bubblePasswordDataStore.bubblePasswordViList.lastIndex)
                        Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "WebView list size ${bubblePasswordDataStore.bubblePasswordViList.size}")
                        bubblePasswordDataStore.bubblePasswordView.destroy()
                        val previousWebView = bubblePasswordDataStore.bubblePasswordViList.last()
                        attachWebViewToContainer(previousWebView)
                        bubblePasswordDataStore.bubblePasswordView = previousWebView
                    }
                }

            })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (bubblePasswordDataStore.bubblePasswordIsFirstCreate) {
            bubblePasswordDataStore.bubblePasswordIsFirstCreate = false
            bubblePasswordDataStore.bubblePasswordContainerView = FrameLayout(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                id = View.generateViewId()
            }
            return bubblePasswordDataStore.bubblePasswordContainerView
        } else {
            return bubblePasswordDataStore.bubblePasswordContainerView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "onViewCreated")
        if (bubblePasswordDataStore.bubblePasswordViList.isEmpty()) {
            bubblePasswordDataStore.bubblePasswordView = BubblePasswordVi(requireContext(), object :
                BubblePasswordCallBack {
                override fun bubblePasswordHandleCreateWebWindowRequest(bubblePasswordVi: BubblePasswordVi) {
                    bubblePasswordDataStore.bubblePasswordViList.add(bubblePasswordVi)
                    Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "WebView list size = ${bubblePasswordDataStore.bubblePasswordViList.size}")
                    Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "CreateWebWindowRequest")
                    bubblePasswordDataStore.bubblePasswordView = bubblePasswordVi
                    bubblePasswordVi.setFileChooserHandler { callback ->
                        handleFileChooser(callback)
                    }
                    attachWebViewToContainer(bubblePasswordVi)
                }

                override fun bubblePasswordOnPermissionRequest(bubblePasswordRequest: PermissionRequest?) {
                    bubblePasswordRequest?.grant(bubblePasswordRequest.resources)
                }

                override fun bubblePasswordOnFirstPageFinished() {
                    bubblePasswordDataStore.bubblePasswordSetIsFirstFinishPage()
                }

            }, bubblePasswordWindow = requireActivity().window).apply {
                setFileChooserHandler { callback ->
                    handleFileChooser(callback)
                }
            }
            bubblePasswordDataStore.bubblePasswordView.bubblePasswordFLoad(arguments?.getString(BubblePasswordLoadFragment.BUBBLE_PASSWORD_D) ?: "")
//            ejvview.fLoad("www.google.com")
            bubblePasswordDataStore.bubblePasswordViList.add(bubblePasswordDataStore.bubblePasswordView)
            attachWebViewToContainer(bubblePasswordDataStore.bubblePasswordView)
        } else {
            bubblePasswordDataStore.bubblePasswordViList.forEach { webView ->
                webView.setFileChooserHandler { callback ->
                    handleFileChooser(callback)
                }
            }
            bubblePasswordDataStore.bubblePasswordView = bubblePasswordDataStore.bubblePasswordViList.last()

            attachWebViewToContainer(bubblePasswordDataStore.bubblePasswordView)
        }
        Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "WebView list size = ${bubblePasswordDataStore.bubblePasswordViList.size}")
    }

    private fun handleFileChooser(callback: ValueCallback<Array<Uri>>?) {
        Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "handleFileChooser called, callback: ${callback != null}")

        bubblePasswordFilePathFromChrome = callback

        val listItems: Array<out String> = arrayOf("Select from file", "To make a photo")
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                0 -> {
                    Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "Launching file picker")
                    bubblePasswordTakeFile.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
                1 -> {
                    Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "Launching camera")
                    bubblePasswordPhoto = bubblePasswordViFun.bubblePasswordSavePhoto()
                    bubblePasswordTakePhoto.launch(bubblePasswordPhoto)
                }
            }
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Choose a method")
            .setItems(listItems, listener)
            .setCancelable(true)
            .setOnCancelListener {
                Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "File chooser canceled")
                callback?.onReceiveValue(null)
                bubblePasswordFilePathFromChrome = null
            }
            .create()
            .show()
    }

    private fun attachWebViewToContainer(w: BubblePasswordVi) {
        bubblePasswordDataStore.bubblePasswordContainerView.post {
            // Убираем предыдущую WebView, если есть
            (w.parent as? ViewGroup)?.removeView(w)
            bubblePasswordDataStore.bubblePasswordContainerView.removeAllViews()
            bubblePasswordDataStore.bubblePasswordContainerView.addView(w)
        }
    }




}