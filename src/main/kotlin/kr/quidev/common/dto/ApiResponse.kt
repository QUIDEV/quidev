package kr.quidev.common.dto

data class ApiResponse(
    val body: Any? = null,
    var error: Error? = null
) {

    companion object {
        fun ok(body: Any): ApiResponse {
            return ApiResponse(body = body)
        }

        fun fail(code: Int?, message: String?, validation: Collection<Error.ValidationResult>? = null): ApiResponse {
            val error = Error.of(code = code, message = message, validation = validation)
            return ApiResponse(error = error)
        }
    }

}
