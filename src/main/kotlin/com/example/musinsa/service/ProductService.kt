package com.example.musinsa.service

import com.example.musinsa.domain.BrandDomainService
import com.example.musinsa.domain.ProductDomainService
import com.example.musinsa.domain.ProductCategoryStatisticDomainService
import com.example.musinsa.exception.*
import com.example.musinsa.model.dto.ProductEventDto
import com.example.musinsa.model.dto.request.product.CreateProductRequest
import com.example.musinsa.model.dto.request.product.UpdateProductRequest
import com.example.musinsa.model.dto.response.product.*
import com.example.musinsa.model.enums.CategoryType
import com.example.musinsa.model.enums.ProductEventType
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productDomainService: ProductDomainService,
    private val productCategoryStatisticDomainService: ProductCategoryStatisticDomainService,
    private val brandDomainService: BrandDomainService,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    @Transactional(readOnly = true)
    fun getCategoryLowesPrices(): GetCategoryLowesPricesResponses {
        val productStatistics = productCategoryStatisticDomainService.getAllProductStatistics()
        val brandIds = productStatistics.map { it.minBrandId }
        val brandMap = brandDomainService.getBrandIdIn(brandIds).associateBy { it.id }
        return GetCategoryLowesPricesResponses(
            products = productStatistics.map {
                GetCategoryLowesPricesResponse(
                    brandId = it.minBrandId,
                    brandName = brandMap[it.minBrandId]!!.name,
                    category = it.category,
                    price = it.minPrice
                )
            },
            totalPrice = productStatistics.sumOf { it.minPrice }
        )
    }

    @Transactional(readOnly = true)
    fun getMinMaxPrice(category: CategoryType): GetMinMaxPriceResponses {
        CategoryType.fromDisplayName(category.displayName)
            ?: throw ProductException(ProductError.INVALID_CATEGORY_EXCEPTION)
        val productStatistics =
            productCategoryStatisticDomainService.getProductStatistics(category) ?: throw ProductCategoryStatisticException(
                ProductCategoryStatisticError.NOT_FOUND_PRODUCT_STATISTICS_EXCEPTION
            )
        val brandIds = listOf(productStatistics.minBrandId, productStatistics.maxBrandId)
        val brandMap = brandDomainService.getBrandIdIn(brandIds).associateBy { it.id }

        return GetMinMaxPriceResponses(
            category = productStatistics.category,
            minProduct = GetMinMaxPriceResponse(
                brandName = brandMap[productStatistics.minBrandId]!!.name,
                price = productStatistics.minPrice
            ),
            maxProduct = GetMinMaxPriceResponse(
                brandName = brandMap[productStatistics.minBrandId]!!.name,
                price = productStatistics.minPrice
            )
        )
    }

    @Transactional
    fun createProduct(createProductRequest: CreateProductRequest): CreateProductResponse {
        if (createProductRequest.price < 0) {
            throw ProductException(ProductError.INVALID_PRICE_EXCEPTION)
        }

        CategoryType.fromDisplayName(createProductRequest.category.displayName)
            ?: throw ProductException(ProductError.INVALID_CATEGORY_EXCEPTION)

        val existsProduct =
            productDomainService.existsByCategoryAndBrandId(createProductRequest.category, createProductRequest.brandId)
        if (existsProduct) throw ProductException(ProductError.DUPLICATE_PRODUCT_EXCEPTION)

        val brand = brandDomainService.getBrand(createProductRequest.brandId)
            ?: throw BrandException(BrandError.NOT_FOUND_BRAND_EXCEPTION)
        val product = productDomainService.save(createProductRequest.toProductDto())

        applicationEventPublisher.publishEvent(ProductEventDto(product, ProductEventType.CREATE))

        return CreateProductResponse(
            id = product.id,
            brandId = brand.id,
            category = product.category,
            price = product.price
        )
    }

    @Transactional
    fun updateProduct(productId: Long, updateProductRequest: UpdateProductRequest): UpdateProductResponse {
        productDomainService.getProduct(productId) ?: throw ProductException(ProductError.NOT_FOUND_PRODUCT_EXCEPTION)

        if (updateProductRequest.price < 0) {
            throw ProductException(ProductError.INVALID_PRICE_EXCEPTION)
        }

        CategoryType.fromDisplayName(updateProductRequest.category.displayName)
            ?: throw ProductException(ProductError.INVALID_CATEGORY_EXCEPTION)

        val existsProduct =
            productDomainService.existsByCategoryAndBrandId(updateProductRequest.category, updateProductRequest.brandId)
        if (existsProduct) throw ProductException(ProductError.DUPLICATE_PRODUCT_EXCEPTION)

        val brand = brandDomainService.getBrand(updateProductRequest.brandId)
            ?: throw BrandException(BrandError.NOT_FOUND_BRAND_EXCEPTION)

        val updateProduct = productDomainService.save(updateProductRequest.toProductDto(productId))
        return UpdateProductResponse(
            id = updateProduct.id,
            brandId = brand.id,
            category = updateProduct.category,
            price = updateProduct.price
        )
    }

    @Transactional
    fun deleteProduct(productId: Long): DeleteProductResponse {
        val product = productDomainService.getProduct(productId)
            ?: throw ProductException(ProductError.NOT_FOUND_PRODUCT_EXCEPTION)
        val deleteProductId = productDomainService.deleteProduct(product.id)
        return DeleteProductResponse(
            productId = deleteProductId
        )
    }
}