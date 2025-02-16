package com.example.musinsa.model.dto

import com.example.musinsa.model.enums.CategoryType
import com.example.musinsa.projection.entity.ProductStatistics

data class ProductStatisticsDto(
    val id: Long = 0L,
    val category: CategoryType,
    val minBrand: String,
    val minPrice: Long,
    val maxBrand: String,
    val maxPrice: Long
) {
    companion object {
        fun of(productStatistics: ProductStatistics): ProductStatisticsDto {
            return ProductStatisticsDto(
                id = productStatistics.id,
                category = productStatistics.category,
                minBrand = productStatistics.minBrand,
                minPrice = productStatistics.minPrice,
                maxBrand = productStatistics.maxBrand,
                maxPrice = productStatistics.maxPrice
            )
        }
    }

    fun toProductStatistics(): ProductStatistics {
        return ProductStatistics(
            id = id,
            category = category,
            minBrand = minBrand,
            minPrice = minPrice,
            maxBrand = maxBrand,
            maxPrice = maxPrice
        )
    }
}