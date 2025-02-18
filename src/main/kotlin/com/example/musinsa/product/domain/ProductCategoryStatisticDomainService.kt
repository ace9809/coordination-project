package com.example.musinsa.product.domain

import com.example.musinsa.CacheKeyConstant
import com.example.musinsa.product.model.dto.ProductCategoryStatisticDto
import com.example.musinsa.product.model.enums.CategoryType
import com.example.musinsa.product.repository.ProductCategoryStatisticRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class ProductCategoryStatisticDomainService(
    private val productCategoryStatisticRepository: ProductCategoryStatisticRepository
) {

    fun getProductStatistic(categoryType: CategoryType): ProductCategoryStatisticDto? {
        val productStatistics = productCategoryStatisticRepository.findByCategory(categoryType) ?: return null
        return ProductCategoryStatisticDto.of(productStatistics)
    }

    @Cacheable(value = [CacheKeyConstant.ALL_PRODUCT_CATEGORY_STATISTICS])
    fun getAllProductStatistics(): List<ProductCategoryStatisticDto> {
        val productStatistics = productCategoryStatisticRepository.findAll()
        return productStatistics.map { ProductCategoryStatisticDto.of(it) }
    }

    @CacheEvict(value = [CacheKeyConstant.ALL_PRODUCT_CATEGORY_STATISTICS])
    fun save(productCategoryStatisticDto: ProductCategoryStatisticDto): ProductCategoryStatisticDto {
        return ProductCategoryStatisticDto.of(productCategoryStatisticRepository.save(productCategoryStatisticDto.toProductStatistics()))
    }

    @CacheEvict(value = [CacheKeyConstant.ALL_PRODUCT_CATEGORY_STATISTICS])
    fun deleteProductStatistic(productStatisticsId: Long) {
        productCategoryStatisticRepository.deleteById(productStatisticsId)
    }
}