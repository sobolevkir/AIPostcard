package com.sobolevkir.aipostcard.util

import com.sobolevkir.aipostcard.domain.model.ErrorType

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val error: ErrorType) : Resource<T>()
}