package com.sobolevkir.aipostcard.data.network

import com.sobolevkir.aipostcard.util.Resource
import retrofit2.Response

class ApiErrorHandler {

    suspend fun <T> safeApiCall(api: suspend () -> Response<T>): Resource<T> {
        try {
            val response = api()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return Resource.Success(body)
                } ?: return errorMessage("Body is empty")
            } else {
                return errorMessage("${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            return errorMessage(e.message.toString())
        }
    }

    private fun <T> errorMessage(e: String): Resource.Error<T> =
        Resource.Error(null, "Api call failed: $e")
}