package com.bubble.passwrosoft.feasd.presentation.ui.load

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bubble.passwrosoft.MainActivity
import com.bubble.passwrosoft.R
import com.bubble.passwrosoft.databinding.FragmentLoadBubblePasswordBinding
import com.bubble.passwrosoft.feasd.data.shar.BubblePasswordSharedPreference
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class BubblePasswordLoadFragment : Fragment(R.layout.fragment_load_bubble_password) {
    private lateinit var bubblePasswordLoadBinding: FragmentLoadBubblePasswordBinding

    private val bubblePasswordLoadViewModel by viewModel<BubblePasswordLoadViewModel>()

    private val bubblePasswordSharedPreference by inject<BubblePasswordSharedPreference>()

    private var bubblePasswordUrl = ""

    private val bubblePasswordRequestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            bubblePasswordNavigateToSuccess(bubblePasswordUrl)
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                bubblePasswordSharedPreference.bubblePasswordNotificationRequest =
                    (System.currentTimeMillis() / 1000) + 259200
                bubblePasswordNavigateToSuccess(bubblePasswordUrl)
            } else {
                bubblePasswordNavigateToSuccess(bubblePasswordUrl)
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bubblePasswordLoadBinding = FragmentLoadBubblePasswordBinding.bind(view)

        bubblePasswordLoadBinding.bubblePasswordGrantButton.setOnClickListener {
            val bubblePasswordPermission = Manifest.permission.POST_NOTIFICATIONS
            bubblePasswordRequestNotificationPermission.launch(bubblePasswordPermission)
            bubblePasswordSharedPreference.bubblePasswordNotificationRequestedBefore = true
        }

        bubblePasswordLoadBinding.bubblePasswordSkipButton.setOnClickListener {
            bubblePasswordSharedPreference.bubblePasswordNotificationRequest =
                (System.currentTimeMillis() / 1000) + 259200
            bubblePasswordNavigateToSuccess(bubblePasswordUrl)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bubblePasswordLoadViewModel.bubblePasswordHomeScreenState.collect {
                    when (it) {
                        is BubblePasswordLoadViewModel.BubblePasswordHomeScreenState.BubblePasswordLoading -> {

                        }

                        is BubblePasswordLoadViewModel.BubblePasswordHomeScreenState.BubblePasswordError -> {
                            requireActivity().startActivity(
                                Intent(
                                    requireContext(),
                                    MainActivity::class.java
                                )
                            )
                            requireActivity().finish()
                        }

                        is BubblePasswordLoadViewModel.BubblePasswordHomeScreenState.BubblePasswordSuccess -> {
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
                                val bubblePasswordPermission = Manifest.permission.POST_NOTIFICATIONS
                                val bubblePasswordPermissionRequestedBefore = bubblePasswordSharedPreference.bubblePasswordNotificationRequestedBefore

                                if (ContextCompat.checkSelfPermission(requireContext(), bubblePasswordPermission) == PackageManager.PERMISSION_GRANTED) {
                                    bubblePasswordNavigateToSuccess(it.data)
                                } else if (!bubblePasswordPermissionRequestedBefore && (System.currentTimeMillis() / 1000 > bubblePasswordSharedPreference.bubblePasswordNotificationRequest)) {
                                    // первый раз — показываем UI для запроса
                                    bubblePasswordLoadBinding.bubblePasswordNotiGroup.visibility = View.VISIBLE
                                    bubblePasswordLoadBinding.bubblePasswordLoadingGroup.visibility = View.GONE
                                    bubblePasswordUrl = it.data
                                } else if (shouldShowRequestPermissionRationale(bubblePasswordPermission)) {
                                    // временный отказ — через 3 дня можно показать
                                    if (System.currentTimeMillis() / 1000 > bubblePasswordSharedPreference.bubblePasswordNotificationRequest) {
                                        bubblePasswordLoadBinding.bubblePasswordNotiGroup.visibility = View.VISIBLE
                                        bubblePasswordLoadBinding.bubblePasswordLoadingGroup.visibility = View.GONE
                                        bubblePasswordUrl = it.data
                                    } else {
                                        bubblePasswordNavigateToSuccess(it.data)
                                    }
                                } else {
                                    // навсегда отклонено — просто пропускаем
                                    bubblePasswordNavigateToSuccess(it.data)
                                }
                            } else {
                                bubblePasswordNavigateToSuccess(it.data)
                            }
                        }

                        BubblePasswordLoadViewModel.BubblePasswordHomeScreenState.BubblePasswordNotInternet -> {
                            bubblePasswordLoadBinding.bubblePasswordLoadConnectionStateText.visibility = View.VISIBLE
                            bubblePasswordLoadBinding.bubblePasswordLoadingGroup.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }


    private fun bubblePasswordNavigateToSuccess(data: String) {
        findNavController().navigate(
            R.id.action_bubblePasswordLoadFragment_to_bubblePasswordV,
            bundleOf(BUBBLE_PASSWORD_D to data)
        )
    }

    companion object {
        const val BUBBLE_PASSWORD_D = "bubblePasswordData"
    }
}