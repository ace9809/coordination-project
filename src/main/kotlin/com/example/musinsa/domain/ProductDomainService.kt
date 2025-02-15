package com.example.musinsa.domain

import com.example.musinsa.model.dto.ProductDto
import com.example.musinsa.model.enums.CategoryType
import com.example.musinsa.projection.entity.Product
import com.example.musinsa.projection.repository.ProductRepository
import jdk.jfr.Category
import org.springframework.stereotype.Service

@Service
class ProductDomainService(
    private val productRepository: ProductRepository
) {

    fun save(productDto: ProductDto): ProductDto {
        return ProductDto.of(productRepository.save(productDto.toProduct()))
    }

    fun existsByCategoryAndBrand(category: CategoryType, brand: String): Boolean {
        return productRepository.existsByCategoryAndBrand(category.name, brand)
    }
}