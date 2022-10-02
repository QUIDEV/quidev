package kr.quidev.common.exception

import kr.quidev.common.enums.ErrorCode
import kr.quidev.common.enums.ErrorCode.NOT_AUTHORIZED

class NotAuthorized(message: String = NOT_AUTHORIZED.message, cause: Throwable? = null) :
    QuidevException(message = message, cause) {

    override val errorCode: ErrorCode
        get() = NOT_AUTHORIZED

}
