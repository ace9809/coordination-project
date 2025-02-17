package com.example.musinsa.model.dto

import com.example.musinsa.brand.entity.Brand

data class BrandDto(
    val id: Long = 0L,
    val name: String
) {
    companion object {
        fun of(brand: Brand): BrandDto {
            return BrandDto(
                id = brand.id,
                name = brand.name
            )
        }
    }

    fun toBrand(): Brand {
        return Brand(
            id = id,
            name = name
        )
    }
}