package com.example.musinsa.projection.eventlistner

import com.example.musinsa.domain.BrandDomainService
import com.example.musinsa.domain.ProductDomainService
import com.example.musinsa.domain.ProductStatisticsDomainService
import com.example.musinsa.model.dto.ProductDto
import com.example.musinsa.model.dto.ProductEventDto
import com.example.musinsa.model.dto.ProductStatisticsDto
import com.example.musinsa.model.enums.ProductEventType
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ProductEventListener(
    private val productStatisticsDomainService: ProductStatisticsDomainService,
    private val productDomainService: ProductDomainService,
    private val brandDomainService: BrandDomainService
) {
    @TransactionalEventListener
    @Transactional
    fun handleProductUpdate(event: ProductEventDto) {
        val productDto = event.product
        brandDomainService.getBrand(productDto.brandId) ?: return

        when (event.type) {
            ProductEventType.CREATE, ProductEventType.UPDATE -> updateCategoryStatistics(productDto)
            ProductEventType.DELETE -> deleteCategoryStatistics(productDto)
        }
    }

    private fun updateCategoryStatistics(productDto: ProductDto) {
        val category = productDto.category
        val productStatistics = productStatisticsDomainService.getProductStatistics(category)

        if (productStatistics == null) {
            ProductStatisticsDto(
                category = category,
                minBrandId = productDto.brandId,
                minProductId = productDto.id,
                minPrice = productDto.price,
                maxBrandId = productDto.brandId,
                maxPrice = productDto.price,
                maxProductId = productDto.id
            )
            return
        }

        if (productStatistics.minProductId == productDto.id || productStatistics.maxProductId == productDto.id) {
            if (productDto.price < productStatistics.minPrice) {
                ProductStatisticsDto(
                    id = productStatistics.id,
                    category = productStatistics.category,
                    minBrandId = productDto.brandId,
                    minProductId = productDto.id,
                    minPrice = productDto.price,
                    maxBrandId = productStatistics.maxBrandId,
                    maxPrice = productStatistics.maxPrice,
                    maxProductId = productStatistics.maxProductId
                )
            }

            if (productDto.price > productStatistics.maxPrice) {
                ProductStatisticsDto(
                    id = productStatistics.id,
                    category = productStatistics.category,
                    minBrandId = productStatistics.minBrandId,
                    minProductId = productStatistics.id,
                    minPrice = productStatistics.minPrice,
                    maxBrandId = productDto.brandId,
                    maxPrice = productDto.price,
                    maxProductId = productDto.id
                )
            }
        }
    }

    private fun deleteCategoryStatistics(productDto: ProductDto) {
        val category = productDto.category
        val productStatistics = productStatisticsDomainService.getProductStatistics(category) ?: return
        val mostExpensiveProduct = productDomainService.getMostExpensiveProductByCategory(category)
        val cheapestProduct = productDomainService.getCheapestProductByCategory(category)
        // 카테고리에 상품이 존재 하지 않는 경우 통계 삭제
        if (mostExpensiveProduct == null || cheapestProduct == null) {
            productStatisticsDomainService.deleteProductStatistics(productStatistics.id)
            return
        }
        // 삭제된 productId로 통계 데이터가 있는 경우 통계 업데이트
        if (productStatistics.minProductId == productDto.id || productStatistics.maxProductId == productDto.id) {
            productStatisticsDomainService.save(
                ProductStatisticsDto(
                    id = productStatistics.id,
                    category = productStatistics.category,
                    minBrandId = cheapestProduct.brandId,
                    minPrice = cheapestProduct.price,
                    minProductId = cheapestProduct.id,
                    maxBrandId = mostExpensiveProduct.brandId,
                    maxPrice = mostExpensiveProduct.price,
                    maxProductId = mostExpensiveProduct.id
                )
            )
        }
    }
}