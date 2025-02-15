package com.example.musinsa.model.dto.response

import io.swagger.v3.oas.annotations.media.Schema

data class DeleteProductResponse(
    @Schema(description = "브랜드", example = "A")
    val productId: Long
)