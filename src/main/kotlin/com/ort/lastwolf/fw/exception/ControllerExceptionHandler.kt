package com.ort.lastwolf.fw.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
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
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        var body = body
        if (body !is LastwolfErrorResponse) {
            body = LastwolfErrorResponse(status.value(), status.reasonPhrase)
        }
        return ResponseEntity(body, headers, status)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val message = ex.bindingResult.allErrors.mapNotNull { it.defaultMessage }.joinToString("\n")
        val headers = HttpHeaders()
        val body = LastwolfErrorResponse(499, message)
        val status = HttpStatus.NOT_FOUND
        return handleExceptionInternal(ex, body, headers, status, request)
    }

    @ExceptionHandler(LastwolfBusinessException::class)
    fun handleBusinessException(ex: LastwolfBusinessException, request: WebRequest?): ResponseEntity<Any> {
        val headers = HttpHeaders()
        val body = LastwolfErrorResponse(499, ex.message)
        val status = HttpStatus.NOT_FOUND // dummy
        return handleExceptionInternal(ex, body, headers, status, request!!)
    }

    @ExceptionHandler(LastwolfBadRequestException::class)
    fun handle400(ex: LastwolfBadRequestException, request: WebRequest?): ResponseEntity<Any> {
        val headers = HttpHeaders()
        val body = LastwolfErrorResponse(400, ex.message)
        val status = HttpStatus.BAD_REQUEST
        return handleExceptionInternal(ex, body, headers, status, request!!)
    }

    @ExceptionHandler(Exception::class)
    fun handle500(ex: Exception, request: WebRequest?): ResponseEntity<Any> {
        val headers = HttpHeaders()
        val body = null
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        return handleExceptionInternal(ex, body, headers, status, request!!)
    }
}