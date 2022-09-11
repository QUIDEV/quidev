package kr.quidev.common

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ExceptionControllerAdvice {

    private val log = LoggerFactory.getLogger(javaClass)

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ExceptionHandler
    fun methodArgumentNotValidHandler(e: MethodArgumentNotValidException): ApiResponse {
        val errors = e.fieldErrors.associate {
            it.field to it.defaultMessage
        }
        return ApiResponse.error(status = 400, errors = errors, message = "invalid request")
    }
}