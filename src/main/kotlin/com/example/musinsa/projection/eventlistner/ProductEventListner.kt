package com.example.musinsa.projection.eventlistner

import com.example.musinsa.domain.ProductStatisticsDomainService
import com.example.musinsa.model.dto.ProductDto
import com.example.musinsa.model.dto.ProductEventDto
import com.example.musinsa.model.dto.ProductStatisticsDto
import com.example.musinsa.model.enums.ProductEventType
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ProductEventListener(
    private val productStatisticsDomainService: ProductStatisticsDomainService
) {
    @TransactionalEventListener
    @Transactional
    fun handleProductUpdate(event: ProductEventDto) {
        val product = event.product
        val category = product.category
        val brand = product.brand

        when (event.type) {
            ProductEventType.CREATE, ProductEventType.UPDATE -> {
                updateCategoryStatistics(product)
            }

            ProductEventType.DELETE -> {

            }

        }
    }

    private fun updateCategoryStatistics(productDto: ProductDto) {
        val category = productDto.category
        val productStatistics = productStatisticsDomainService.getProductStatistics(category)

        if (productStatistics != null) {
            if (productDto.price < productStatistics.minPrice) {
                ProductStatisticsDto(
                    id = productStatistics.id,
                    category = productStatistics.category,
                    minBrand = productDto.brand,
                    minPrice = productDto.price,
                    maxBrand = productStatistics.maxBrand,
                    maxPrice = productStatistics.maxPrice,
                )
            }

            if (productDto.price < productStatistics.maxPrice) {
                ProductStatisticsDto(
                    id = productStatistics.id,
                    category = productStatistics.category,
                    minBrand = productStatistics.minBrand,
                    minPrice = productStatistics.minPrice,
                    maxBrand = productDto.brand,
                    maxPrice = productDto.price,
                )
            }
        } else {
            productStatisticsDomainService.save(ProductStatisticsDto(
                category = productDto.category,
                minBrand = productDto.brand,
                minPrice = productDto.price,
                maxBrand = productDto.brand,
                maxPrice = productDto.price,
            ))
        }
    }
}