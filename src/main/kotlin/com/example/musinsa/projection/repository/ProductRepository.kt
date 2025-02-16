package com.example.musinsa.projection.repository

import com.example.musinsa.model.enums.CategoryType
import com.example.musinsa.projection.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: JpaRepository<Product, Long> {
    fun existsByCategoryAndBrandId(category: CategoryType, brandId: Long): Boolean
}