package com.example.musinsa.model.dto

import com.example.musinsa.model.enums.CategoryType
import com.example.musinsa.projection.entity.ProductStatistics

data class ProductStatisticsDto(
    val id: Long = 0L,
    val category: CategoryType,
    val minBrandId: Long,
    val minProductId: Long,
    val minPrice: Long,
    val maxBrandId: Long,
    val maxProductId: Long,
    val maxPrice: Long
) {
    companion object {
        fun of(productStatistics: ProductStatistics): ProductStatisticsDto {
            return ProductStatisticsDto(
                id = productStatistics.id,
                category = productStatistics.category,
                minBrandId = productStatistics.minBrandId,
                minPrice = productStatistics.minPrice,
                minProductId = productStatistics.minProductId,
                maxBrandId = productStatistics.maxBrandId,
                maxPrice = productStatistics.maxPrice,
                maxProductId = productStatistics.maxProductId
            )
        }
    }

    fun toProductStatistics(): ProductStatistics {
        return ProductStatistics(
            id = id,
            category = category,
            minBrandId = minBrandId,
            minPrice = minPrice,
            minProductId = minProductId,
            maxBrandId = maxBrandId,
            maxPrice = maxPrice,
            maxProductId = maxProductId,
        )
    }
}