package com.bubble.passwrosoft

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.ValueCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.bubble.passwrosoft.feasd.presentation.app.BubblePasswordApp
import com.bubble.passwrosoft.feasd.presentation.pushhandler.BubblePasswordPushHandler
import org.koin.android.ext.android.inject

class BubblePasswordActivity : AppCompatActivity() {

    lateinit var bubblePasswordPhoto: Uri
    var bubblePasswordFilePathFromChrome: ValueCallback<Array<Uri>>? = null

    val bubblePasswordTakeFile = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        bubblePasswordFilePathFromChrome?.onReceiveValue(arrayOf(it ?: Uri.EMPTY))
    }

    val bubblePasswordTakePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            bubblePasswordFilePathFromChrome?.onReceiveValue(arrayOf(bubblePasswordPhoto))
        } else {
            bubblePasswordFilePathFromChrome?.onReceiveValue(null)
        }
    }

    private val bubblePasswordPushHandler by inject<BubblePasswordPushHandler>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bubblePasswordSetupSystemBars()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_bubble_password)
        val bubblePasswordRootView = findViewById<View>(android.R.id.content)
        GlobalLayoutUtil().assistActivity(this)
        ViewCompat.setOnApplyWindowInsetsListener(bubblePasswordRootView) { bubblePasswordView, bubblePasswordInsets ->
            val bubblePasswordSystemBars = bubblePasswordInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val bubblePasswordDisplayCutout = bubblePasswordInsets.getInsets(WindowInsetsCompat.Type.displayCutout())
            val bubblePasswordIme = bubblePasswordInsets.getInsets(WindowInsetsCompat.Type.ime())


            val bubblePasswordTopPadding = maxOf(bubblePasswordSystemBars.top, bubblePasswordDisplayCutout.top)
            val bubblePasswordLeftPadding = maxOf(bubblePasswordSystemBars.left, bubblePasswordDisplayCutout.left)
            val bubblePasswordRightPadding = maxOf(bubblePasswordSystemBars.right, bubblePasswordDisplayCutout.right)
            window.setSoftInputMode(BubblePasswordApp.bubblePasswordInputMode)

            if (window.attributes.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN) {
                Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "ADJUST PUN")
                val bubblePasswordBottomInset = maxOf(bubblePasswordSystemBars.bottom, bubblePasswordDisplayCutout.bottom)

                bubblePasswordView.setPadding(bubblePasswordLeftPadding, bubblePasswordTopPadding, bubblePasswordRightPadding, 0)

                bubblePasswordView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = bubblePasswordBottomInset
                }
            } else {
                Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "ADJUST RESIZE")

                val bubblePasswordBottomInset = maxOf(bubblePasswordSystemBars.bottom, bubblePasswordDisplayCutout.bottom, bubblePasswordIme.bottom)

                bubblePasswordView.setPadding(bubblePasswordLeftPadding, bubblePasswordTopPadding, bubblePasswordRightPadding, 0)

                bubblePasswordView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = bubblePasswordBottomInset
                }
            }



            WindowInsetsCompat.CONSUMED
        }
        Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "Activity onCreate()")
        bubblePasswordPushHandler.bubblePasswordHandlePush(intent.extras)
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            bubblePasswordSetupSystemBars()
        }
    }

    override fun onResume() {
        super.onResume()
        bubblePasswordSetupSystemBars()
    }
}