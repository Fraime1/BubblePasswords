package com.bubble.passwrosoft.feasd.domain.usecases

import android.util.Log
import com.bubble.passwrosoft.feasd.data.repo.BubblePasswordRepository
import com.bubble.passwrosoft.feasd.data.utils.BubblePasswordPushToken
import com.bubble.passwrosoft.feasd.data.utils.BubblePasswordSystemService
import com.bubble.passwrosoft.feasd.domain.model.BubblePasswordEntity
import com.bubble.passwrosoft.feasd.domain.model.BubblePasswordParam
import com.bubble.passwrosoft.feasd.presentation.app.BubblePasswordApp

class BubblePasswordGetAllUseCase(
    private val bubblePasswordRepository: BubblePasswordRepository,
    private val bubblePasswordSystemService: BubblePasswordSystemService,
    private val bubblePasswordPushToken: BubblePasswordPushToken,
) {
    suspend operator fun invoke(conversion: MutableMap<String, Any>?) : BubblePasswordEntity?{
        val params = BubblePasswordParam(
            locale = bubblePasswordSystemService.bubblePasswordGetLocale(),
            pushToken = bubblePasswordPushToken.bubblePasswordGetToken(),
            afId = bubblePasswordSystemService.bubblePasswordGetAppsflyerId()
        )
//        bubblePasswordSystemService.bubblePasswrodGetGaid()
        Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "Params for request: $params")
        return bubblePasswordRepository.bubblePasswordGetClient(params, conversion)
    }



}