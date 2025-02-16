package com.example.musinsa.model.dto.response.brand

import io.swagger.v3.oas.annotations.media.Schema

data class UpdateBrandResponse(
    @Schema(description = "브랜드 ID", example = "1")
    val id: Long,
    @Schema(description = "이름", example = "A")
    val name: String,
)