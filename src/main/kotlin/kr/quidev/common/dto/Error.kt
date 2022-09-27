package kr.quidev.common.dto

class Error(
    val code: Int? = null,
    val message: String? = null,
    val validation: Collection<ValidationResult>? = null
) {

    companion object {

        fun of(
            code: Int?,
            message: String?,
            validation: Collection<ValidationResult>?
        ): Error {
            return Error(code = code, message = message, validation = validation)
        }

        fun of(e: Throwable): Error {
            return Error(message = e.message)
        }
    }

    data class ValidationResult(
        val field: String,
        val message: String?
    )

}
