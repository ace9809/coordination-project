package com.example.musinsa.model.dto

import com.example.musinsa.model.enums.CategoryType
import com.example.musinsa.projection.entity.Product

data class ProductDto(
    var id: Long = 0L,
    val category: CategoryType,
    val brand: String,
    val price: Long
) {
    companion object {
        fun of(product: Product): ProductDto {
            return ProductDto(
                id = product.id,
                category = product.category,
                brand = product.brand,
                price = product.price
            )
        }
    }

    fun toProduct(productDto: ProductDto): Product {
        return Product(
            category = productDto.category,
            brand = productDto.brand,
            price = productDto.price
        ).apply {
            id = this@ProductDto.id
        }
    }
}