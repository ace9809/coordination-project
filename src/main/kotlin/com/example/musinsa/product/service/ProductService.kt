package com.example.musinsa.product.service

import com.example.musinsa.brand.domain.BrandDomainService
import com.example.musinsa.product.domain.ProductBrandStatisticDomainService
import com.example.musinsa.product.domain.ProductCategoryStatisticDomainService
import com.example.musinsa.product.domain.ProductDomainService
import com.example.musinsa.exception.*
import com.example.musinsa.product.model.dto.ProductEventDto
import com.example.musinsa.product.model.dto.request.product.CreateProductRequest
import com.example.musinsa.product.model.dto.request.product.UpdateProductRequest
import com.example.musinsa.product.model.enums.CategoryType
import com.example.musinsa.product.model.enums.ProductEventType
import com.example.musinsa.product.model.dto.response.product.*
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productDomainService: ProductDomainService,
    private val productCategoryStatisticDomainService: ProductCategoryStatisticDomainService,
    private val productBrandStatisticDomainService: ProductBrandStatisticDomainService,
    private val brandDomainService: BrandDomainService,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    @Transactional(readOnly = true)
    fun getCategoryMinPrices(): GetCategoryMinPricesResponses {
        val productCategoryStatistics = productCategoryStatisticDomainService.getAllProductStatistics()
        val brandIds = productCategoryStatistics.map { it.minBrandId }
        val brandMap = brandDomainService.getBrandIdIn(brandIds).associateBy { it.id }
        return GetCategoryMinPricesResponses(
            products = productCategoryStatistics.map {
                GetCategoryMinPricesResponse(
                    brandId = it.minBrandId,
                    brandName = brandMap[it.minBrandId]!!.name,
                    category = it.category.name,
                    categoryName = it.category.displayName,
                    price = it.minPrice
                )
            },
            totalPrice = productCategoryStatistics.sumOf { it.minPrice }
        )
    }

    @Transactional(readOnly = true)
    fun getMinTotalPriceBrand(): GetMinTotalPriceBrandResponses {
        val productBrandStatistic =
            productBrandStatisticDomainService.getTopByOrderByTotalPriceAsc() ?: throw ProductBrandStatisticException(
                ProductBrandStatisticError.NOT_FOUND_PRODUCT_BRAND_STATISTICS_EXCEPTION
            )
        val minPriceBrandId = productBrandStatistic.brandId
        val products = productDomainService.getAllByBrandId(minPriceBrandId)
        val totalPrice = products.sumOf { it.price }
        val brand = brandDomainService.getBrand(minPriceBrandId)
            ?: throw BrandException(BrandError.NOT_FOUND_BRAND_EXCEPTION)

        return GetMinTotalPriceBrandResponses(
            products = GetMinTotalPriceBrandResponse(
                brandName = brand.name,
                categories = products.map {
                    GetCategoriesResponse(
                        category = it.category.name,
                        categoryName = it.category.displayName,
                        price = it.price
                    )
                },
                totalPrice = totalPrice
            )
        )
    }

    @Transactional(readOnly = true)
    fun getMinMaxPrice(category: String): GetMinMaxPriceResponses {
        val categoryType = CategoryType.entries.find { it.name == category }
            ?: throw ProductException(ProductError.INVALID_CATEGORY_EXCEPTION)
        val productStatistic =
            productCategoryStatisticDomainService.getProductStatistic(categoryType)
                ?: throw ProductCategoryStatisticException(
                    ProductCategoryStatisticError.NOT_FOUND_PRODUCT_CATEGORY_STATISTICS_EXCEPTION
                )
        val brandIds = listOf(productStatistic.minBrandId, productStatistic.maxBrandId)
        val brandMap = brandDomainService.getBrandIdIn(brandIds).associateBy { it.id }

        return GetMinMaxPriceResponses(
            category = productStatistic.category.name,
            categoryName = productStatistic.category.displayName,
            minProduct = GetMinMaxPriceResponse(
                brandName = brandMap[productStatistic.minBrandId]!!.name,
                price = productStatistic.minPrice
            ),
            maxProduct = GetMinMaxPriceResponse(
                brandName = brandMap[productStatistic.maxBrandId]!!.name,
                price = productStatistic.maxPrice
            )
        )
    }

    @Transactional
    fun createProduct(createProductRequest: CreateProductRequest): CreateProductResponse {
        if (createProductRequest.price < 0) {
            throw ProductException(ProductError.INVALID_PRICE_EXCEPTION)
        }

        val categoryType = CategoryType.entries.find { it.name == createProductRequest.category }
            ?: throw ProductException(ProductError.INVALID_CATEGORY_EXCEPTION)

        val existsProduct =
            productDomainService.existsByCategoryAndBrandId(categoryType, createProductRequest.brandId)
        if (existsProduct) throw ProductException(ProductError.DUPLICATE_PRODUCT_EXCEPTION)

        val brand = brandDomainService.getBrand(createProductRequest.brandId)
            ?: throw BrandException(BrandError.NOT_FOUND_BRAND_EXCEPTION)
        val product = productDomainService.save(createProductRequest.toProductDto(categoryType))

        applicationEventPublisher.publishEvent(ProductEventDto(product, null, ProductEventType.CREATE))

        return CreateProductResponse(
            id = product.id,
            brandId = brand.id,
            category = product.category,
            price = product.price
        )
    }

    @Transactional
    fun updateProduct(productId: Long, updateProductRequest: UpdateProductRequest): UpdateProductResponse {
        val prevProduct = productDomainService.getProduct(productId) ?: throw ProductException(ProductError.NOT_FOUND_PRODUCT_EXCEPTION)

        if (updateProductRequest.price < 0) {
            throw ProductException(ProductError.INVALID_PRICE_EXCEPTION)
        }

        val updateProduct = productDomainService.save(updateProductRequest.toProductDto(productId, prevProduct.brandId, prevProduct.category))
        applicationEventPublisher.publishEvent(ProductEventDto(updateProduct, prevProduct, ProductEventType.UPDATE))
        return UpdateProductResponse(
            id = updateProduct.id,
            brandId = updateProduct.brandId,
            category = updateProduct.category,
            price = updateProduct.price
        )
    }

    @Transactional
    fun deleteProduct(productId: Long): DeleteProductResponse {
        val product = productDomainService.getProduct(productId)
            ?: throw ProductException(ProductError.NOT_FOUND_PRODUCT_EXCEPTION)
        val deleteProductId = productDomainService.deleteProduct(product.id)

        applicationEventPublisher.publishEvent(ProductEventDto(product, null, ProductEventType.DELETE))
        return DeleteProductResponse(
            productId = deleteProductId
        )
    }
}