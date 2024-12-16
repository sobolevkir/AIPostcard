package com.sobolevkir.aipostcard.data.network

// Api config constants
object ApiConstants {
    const val HEADER_KEY = "X-Key"
    const val HEADER_SECRET = "X-Secret"
    const val PREFIX_KEY = "Key "
    const val PREFIX_SECRET = "Secret "
    const val BASE_URL = "https://api-key.fusionbrain.ai/"
    const val GET_STYLES_URL = "https://cdn.fusionbrain.ai/static/styles/key"
    const val GET_MODELS_ENDPOINT = "key/api/v1/models"
    const val IMAGE_GENERATION_REQUEST_ENDPOINT = "/key/api/v1/text2image/run"
    const val CHECK_GENERATION_RESULT_ENDPOINT = "/key/api/v1/text2image/status/{id}"
    const val REQUEST_PARAMETER_TYPE_GENERATE = "GENERATE"
    const val IMAGE_SIZE = 1024
    const val STATUS_PROCESSING = "FAIL"
    const val STATUS_DONE = "DONE"
    const val STATUS_INITIAL = "INITIAL"
}