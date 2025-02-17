package com.example.musinsa.product.repository

import com.example.musinsa.product.model.enums.CategoryType
import com.example.musinsa.product.entity.ProductCategoryStatistic
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductCategoryStatisticRepository: JpaRepository<ProductCategoryStatistic, Long> {
    fun findByCategory(category: CategoryType): ProductCategoryStatistic?
}