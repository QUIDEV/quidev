package kr.quidev.common

data class ApiResponse(
    val body: Any?,
    val status: Int?,
) {
    var error: Error? = null

    class Error(
        val message: String?,
    ) {
        val validation: MutableList<ValidationResult> = mutableListOf()
    }

    companion object {
        fun ok(body: Any): ApiResponse {
            return ApiResponse(body = body, status = 200)
        }

        fun error(status: Int?, message: String?, validation: Collection<ValidationResult>): ApiResponse {
            val apiResponse = ApiResponse(body = null, status = status)
            val error = Error(message)
            apiResponse.error = error
            error.validation.addAll(validation)
            return apiResponse
        }
    }

    data class ValidationResult(
        val field: String,
        val message: String
    )

}
