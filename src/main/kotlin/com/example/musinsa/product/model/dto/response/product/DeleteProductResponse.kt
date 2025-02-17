package com.example.musinsa.product.model.dto.response.product

import io.swagger.v3.oas.annotations.media.Schema

data class DeleteProductResponse(
    @Schema(description = "상품 ID", example = "1")
    val productId: Long
)