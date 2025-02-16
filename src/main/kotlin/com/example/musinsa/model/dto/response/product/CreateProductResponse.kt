package com.example.musinsa.model.dto.response.product

import com.example.musinsa.model.enums.CategoryType
import io.swagger.v3.oas.annotations.media.Schema

data class CreateProductResponse(
    @Schema(description = "상품 ID", example = "1")
    val id: Long,
    @Schema(description = "브랜드 ID", example = "1")
    val brandId: Long,
    @Schema(description = "카테고리", example = "상의")
    val category: CategoryType,
    @Schema(description = "가격", example = "1000")
    val price: Long,
)