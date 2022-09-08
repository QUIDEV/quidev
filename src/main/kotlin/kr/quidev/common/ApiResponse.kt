package kr.quidev.common

data class ApiResponse(
    val body: Any?,
    val status: Int?,
    val errors: Map<String, String?>
) {

    companion object {
        fun ok(body: Any): ApiResponse {
            return ApiResponse(errors = emptyMap(), body = body, status = 200)
        }

        fun error(errors: Map<String, String?>, status: Int?): ApiResponse {
            return ApiResponse(errors = errors, body = null, status = status)
        }

        fun error(errors: Map<String, String?>): ApiResponse {
            return error(errors = errors, status = null)
        }
    }
}
