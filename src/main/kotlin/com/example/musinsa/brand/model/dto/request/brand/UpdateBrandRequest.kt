package com.example.musinsa.brand.model.dto.request.brand

import com.example.musinsa.model.dto.BrandDto
import io.swagger.v3.oas.annotations.media.Schema

data class UpdateBrandRequest(
    @Schema(description = "브랜드 이름", example = "A")
    val name: String
) {
    fun toBrandDto(id: Long): BrandDto {
        return BrandDto(
            id = id,
            name = name
        )
    }
}
