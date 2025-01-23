package com.sobolevkir.aipostcard.presentation.screen.generate

sealed class GenerateScreenEvent {

    data class PromptChange(val prompt: String) : GenerateScreenEvent()
    data class NegativePromptChange(val negativePrompt: String) : GenerateScreenEvent()
    data class StyleSelect(val styleName: String) : GenerateScreenEvent()
    data object MessageShown : GenerateScreenEvent()
    data object FullScreenToggle : GenerateScreenEvent()
    data object RetryButtonClick : GenerateScreenEvent()
    data object SubmitButtonClick : GenerateScreenEvent()
    data object SaveToDeviceGalleryClick : GenerateScreenEvent()
    data object ShareClick : GenerateScreenEvent()

}