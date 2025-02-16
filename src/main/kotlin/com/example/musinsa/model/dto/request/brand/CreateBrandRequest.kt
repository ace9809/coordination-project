package com.example.musinsa.model.dto.request.brand

import com.example.musinsa.model.dto.BrandDto
import io.swagger.v3.oas.annotations.media.Schema

data class CreateBrandRequest(
    @Schema(description = "브랜드 이름", example = "A")
    val name: String
) {
    fun toBrandDto(): BrandDto {
        return BrandDto(
            name = name
        )
    }
}
