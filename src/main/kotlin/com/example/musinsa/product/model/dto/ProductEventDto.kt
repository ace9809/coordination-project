package com.example.musinsa.product.model.dto

import com.example.musinsa.product.model.enums.ProductEventType

data class ProductEventDto(
    val product: ProductDto,
    val prevProduct: ProductDto ?= null,
    val type: ProductEventType
)