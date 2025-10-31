package com.bubble.passwrosoft.feasd.presentation.ui.view

import android.annotation.SuppressLint
import android.widget.FrameLayout
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class BubblePasswordDataStore : ViewModel(){
    val bubblePasswordViList: MutableList<BubblePasswordVi> = mutableListOf()
    private val _bubblePasswordIsFirstFinishPage: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var bubblePasswordIsFirstCreate = true
    @SuppressLint("StaticFieldLeak")
    lateinit var bubblePasswordContainerView: FrameLayout
    @SuppressLint("StaticFieldLeak")
    lateinit var bubblePasswordView: BubblePasswordVi
    fun bubblePasswordSetIsFirstFinishPage() {
        _bubblePasswordIsFirstFinishPage.value = false
    }
}