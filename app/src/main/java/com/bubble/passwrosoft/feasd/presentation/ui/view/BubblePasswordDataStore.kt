package com.bubble.passwrosoft.feasd.presentation.ui.view

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class BubblePasswordDataStore : ViewModel(){
    val bubblePasswordViList: MutableList<BubblePasswordVi> = mutableListOf()
    private val _bubblePasswordIsFirstFinishPage: MutableStateFlow<Boolean> = MutableStateFlow(true)

    fun bubblePasswordSetIsFirstFinishPage() {
        _bubblePasswordIsFirstFinishPage.value = false
    }
}