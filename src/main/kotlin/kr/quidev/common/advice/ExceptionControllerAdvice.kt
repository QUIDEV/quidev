package kr.quidev.common.advice

import kr.quidev.common.dto.ApiResponse
import kr.quidev.common.dto.Error
import kr.quidev.common.enums.ErrorCode.*
import kr.quidev.common.exception.NotAuthorized
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
        val validation = e.fieldErrors.map { e ->
            Error.ValidationResult(field = e.field, message = e.defaultMessage)
        }
        return ApiResponse.fail(VALIDATION_FAILED, validation = validation)
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ExceptionHandler
    fun noSuchElementExceptionHandler(e: NoSuchElementException): ApiResponse {
        return ApiResponse.fail(NOT_FOUND)
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ExceptionHandler
    fun notAuthorizedHandler(e: NotAuthorized): ApiResponse {
        return ApiResponse.fail(NOT_AUTHORIZED)
    }

}
