package com.example.musinsa.model.dto.response.product

import io.swagger.v3.oas.annotations.media.Schema

data class GetMinMaxPriceResponses(
    @Schema(description = "카테고리 타입", example = "TOP")
    val category: String,
    @Schema(description = "카테고리 이름", example = "상의")
    val categoryName: String,
    @Schema(description = "최저가 상품")
    val minProduct: GetMinMaxPriceResponse,
    @Schema(description = "최고가 상품")
    val maxProduct: GetMinMaxPriceResponse,
)