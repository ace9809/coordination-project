package com.example.musinsa.service

import com.example.musinsa.domain.ProductDomainService
import com.example.musinsa.exception.ProductError
import com.example.musinsa.exception.ProductException
import com.example.musinsa.model.dto.request.CreateProductRequest
import com.example.musinsa.model.dto.response.CreateProductResponse
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



        val product = productDomainService.save(createProductRequest.toProductDto())
        return CreateProductResponse(
            brand = product.brand,
            category = product.category,
            price = product.price
        )
    }


}