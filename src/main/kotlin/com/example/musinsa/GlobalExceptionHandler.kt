package com.example.musinsa

import com.example.musinsa.exception.MusinsaRuntimeException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler(

) {
    @ExceptionHandler(value = [MusinsaRuntimeException::class])
    fun handleKcdRuntimeException(
        request: HttpServletRequest,
        musinsaRuntimeException: MusinsaRuntimeException
    ): ResponseEntity<Response<Any>> {
        val response = Response(false, musinsaRuntimeException.response)
        response.errorCode = musinsaRuntimeException.code
        response.message = musinsaRuntimeException.message

        return ResponseEntity(response, musinsaRuntimeException.statusCode)
    }

    @ExceptionHandler(value = [Exception::class])
    fun handleRuntimeException(
        request: HttpServletRequest,
        exception: Exception
    ): ResponseEntity<Response<Nothing>> {
        val response = Response(false, null)
        response.errorCode = "-1"
        response.message = exception.message
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
