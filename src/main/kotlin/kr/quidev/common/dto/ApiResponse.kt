package kr.quidev.common.dto

import kr.quidev.common.enums.ErrorCode
import org.springframework.data.domain.Page

data class ApiResponse(
    val body: Any? = null,
    var error: Error? = null
) {

    companion object {

        fun ok(page: Page<out Any>): ApiResponse {
            return ApiResponse(PageableContent.of(page))
        }

        fun ok(body: Any): ApiResponse {
            return ApiResponse(body = body)
        }

        fun fail(code: Int?, message: String?, validation: Collection<Error.ValidationResult>? = null): ApiResponse {
            val error = Error.of(code = code, message = message, validation = validation)
            return ApiResponse(error = error)
        }

        fun fail(errorCode: ErrorCode, validation: List<Error.ValidationResult>): ApiResponse {
            return fail(code = errorCode.code, validation = validation, message = errorCode.message)
        }

        fun fail(notFound: ErrorCode): ApiResponse {
            return fail(code = notFound.code, message = notFound.message)
        }
    }

}
