package com.example.musinsa.model.dto.response.brand

import io.swagger.v3.oas.annotations.media.Schema

data class DeleteBrandResponse(
    @Schema(description = "브랜드 ID", example = "1")
    val brandId: Long
)