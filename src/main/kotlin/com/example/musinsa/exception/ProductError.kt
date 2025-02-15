package com.example.musinsa.exception

import org.springframework.http.HttpStatus

enum class ProductError(val code: String, val httpStatus: HttpStatus, var message: String) {
    INVALID_CATEGORY_EXCEPTION("100001", HttpStatus.BAD_REQUEST, "잘못된 카테고리입니다."),
    INVALID_PRICE_EXCEPTION("100002", HttpStatus.BAD_REQUEST, "가격은 0이하로 설정할 수 없습니다."),
    DUPLICATE_PRODUCT_EXCEPTION("100003", HttpStatus.CONFLICT, "해당 브랜드에 이미 존재하는 상품입니다."),
    NOT_FOUND_PRODUCT_EXCEPTION("100004", HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다.")
}