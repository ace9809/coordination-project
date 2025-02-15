package com.example.musinsa.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class Response<T>(
    @Schema(description = "성공,실패 여부", example = "true")
    val success: Boolean,
    val response: T?
) {

    constructor(success: Boolean, response: T?, errorCode: String, message: String) : this(success, response) {
        this.message = message
        this.errorCode = errorCode
    }

    @Schema(description = "실패일 경우 메세지", example = "Identification not found")
    var message: String? = null

    @Schema(description = "실패일 경우 error code", example = "2001")
    var errorCode: String? = null
}
