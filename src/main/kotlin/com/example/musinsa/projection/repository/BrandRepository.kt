package com.example.musinsa.projection.repository

import com.example.musinsa.projection.entity.Brand
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BrandRepository: JpaRepository<Brand, Long> {
    fun existsByName(name: String): Boolean

    fun findByIdIn(brandIds: List<Long>): List<Brand>
}