package com.example.musinsa.product.eventlistner

import com.example.musinsa.brand.domain.BrandDomainService
import com.example.musinsa.product.domain.ProductBrandStatisticDomainService
import com.example.musinsa.product.domain.ProductCategoryStatisticDomainService
import com.example.musinsa.product.domain.ProductDomainService
import com.example.musinsa.product.model.dto.ProductBrandStatisticDto
import com.example.musinsa.product.model.dto.ProductCategoryStatisticDto
import com.example.musinsa.product.model.dto.ProductDto
import com.example.musinsa.product.model.dto.ProductEventDto
import com.example.musinsa.product.model.enums.ProductEventType
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

    // TODO: 테스트 코드 추가 및 Opensearch 같은 검색엔진 도입
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
            productCategoryStatisticDomainService.save(
                ProductCategoryStatisticDto(
                    category = category,
                    minBrandId = productDto.brandId,
                    minProductId = productDto.id,
                    minPrice = productDto.price,
                    maxBrandId = productDto.brandId,
                    maxPrice = productDto.price,
                    maxProductId = productDto.id
                )
            )
            return
        }

        if (productDto.price < productCategoryStatistic.minPrice) {
            productCategoryStatisticDomainService.save(
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
            )
        }

        if (productDto.price > productCategoryStatistic.maxPrice) {
            productCategoryStatisticDomainService.save(
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
            )
        }
    }

    private fun deleteCategoryStatistic(productDto: ProductDto) {
        val category = productDto.category
        val productCategoryStatistic = productCategoryStatisticDomainService.getProductStatistic(category) ?: return
        val mostExpensiveProduct = productDomainService.getMostExpensiveProductByCategory(category)
        val cheapestProduct = productDomainService.getCheapestProductByCategory(category)
        if (mostExpensiveProduct == null || cheapestProduct == null) {
            productCategoryStatisticDomainService.deleteProductStatistic(productCategoryStatistic.id)
            return
        }
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