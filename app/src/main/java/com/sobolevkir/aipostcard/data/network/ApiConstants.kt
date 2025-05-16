package com.sobolevkir.aipostcard.data.network

// Api config constants
object ApiConstants {
    const val HEADER_KEY = "X-Key"
    const val HEADER_SECRET = "X-Secret"
    const val PREFIX_KEY = "Key "
    const val PREFIX_SECRET = "Secret "
    const val BASE_URL = "https://api-key.fusionbrain.ai/"
    const val GET_STYLES_URL = "https://cdn.fusionbrain.ai/static/styles/key"
    const val GET_MODELS_ENDPOINT = "key/api/v1/pipelines"
    const val GENERATION_REQUEST_ENDPOINT = "/key/api/v1/pipeline/run"
    const val CHECK_GENERATION_RESULT_ENDPOINT = "/key/api/v1/pipeline/status/{id}"
    const val REQUEST_PARAMETER_TYPE_GENERATE = "GENERATE"
    const val REQUEST_PARAMETER_IMAGE_SIZE = 1024
    const val REQUEST_PARAMETER_IMAGES_NUMBER = 1
    const val STATUS_PROCESSING = "FAIL"
    const val STATUS_DONE = "DONE"
    const val STATUS_INITIAL = "INITIAL"
}