package com.example.musinsa.service

import com.example.musinsa.domain.ProductDomainService
import com.example.musinsa.exception.ProductError
import com.example.musinsa.exception.ProductException
import com.example.musinsa.model.dto.request.CreateProductRequest
import com.example.musinsa.model.dto.response.CreateProductResponse
import com.example.musinsa.model.dto.response.DeleteProductResponse
import com.example.musinsa.model.enums.CategoryType
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productDomainService: ProductDomainService
) {
    fun createProduct(createProductRequest: CreateProductRequest): CreateProductResponse {
        if (createProductRequest.price < 0) {
            throw ProductException(ProductError.INVALID_PRICE_EXCEPTION)
        }

        CategoryType.fromDisplayName(createProductRequest.category.displayName)
            ?: throw ProductException(ProductError.INVALID_CATEGORY_EXCEPTION)

        val existsProduct = productDomainService.existsByCategoryAndBrand(createProductRequest.category, createProductRequest.brand)
        if (existsProduct) throw ProductException(ProductError.DUPLICATE_PRODUCT_EXCEPTION)

        val product = productDomainService.save(createProductRequest.toProductDto())
        return CreateProductResponse(
            id = product.id,
            brand = product.brand,
            category = product.category,
            price = product.price
        )
    }

    fun deleteProduct(productId: Long): DeleteProductResponse {
        val product = productDomainService.getProduct(productId) ?: throw ProductException(ProductError.NOT_FOUND_PRODUCT_EXCEPTION)
        val deleteProduct = productDomainService.deleteProduct(product)
        return DeleteProductResponse(
            productId = deleteProduct.id
        )
    }
}