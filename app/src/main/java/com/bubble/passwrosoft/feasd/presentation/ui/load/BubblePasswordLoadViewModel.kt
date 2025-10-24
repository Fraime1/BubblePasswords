package com.bubble.passwrosoft.feasd.presentation.ui.load

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bubble.passwrosoft.feasd.data.shar.BubblePasswordSharedPreference
import com.bubble.passwrosoft.feasd.data.utils.BubblePasswordSystemService
import com.bubble.passwrosoft.feasd.domain.usecases.BubblePasswordGetAllUseCase
import com.bubble.passwrosoft.feasd.presentation.app.BubblePasswordAppsFlyerState
import com.bubble.passwrosoft.feasd.presentation.app.BubblePasswordApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BubblePasswordLoadViewModel(
    private val bubblePasswordGetAllUseCase: BubblePasswordGetAllUseCase,
    private val bubblePasswordSharedPreference: BubblePasswordSharedPreference,
    private val bubblePasswordSystemService: BubblePasswordSystemService
) : ViewModel() {

    private val _bubblePasswordHomeScreenState: MutableStateFlow<BubblePasswordHomeScreenState> =
        MutableStateFlow(BubblePasswordHomeScreenState.BubblePasswordLoading)
    val bubblePasswordHomeScreenState = _bubblePasswordHomeScreenState.asStateFlow()

    private var bubblePasswordGetApps = false


    init {
        viewModelScope.launch {
            when (bubblePasswordSharedPreference.bubblePasswordAppState) {
                0 -> {
                    if (bubblePasswordSystemService.bubblePasswordIsOnline()) {
                        BubblePasswordApp.bubblePasswordConversionFlow.collect {
                            when(it) {
                                BubblePasswordAppsFlyerState.BubblePasswordDefault -> {}
                                BubblePasswordAppsFlyerState.BubblePasswordError -> {
                                    bubblePasswordSharedPreference.bubblePasswordAppState = 2
                                    _bubblePasswordHomeScreenState.value =
                                        BubblePasswordHomeScreenState.BubblePasswordError
                                    bubblePasswordGetApps = true
                                }
                                is BubblePasswordAppsFlyerState.BubblePasswordSuccess -> {
                                    if (!bubblePasswordGetApps) {
                                        bubblePasswordGetData(it.bubblePasswordData)
                                        bubblePasswordGetApps = true
                                    }
                                }
                            }
                        }
                    } else {
                        _bubblePasswordHomeScreenState.value = BubblePasswordHomeScreenState.BubblePasswordNotInternet
                    }
                }
                1 -> {
                    if (bubblePasswordSystemService.bubblePasswordIsOnline()) {
                        if (BubblePasswordApp.BUBBLE_PASSWORD_FB_LI != null) {
                            _bubblePasswordHomeScreenState.value = BubblePasswordHomeScreenState.BubblePasswordSuccess(
                                BubblePasswordApp.BUBBLE_PASSWORD_FB_LI.toString()
                            )
                        } else if (System.currentTimeMillis() / 1000 > bubblePasswordSharedPreference.bubblePasswordExpired) {
                            Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "Current time more then expired, repeat request")
                            BubblePasswordApp.bubblePasswordConversionFlow.collect {
                                when(it) {
                                    BubblePasswordAppsFlyerState.BubblePasswordDefault -> {}
                                    BubblePasswordAppsFlyerState.BubblePasswordError -> {
                                        _bubblePasswordHomeScreenState.value =
                                            BubblePasswordHomeScreenState.BubblePasswordSuccess(
                                                bubblePasswordSharedPreference.bubblePasswordSavedUrl
                                            )
                                        bubblePasswordGetApps = true
                                    }
                                    is BubblePasswordAppsFlyerState.BubblePasswordSuccess -> {
                                        if (!bubblePasswordGetApps) {
                                            bubblePasswordGetData(it.bubblePasswordData)
                                            bubblePasswordGetApps = true
                                        }
                                    }
                                }
                            }
                        } else {
                            Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "Current time less then expired, use saved url")
                            _bubblePasswordHomeScreenState.value =
                                BubblePasswordHomeScreenState.BubblePasswordSuccess(bubblePasswordSharedPreference.bubblePasswordSavedUrl)
                        }
                    } else {
                        _bubblePasswordHomeScreenState.value = BubblePasswordHomeScreenState.BubblePasswordNotInternet
                    }
                }
                2 -> {
                    _bubblePasswordHomeScreenState.value = BubblePasswordHomeScreenState.BubblePasswordError
                }
            }
        }
    }


    private suspend fun bubblePasswordGetData(conversation: MutableMap<String, Any>?) {
        val bubblePasswordData = bubblePasswordGetAllUseCase.invoke(conversation)
        if (bubblePasswordSharedPreference.bubblePasswordAppState == 0) {
            if (bubblePasswordData == null) {
                bubblePasswordSharedPreference.bubblePasswordAppState = 2
                _bubblePasswordHomeScreenState.value = BubblePasswordHomeScreenState.BubblePasswordError
            } else {
                bubblePasswordSharedPreference.bubblePasswordAppState = 1
                bubblePasswordSharedPreference.apply {
                    bubblePasswordExpired = bubblePasswordData.expires
                    bubblePasswordSavedUrl = bubblePasswordData.url
                }
                _bubblePasswordHomeScreenState.value = BubblePasswordHomeScreenState.BubblePasswordSuccess(bubblePasswordData.url)
            }
        } else  {
            if (bubblePasswordData == null) {
                _bubblePasswordHomeScreenState.value =
                    BubblePasswordHomeScreenState.BubblePasswordSuccess(bubblePasswordSharedPreference.bubblePasswordSavedUrl)
            } else {
                bubblePasswordSharedPreference.apply {
                    bubblePasswordExpired = bubblePasswordData.expires
                    bubblePasswordSavedUrl = bubblePasswordData.url
                }
                _bubblePasswordHomeScreenState.value = BubblePasswordHomeScreenState.BubblePasswordSuccess(bubblePasswordData.url)
            }
        }
    }


    sealed class BubblePasswordHomeScreenState {
        data object BubblePasswordLoading : BubblePasswordHomeScreenState()
        data object BubblePasswordError : BubblePasswordHomeScreenState()
        data class BubblePasswordSuccess(val data: String) : BubblePasswordHomeScreenState()
        data object BubblePasswordNotInternet: BubblePasswordHomeScreenState()
    }
}