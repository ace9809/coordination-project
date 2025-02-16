package com.example.musinsa.exception

import org.springframework.http.HttpStatus

enum class BrandError(val code: String, val httpStatus: HttpStatus, var message: String) {
    DUPLICATE_NAME_EXCEPTION("110001", HttpStatus.CONFLICT, "이미 존재하는 브랜드 이름입니다."),
    NOT_FOUND_BRAND_EXCEPTION("110002", HttpStatus.NOT_FOUND, "존재하지 않는 브랜드입니다.")
}