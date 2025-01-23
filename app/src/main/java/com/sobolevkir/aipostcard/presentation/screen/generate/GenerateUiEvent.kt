package com.sobolevkir.aipostcard.presentation.screen.generate

sealed interface GenerateUiEvent {

    data class PromptChange(val prompt: String) : GenerateUiEvent
    data class NegativePromptChange(val negativePrompt: String) : GenerateUiEvent
    data class StyleSelect(val styleName: String) : GenerateUiEvent
    data object FullScreenToggle : GenerateUiEvent
    data object RetryButtonClick : GenerateUiEvent
    data object SubmitButtonClick : GenerateUiEvent
    data object SaveToDeviceGalleryClick : GenerateUiEvent
    data object AddToAlbumClick : GenerateUiEvent
    data object ShareClick : GenerateUiEvent

}