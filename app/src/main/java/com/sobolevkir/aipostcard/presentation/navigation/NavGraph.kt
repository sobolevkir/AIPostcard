package com.sobolevkir.aipostcard.presentation.navigation

import androidx.annotation.StringRes
import com.sobolevkir.aipostcard.R

enum class NavGraph(@StringRes val title: Int) {

    Generate(title = R.string.title_generate),
    Album(title = R.string.title_album),
    Info(title = R.string.title_info),

}