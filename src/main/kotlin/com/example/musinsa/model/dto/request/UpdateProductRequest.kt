package com.example.musinsa.model.dto.request

import com.example.musinsa.model.dto.ProductDto
import com.example.musinsa.model.enums.CategoryType
import io.swagger.v3.oas.annotations.media.Schema

data class UpdateProductRequest(
    @Schema(description = "브랜드", example = "A")
    val brand: String,
    @Schema(description = "카테고리", example = "상의")
    val category: CategoryType,
    @Schema(description = "가격", example = "1000")
    val price: Long,
) {
    fun toProductDto(): ProductDto {
        return ProductDto(
            brand = brand,
            category = category,
            price = price
        )
    }
}
