package com.example.musinsa.projection.repository

import com.example.musinsa.projection.entity.Product
import jdk.jfr.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: JpaRepository<Product, Long> {
    fun existsByCategoryAndBrand(category: String, brand: String): Boolean
}