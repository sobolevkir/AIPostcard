package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.domain.api.ExternalNavigator
import javax.inject.Inject

class ShareImageUseCase @Inject constructor(private val externalNavigator: ExternalNavigator) {

    operator fun invoke(imageStringUri: String) = externalNavigator.shareImage(imageStringUri)

}
