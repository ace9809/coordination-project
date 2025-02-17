package com.example.musinsa.product.repository

import com.example.musinsa.product.entity.ProductBrandStatistic
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductBrandStatisticRepository: JpaRepository<ProductBrandStatistic, Long> {
    fun findByBrandId(brandId: Long): ProductBrandStatistic?

    fun findTopByOrderByTotalPriceAsc(): ProductBrandStatistic?
}