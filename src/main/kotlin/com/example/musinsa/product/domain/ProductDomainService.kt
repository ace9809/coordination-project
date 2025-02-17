package com.example.musinsa.product.domain

import com.example.musinsa.product.model.dto.ProductDto
import com.example.musinsa.product.model.enums.CategoryType
import com.example.musinsa.product.repository.ProductRepository
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

    fun getAllByBrandId(brandId: Long): List<ProductDto> {
        val products = productRepository.findAllByBrandId(brandId)
        return products.map { ProductDto.of(it) }
    }

    fun save(productDto: ProductDto): ProductDto {
        return ProductDto.of(productRepository.save(productDto.toProduct()))
    }

    fun existsByCategoryAndBrandId(category: CategoryType, brandId: Long): Boolean {
        return productRepository.existsByCategoryAndBrandId(category, brandId)
    }

    fun deleteProduct(productId: Long): Long {
        productRepository.deleteById(productId)
        return productId
    }

    fun getMostExpensiveProductByCategory(category: CategoryType): ProductDto? {
        val product = productRepository.findTopByCategoryOrderByPriceDesc(category) ?: return null
        return ProductDto.of(product)
    }

    fun getCheapestProductByCategory(category: CategoryType): ProductDto? {
        val product = productRepository.findTopByCategoryOrderByPriceAsc(category) ?: return null
        return ProductDto.of(product)
    }
}