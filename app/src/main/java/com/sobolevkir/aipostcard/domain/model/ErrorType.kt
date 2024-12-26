package com.sobolevkir.aipostcard.domain.model

enum class ErrorType {
    CONNECTION_PROBLEM,
    NOT_FOUND,
    BAD_REQUEST,
    SERVER_ERROR,
    TIMEOUT,
    UNKNOWN_ERROR
}