package com.sobolevkir.aipostcard.data.network

class ResultCode {
    companion object {
        const val UNAUTHORIZED_CODE = 401
        const val NOT_FOUND_CODE = 404
        const val BAD_REQUEST_CODE = 400
        const val SERVER_ERROR_CODE = 500
        const val TIMEOUT_CODE = 408
    }
}