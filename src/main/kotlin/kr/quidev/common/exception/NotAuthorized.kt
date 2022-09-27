package kr.quidev.common.exception

class NotAuthorized(message: String, cause: Throwable?) : RuntimeException(message, cause) {
    constructor() : this(message = "not authorized", cause = null)
}
