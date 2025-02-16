package com.example.musinsa.domain

import com.example.musinsa.model.dto.ProductCategoryStatisticDto
import com.example.musinsa.model.enums.CategoryType
import com.example.musinsa.projection.repository.ProductCategoryStatisticRepository
import org.springframework.stereotype.Service

@Service
class ProductCategoryStatisticDomainService(
    private val productCategoryStatisticRepository: ProductCategoryStatisticRepository
) {
    fun existsByCategory(categoryType: CategoryType): Boolean {
        return productCategoryStatisticRepository.existsByCategory(categoryType)
    }

    fun getProductStatistic(categoryType: CategoryType): ProductCategoryStatisticDto? {
        val productStatistics = productCategoryStatisticRepository.findByCategory(categoryType) ?: return null
        return ProductCategoryStatisticDto.of(productStatistics)
    }

    fun getAllProductStatistics(): List<ProductCategoryStatisticDto> {
        val productStatistics = productCategoryStatisticRepository.findAll()
        return productStatistics.map { ProductCategoryStatisticDto.of(it) }
    }

    fun save(productCategoryStatisticDto: ProductCategoryStatisticDto): ProductCategoryStatisticDto {
        return ProductCategoryStatisticDto.of(productCategoryStatisticRepository.save(productCategoryStatisticDto.toProductStatistics()))
    }

    fun deleteProductStatistic(productStatisticsId: Long) {
        productCategoryStatisticRepository.deleteById(productStatisticsId)
    }
}