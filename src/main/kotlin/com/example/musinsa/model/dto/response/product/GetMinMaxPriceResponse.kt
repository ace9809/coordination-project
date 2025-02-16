package com.example.musinsa.model.dto.response.product

import io.swagger.v3.oas.annotations.media.Schema

data class GetMinMaxPriceResponse(
    @Schema(description = "브랜드 이름", example = "A")
    val brandName: String,
    @Schema(description = "가격", example = "1000")
    val price: Long,
)