package kr.quidev.common

data class ApiResponse(
    val body: Any?,
    val status: Int?,
) {
    var error: Error? = null

    class Error(
        val message: String?,
        val validation: Map<String, String?>?
    )

    companion object {
        fun ok(body: Any): ApiResponse {
            return ApiResponse(body = body, status = 200)
        }

        fun error(status: Int?, message: String?, errors: Map<String, String?>): ApiResponse {
            val apiResponse = ApiResponse(body = null, status = status)
            apiResponse.error = Error(message, validation = errors)
            return apiResponse
        }
    }
}
