package com.example.musinsa.projection.repository

import com.example.musinsa.model.enums.CategoryType
import com.example.musinsa.projection.entity.ProductStatistics
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductStatisticsRepository: JpaRepository<ProductStatistics, Long> {
    fun existsByCategory(category: CategoryType): Boolean

    fun findByCategory(category: CategoryType): ProductStatistics?
}