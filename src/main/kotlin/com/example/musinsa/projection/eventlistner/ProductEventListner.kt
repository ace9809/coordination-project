package com.example.musinsa.projection.eventlistner

import com.example.musinsa.domain.BrandDomainService
import com.example.musinsa.domain.ProductBrandStatisticDomainService
import com.example.musinsa.domain.ProductCategoryStatisticDomainService
import com.example.musinsa.domain.ProductDomainService
import com.example.musinsa.model.dto.ProductBrandStatisticDto
import com.example.musinsa.model.dto.ProductCategoryStatisticDto
import com.example.musinsa.model.dto.ProductDto
import com.example.musinsa.model.dto.ProductEventDto
import com.example.musinsa.model.enums.ProductEventType
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ProductEventListener(
    private val productCategoryStatisticDomainService: ProductCategoryStatisticDomainService,
    private val productDomainService: ProductDomainService,
    private val productBrandStatisticDomainService: ProductBrandStatisticDomainService,
    private val brandDomainService: BrandDomainService
) {
    @Async
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun handleProductUpdate(event: ProductEventDto) {
        val productDto = event.product
        brandDomainService.getBrand(productDto.brandId) ?: return

        when (event.type) {
            ProductEventType.CREATE, ProductEventType.UPDATE -> updateStatistic(productDto)
            ProductEventType.DELETE -> deleteStatistic(productDto)
        }
    }

    private fun updateStatistic(productDto: ProductDto) {
        updateCategoryStatistic(productDto)
        updateBrandStatistic(productDto)
    }

    private fun deleteStatistic(productDto: ProductDto) {
        deleteCategoryStatistic(productDto)
        updateBrandStatistic(productDto)
    }


    private fun updateCategoryStatistic(productDto: ProductDto) {
        val category = productDto.category
        val productCategoryStatistic = productCategoryStatisticDomainService.getProductStatistic(category)

        if (productCategoryStatistic == null) {
            ProductCategoryStatisticDto(
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

        if (productCategoryStatistic.minProductId == productDto.id || productCategoryStatistic.maxProductId == productDto.id) {
            if (productDto.price < productCategoryStatistic.minPrice) {
                ProductCategoryStatisticDto(
                    id = productCategoryStatistic.id,
                    category = productCategoryStatistic.category,
                    minBrandId = productDto.brandId,
                    minProductId = productDto.id,
                    minPrice = productDto.price,
                    maxBrandId = productCategoryStatistic.maxBrandId,
                    maxPrice = productCategoryStatistic.maxPrice,
                    maxProductId = productCategoryStatistic.maxProductId
                )
            }

            if (productDto.price > productCategoryStatistic.maxPrice) {
                ProductCategoryStatisticDto(
                    id = productCategoryStatistic.id,
                    category = productCategoryStatistic.category,
                    minBrandId = productCategoryStatistic.minBrandId,
                    minProductId = productCategoryStatistic.id,
                    minPrice = productCategoryStatistic.minPrice,
                    maxBrandId = productDto.brandId,
                    maxPrice = productDto.price,
                    maxProductId = productDto.id
                )
            }
        }
    }

    private fun deleteCategoryStatistic(productDto: ProductDto) {
        val category = productDto.category
        val productCategoryStatistic = productCategoryStatisticDomainService.getProductStatistic(category) ?: return
        val mostExpensiveProduct = productDomainService.getMostExpensiveProductByCategory(category)
        val cheapestProduct = productDomainService.getCheapestProductByCategory(category)
        // 카테고리에 상품이 존재 하지 않는 경우 통계 삭제
        if (mostExpensiveProduct == null || cheapestProduct == null) {
            productCategoryStatisticDomainService.deleteProductStatistic(productCategoryStatistic.id)
            return
        }
        // 삭제된 productId로 통계 데이터가 있는 경우 통계 업데이트
        if (productCategoryStatistic.minProductId == productDto.id || productCategoryStatistic.maxProductId == productDto.id) {
            productCategoryStatisticDomainService.save(
                ProductCategoryStatisticDto(
                    id = productCategoryStatistic.id,
                    category = productCategoryStatistic.category,
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

    private fun updateBrandStatistic(productDto: ProductDto) {
        val brandId = productDto.brandId
        val productBrandStatistic = productBrandStatisticDomainService.getByBrandId(brandId)
        val products = productDomainService.getAllByBrandId(brandId)
        val totalPrice = products.sumOf { it.price }

        if (productBrandStatistic == null) {
            productBrandStatisticDomainService.save(
                ProductBrandStatisticDto(
                    brandId = brandId,
                    totalPrice = totalPrice,
                )
            )
        } else {
            productBrandStatisticDomainService.save(
                ProductBrandStatisticDto(
                    id = brandId,
                    brandId = brandId,
                    totalPrice = totalPrice,
                )
            )
        }
    }
}