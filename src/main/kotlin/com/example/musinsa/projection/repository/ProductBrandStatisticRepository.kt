package com.example.musinsa.projection.repository

import com.example.musinsa.model.enums.CategoryType
import com.example.musinsa.projection.entity.Product
import com.example.musinsa.projection.entity.ProductBrandStatistic
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductBrandStatisticRepository: JpaRepository<ProductBrandStatistic, Long> {
    fun findByBrandId(brandId: Long): ProductBrandStatistic?

    fun findTopByOrderByTotalPriceAsc(): ProductBrandStatistic?
}