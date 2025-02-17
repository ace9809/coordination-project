package com.example.musinsa.product.model.dto

import com.example.musinsa.product.model.enums.CategoryType
import com.example.musinsa.product.entity.ProductCategoryStatistic

data class ProductCategoryStatisticDto(
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
        fun of(productCategoryStatistic: ProductCategoryStatistic): ProductCategoryStatisticDto {
            return ProductCategoryStatisticDto(
                id = productCategoryStatistic.id,
                category = productCategoryStatistic.category,
                minBrandId = productCategoryStatistic.minBrandId,
                minPrice = productCategoryStatistic.minPrice,
                minProductId = productCategoryStatistic.minProductId,
                maxBrandId = productCategoryStatistic.maxBrandId,
                maxPrice = productCategoryStatistic.maxPrice,
                maxProductId = productCategoryStatistic.maxProductId
            )
        }
    }

    fun toProductStatistics(): ProductCategoryStatistic {
        return ProductCategoryStatistic(
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