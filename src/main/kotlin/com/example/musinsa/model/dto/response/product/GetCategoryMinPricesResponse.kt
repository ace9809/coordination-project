package com.example.musinsa.model.dto.response.product

import io.swagger.v3.oas.annotations.media.Schema

data class GetCategoryMinPricesResponse(
    @Schema(description = "브랜드 ID", example = "1")
    val brandId: Long,
    @Schema(description = "브랜드 이름", example = "A")
    val brandName: String,
    @Schema(description = "카테고리 타입", example = "TOP")
    val category: String,
    @Schema(description = "카테고리 이름", example = "상의")
    val categoryName: String,
    @Schema(description = "가격", example = "1000")
    val price: Long,
)