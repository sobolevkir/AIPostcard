package com.sobolevkir.aipostcard.presentation.screen.album

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AlbumScreen() {
    //val viewModel: AlbumViewModel = hiltViewModel()
    //val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AlbumView(
        //onEvent = viewModel::onEvent,
        //state = uiState
    )
}

@Composable
fun AlbumView(
    //onEvent: (AlbumScreenEvent) -> Unit = {},
    //state: AlbumScreenState = AlbumScreenState()
) {

    Text("Album")

}