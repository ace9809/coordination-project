package com.example.musinsa.model.dto

import com.example.musinsa.projection.entity.ProductBrandStatistic

data class ProductBrandStatisticDto(
    val id: Long = 0L,
    val brandId: Long,
    val totalPrice: Long,
) {
    companion object {
        fun of(productBrandStatistic: ProductBrandStatistic): ProductBrandStatisticDto {
            return ProductBrandStatisticDto(
                id = productBrandStatistic.id,
                brandId = productBrandStatistic.brandId,
                totalPrice = productBrandStatistic.totalPrice,
            )
        }
    }

    fun toProductStatistics(): ProductBrandStatistic {
        return ProductBrandStatistic(
            id = id,
            brandId = brandId,
            totalPrice = totalPrice
        )
    }
}