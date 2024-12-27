package com.sobolevkir.aipostcard.util

import com.sobolevkir.aipostcard.domain.model.ErrorType

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val error: ErrorType) : Resource<T>()

    inline fun <R> mapResource(transform: (T) -> R): Resource<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(error)
    }
}