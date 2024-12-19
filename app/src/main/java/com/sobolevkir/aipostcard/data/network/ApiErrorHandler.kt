package com.sobolevkir.aipostcard.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.sobolevkir.aipostcard.data.network.ResultCode.Companion.BAD_REQUEST_CODE
import com.sobolevkir.aipostcard.data.network.ResultCode.Companion.NOT_FOUND_CODE
import com.sobolevkir.aipostcard.data.network.ResultCode.Companion.SERVER_ERROR_CODE
import com.sobolevkir.aipostcard.data.network.ResultCode.Companion.UNAUTHORIZED_CODE
import com.sobolevkir.aipostcard.domain.model.ErrorType
import com.sobolevkir.aipostcard.util.Resource
import retrofit2.Response

class ApiErrorHandler(private val context: Context) {

    suspend fun <T> safeApiCall(api: suspend () -> Response<T>): Resource<T> {
        return if (!isConnected()) {
            Resource.Error(ErrorType.CONNECTION_PROBLEM)
        } else {
            try {
                val response = api()
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        Resource.Success(body)
                    } ?: Resource.Error(ErrorType.UNKNOWN_ERROR)
                } else {
                    Resource.Error(mapErrorCode(response.code()))
                }
            } catch (e: Exception) {
                Resource.Error(ErrorType.UNKNOWN_ERROR)
            }
        }
    }

    private fun mapErrorCode(code: Int): ErrorType {
        return when (code) {
            UNAUTHORIZED_CODE -> ErrorType.CONNECTION_PROBLEM
            NOT_FOUND_CODE -> ErrorType.NOT_FOUND
            BAD_REQUEST_CODE -> ErrorType.BAD_REQUEST
            SERVER_ERROR_CODE -> ErrorType.SERVER_ERROR
            else -> ErrorType.UNKNOWN_ERROR
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}