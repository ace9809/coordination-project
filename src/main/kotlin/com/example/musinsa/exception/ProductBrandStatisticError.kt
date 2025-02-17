package com.example.musinsa.exception

import org.springframework.http.HttpStatus

enum class ProductBrandStatisticError(val code: String, val httpStatus: HttpStatus, var message: String) {
    NOT_FOUND_PRODUCT_BRAND_STATISTICS_EXCEPTION("130001", HttpStatus.NOT_FOUND, "최저 가격에 판매하는 브랜드가 존재하지 않습니다.")
}