package com.example.musinsa.brand.repository

import com.example.musinsa.brand.entity.Brand
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BrandRepository: JpaRepository<Brand, Long> {
    fun existsByName(name: String): Boolean

    fun findByIdIn(brandIds: List<Long>): List<Brand>
}