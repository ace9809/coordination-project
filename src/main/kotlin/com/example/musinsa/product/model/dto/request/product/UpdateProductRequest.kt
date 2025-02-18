package com.example.musinsa.product.model.dto.request.product

import com.example.musinsa.product.model.dto.ProductDto
import com.example.musinsa.product.model.enums.CategoryType
import io.swagger.v3.oas.annotations.media.Schema

data class UpdateProductRequest(
    @Schema(description = "가격", example = "1000")
    val price: Long,
) {
    fun toProductDto(id: Long, brandId: Long, categoryType: CategoryType): ProductDto {
        return ProductDto(
            id = id,
            brandId = brandId,
            category = categoryType,
            price = price
        )
    }
}
