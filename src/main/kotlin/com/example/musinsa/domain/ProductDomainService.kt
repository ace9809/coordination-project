package com.example.musinsa.domain

import com.example.musinsa.model.dto.ProductDto
import com.example.musinsa.model.enums.CategoryType
import com.example.musinsa.projection.repository.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProductDomainService(
    private val productRepository: ProductRepository
) {
    fun getProduct(productId: Long): ProductDto? {
        val product = productRepository.findByIdOrNull(productId) ?: return null
        return ProductDto.of(product)
    }

    fun save(productDto: ProductDto): ProductDto {
        return ProductDto.of(productRepository.save(productDto.toProduct()))
    }

    fun existsByCategoryAndBrand(category: CategoryType, brand: String): Boolean {
        return productRepository.existsByCategoryAndBrand(category.name, brand)
    }

    fun deleteProduct(productDto: ProductDto): ProductDto {
        productRepository.deleteById(productDto.id)

        return productDto
    }
}