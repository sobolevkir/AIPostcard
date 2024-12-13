package com.sobolevkir.aipostcard.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(data: T?, message: String?): Resource<T>(data, message)
}