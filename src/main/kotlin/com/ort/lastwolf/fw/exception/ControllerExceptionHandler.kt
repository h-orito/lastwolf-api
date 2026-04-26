package com.ort.lastwolf.fw.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ControllerExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders,
        statusCode: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        var b = body
        if (b !is LastwolfErrorResponse) {
            val httpStatus = HttpStatus.resolve(statusCode.value())
            b = LastwolfErrorResponse(statusCode.value(), httpStatus?.reasonPhrase ?: "Error")
        }
        return ResponseEntity(b, headers, statusCode)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val message = ex.bindingResult.allErrors.mapNotNull { it.defaultMessage }.joinToString("\n")
        val body = LastwolfErrorResponse(499, message)
        return handleExceptionInternal(ex, body, headers, HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(LastwolfBusinessException::class)
    fun handleBusinessException(ex: LastwolfBusinessException, request: WebRequest?): ResponseEntity<Any> {
        val headers = HttpHeaders()
        val body = LastwolfErrorResponse(499, ex.message)
        val status = HttpStatus.NOT_FOUND
        return handleExceptionInternal(ex, body, headers, status, request!!) ?: ResponseEntity(body, status)
    }

    @ExceptionHandler(LastwolfBadRequestException::class)
    fun handle400(ex: LastwolfBadRequestException, request: WebRequest?): ResponseEntity<Any> {
        val headers = HttpHeaders()
        val body = LastwolfErrorResponse(400, ex.message)
        val status = HttpStatus.BAD_REQUEST
        return handleExceptionInternal(ex, body, headers, status, request!!) ?: ResponseEntity(body, status)
    }

    @ExceptionHandler(Exception::class)
    fun handle500(ex: Exception, request: WebRequest?): ResponseEntity<Any> {
        val headers = HttpHeaders()
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        return handleExceptionInternal(ex, null, headers, status, request!!) ?: ResponseEntity(status)
    }
}
