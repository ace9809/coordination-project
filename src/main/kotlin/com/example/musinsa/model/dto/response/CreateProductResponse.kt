package com.example.musinsa.model.dto.response

import com.example.musinsa.model.enums.CategoryType
import io.swagger.v3.oas.annotations.media.Schema

data class CreateProductResponse(
    @Schema(description = "브랜드", example = "A")
    val brand: String,
    @Schema(description = "카테고리", example = "상의")
    val category: CategoryType,
    @Schema(description = "가격", example = "1000")
    val price: Long,
)