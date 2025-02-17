package com.example.musinsa.domain

import com.example.musinsa.model.dto.ProductBrandStatisticDto
import com.example.musinsa.model.dto.ProductCategoryStatisticDto
import com.example.musinsa.projection.entity.ProductBrandStatistic
import com.example.musinsa.projection.repository.ProductBrandStatisticRepository
import org.springframework.stereotype.Service

@Service
class ProductBrandStatisticDomainService(
    private val productBrandStatisticRepository: ProductBrandStatisticRepository
) {
    fun getByBrandId(brandId: Long): ProductBrandStatisticDto? {
        val productBrandStatistics = productBrandStatisticRepository.findByBrandId(brandId) ?: return null
        return ProductBrandStatisticDto.of(productBrandStatistics)
    }

    fun getTopByOrderByTotalPriceAsc(): ProductBrandStatisticDto? {
        val productBrandStatistics = productBrandStatisticRepository.findTopByOrderByTotalPriceAsc() ?: return null
        return ProductBrandStatisticDto.of(productBrandStatistics)
    }

    fun save(productBrandStatisticDto: ProductBrandStatisticDto): ProductBrandStatisticDto {
        return ProductBrandStatisticDto.of(productBrandStatisticRepository.save(productBrandStatisticDto.toProductStatistics()))
    }
}