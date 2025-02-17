package com.example.musinsa.model.dto.request.product

import com.example.musinsa.model.dto.ProductDto
import com.example.musinsa.model.enums.CategoryType
import io.swagger.v3.oas.annotations.media.Schema

data class CreateProductRequest(
    @Schema(description = "브랜드 ID", example = "1")
    val brandId: Long,
    @Schema(description = "카테고리", example = "TOP")
    val category: String,
    @Schema(description = "가격", example = "1000")
    val price: Long,
) {
    fun toProductDto(categoryType: CategoryType): ProductDto {
        return ProductDto(
            brandId = brandId,
            category = categoryType,
            price = price
        )
    }
}
