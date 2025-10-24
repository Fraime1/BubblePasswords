package com.bubble.passwrosoft.feasd.presentation.di

import com.bubble.passwrosoft.feasd.data.repo.BubblePasswordRepository
import com.bubble.passwrosoft.feasd.data.shar.BubblePasswordSharedPreference
import com.bubble.passwrosoft.feasd.data.utils.BubblePasswordPushToken
import com.bubble.passwrosoft.feasd.data.utils.BubblePasswordSystemService
import com.bubble.passwrosoft.feasd.domain.usecases.BubblePasswordGetAllUseCase
import com.bubble.passwrosoft.feasd.presentation.pushhandler.BubblePasswordPushHandler
import com.bubble.passwrosoft.feasd.presentation.ui.load.BubblePasswordLoadViewModel
import com.bubble.passwrosoft.feasd.presentation.ui.view.BubblePasswordViFun
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val bubblePasswordModule = module {
    factory {
        BubblePasswordPushHandler()
    }
    single {
        BubblePasswordRepository()
    }
    single {
        BubblePasswordSharedPreference(get())
    }
    factory {
        BubblePasswordPushToken()
    }
    factory {
        BubblePasswordSystemService(get())
    }
    factory {
        BubblePasswordGetAllUseCase(
            get(), get(), get()
        )
    }
    factory {
        BubblePasswordViFun(get())
    }
    viewModel {
        BubblePasswordLoadViewModel(get(), get(), get())
    }
}