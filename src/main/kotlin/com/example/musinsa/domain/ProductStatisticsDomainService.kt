package com.example.musinsa.domain

import com.example.musinsa.model.dto.ProductStatisticsDto
import com.example.musinsa.model.enums.CategoryType
import com.example.musinsa.projection.repository.ProductStatisticsRepository
import org.springframework.stereotype.Service

@Service
class ProductStatisticsDomainService(
    private val productStatisticsRepository: ProductStatisticsRepository
) {
    fun existsByCategory(categoryType: CategoryType): Boolean {
        return productStatisticsRepository.existsByCategory(categoryType)
    }

    fun getProductStatistics(categoryType: CategoryType): ProductStatisticsDto? {
        val productStatistics = productStatisticsRepository.findByCategory(categoryType) ?: return null
        return ProductStatisticsDto.of(productStatistics)
    }

    fun getAllProductStatistics(): List<ProductStatisticsDto> {
        val productStatistics = productStatisticsRepository.findAll()
        return productStatistics.map { ProductStatisticsDto.of(it) }
    }

    fun save(productStatisticsDto: ProductStatisticsDto): ProductStatisticsDto {
        return ProductStatisticsDto.of(productStatisticsRepository.save(productStatisticsDto.toProductStatistics()))
    }

    fun deleteProductStatistics(productStatisticsId: Long) {
        productStatisticsRepository.deleteById(productStatisticsId)
    }
}