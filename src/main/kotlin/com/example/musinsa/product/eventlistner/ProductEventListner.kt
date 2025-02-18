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
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ProductEventListener(
    private val productCategoryStatisticDomainService: ProductCategoryStatisticDomainService,
    private val productDomainService: ProductDomainService,
    private val productBrandStatisticDomainService: ProductBrandStatisticDomainService,
    private val brandDomainService: BrandDomainService
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun handleProductUpdate(event: ProductEventDto) {
        val productDto = event.product
        val prevProductDto = event.prevProduct
        brandDomainService.getBrand(productDto.brandId) ?: return

        when (event.type) {
            ProductEventType.CREATE -> createStatistic(productDto)
            ProductEventType.UPDATE -> updateStatistic(productDto, prevProductDto)
            ProductEventType.DELETE -> deleteStatistic(productDto)
        }
    }

    private fun createStatistic(productDto: ProductDto) {
        updateCategoryStatistic(productDto)
        updateBrandStatistic(productDto)
    }

    private fun updateStatistic(productDto: ProductDto, prevProductDto: ProductDto?) {
        if (prevProductDto == null) {
            updateCategoryStatistic(productDto)
            updateBrandStatistic(productDto)
            return
        }

        // 카테고리 변경 감지
        if (productDto.category != prevProductDto.category) {
            deleteCategoryStatistic(prevProductDto)
            updateCategoryStatistic(productDto)
        } else {
            updateCategoryStatistic(productDto)
        }

        // 브랜드 변경 감지
        if (productDto.brandId != prevProductDto.brandId) {
            updateBrandStatistic(prevProductDto)
            updateBrandStatistic(productDto)
        } else {
            updateBrandStatistic(productDto)
        }
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
                    minProductId = cheapestProduct.id,
                    minPrice = cheapestProduct.price,
                    maxBrandId = mostExpensiveProduct.brandId,
                    maxProductId = mostExpensiveProduct.id,
                    maxPrice = mostExpensiveProduct.price
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
                    id = productBrandStatistic.id,
                    brandId = productBrandStatistic.brandId,
                    totalPrice = totalPrice,
                )
            )
        }
    }
}
