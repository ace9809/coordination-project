package com.example.musinsa.product.model.dto

import com.example.musinsa.product.model.enums.CategoryType
import com.example.musinsa.product.entity.Product

data class ProductDto(
    val id: Long = 0L,
    val category: CategoryType,
    val brandId: Long,
    val price: Long
) {
    companion object {
        fun of(product: Product): ProductDto {
            return ProductDto(
                id = product.id,
                category = product.category,
                brandId = product.brandId,
                price = product.price
            )
        }
    }

    fun toProduct(): Product {
        return Product(
            id = id,
            category = category,
            brandId = brandId,
            price = price
        )
    }
}