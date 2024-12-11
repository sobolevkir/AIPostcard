package com.sobolevkir.aipostcard.data.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val apiKey: String,
    private val apiSecret: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val request = originalRequest
            .newBuilder()
            .header(ApiConstants.HEADER_KEY, ApiConstants.PREFIX_KEY + apiKey)
            .header(ApiConstants.HEADER_SECRET, ApiConstants.PREFIX_SECRET + apiSecret)
            .build()

        return chain.proceed(request)
    }
}
