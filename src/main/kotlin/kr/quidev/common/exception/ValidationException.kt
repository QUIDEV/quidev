package kr.quidev.common.exception

import kr.quidev.common.enums.ErrorCode
import kr.quidev.common.enums.ErrorCode.VALIDATION_FAILED

class ValidationException(message: String = VALIDATION_FAILED.message, cause: Throwable?) :
    QuidevException(message = message, cause = cause) {

    constructor(field:String, message:String) : this(message = VALIDATION_FAILED.message, cause = null) {
        addValidation(field, message)
    }

    override val errorCode: ErrorCode
        get() = VALIDATION_FAILED
}
