package com.bubble.passwrosoft.feasd.presentation.ui.view

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bubble.passwrosoft.BubblePasswordActivity
import com.bubble.passwrosoft.R
import com.bubble.passwrosoft.feasd.presentation.ui.load.BubblePasswordLoadFragment
import org.koin.android.ext.android.inject

class BubblePasswordV : Fragment(){

    private val bubblePasswordDataStore by activityViewModels<BubblePasswordDataStore>()
    private lateinit var bubblePasswordView: BubblePasswordVi
    lateinit var bubblePasswordRequestFromChrome: PermissionRequest


    private val bubblePasswordViFun by inject<BubblePasswordViFun>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CookieManager.getInstance().setAcceptCookie(true)
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (bubblePasswordView.canGoBack()) {
                        bubblePasswordView.goBack()
                    } else if (bubblePasswordDataStore.bubblePasswordViList.size > 1) {
                        this.isEnabled = false
                        bubblePasswordDataStore.bubblePasswordViList.removeAt(bubblePasswordDataStore.bubblePasswordViList.lastIndex)
                        bubblePasswordView.destroy()
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                }

            })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (bubblePasswordDataStore.bubblePasswordViList.isEmpty()) {
            bubblePasswordView = BubblePasswordVi(requireContext(), object : BubblePasswordCallBack {
                override fun bubblePasswordHandleCreateWebWindowRequest(bubblePasswordVi: BubblePasswordVi) {
                    bubblePasswordDataStore.bubblePasswordViList.add(bubblePasswordVi)
                    findNavController().navigate(R.id.action_bubblePasswordV_self)
                }

                override fun bubblePasswordOnPermissionRequest(bubblePasswordRequest: PermissionRequest?) {
                    if (bubblePasswordRequest != null) {
                        bubblePasswordRequestFromChrome = bubblePasswordRequest
                    }
                    bubblePasswordRequestFromChrome.grant(bubblePasswordRequestFromChrome.resources)
                }

                override fun bubblePasswordOnShowFileChooser(bubblePasswordFilePathCallback: ValueCallback<Array<Uri>>?) {
                    (requireActivity() as BubblePasswordActivity).bubblePasswordFilePathFromChrome = bubblePasswordFilePathCallback
                    val listItems: Array<out String> =
                        arrayOf("Select from file", "To make a photo")
                    val listener = DialogInterface.OnClickListener { _, which ->
                        when (which) {
                            0 -> {
                                (requireActivity() as BubblePasswordActivity).bubblePasswordTakeFile.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            1 -> {
                                (requireActivity() as BubblePasswordActivity).bubblePasswordPhoto = bubblePasswordViFun.bubblePasswordSavePhoto()
                                (requireActivity() as BubblePasswordActivity).bubblePasswordTakePhoto.launch((requireActivity() as BubblePasswordActivity).bubblePasswordPhoto)
                            }
                        }
                    }
                    AlertDialog.Builder(requireActivity())
                        .setTitle("Choose a method")
                        .setItems(listItems, listener)
                        .setCancelable(true)
                        .setOnCancelListener {
                            bubblePasswordFilePathCallback?.onReceiveValue(arrayOf(Uri.EMPTY))
                        }
                        .create()
                        .show()
                }

                override fun bubblePasswordOnFirstPageFinished() {
                    bubblePasswordDataStore.bubblePasswordSetIsFirstFinishPage()
                }

            }, bubblePasswordWindow = requireActivity().window)
            bubblePasswordView.bubblePasswordFLoad(arguments?.getString(BubblePasswordLoadFragment.BUBBLE_PASSWORD_D) ?: "")
//            ejvview.fLoad("www.google.com")
            bubblePasswordDataStore.bubblePasswordViList.add(bubblePasswordView)
        } else {
            bubblePasswordView = bubblePasswordDataStore.bubblePasswordViList.last()
        }
        return bubblePasswordView
    }




}