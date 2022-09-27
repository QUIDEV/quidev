package kr.quidev.common.enums

enum class ErrorCode(code: Int, message: String) {

    VALIDATION_FAILED(400, "validation failed"),
    NOT_FOUND(404, "not found"),
    NOT_AUTHORIZED(401, "not authorized");

    val code: Int = code
    val message: String = message

}
