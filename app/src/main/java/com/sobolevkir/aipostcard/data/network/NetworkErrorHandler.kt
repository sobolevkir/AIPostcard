package com.sobolevkir.aipostcard.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.sobolevkir.aipostcard.domain.model.GenerationErrorType
import com.sobolevkir.aipostcard.util.Resource
import retrofit2.Response
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkErrorHandler(private val context: Context) {

    suspend fun <T> safeApiCall(api: suspend () -> Response<T>): Resource<T> {
        if (!isConnected()) return Resource.Error(GenerationErrorType.CONNECTION_PROBLEM)
        return try {
            val response = api()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    Resource.Success(body)
                } ?: Resource.Error(GenerationErrorType.UNKNOWN_ERROR)
            } else {
                val errorCode = response.code()
                Log.e("NETWORK_ERROR_HANDLER", "Api error code: $errorCode")
                Resource.Error(GenerationErrorType.UNKNOWN_ERROR)
            }
        } catch (e: Exception) {
            Log.e("NETWORK_ERROR_HANDLER", "Error: $e :: In: $api")
            when (e) {
                is SocketException,
                is UnknownHostException,
                is SocketTimeoutException -> Resource.Error(GenerationErrorType.CONNECTION_PROBLEM)

                else -> Resource.Error(GenerationErrorType.UNKNOWN_ERROR)
            }
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