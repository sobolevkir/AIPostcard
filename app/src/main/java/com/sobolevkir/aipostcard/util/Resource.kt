package com.sobolevkir.aipostcard.util

import com.sobolevkir.aipostcard.domain.model.ErrorType

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val error: ErrorType) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()

    inline fun <R> mapResource(transform: (T) -> R): Resource<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(error)
        is Loading -> Loading
    }
}