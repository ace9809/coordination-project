package com.example.musinsa.service

import com.example.musinsa.domain.ProductDomainService
import com.example.musinsa.exception.ProductError
import com.example.musinsa.exception.ProductException
import com.example.musinsa.model.dto.request.CreateProductRequest
import com.example.musinsa.model.dto.request.UpdateProductRequest
import com.example.musinsa.model.dto.response.CreateProductResponse
import com.example.musinsa.model.dto.response.DeleteProductResponse
import com.example.musinsa.model.dto.response.UpdateProductResponse
import com.example.musinsa.model.enums.CategoryType
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productDomainService: ProductDomainService
) {
    fun createProduct(saveProductRequest: CreateProductRequest): CreateProductResponse {
        if (saveProductRequest.price < 0) {
            throw ProductException(ProductError.INVALID_PRICE_EXCEPTION)
        }

        CategoryType.fromDisplayName(saveProductRequest.category.displayName)
            ?: throw ProductException(ProductError.INVALID_CATEGORY_EXCEPTION)

        val existsProduct = productDomainService.existsByCategoryAndBrand(saveProductRequest.category, saveProductRequest.brand)
        if (existsProduct) throw ProductException(ProductError.DUPLICATE_PRODUCT_EXCEPTION)

        val product = productDomainService.save(saveProductRequest.toProductDto())
        return CreateProductResponse(
            id = product.id,
            brand = product.brand,
            category = product.category,
            price = product.price
        )
    }

    fun updateProduct(productId: Long, updateProductRequest: UpdateProductRequest): UpdateProductResponse {
        productDomainService.getProduct(productId) ?: throw ProductException(ProductError.NOT_FOUND_PRODUCT_EXCEPTION)

        if (updateProductRequest.price < 0) {
            throw ProductException(ProductError.INVALID_PRICE_EXCEPTION)
        }

        CategoryType.fromDisplayName(updateProductRequest.category.displayName)
            ?: throw ProductException(ProductError.INVALID_CATEGORY_EXCEPTION)

        val existsProduct = productDomainService.existsByCategoryAndBrand(updateProductRequest.category, updateProductRequest.brand)
        if (existsProduct) throw ProductException(ProductError.DUPLICATE_PRODUCT_EXCEPTION)

        val updateProduct = productDomainService.save(updateProductRequest.toProductDto())
        return UpdateProductResponse(
            id = updateProduct.id,
            brand = updateProduct.brand,
            category = updateProduct.category,
            price = updateProduct.price
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