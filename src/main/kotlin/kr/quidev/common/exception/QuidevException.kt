package kr.quidev.common.exception

import kr.quidev.common.dto.Error
import kr.quidev.common.enums.ErrorCode

abstract class QuidevException(message: String, cause: Throwable?) : RuntimeException(message, cause) {

    val validation: MutableList<Error.ValidationResult> = mutableListOf()
    abstract val errorCode: ErrorCode

    fun addValidation(field: String, message: String) {
        validation.add(Error.ValidationResult(field, message))
    }

}
