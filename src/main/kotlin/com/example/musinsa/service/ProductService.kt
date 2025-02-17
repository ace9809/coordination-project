package com.example.musinsa.service

import com.example.musinsa.domain.BrandDomainService
import com.example.musinsa.domain.ProductBrandStatisticDomainService
import com.example.musinsa.domain.ProductCategoryStatisticDomainService
import com.example.musinsa.domain.ProductDomainService
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
                    category = it.category,
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
                        category = it.category,
                        price = it.price
                    )
                },
                totalPrice = totalPrice
            )
        )
    }

    @Transactional(readOnly = true)
    fun getMinMaxPrice(category: CategoryType): GetMinMaxPriceResponses {
        CategoryType.fromDisplayName(category.displayName)
            ?: throw ProductException(ProductError.INVALID_CATEGORY_EXCEPTION)
        val productStatistic =
            productCategoryStatisticDomainService.getProductStatistic(category)
                ?: throw ProductCategoryStatisticException(
                    ProductCategoryStatisticError.NOT_FOUND_PRODUCT_CATEGORY_STATISTICS_EXCEPTION
                )
        val brandIds = listOf(productStatistic.minBrandId, productStatistic.maxBrandId)
        val brandMap = brandDomainService.getBrandIdIn(brandIds).associateBy { it.id }

        return GetMinMaxPriceResponses(
            category = productStatistic.category,
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

        val category = CategoryType.fromDisplayName(createProductRequest.category)
            ?: throw ProductException(ProductError.INVALID_CATEGORY_EXCEPTION)

        val existsProduct =
            productDomainService.existsByCategoryAndBrandId(category, createProductRequest.brandId)
        if (existsProduct) throw ProductException(ProductError.DUPLICATE_PRODUCT_EXCEPTION)

        val brand = brandDomainService.getBrand(createProductRequest.brandId)
            ?: throw BrandException(BrandError.NOT_FOUND_BRAND_EXCEPTION)
        val product = productDomainService.save(createProductRequest.toProductDto(category))

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

        val category = CategoryType.fromDisplayName(updateProductRequest.category)
            ?: throw ProductException(ProductError.INVALID_CATEGORY_EXCEPTION)

        val existsProduct =
            productDomainService.existsByCategoryAndBrandId(category, updateProductRequest.brandId)
        if (existsProduct) throw ProductException(ProductError.DUPLICATE_PRODUCT_EXCEPTION)

        val brand = brandDomainService.getBrand(updateProductRequest.brandId)
            ?: throw BrandException(BrandError.NOT_FOUND_BRAND_EXCEPTION)

        val updateProduct = productDomainService.save(updateProductRequest.toProductDto(productId, category))
        applicationEventPublisher.publishEvent(ProductEventDto(updateProduct, ProductEventType.UPDATE))
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

        applicationEventPublisher.publishEvent(ProductEventDto(product, ProductEventType.DELETE))
        return DeleteProductResponse(
            productId = deleteProductId
        )
    }
}