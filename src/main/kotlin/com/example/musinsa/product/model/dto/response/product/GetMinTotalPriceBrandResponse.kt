package com.example.musinsa.product.model.dto.response.product

import io.swagger.v3.oas.annotations.media.Schema

data class GetMinTotalPriceBrandResponse(
    @Schema(description = "브랜드 이름", example = "A")
    val brandName: String,
    @Schema(description = "카테고리 목록")
    val categories: List<GetCategoriesResponse>,
    @Schema(description = "총액", example = "1000")
    val totalPrice: Long
)

data class GetCategoriesResponse(
    @Schema(description = "카테고리 타입", example = "TOP")
    val category: String,
    @Schema(description = "카테고리 이름", example = "상의")
    val categoryName: String,
    @Schema(description = "가격", example = "1000")
    val price: Long
)