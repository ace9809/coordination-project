package com.example.musinsa.exception

import org.springframework.http.HttpStatus

enum class ProductCategoryStatisticError(val code: String, val httpStatus: HttpStatus, var message: String) {
    NOT_FOUND_PRODUCT_CATEGORY_STATISTICS_EXCEPTION("120001", HttpStatus.NOT_FOUND, "상품 정보가 존재하지 않습니다.")
}