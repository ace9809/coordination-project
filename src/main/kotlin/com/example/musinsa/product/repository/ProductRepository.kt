package com.example.musinsa.product.repository

import com.example.musinsa.product.model.enums.CategoryType
import com.example.musinsa.product.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: JpaRepository<Product, Long> {
    fun existsByCategoryAndBrandId(category: CategoryType, brandId: Long): Boolean

    fun findTopByCategoryOrderByPriceDesc(category: CategoryType): Product?

    fun findTopByCategoryOrderByPriceAsc(category: CategoryType): Product?

    fun findAllByBrandId(brandId: Long): List<Product>
}