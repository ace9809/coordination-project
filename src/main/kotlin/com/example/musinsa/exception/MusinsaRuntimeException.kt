package com.example.musinsa.exception

import org.springframework.http.HttpStatus

open class MusinsaRuntimeException(val code:String?, val statusCode: HttpStatus, message: String?, val response: Any? = null): RuntimeException(message)

class ProductException(productError: ProductError): MusinsaRuntimeException(productError.code, productError.httpStatus, productError.message)
