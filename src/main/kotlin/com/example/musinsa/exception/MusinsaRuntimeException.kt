package com.example.musinsa.exception

import org.springframework.http.HttpStatus

open class MusinsaRuntimeException(
    val code: String?,
    val statusCode: HttpStatus,
    message: String?,
    val response: Any? = null
) : RuntimeException(message)

class ProductException(productError: ProductError) :
    MusinsaRuntimeException(productError.code, productError.httpStatus, productError.message)

class BrandException(brandError: BrandError) :
    MusinsaRuntimeException(brandError.code, brandError.httpStatus, brandError.message)

class ProductCategoryStatisticException(productCategoryStatisticError: ProductCategoryStatisticError) :
    MusinsaRuntimeException(
        productCategoryStatisticError.code,
        productCategoryStatisticError.httpStatus,
        productCategoryStatisticError.message
    )

class ProductBrandStatisticException(productBrandStatisticError: ProductBrandStatisticError) :
    MusinsaRuntimeException(
        productBrandStatisticError.code,
        productBrandStatisticError.httpStatus,
        productBrandStatisticError.message
    )
